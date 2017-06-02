package com.askdog.model.validation;

import javax.validation.groups.Default;

public interface Group {

    interface Create extends Default {}
    interface CreateByApi extends Default {}
    interface Edit extends Default {}
    interface Delete extends Default {}

}
