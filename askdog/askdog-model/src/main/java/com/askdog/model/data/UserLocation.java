package com.askdog.model.data;

import com.askdog.model.data.inner.location.LocationRecord;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ad_user_location")
public class UserLocation extends LocationRecord {
    private static final long serialVersionUID = 3336531956056044289L;
}
