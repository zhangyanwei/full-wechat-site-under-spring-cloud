package com.askdog.dao.repository.mongo.extention;

import com.askdog.model.data.inner.VoteDirection;

public interface ProductStatisticsRepositoryExtension {
    long increaseViewCount(Long productId);

    long updateVoteCount(Long productId, VoteDirection current, VoteDirection update);
}