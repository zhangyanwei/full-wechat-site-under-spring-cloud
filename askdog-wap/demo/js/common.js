//Load common code that includes config, then load the app
//logic for this page. Do the require calls here instead of
//a separate file so after a build there are only 2 HTTP
//requests instead of three.
define(['askdog-exp'], function () {

    var lib = AskDogExp.Util.libPath;

    //For any third party dependencies, like jQuery, place them in the lib folder.
    requirejs.config({

        //To get timely, correct error triggers in IE, force a define/shim exports check.
        //enforceDefine: true,

        waitSeconds: 0,

        baseUrl: '/js',

        paths: {
            'lib': AskDogExp.Util.LIB_PATH,

            // requirejs plugins
            'domReady': lib('domReady/domReady'),

            // jquery and plugins
            'jquery': [
                // 'https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min',
                lib('jquery/jquery.min')
            ],
            'validate': lib('jquery-plugin/validate'),
            'validator': lib('jquery-plugin/validator/jquery.validate'),
            // angular and plugins
            'angular': [
                // 'https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular',
                // 'https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min',
                lib('angular/angular.min')
            ],
            'angular.ui.router': lib('angular/angular-ui-router.min'),
            'angular.ui.bootstrap': lib('angular/ui-bootstrap-tpls-2.0.0.min'),
            'angular.translate': lib('angular/angular-translate.min'),
            'angular.translate.loaderStaticFiles': lib('angular/angular-translate-loader-static-files.min'),
            'angular.oc.lazyLoad': [
                lib("angular/oclazyload/ocLazyLoad"),
                lib("angular/oclazyload/ocLazyLoad.min")
            ],
            'angular.sanitize': lib('angular/angular-sanitize.min'),
            'angular-messages': lib('angular/angular-messages.min'),
            'angular-resource': lib('angular/angular-resource.min'),
            'angular-touch': lib('angular/angular-touch.min'),

            // Add for editor md
            'katex': lib('editormd/lib/katex.min'),
            'marked': lib('editormd/lib/marked.min'),
            'prettify': lib('editormd/lib/prettify.min'),
            'raphael': lib('editormd/lib/raphael.min'),
            'underscore': lib('editormd/lib/underscore.min'),
            'sequence-diagram': lib('editormd/lib/sequence-diagram.min'),
            'flowchart': lib('editormd/lib/flowchart.min'),
            'jqueryflowchart': lib('editormd/lib/jquery.flowchart.min'),
            'editor-md': lib('editormd/editormd'),
            // Add for editor md />

            //web socket
            'socket': lib('socket/sockjs.min'),
            'stomp': lib('socket/stomp.min'),

            // bootstrap and plugins
            'bootstrap': lib('bootstrap/bootstrap'),
            'class': lib('class'),
            'namespace': lib('namespace'),

            //jquery plugins
            'jquery-plugin': lib("jquery-plugin"),
            'jquery.video': lib('jquery-plugin/video/video'),
            'jquery.velocity': lib('jquery-plugin/velocity'),
            "jquery-toggle-password": lib("jquery-plugin/jquery.toggle-password"),

            // weixin
            'weixin': lib("wechat/jweixin-1.0.0")

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
            'angular.translate': ['angular'],
            'angular.translate.loaderStaticFiles': ['angular.translate'],
            'angular.oc.lazyLoad': ['angular'],

            // twitter bootstrap
            'bootstrap': ['jquery'],

            // Add for editor md
            'sequence-diagram': ['flowchart'],
            'jqueryflowchart': ['flowchart'],
            // 'katex'
            'editor-md': ['marked', 'prettify', 'raphael', 'underscore', 'sequence-diagram', 'flowchart', 'jqueryflowchart'],
            // Add for editor md />
            'validate': ['validator']
        }
    });

});