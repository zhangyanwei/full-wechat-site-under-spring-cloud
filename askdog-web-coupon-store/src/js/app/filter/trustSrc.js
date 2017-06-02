define(['angular'], function () {

    'use strict';

    function trustSrc($sce) {
        return function (src) {
            return $sce.trustAsResourceUrl(src);
        }
    }

    angular.module('app.filter.trustSrc', []).filter('trustSrc', ["$sce", trustSrc]);
});