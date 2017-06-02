package com.askdog.service.impl.location;

import com.askdog.dao.repository.mongo.UserLocationRepository;
import com.askdog.dao.repository.mongo.UserResidenceRepository;
import com.askdog.model.data.UserResidence;
import com.askdog.model.data.builder.LocationRecordBuilder;
import com.askdog.model.data.inner.EntityType;
import com.askdog.model.data.inner.location.LocationDescription;
import com.askdog.model.data.inner.location.LocationProvider;
import com.askdog.model.data.inner.location.LocationRecord;
import com.askdog.service.exception.ConflictException;
import com.askdog.service.exception.NotFoundException;
import com.askdog.web.common.async.AsyncCaller;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.List;

import static com.askdog.model.data.builder.LocationRecordBuilder.locationRecordBuilder;
import static com.askdog.service.exception.NotFoundException.Error.USER_RESIDENCE;

@Component
public class LocationRecorder {

    @Autowired
    private UserLocationRepository userLocationRepository;

    @Autowired
    private UserResidenceRepository userResidenceRepository;

    @Autowired
    private LocationConfiguration locationConfiguration;

    @Autowired
    private AsyncCaller asyncCaller;

    public LocationRecord newRecord(LocationProvider provider, EntityType targetType, Long targetId, String ip, LocationDescription description) throws ConflictException {
        LocationRecordBuilder locationRecordBuilder = locationRecordBuilder().setProvider(provider).setTargetType(targetType).setTarget(targetId).setIp(ip).setDescription(description);
        return userLocationRepository.save(locationRecordBuilder.buildUserLocation());
    }

    public UserResidence getUserResidence(@Nonnull Long userId) throws NotFoundException {
        return userResidenceRepository.findByTargetId(userId).orElseThrow(() -> new NotFoundException(USER_RESIDENCE));
    }

    public void updateUserResidence(@Nonnull Long userId) {
        try {
            getUserResidence(userId);
        } catch (NotFoundException e) {
            // User's resident data 7 days after failure then need to find again.
            asyncCaller.asyncCall(() -> {
                UserResidence userResidence = findUserResidence(userId);
                if (userResidence != null) {
                    newResidence(userResidence);
                }
            });
        }
    }

    private UserResidence findUserResidence(@Nonnull Long userId) {
        final List<UserResidence> locationRecords = userResidenceRepository.findUserLocation(userId, locationConfiguration.getRencentLimit());
        if (locationRecords.isEmpty()) {
            return null;
        }
        List<UserResidence> sortResidences = Lists.newArrayList();
        for (UserResidence record : locationRecords) {
            UserResidence nearbyPoints = userResidenceRepository.countNearbyPoints(userId, record.getLat(), record.getLng(), locationConfiguration.getRateDistance());
            UserResidence residence = locationRecordBuilder()
                    .setProvider(record.getProvider())
                    .setTargetType(record.getTargetType())
                    .setTarget(record.getTargetId())
                    .setIp(record.getIp())
                    .setDescription(record.getDescription())
                    .bulidUserResidence(nearbyPoints.getRate(), record.getCreationDate());
            if (residence.getRate() >= (locationRecords.size() / 2)) {
                return residence; // More than half of the thought is the hottest
            }
            sortResidences.add(residence);
        }
        // sort nearby record by rate then sort first considered to be the hottest
        Comparator<UserResidence> comparatorByRate = (o1, o2) -> o1.getRate() > o2.getRate() ? -1 : 0;
        sortResidences = Ordering.from(comparatorByRate).sortedCopy(sortResidences);
        return sortResidences.get(0);
    }

    private void newResidence(@Nonnull UserResidence userResidence) {
        userResidenceRepository.save(userResidence);
    }


}
