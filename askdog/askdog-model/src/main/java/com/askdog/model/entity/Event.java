package com.askdog.model.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "mc_event")
public class Event extends Base {

    private static final long serialVersionUID = 6148513638277222700L;

    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "name", length = 15)
    private String name;

    @Size(max = 50000)
    @Column(name = "description", length = 50000)
    private String description;

    @Column(name = "period")
    private String period;

    /**
     * 活动内容，体验套餐等
     */
    @Column(name = "content", length = 50000)
    private String content;

    /**
     * 活动海报/视频封面
     */
    @Column(name = "poster_id")
    private Long poster;

    @Column(name = "video_id")
    private Long video;

    @NotNull
    @Column(name = "creation_time")
    private Date creationTime;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, targetEntity = Coupon.class)
    @JoinTable(
            name = "mc_event_coupon",
            joinColumns = {@JoinColumn(name = "event_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "coupon_id", referencedColumnName = "id")}
    )
    @Size(max = 2)
    private Set<Coupon> coupons;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false, updatable = false)
    private Store store;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getPoster() {
        return poster;
    }

    public void setPoster(Long poster) {
        this.poster = poster;
    }

    public Long getVideo() {
        return video;
    }

    public void setVideo(Long video) {
        this.video = video;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Set<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(Set<Coupon> coupons) {
        this.coupons = coupons;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
