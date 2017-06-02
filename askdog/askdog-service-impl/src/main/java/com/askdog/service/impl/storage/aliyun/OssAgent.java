package com.askdog.service.impl.storage.aliyun;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PolicyConditions;
import com.askdog.common.utils.IDGenerator;
import com.askdog.model.data.StorageRecord;
import com.askdog.model.data.inner.ResourceType;
import com.askdog.service.exception.ConflictException;
import com.askdog.service.exception.ServiceException;
import com.askdog.service.impl.storage.StorageRecorder;
import com.askdog.service.impl.storage.aliyun.OssConfiguration.Callback;
import com.askdog.service.impl.storage.aliyun.description.OssResourceDescription;
import com.askdog.service.storage.AccessToken;
import com.askdog.service.storage.Provider;
import com.askdog.service.storage.StorageAgent;
import com.askdog.service.storage.StorageResource;
import com.askdog.service.storage.aliyun.OssAccessToken;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.aliyun.oss.common.utils.BinaryUtil.toBase64String;
import static com.aliyun.oss.model.MatchMode.Exact;
import static com.aliyun.oss.model.PolicyConditions.COND_CONTENT_LENGTH_RANGE;
import static com.aliyun.oss.model.PolicyConditions.COND_KEY;
import static com.askdog.common.utils.Json.writeValueAsString;
import static com.askdog.common.utils.Variables.variables;
import static com.askdog.model.data.inner.ResourceState.PERSISTENT;
import static com.askdog.model.data.inner.ResourceType.*;
import static com.askdog.model.data.inner.StorageProvider.ALI_OSS;
import static com.google.common.collect.ImmutableMap.of;
import static java.lang.System.currentTimeMillis;
import static org.elasticsearch.common.Strings.toUTF8Bytes;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Configuration
@Provider(ALI_OSS)
public class OssAgent implements StorageAgent {

    private Map<ResourceType, OSSClient> clients = new HashMap<>();
    private Map<ResourceType, OssConfiguration> configurations;

    @Autowired private StorageRecorder storageRecorder;

    @StorageConfiguration(USER_AVATAR)
    @Autowired private OssConfiguration userAvatarPictureConfiguration;

    @StorageConfiguration(STORE_COVER)
    @Autowired private OssConfiguration storeCoverConfiguration;

    @StorageConfiguration(PRODUCT_COVER)
    @Autowired private OssConfiguration productCoverConfiguration;

    @StorageConfiguration(EVENT_POSTER)
    @Autowired private OssConfiguration eventPosterConfiguration;

    @StorageConfiguration(PRODUCT_VIDEO)
    @Autowired private OssConfiguration productVideoConfiguration;

    @StorageConfiguration(EVENT_VIDEO)
    @Autowired private OssConfiguration eventVideoConfiguration;

    @PostConstruct
    private void init() {
        configurations = ImmutableMap.<ResourceType, OssConfiguration>builder()
                .put(USER_AVATAR, userAvatarPictureConfiguration)
                .put(STORE_COVER, storeCoverConfiguration)
                .put(PRODUCT_COVER, productCoverConfiguration)
                .put(EVENT_POSTER, eventPosterConfiguration)
                .put(PRODUCT_VIDEO, productVideoConfiguration)
                .put(EVENT_VIDEO, eventVideoConfiguration)
                .build();
    }

    @Nonnull
    @Override
    public AccessToken generateAccessToken(@Nonnull ResourceType type, @Nonnull String extension) throws ServiceException {
        OssConfiguration configuration = getOssConfiguration(type);
        OssConfiguration.Policy policy = configuration.getPolicy();

        long resourceId = IDGenerator.next();
        StorageRecord record = generateStorageRecord(type, resourceId, resourceId + "." + extension);
        OssResourceDescription ossResourceDescription = (OssResourceDescription) record.getDescription();

        PolicyConditions conditions = new PolicyConditions();
        conditions.addConditionItem(COND_CONTENT_LENGTH_RANGE, 0, Long.valueOf(policy.getMaxSize()) * 1024);
        conditions.addConditionItem(Exact, COND_KEY, ossResourceDescription.getPersistenceName());

        long expireEndTime = currentTimeMillis() + Long.valueOf(policy.getExpireTime()) * 1000;
        String postPolicy = getOSSClient(type).generatePostPolicy(new Date(expireEndTime), conditions);

        OssAccessToken accessToken = new OssAccessToken();
        accessToken.setAccessId(configuration.getAccessId());
        accessToken.setPolicy(toBase64String(toUTF8Bytes(postPolicy)));
        accessToken.setSignature(getOSSClient(type).calculatePostSignature(postPolicy));
        accessToken.setPersistenceName(ossResourceDescription.getPersistenceName());
        accessToken.setHost(configuration.getHost());
        accessToken.setExpire(expireEndTime);

        accessToken.setCallback(createCallback(record, configuration));

        // TODO this is use for vod, use STS instead this.
        if (type == PRODUCT_VIDEO || type == EVENT_VIDEO) {
            accessToken.setSecretKey(configuration.getAccessKey());
            accessToken.setResourceId(resourceId);
            accessToken.setEndpoint(configuration.getEndpoint().startsWith("http://")
                    ? configuration.getEndpoint()
                    : "http://" + configuration.getEndpoint());
            accessToken.setBucket(configuration.getBucket());
        }

        return accessToken;
    }

