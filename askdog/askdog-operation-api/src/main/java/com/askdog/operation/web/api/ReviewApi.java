package com.askdog.operation.web.api;

import com.askdog.model.common.EventType.EventTypeGroup;
import com.askdog.model.data.ReviewItem;
import com.askdog.service.ReviewService;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.review.PureReviewItem;
import com.askdog.service.bo.review.ReviewItemRo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;


@RestController
@RequestMapping("/api/reviews")
public class ReviewApi {

    @Autowired private ReviewService reviewService;

    @PreAuthorize("hasRole('ROLE_OP_REVIEW')")
    @RequestMapping
    public PagedData<ReviewItemRo> findAllReviewing(@RequestParam("type") EventTypeGroup eventTypeGroup,
                                             @PageableDefault(sort = "eventDate", direction = ASC) Pageable pageable) {
        return reviewService.findAllReviewing(eventTypeGroup, pageable);
    }

    @RequestMapping(method = GET, params = {"targetId"})
    public ReviewItem getLatestReviewByTargetId(@Nonnull @RequestParam("targetId") Long targetId) {
        return reviewService.getLatestReviewByTargetId(targetId);
    }

    @PreAuthorize("hasRole('ROLE_OP_REVIEW')")
    @RequestMapping(value = "/{id}", method = PUT)
    void update(@PathVariable("id") String reviewId, @RequestBody PureReviewItem pureReviewItem) {
        reviewService.update(reviewId, pureReviewItem);
    }

}
