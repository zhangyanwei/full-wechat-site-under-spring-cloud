package com.askdog.service.impl;

import com.askdog.dao.repository.CouponRepository;
import com.askdog.dao.repository.EventRepository;
import com.askdog.dao.repository.StoreRepository;
import com.askdog.dao.repository.UserCouponRepository;
import com.askdog.model.data.inner.ResourceDescription;
import com.askdog.model.data.video.Video;
import com.askdog.model.entity.Event;
import com.askdog.service.EventService;
import com.askdog.service.StoreService;
import com.askdog.service.bo.StoreDetail;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.event.AmendedEvent;
import com.askdog.service.bo.event.EventDetail;
import com.askdog.service.bo.event.PureEvent;
import com.askdog.service.exception.ForbiddenException;
import com.askdog.service.exception.NotFoundException;
import com.askdog.service.impl.cell.StoreCell;
import com.askdog.service.impl.storage.StorageRecorder;
import com.askdog.service.impl.storage.aliyun.description.OssVideoResourceDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.askdog.common.utils.Conditions.checkState;
import static com.askdog.dao.repository.UserCouponRepository.UserCouponSpecs.couponIn;
import static com.askdog.service.bo.common.PagedDataUtils.rePage;
import static com.askdog.service.exception.ForbiddenException.Error.*;
import static com.askdog.service.exception.NotFoundException.Error.EVENT;
import static com.google.common.collect.Sets.newHashSet;

@RestController
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;
    @Autowired
    private StorageRecorder storageRecorder;
    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreCell storeCell;

    @Override
    public void createEvent(@RequestParam("userId") Long userId, @Valid @RequestBody PureEvent pureEvent) {
        com.askdog.service.bo.StoreDetail storeDetail = storeService.findDetailWithState(userId, pureEvent.getStore(), false);
        checkState(storeDetail.isManageable(), () -> new ForbiddenException(CREATE_EVENT));

        Event event = new Event();
        event.setStore(storeRepository.findOne(pureEvent.getStore()));
        event.setName(pureEvent.getName());
        event.setDescription(pureEvent.getDescription());
        event.setContent(pureEvent.getContent());
        event.setPeriod(pureEvent.getPeriod());
        event.setPoster(pureEvent.getPoster());
        event.setVideo(pureEvent.getVideo());
        event.setCoupons(newHashSet(couponRepository.findAll(pureEvent.getCoupons())));
        event.setCreationTime(new Date());
        eventRepository.save(event);
        storeCell.refreshBasicCache(pureEvent.getStore());
    }

    @Override
    public void updateEvent(@RequestParam("userId") Long userId, @RequestBody AmendedEvent amendedEvent) {

        Long eventId = amendedEvent.getId();
        Event savedEvent = eventRepository.findOne(eventId);
        if (savedEvent == null) {
            throw new NotFoundException(EVENT);
        }

        com.askdog.service.bo.StoreDetail storeDetail = storeService.findDetailWithState(userId, savedEvent.getStore().getId(), false);
        checkState(storeDetail.isManageable(), () -> new ForbiddenException(CREATE_EVENT));

        savedEvent.setName(amendedEvent.getName());
        savedEvent.setDescription(amendedEvent.getDescription());
        savedEvent.setContent(amendedEvent.getContent());
        savedEvent.setPeriod(amendedEvent.getPeriod());
        savedEvent.setCoupons(newHashSet(couponRepository.findAll(amendedEvent.getCoupons())));

        Long poster = amendedEvent.getPoster();
        if (poster != null) {
            savedEvent.setPoster(poster);
        }

        Long video = amendedEvent.getVideo();
        if (video != null) {
            savedEvent.setVideo(video);
        }

        eventRepository.save(savedEvent);
        storeCell.refreshBasicCache(storeDetail.getId());
    }

    @Override
    public EventDetail getEvent(@PathVariable("eventId") long eventId) {
        Event event = eventRepository.findOne(eventId);
        if (event == null) {
            throw new NotFoundException(EVENT);
        }

        return toEventDetail(event);
    }

    @Override
    public void deleteEvent(@RequestParam("userId") Long userId, @PathVariable("eventId") long eventId) {
        Event event = eventRepository.findOne(eventId);
        if (event == null) {
            throw new NotFoundException(EVENT);
        }

        com.askdog.service.bo.StoreDetail storeDetail = storeService.findDetailWithState(userId, event.getStore().getId(), false);
        checkState(storeDetail.isManageable(), () -> new ForbiddenException(DELETE_EVENT));
        eventRepository.delete(eventId);
        storeCell.refreshBasicCache(storeDetail.getId());
    }

    @Override
    public PagedData<EventDetail> getEvents(@RequestParam("userId") Long userId, @RequestParam("storeId") long storeId, @RequestBody Pageable pageable) {
        com.askdog.service.bo.StoreDetail storeDetail = storeService.findDetailWithState(userId, storeId, false);
        checkState(storeDetail.isManageable(), () -> new ForbiddenException(EVENT_LIST));

        Page<Event> events = eventRepository.findByStore_Id(storeId, pageable);
        return rePage(events, pageable, this::toEventDetail);
    }

    private EventDetail toEventDetail(Event event) {
        EventDetail eventDetail = new EventDetail();
        eventDetail.setId(event.getId());
        eventDetail.setName(event.getName());
        eventDetail.setDescription(event.getDescription());
        eventDetail.setContent(event.getContent());
        eventDetail.setPeriod(event.getPeriod());

        Long poster = event.getPoster();
        if (poster != null) {
            eventDetail.setPoster(storageRecorder.getResource(poster).getDescription().getResourceUrl());
        }

        Long videoId = event.getVideo();
        if (videoId != null) {
            ResourceDescription description = storageRecorder.getResource(videoId).getDescription();
            if (description instanceof OssVideoResourceDescription) {
                Video video = ((OssVideoResourceDescription) description).getVideo();
                eventDetail.setVideo(video);
            }
        }

        StoreDetail storeDetail = storeCell.findDetail(event.getStore().getId());
        EventDetail.Store store = new EventDetail.Store();
        store.setId(storeDetail.getId());
        store.setName(storeDetail.getName());
        store.setAddress(storeDetail.getAddress());
        store.setLocation(storeDetail.getLocation());
        store.setPhone(storeDetail.getPhone());
        store.setType(storeDetail.getType());
        store.setCpc(storeDetail.getCpc());
        eventDetail.setStore(store);

        List<Long> couponIds = new ArrayList<>();
        List<EventDetail.Coupon> coupons = event.getCoupons().stream().map(coupon -> {
            couponIds.add(coupon.getId());
            EventDetail.Coupon basic = new EventDetail.Coupon();
            basic.setId(coupon.getId());
            basic.setName(coupon.getName());
            return basic;
        }).collect(Collectors.toList());
        eventDetail.setCoupons(coupons);

        if (couponIds.size() > 0) {
            eventDetail.setUserCouponCount(userCouponRepository.count(couponIn(couponIds)));
        }

        eventDetail.setCreationTime(event.getCreationTime());
        return eventDetail;
    }
}
