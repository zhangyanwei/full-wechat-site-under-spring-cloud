package com.askdog.dao.repository.extend;

import com.askdog.model.data.UserLocation;
import com.askdog.model.data.UserResidence;

import javax.annotation.Nonnull;
import java.util.List;

public interface ExtendedLocationRecordRepository {

    List<UserLocation> findNearbyUsers(@Nonnull Long userId, @Nonnull Double lat, @Nonnull Double lng, double nearbyDistance, long nearbyLimit);

    List<UserResidence> findUserLocation(@Nonnull Long userId, long recentLimit);

    UserResidence countNearbyPoints(@Nonnull Long userId, @Nonnull Double lat, @Nonnull Double lng, double rateDistance);
}
