package com.askdog.bootstrap.core;

import com.askdog.model.entity.Template;
import com.askdog.model.entity.User;

public interface DataCreator {

    DataCreator user(User user);

    DataCreator template(Template template);

}