    @Nonnull
    @Override
    public StorageResource persistResource(@Nonnull Long resourceId) throws ServiceException {
        StorageRecord storageRecord = storageRecorder.updateResourceStatus(resourceId, PERSISTENT);
        return new StorageResource(resourceId, storageRecord.getDescription().getResourceUrl());
    }

    @Nonnull
    @Override
    public StorageRecord save(@Nonnull ResourceType type,
                              @Nonnull Long objectId,
                              @Nonnull String fileName,
                              @Nonnull InputStream stream) throws ServiceException {
        StorageRecord storageRecord = generateStorageRecord(type, objectId, fileName);
        String persistenceName = ((OssResourceDescription) storageRecord.getDescription()).getPersistenceName();
        getOSSClient(type).putObject(getOssConfiguration(type).getBucket(), persistenceName, stream);
        persistResource(storageRecord.getResourceId());
        return storageRecord;
    }

    @Bean(name = "default")
    @Scope(SCOPE_PROTOTYPE)
    @ConfigurationProperties(prefix = "askdog.storage.default")
    public OssConfiguration defaultStorageConfig() {
        return new OssConfiguration();
    }

    @Bean(name = "picture.default")
    @Scope(SCOPE_PROTOTYPE)
    @ConfigurationProperties(prefix = "askdog.storage.picture.default")
    public OssConfiguration pictureDefaultStorageConfig(@Qualifier("default") OssConfiguration configuration) {
        return configuration;
    }

    @Bean
    @StorageConfiguration(USER_AVATAR)
    @ConfigurationProperties(prefix = "askdog.storage.picture.avatar")
    public OssConfiguration avatarStorageConfig(@Qualifier("picture.default") OssConfiguration configuration) {
        return configuration;
    }

    @Bean
    @StorageConfiguration(STORE_COVER)
    @ConfigurationProperties(prefix = "askdog.storage.picture.store")
    public OssConfiguration storeCoverStorageConfig(@Qualifier("picture.default") OssConfiguration configuration) {
        return configuration;
    }

    @Bean
    @StorageConfiguration(PRODUCT_COVER)
    @ConfigurationProperties(prefix = "askdog.storage.picture.product")
    public OssConfiguration productCoverStorageConfig(@Qualifier("picture.default") OssConfiguration configuration) {
        return configuration;
    }

    @Bean
    @StorageConfiguration(EVENT_POSTER)
    @ConfigurationProperties(prefix = "askdog.storage.picture.event")
    public OssConfiguration eventPosterStorageConfig(@Qualifier("picture.default") OssConfiguration configuration) {
        return configuration;
    }

    @Bean(name = "video.default")
    @Scope(SCOPE_PROTOTYPE)
    @ConfigurationProperties(prefix = "askdog.storage.video.default")
    public OssConfiguration videoDefaultStorageConfig(@Qualifier("default") OssConfiguration configuration) {
        return configuration;
    }

    @Bean
    @StorageConfiguration(PRODUCT_VIDEO)
    @ConfigurationProperties(prefix = "askdog.storage.video.product")
    public OssConfiguration productVideoStorageConfig(@Qualifier("video.default") OssConfiguration configuration) {
        return configuration;
    }

    @Bean
    @StorageConfiguration(EVENT_VIDEO)
    @ConfigurationProperties(prefix = "askdog.storage.video.event")
    public OssConfiguration eventVideoStorageConfig(@Qualifier("video.default") OssConfiguration configuration) {
        return configuration;
    }

    private String createCallback(StorageRecord record, OssConfiguration configuration) throws ConflictException {
        Callback callback = configuration.getCallback().clone();
        callback.setBody(variables(of("resourceId", record.getResourceId())).replace(callback.getBody()));
        return toBase64String(toUTF8Bytes(writeValueAsString(callback)));
    }

    private OSSClient getOSSClient(ResourceType type) {
        // not need synchronize it
        OSSClient client = clients.get(type);
        if (client == null) {
            OssConfiguration configuration = getOssConfiguration(type);
            client = new OSSClient(configuration.getEndpoint(), configuration.getAccessId(), configuration.getAccessKey());
            clients.put(type, client);
        }

        return client;
    }

    private OssConfiguration getOssConfiguration(ResourceType type) {
        return configurations.get(type);
    }

    private StorageRecord generateStorageRecord(@Nonnull ResourceType type, Long resourceId, @Nonnull String fileName) throws ServiceException {
        OssConfiguration configuration = getOssConfiguration(type);
        OssConfiguration.Policy policy = configuration.getPolicy();
        String baseDir = policy.getBaseDir();

        OssResourceDescription description = new OssResourceDescription();
        description.setEndpoint(configuration.getEndpoint());
        description.setBucket(configuration.getBucket());
        description.setUrlAlias(configuration.getUrlAlias());
        description.setPersistenceName((baseDir != null ? baseDir + "/" : "") + fileName);
        return storageRecorder.newRecord(ALI_OSS, type, resourceId, description);
    }

}
