package com.askdog.model.data;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ad_product_statistics")
public class ProductStatistics extends Base {

    private static final long serialVersionUID = -1303279832886460499L;

    private long productId;
    private long viewCount;
    private long upVoteCount;
    private long downVoteCount;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

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
}