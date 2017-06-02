package com.askdog.service.impl.cache.hibernate.redis.strategy;

import com.askdog.service.impl.cache.hibernate.redis.regions.RedisCollectionRegion;
import com.askdog.service.impl.cache.hibernate.redis.regions.RedisEntityRegion;
import com.askdog.service.impl.cache.hibernate.redis.regions.RedisNaturalIdRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;

public interface RedisAccessStrategyFactory {

    /**
     * Create {@link EntityRegionAccessStrategy}
     * for the input {@link com.askdog.service.impl.cache.hibernate.redis.regions.RedisEntityRegion} and {@link AccessType}
     *
     * @return the created {@link EntityRegionAccessStrategy}
     */
    EntityRegionAccessStrategy createEntityRegionAccessStrategy(RedisEntityRegion entityRegion, AccessType accessType);

    /**
     * Create {@link CollectionRegionAccessStrategy}
     * for the input {@link com.askdog.service.impl.cache.hibernate.redis.regions.RedisCollectionRegion} and {@link AccessType}
     *
     * @return the created {@link com.askdog.service.impl.cache.hibernate.redis.regions.RedisCollectionRegion}
     */
    CollectionRegionAccessStrategy createCollectionRegionAccessStrategy(RedisCollectionRegion collectionRegion, AccessType accessType);

    /**
     * Create {@link CollectionRegionAccessStrategy}
     * for the input {@link com.askdog.service.impl.cache.hibernate.redis.regions.RedisNaturalIdRegion} and {@link AccessType}
     *
     * @return the created {@link com.askdog.service.impl.cache.hibernate.redis.regions.RedisNaturalIdRegion}
     */
    NaturalIdRegionAccessStrategy createNaturalIdRegionAccessStrategy(RedisNaturalIdRegion naturalIdRegion, AccessType accessType);

}
