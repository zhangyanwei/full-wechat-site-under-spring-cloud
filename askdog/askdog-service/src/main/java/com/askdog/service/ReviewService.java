package com.askdog.service;

import com.askdog.model.common.EventType.EventTypeGroup;
import com.askdog.model.data.ReviewItem;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.review.PureReviewItem;
import com.askdog.service.bo.review.ReviewItemRo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nonnull;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@FeignClient("service")
@RequestMapping("/service/reviews")
public interface ReviewService {

    @RequestMapping(value = "/{id}", method = GET)
    ReviewItem get(@Nonnull @PathVariable("id") String reviewId);

    @RequestMapping(method = GET)
    ReviewItem getLatestReviewByTargetId(@Nonnull @RequestParam("targetId") Long targetId);

    @RequestMapping(value = "/{id}", method = PUT)
    void update(@Nonnull @PathVariable("id") String reviewId, @RequestBody PureReviewItem pureReviewItem);

    @Nonnull
    @RequestMapping(value = "", method = POST)
    PagedData<ReviewItemRo> findAllReviewing(@Nonnull @RequestParam("type") EventTypeGroup eventTypeGroup,
                                             @Nonnull @RequestBody Pageable pageable);
}
