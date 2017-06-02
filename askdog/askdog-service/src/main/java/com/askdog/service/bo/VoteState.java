package com.askdog.service.bo;

import com.askdog.model.data.inner.VoteDirection;

public class VoteState {

    private Long upVoteCount;
    private Long downVoteCount;
    private VoteDirection direction;

    public Long getUpVoteCount() {
        return upVoteCount;
    }

    public void setUpVoteCount(Long upVoteCount) {
        this.upVoteCount = upVoteCount;
    }

    public Long getDownVoteCount() {
        return downVoteCount;
    }

    public void setDownVoteCount(Long downVoteCount) {
        this.downVoteCount = downVoteCount;
    }

    public VoteDirection getDirection() {
        return direction;
    }

    public void setDirection(VoteDirection direction) {
        this.direction = direction;
    }
}
