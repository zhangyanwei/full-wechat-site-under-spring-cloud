package com.askdog.model.data;

import com.askdog.model.data.inner.VoteDirection;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;

public interface Actions {

    // abstract supper action definition
    abstract class Action extends Target {

        private static final long serialVersionUID = -5348984259583069521L;

        private Long user;

        @NotNull
        private Date time;

        public Long getUser() {
            return user;
        }

        public void setUser(Long user) {
            this.user = user;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }
    }

    @Document(collection = "ad_vote")
    class VoteAction extends Action {

        private static final long serialVersionUID = 6731418374631610651L;

        private VoteDirection direction;

        public VoteDirection getDirection() {
            return direction;
        }

        public void setDirection(VoteDirection direction) {
            this.direction = direction;
        }
    }
}
