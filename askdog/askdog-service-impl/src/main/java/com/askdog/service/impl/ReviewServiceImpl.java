package com.askdog.service.impl;

import com.askdog.dao.repository.mongo.ReviewRepository;
import com.askdog.messaging.event.Event;
import com.askdog.messaging.event.EventMessage;
import com.askdog.messaging.review.ReviewEventMessageInputChannel;
import com.askdog.model.common.EventType;
import com.askdog.model.common.EventType.EventTypeGroup;
import com.askdog.model.data.ReviewItem;
import com.askdog.service.MessageService;
import com.askdog.service.ReviewService;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.review.PureReviewItem;
import com.askdog.service.bo.review.ReviewItemRo;
import com.askdog.service.exception.NotFoundException;
import com.askdog.service.helper.EventHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.Set;

import static com.askdog.common.utils.Conditions.checkState;
import static com.askdog.messaging.review.ReviewEventMessageInputChannel.REVIEW_EVENT;
import static com.askdog.model.common.EventType.EventTypeAction.DELETE;
import static com.askdog.model.data.ReviewItem.ReviewStatus.REVIEWING;
import static com.askdog.service.bo.common.PagedDataUtils.rePage;
import static com.askdog.service.exception.NotFoundException.Error.REVIEW_ITEM;


@RestController
@Service
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
@EnableBinding(ReviewEventMessageInputChannel.class)
public class ReviewServiceImpl implements ReviewService {

    @Autowired private EventHelper eventHelper;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private MessageService messageService;

    @StreamListener(REVIEW_EVENT)
    public void accept(EventMessage eventMessage) {
        Event event = eventMessage.getPayload();
        EventType eventType = event.getEventType();

        if (eventType.getEventTypeAction() == DELETE) {
            reviewRepository.deleteByResourceTypeAndTargetIdAndReviewStatus(eventType.getEventTypeGroup(), event.getTargetId(), REVIEWING);
            return;
        }

        // TODO check event types
        reviewRepository.deleteByResourceTypeAndTargetIdAndReviewStatus(eventType.getEventTypeGroup(), event.getTargetId(), REVIEWING);
        ReviewItem reviewItem = new ReviewItem();
        reviewItem.setPerformerId(event.getPerformerId());
        reviewItem.setEventType(eventType);
        reviewItem.setTargetId(event.getTargetId());
        reviewItem.setResourceType(eventType.getEventTypeGroup());
        reviewRepository.save(reviewItem);
    }

    @Override
    public ReviewItem get(@Nonnull @PathVariable("id") String reviewId) {
        return reviewRepository.findOne(reviewId);
    }

    @Override
    public ReviewItem getLatestReviewByTargetId(@Nonnull @RequestParam("targetId") Long targetId) {
        return reviewRepository.findTopByTargetIdOrderByReviewDateDesc(targetId);
    }

    @Override
    public void update(@Nonnull @PathVariable("id") String reviewId, @RequestBody PureReviewItem pureReviewItem) {
        ReviewItem reviewItem = reviewRepository.findOne(reviewId);
        checkState(reviewItem != null && reviewItem.getReviewStatus() == REVIEWING, () -> new NotFoundException(REVIEW_ITEM));

        reviewItem.setReviewStatus(pureReviewItem.getStatus());
        reviewItem.setRejectType(pureReviewItem.getRejectType());
        reviewItem.setReviewNote(pureReviewItem.getNote());
        reviewRepository.save(reviewItem);
        messageService.sendReviewMessage(reviewItem);
    }

    @Nonnull
    @Override
    public PagedData<ReviewItemRo> findAllReviewing(@Nonnull @RequestParam("type") EventTypeGroup eventTypeGroup,
                                                    @Nonnull @RequestBody Pageable pageable) {
        Set<EventTypeGroup> typeGroups = EnumSet.of(eventTypeGroup);
        return rePage(reviewRepository.findByResourceTypeInAndReviewStatus(typeGroups, REVIEWING, pageable), pageable,
                reviewItem -> {
                    ReviewItemRo reviewItemRo = new ReviewItemRo();
                    reviewItemRo.setReviewId(reviewItem.getId());
                    reviewItemRo.setEvent(eventHelper.convert(reviewItem.getPerformerId(), reviewItem.getEventType(), reviewItem.getTargetId()));
                    // TODO reviewItemRo.setDetail(new ReviewUserRo().from(userService.findDetail(reviewItem.getTargetId())));
                    return reviewItemRo;
                });
    }
}