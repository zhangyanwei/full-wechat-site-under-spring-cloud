//Load common code that includes config, then load the app
//logic for this page. Do the require calls here instead of
//a separate file so after a build there are only 2 HTTP
//requests instead of three.
define(['coupon-store'], function () {

    //For any third party dependencies, like jQuery, place them in the lib folder.
    requirejs.config({

        //To get timely, correct error triggers in IE, force a define/shim exports check.
        //enforceDefine: true,
        baseUrl: 'js',
        paths: {
            'lib': '../lib',

            // requirejs plugins
            'domReady': '../lib/domReady/domReady',

            // jquery and plugins
            'jquery': [
                // 'https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min',
                '../lib/jquery/jquery.min'
            ],
            'jquery.validator': '../lib/jquery/validate',
            'jquery.qrcode': '../lib/jquery/qrcode',
            'jquery.velocity': '../lib/jquery/velocity',
            'jquery.morris': '../lib/jquery/morris/morris.min',
            'raphael': '../lib/raphael',

            // angular and plugins
            'angular': [
                // 'https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular',
                // 'https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min',
                '../lib/angular/angular.min'
            ],
            'angular.ui.router': '../lib/angular/angular-ui-router.min',
            'angular.ui.bootstrap': '../lib/angular/ui-bootstrap-tpls-2.0.0.min',
            'angular.oc.lazyLoad': [
                '../lib/angular/oclazyload/ocLazyLoad.min'
            ],
            'angular.sanitize': '../lib/angular/angular-sanitize.min',
            'angular-messages': '../lib/angular/angular-messages.min',
            'angular-resource': '../lib/angular/angular-resource.min',
            'angular-touch': '../lib/angular/angular-touch.min',

            //aliyun vod lib
            'aliyun-sdk': '../lib/aliyun/aliyun-sdk.min',
            'vod-sdk': '../lib/aliyun/vod-sdk-upload.min',

            //web socket
            'socket': '../lib/socket/sockjs.min',
            'stomp': '../lib/socket/stomp.min',

            // bootstrap and plugins
            'bootstrap': '../lib/bootstrap/js/bootstrap.min',
            'bootstrap.datetimepicker.base': '../lib/bootstrap/datetimepicker/bootstrap-datetimepicker.min',
            'bootstrap.datetimepicker': '../lib/bootstrap/datetimepicker/locales/bootstrap-datetimepicker.zh-CN',

            'class': '../lib/class',
            'namespace': '../lib/namespace'
        },

        //Remember: only use shim config for non-AMD scripts,
        //scripts that do not already call define(). The shim
        //config will not work correctly if used on AMD scripts,
        //in particular, the exports and init config will not
        //be triggered, and the deps config will be confusing
        //for those cases.
        shim: {
            'class': {
                exports: 'Class'
            },
            // exports
            'angular': {
                exports: 'angular'
            },

            // angular
            'angular.ui.router': ['angular'],
            'angular.ui.bootstrap': ['angular', 'bootstrap'],
            'angular.oc.lazyLoad': ['angular'],

            // dependencies
            // jquery
            'jquery.ui': ['jquery'],
            'bootstrap': ['jquery'],
            'jquery.validator': ['jquery'],
            'jquery.qrcode': ['jquery'],
            'jquery.velocity': ['jquery'],
            'jquery.morris': ['jquery', 'raphael'],

            // 'bootstrap.datetimepicker.base': ['bootstrap'],
            'bootstrap.datetimepicker': ['bootstrap', 'bootstrap.datetimepicker.base'],

            //OSS storage
            'vod-sdk': ['aliyun-sdk']
        }
    });

});