package com.askdog.service.helper;

import com.askdog.model.common.EventType;
import com.askdog.model.common.EventType.EventTypeGroup;
import com.askdog.service.StoreService;
import com.askdog.service.UserCouponService;
import com.askdog.service.UserService;
import com.askdog.service.bo.StoreDetail;
import com.askdog.service.bo.system.event.EventRo;
import com.askdog.service.bo.system.event.EventTarget;
import com.askdog.service.bo.system.event.EventUser;
import com.askdog.service.bo.usercoupon.UserCouponDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.askdog.model.common.EventType.EventTypeGroup.STORE;
import static com.askdog.model.common.EventType.EventTypeGroup.USER_COUPON;

@Component
public class EventHelper {

    @Autowired private UserService userService;
    @Autowired private UserCouponService userCouponService;
    @Autowired private StoreService storeService;

    public EventRo convert(Long performerId, EventType eventType, Long targetId) {

        EventTypeGroup eventTypeGroup = eventType.getEventTypeGroup();

        EventRo eventRo = new EventRo();
        eventRo.setType(eventType);
        eventRo.setUser(new EventUser().setId(performerId).setName(userService.findDetail(performerId).getNickname()));

        switch (eventTypeGroup) {
            case USER_COUPON: {
                UserCouponDetail userCouponDetail = userCouponService.getDetail(null, targetId);
                eventRo.setTarget(new EventTarget(USER_COUPON, targetId, userCouponDetail.getCouponDetail().getName(), false));
                break;
            }

            case STORE: {
                StoreDetail storeDetail = storeService.findDetail(targetId, true);
                eventRo.setTarget(new EventTarget(STORE, targetId, storeDetail.getName(), storeDetail.isDeleted()));
                break;
            }
        }

        return eventRo;
    }
}