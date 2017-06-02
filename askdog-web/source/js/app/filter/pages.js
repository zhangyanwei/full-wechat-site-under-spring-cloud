/**
 * formart pages number
 * @param {number} total - data total count
 * @param {number} size - page display limit
 * @returns {number}
 **/
define(['angular'], function () {

    'use strict';

    function pages() {
        return function (total, size) {
            if (typeof(total) != 'number' || typeof(size) != 'number') {
                return '';
            }
            return Math.ceil(total / size);
        }
    }

    angular.module('app.filter.pages', []).filter('pages', pages);
});