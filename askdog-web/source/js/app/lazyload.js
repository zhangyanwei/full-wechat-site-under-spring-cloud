define([
    'angular.oc.lazyLoad'
], function () {
    'use strict';

    angular.module('app.lazyload', [
        'oc.lazyLoad'
    ])
        .config([
            "$ocLazyLoadProvider",
            function ($ocLazyLoadProvider) {
                $ocLazyLoadProvider.config({
                    // Set to true if you want to see what and when is dynamically loaded
                    // debug: true,
                    events: true,
                    jsLoader: requirejs
                });
            }
        ])

});