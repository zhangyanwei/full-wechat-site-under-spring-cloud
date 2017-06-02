package com.askdog.dao.repository.mongo.extention.impl;

import com.askdog.dao.repository.mongo.extention.ProductStatisticsRepositoryExtension;
import com.askdog.model.data.ProductStatistics;
import com.askdog.model.data.inner.VoteDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static com.askdog.model.data.inner.VoteDirection.DOWN;
import static com.askdog.model.data.inner.VoteDirection.UP;

public class ProductStatisticsRepositoryImpl implements ProductStatisticsRepositoryExtension {

    private final FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true);

    @Autowired private MongoTemplate mongoTemplate;

    @Override
    public long increaseViewCount(Long productId) {
        Query query = new Query().addCriteria(Criteria.where("productId").is(productId));
        Update update = new Update().inc("viewCount", 1);

        return mongoTemplate.findAndModify(query, update, options, ProductStatistics.class).getViewCount();
    }

    @Override
    public long updateVoteCount(Long productId, VoteDirection current, VoteDirection update) {
        Query query = new Query().addCriteria(Criteria.where("productId").is(productId));
        Update updateOp = new Update();

        if (current != update) {
            updateOp.inc("upVoteCount", current == UP ? -1 : update == UP ? 1 : 0);
            updateOp.inc("downVoteCount", current == DOWN ? -1 : update == DOWN ? 1 : 0);
        }

        return mongoTemplate.findAndModify(query, updateOp, options, ProductStatistics.class).getUpVoteCount();
    }
}