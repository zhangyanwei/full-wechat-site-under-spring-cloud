package com.askdog.service.bo.product;

import com.askdog.common.Out;
import com.askdog.model.data.ProductStatistics;

import java.io.Serializable;

public class ProductDetailStatistics implements Serializable, Out<ProductDetailStatistics, ProductStatistics> {

    private static final long serialVersionUID = 5275314875009529172L;

    private long viewCount;
    private long upVoteCount;
    private long downVoteCount;

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    public long getUpVoteCount() {
        return upVoteCount;
    }

    public void setUpVoteCount(long upVoteCount) {
        this.upVoteCount = upVoteCount;
    }

    public long getDownVoteCount() {
        return downVoteCount;
    }

    public void setDownVoteCount(long downVoteCount) {
        this.downVoteCount = downVoteCount;
    }

    @Override
    public ProductDetailStatistics from(ProductStatistics entity) {
        this.viewCount = entity.getViewCount();
        this.upVoteCount = entity.getUpVoteCount();
        this.downVoteCount = entity.getDownVoteCount();
        return this;
    }
}