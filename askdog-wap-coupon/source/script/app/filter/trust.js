/**
 * formart text html
 * @param {string} text
 * @returns {string}
 **/
define(['angular'], function () {

    'use strict';

    function trustHtml($sce) {
        return function (text) {
            return $sce.trustAsHtml(text);
        }
    }

    angular.module('app.filter.trust', []).filter('trusted', ["$sce", trustHtml]);
});