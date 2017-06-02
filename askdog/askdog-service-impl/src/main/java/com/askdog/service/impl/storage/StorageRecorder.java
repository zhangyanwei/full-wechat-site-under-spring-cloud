package com.askdog.service.impl.storage;

import com.askdog.dao.repository.mongo.StorageRecordRepository;
import com.askdog.model.data.StorageRecord;
import com.askdog.model.data.inner.ResourceDescription;
import com.askdog.model.data.inner.ResourceState;
import com.askdog.model.data.inner.ResourceType;
import com.askdog.model.data.inner.StorageProvider;
import com.askdog.model.data.video.Video;
import com.askdog.service.exception.IllegalArgumentException;
import com.askdog.service.exception.NotFoundException;
import com.askdog.service.impl.cache.annotation.video.ResourceCache;
import com.askdog.service.impl.cache.annotation.video.ResourceCacheRefresh;
import com.askdog.service.impl.storage.aliyun.OssConfiguration;
import com.askdog.service.impl.storage.aliyun.StorageConfiguration;
import com.askdog.service.impl.storage.aliyun.description.OssVideoResourceDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.askdog.common.utils.Conditions.checkState;
import static com.askdog.model.data.inner.ResourceState.PERSISTENT;
import static com.askdog.model.data.inner.ResourceState.TEMPORARY;
import static com.askdog.model.data.inner.ResourceType.PRODUCT_VIDEO;
import static com.askdog.service.exception.IllegalArgumentException.Error.INVALID_RESOURCE;
import static com.askdog.service.exception.NotFoundException.Error.STORAGE_RECORD;
import static org.springframework.util.Assert.notNull;

@Component
public class StorageRecorder {

    @Autowired private StorageRecordRepository storageRecordRepository;

    @Value("${askdog.storage.video.default.url-alias}")
    private String videoUrlAlias;

    public void assertValid(Long resourceId) {
        StorageRecord storageRecord = storageRecordRepository.findByResourceId(resourceId);
        checkState(storageRecord != null && storageRecord.getResourceState() == PERSISTENT,
                () -> new IllegalArgumentException(INVALID_RESOURCE));
    }

    public void assertVideoValid(Long resourceId) {
        StorageRecord storageRecord = storageRecordRepository.findByResourceId(resourceId);
        checkState(storageRecord != null, () -> new IllegalArgumentException(INVALID_RESOURCE));
    }

    public StorageRecord newRecord(StorageProvider provider, ResourceType resourceType, Long resourceId,
                                   ResourceDescription description) {
        StorageRecord record = new StorageRecord();
        record.setResourceId(resourceId);
        record.setProvider(provider);
        record.setResourceType(resourceType);
        record.setResourceState(TEMPORARY);
        record.setDescription(description);
        record.setCreationDate(new Date());
        return storageRecordRepository.save(record);
    }

    public StorageRecord updateResourceStatus(Long resourceId, ResourceState status) {
        StorageRecord storageRecord = getResource(resourceId);
        storageRecord.setResourceState(status);
        return storageRecordRepository.save(storageRecord);
    }

    @ResourceCache
    public StorageRecord getResource(Long resourceId) {
        notNull(resourceId);
        StorageRecord storageRecord = storageRecordRepository.findByResourceId(resourceId);
        checkState(storageRecord != null, () -> new NotFoundException(STORAGE_RECORD));

        ResourceDescription description = storageRecord.getDescription();
        if (description instanceof OssVideoResourceDescription) {
            Video video = ((OssVideoResourceDescription) description).getVideo();
            if (video != null && video.getSnapshots() != null) {
                video.getSnapshots().forEach(snapshot -> snapshot.setUrl(replaceWithUrlAlias(snapshot.getUrl())));
            }
            if (video != null && video.getTranscodeVideos() != null) {
                video.getTranscodeVideos().forEach(videoItem -> videoItem.setUrl(replaceWithUrlAlias(videoItem.getUrl())));
            }
        }

        return storageRecord;
    }

    @ResourceCacheRefresh
    public StorageRecord refreshResourceCache(Long resourceId) {
        return getResource(resourceId);
    }

    public StorageRecord save(StorageRecord storageRecord) {
        return storageRecordRepository.save(storageRecord);
    }

    private String replaceWithUrlAlias(String ossUrl) {

        if (!ossUrl.contains(".aliyuncs.com")) {
            return ossUrl;
        }

        // maybe this is not a suitable way to replace it. but it is a simple way.
        return ossUrl.replace(ossUrl.substring(7, ossUrl.indexOf("com") + 3), videoUrlAlias);
    }

}
