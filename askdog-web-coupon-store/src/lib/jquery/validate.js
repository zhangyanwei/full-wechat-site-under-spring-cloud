define(["lib/jquery/validate/jquery.validate.min"], function () {

    jQuery.validator.methods.phone = function (value, element) {
        return this.optional(element) || /^(1[3578][0-9]{9}|(\\d{3,4}-)\\d{7,8}(-\\d{1,4})?)$/.test(value);
    };
    jQuery.validator.methods.password = function (value, element) {
        return this.optional(element) || /^[\s\S]{6,20}$/.test(value);
    };
    jQuery.validator.methods.username = function (value, element) {
        return this.optional(element) || /^[a-zA-Z][\w.]{3,18}$/.test(value);
    };
    jQuery.validator.methods.nickname = function (value, element) {
        return this.optional(element) || /^[^-!$%^&*()_+|~=`{}\[\]:/;<>?,.@#'"\\]{1,20}$/.test(value);
    };
    jQuery.validator.methods.email = function (value, element) {
        return this.optional(element) || /^[^@\s]+@(?:[^@\s.]+)(?:\.[^@\s.]+)+$/.test(value);
    };
    jQuery.validator.methods.couponNO = function (value, element) {
        return this.optional(element) || /^\d{10,}$/.test(value);
    };
    jQuery.validator.methods.number = function (value, element) {
        return this.optional(element) || /^(([1-9]+[0-9]*.{1}[0-9]+)|([0].{1}[1-9]+[0-9]*)|([1-9][0-9]*)|([0][.][0-9]+[1-9]*))$/.test(value);
    };
    jQuery.validator.methods.fixPhone = function (value, element) {
        return this.optional(element) || /^(0\d{2,3}-\d{5,9}|0\d{2,3}-\d{5,9})$/.test(value);
    };
    jQuery.validator.setDefaults({
        errorElement: "small",
        errorPlacement: function (error, element) {
            // Add the `help-block` class to the error element
            //error.addClass("help-block");
            // Add `has-feedback` class to the parent div.form-group
            // in order to add icons to inputs
            element.parents(".control-group, .form-group").addClass("has-feedback");
            if (element.prop("type") === "checkbox") {
                error.insertAfter(element.parent("label"));
            } else {
                error.insertAfter(element);
            }
            // // Add the span element, if doesn't exists, and apply the icon classes to it.
            // if (!element.next("span")[0]) {
            //     $("<span class='glyphicon glyphicon-remove form-control-feedback'></span>").insertAfter(element);
            // }
        },
        success: function (label, element) {
            // Add the span element, if doesn't exists, and apply the icon classes to it.
            // if (!$(element).next( "span" )[0] ) {
            //     $("<span class='glyphicon glyphicon-ok form-control-feedback'></span>").insertAfter($(element));
            // }
        },
        highlight: function (element, errorClass, validClass) {
            $(element).parents(".control-group, .form-group").addClass("has-error").removeClass("has-success");
            //$(element).next("span").addClass("glyphicon-remove").removeClass("glyphicon-ok");
        },
        unhighlight: function (element, errorClass, validClass) {
            $(element).parents(".control-group, .form-group").addClass("has-success").removeClass("has-error");
            //$(element).next("span").addClass("glyphicon-ok").removeClass("glyphicon-remove");
        }
    });
    return jQuery;
});