//Load common code that includes config, then load the app
//logic for this page. Do the require calls here instead of
//a separate file so after a build there are only 2 HTTP
//requests instead of three.
define(['askDog'], function () {

    //For any third party dependencies, like jQuery, place them in the lib folder.
    requirejs.config({

        //To get timely, correct error triggers in IE, force a define/shim exports check.
        //enforceDefine: true,
        waitSeconds: 0,
        baseUrl: 'script',
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
            'jquery.velocity': '../lib/jquery/velocity',
            'jquery.qrcode': '../lib/jquery/qrcode',
            'jquery.video': '../lib/jquery/video/video',

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
            'angular-imgcrop': '../lib/angular/imgcrop/ng-img-crop.min',
            'angular-cookies': '../lib/angular/angular-cookies.min',

            // editor md lib
            'katex': '../lib/editormd/lib/katex.min',
            'marked': '../lib/editormd/lib/marked.min',
            'prettify': '../lib/editormd/lib/prettify.min',
            'raphael': '../lib/editormd/lib/raphael.min',
            'underscore': '../lib/editormd/lib/underscore.min',
            'sequence-diagram': '../lib/editormd/lib/sequence-diagram.min',
            'flowchart': '../lib/editormd/lib/flowchart.min',
            'jqueryflowchart': '../lib/editormd/lib/jquery.flowchart.min',
            'editor-md': '../lib/editormd/editormd',

            //web socket
            'socket': '../lib/socket/sockjs.min',
            'stomp': '../lib/socket/stomp.min',

            //aliyun vod lib
            'aliyun-sdk': '../lib/aliyun/aliyun-sdk.min',
            'vod-sdk': '../lib/aliyun/vod-sdk-upload.min',

            // wechat
            'wechat': '../lib/wechat/jweixin-1.0.0',

            'swiper': '../lib/swiper/swiper',

            // bootstrap and plugins
            'bootstrap': '../lib/bootstrap/js/bootstrap.min',
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
            'jquery.velocity': ['jquery'],
            'jquery.qrcode': ['jquery'],
            'jquery.video': ['jquery'],

            // editor md
            'sequence-diagram': ['flowchart', 'raphael'],
            'jqueryflowchart': ['flowchart', 'jquery'],
            'editor-md': ['marked', 'prettify', 'raphael', 'underscore', 'sequence-diagram', 'jqueryflowchart'],

            //OSS storage
            'vod-sdk': ['aliyun-sdk']
        }
    });

});