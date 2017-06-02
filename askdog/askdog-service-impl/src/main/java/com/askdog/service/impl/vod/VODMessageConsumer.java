package com.askdog.service.impl.vod;

import com.aliyun.vod.client.*;
import com.aliyun.vod.common.MediaWorkflowMessageType;
import com.askdog.model.data.StorageRecord;
import com.askdog.model.data.video.FailedInfo;
import com.askdog.model.data.video.Video;
import com.askdog.model.data.video.VideoItem;
import com.askdog.model.data.video.VideoSnapshot;
import com.askdog.service.impl.storage.StorageRecorder;
import com.askdog.service.impl.storage.aliyun.OssConfiguration;
import com.askdog.service.impl.storage.aliyun.StorageConfiguration;
import com.askdog.service.impl.storage.aliyun.description.OssResourceDescription;
import com.askdog.service.impl.storage.aliyun.description.OssVideoResourceDescription;
import com.askdog.web.common.async.AsyncCaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.function.Consumer;

import static com.askdog.model.data.inner.ResourceState.PERSISTENT;
import static com.askdog.model.data.inner.ResourceType.EVENT_VIDEO;
import static com.askdog.model.data.inner.ResourceType.PRODUCT_VIDEO;
import static com.askdog.model.data.video.FailedInfo.FailedType.SNAPSHOT;
import static com.askdog.model.data.video.FailedInfo.FailedType.TRANSCODE;
import static com.askdog.model.data.video.VideoItem.Definition.parse;
import static java.lang.Long.valueOf;
import static java.util.Collections.singletonList;

@Configuration
@EnableScheduling
public class VODMessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(VODMessageConsumer.class);

    private VODClient client;

    @Autowired private AsyncCaller asyncCaller;
    @Autowired private StorageRecorder storageRecorder;

    @StorageConfiguration(PRODUCT_VIDEO)
    @Autowired private OssConfiguration productVideoOssConfiguration;

    @StorageConfiguration(EVENT_VIDEO)
    @Autowired private OssConfiguration eventVideoOssConfiguration;

    @PostConstruct
    public void start() {
        client = new DefaultVODClient(productVideoOssConfiguration.getAccessId(), productVideoOssConfiguration.getAccessKey());
    }

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        logger.debug("processing the vod messages ...");
        asyncCaller.asyncCall(this::process);
    }

    private void process() {
        OssConfiguration.Mns productVideoMns = productVideoOssConfiguration.getMns();
        process(productVideoMns.getUrl(), productVideoMns.getQueueName());

        OssConfiguration.Mns eventVideoMns = eventVideoOssConfiguration.getMns();
        process(eventVideoMns.getUrl(), eventVideoMns.getQueueName());
    }

    private void process(String url, String queueName) {
        logger.debug("retrieving messages from vod server (url: [], queueName: [])", url, queueName);
        List<MediaWorkflowMessage> messages = client.popMessages(url, queueName, 30);
        messages.forEach(message -> {
            try {
                logger.debug("processing message for file: []", message.fileURL());

                if (message.type() == MediaWorkflowMessageType.Start) {
                    deleteQueueMessage(url, queueName, message.receiptHandle());
                    return;
                }

                logger.debug("retrieving media for file: []", message.fileURL());
                Media media = client.getMedia(message.messageWorkflowName(), message.fileURL());

                Video video = new Video();
                parseResource(video, media);

                WorkflowExecutionOutput workflowExecutionOutput = media.workflowExecutionOutputs().get(0);
                workflowExecutionOutput.transcodeOutputs().forEach(parseTranscodeOutput(video));
                workflowExecutionOutput.snapshotOutputs().forEach(parseSnapshotOutput(video));

                logger.debug("updating storage record");
                StorageRecord resource = storageRecorder.getResource(parseResourceId(message.fileURL()));
                resource.setDescription(new OssVideoResourceDescription((OssResourceDescription) resource.getDescription(), video));
                resource.setResourceState(PERSISTENT);
                storageRecorder.save(resource);
                storageRecorder.refreshResourceCache(resource.getResourceId());

                deleteQueueMessage(url, queueName, message.receiptHandle());
            } catch (Exception e) {
                logger.error("error occurs when processing message", e);
            }
        });
    }

    private Long parseResourceId(String fileURL) {
        String filePath = fileURL.substring(fileURL.lastIndexOf("/") + 1);
        return valueOf(filePath.substring(0, filePath.lastIndexOf(".")));
    }

    private void parseResource(Video video, Media media) {
        try {
            VideoItem videoItem = new VideoItem();
            videoItem.setUrl(media.fileURL());
            videoItem.setWidth(media.mediaInfo().width());
            videoItem.setHeight(media.mediaInfo().height());
            videoItem.setDuration(media.mediaInfo().duration());
            videoItem.setFormat(media.mediaInfo().format());
            videoItem.setFileSize(media.mediaInfo().fileSize());
            videoItem.setBitRate(media.mediaInfo().bitrate());
            videoItem.setDefinition(parse(media.mediaInfo().width()));
            video.setSource(videoItem);

        } catch (Exception e) {
            video.getFailures().add(new FailedInfo(SNAPSHOT, "InvalidParameter.InvalidResource", e.getMessage()));
        }
    }

    private Consumer<TranscodeOutput> parseTranscodeOutput(Video video) {
        return transcodeOutput -> {

            if (!transcodeOutput.isSuccess()) {
                com.aliyun.vod.common.FailedInfo failedInfo = transcodeOutput.failedInfo();
                video.getFailures().add(new FailedInfo(TRANSCODE, failedInfo.getCode(), failedInfo.getMessage()));
                return;
            }

            VideoItem videoItem = new VideoItem();
            videoItem.setUrl(transcodeOutput.ossUrl());
            videoItem.setWidth(transcodeOutput.mediaInfo().width());
            videoItem.setHeight(transcodeOutput.mediaInfo().height());
            videoItem.setDuration(transcodeOutput.mediaInfo().duration());
            videoItem.setFormat(transcodeOutput.mediaInfo().format());
            videoItem.setFileSize(transcodeOutput.mediaInfo().fileSize());
            videoItem.setBitRate(transcodeOutput.mediaInfo().bitrate());
            videoItem.setDefinition(parse(transcodeOutput.mediaInfo().width()));

            video.getTranscodeVideos().add(videoItem);
        };
    }

    private Consumer<SnapshotOutput> parseSnapshotOutput(Video video) {
        return snapshotOutput -> {

            if (!snapshotOutput.isSuccess()) {
                com.aliyun.vod.common.FailedInfo failedInfo = snapshotOutput.failedInfo();
                video.getFailures().add(new FailedInfo(SNAPSHOT, failedInfo.getCode(), failedInfo.getMessage()));
                return;
            }

            video.getSnapshots().add(parseVideoSnapshot(snapshotOutput));
        };
    }

    private VideoSnapshot parseVideoSnapshot(SnapshotOutput snapshotOutput) {
        VideoSnapshot videoSnapshot = new VideoSnapshot();
        videoSnapshot.setUrl(snapshotOutput.ossUrl());
        return videoSnapshot;
    }

    private void deleteQueueMessage(String url, String queueName, String receiptHandle) {
        logger.debug("deleting message for file: {0}", receiptHandle);
        client.deleteQueueMessages(url, queueName, singletonList(receiptHandle));
    }

}
