package com.askdog.dao.repository.extend;

import com.askdog.model.data.StoreAttribute;
import com.askdog.model.entity.partial.TimeBasedStatistics;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.GeoPage;
import org.springframework.data.geo.Point;

import java.util.Date;
import java.util.List;

public interface StoreRepositoryExtension {

    List<TimeBasedStatistics> storeRegistrationStatistics(String unit, String interval);

    GeoPage<StoreAttribute> findStore(String cityCode, Point point, Pageable pageable);

}
