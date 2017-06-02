require({async: false}, ["jquery"]);
define(function () {
    jQuery.validator.methods.phone = function (value, element) {
        return this.optional(element) || /^(1[3578][0-9]{9}|(\\d{3,4}-)\\d{7,8}(-\\d{1,4})?)$/.test(value);
    };
    jQuery.validator.methods.username = function (value, element) {
        return this.optional(element) || /^[^-!$%^&*()_+|~=`{}[\]:/;<>?,.@#'"\\]{1,20}$/.test(value);
    };
    jQuery.validator.methods.password = function (value, element) {
        return this.optional(element) || /^[\s\S]{6,20}$/.test(value);
    };
    jQuery.validator.methods.email = function (value, element) {
        return this.optional(element) || /^[^@\s]+@(?:[^@\s.]+)(?:\.[^@\s.]+)+$/.test(value);
    };
    jQuery.validator.methods.emailOrPhone = function (value, element) {
        return this.optional(element) || /^[^@\s]+@(?:[^@\s.]+)(?:\.[^@\s.]+)+$/.test(value) || /^(1[3578][0-9]{9}|(\\d{3,4}-)\\d{7,8}(-\\d{1,4})?)$/.test(value);
    };
    jQuery.validator.setDefaults({
        errorElement: "small",
        errorPlacement: function (error, element) {
            // Add the `help-block` class to the error element
            error.addClass("help-block");

            // Add `has-feedback` class to the parent div.form-group
            // in order to add icons to inputs
            element.parents(".control-group, .form-group").addClass("has-feedback");
            if (element.prop("type") === "checkbox") {
                error.insertAfter(element.parent("label"));
            } else {
                error.insertAfter(element);
            }

            // Add the span element, if doesn't exists, and apply the icon classes to it.

        },

        highlight: function (element, errorClass, validClass) {
            $(element).parents(".control-group, .form-group").addClass("has-error").removeClass("has-success");
        },
        unhighlight: function (element, errorClass, validClass) {
            $(element).parents(".control-group, .form-group").addClass("has-success").removeClass("has-error");
        }
    });
    return jQuery;
});