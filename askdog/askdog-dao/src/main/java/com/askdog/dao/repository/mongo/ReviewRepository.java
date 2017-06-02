package com.askdog.dao.repository.mongo;

import com.askdog.model.common.EventType;
import com.askdog.model.common.EventType.EventTypeGroup;
import com.askdog.model.data.ReviewItem;
import com.askdog.model.data.ReviewItem.ReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

public interface ReviewRepository extends MongoRepository<ReviewItem, String> {
    long deleteByResourceTypeAndTargetIdAndReviewStatus(EventTypeGroup eventTypeGroup, Long targetId, ReviewStatus reviewStatus);

    Page<ReviewItem> findByResourceTypeInAndReviewStatus(Set<EventTypeGroup> eventTypeGroups, ReviewStatus reviewStatus, Pageable pageable);

    List<ReviewItem> findTop2ByEventTypeAndTargetIdOrderByReviewDateDesc(EventType eventType, Long targetId);

    ReviewItem findTopByTargetIdAndReviewStatusOrderByReviewDateDesc(Long id, ReviewStatus reject);

    ReviewItem findTopByTargetIdOrderByReviewDateDesc(Long id);
}
