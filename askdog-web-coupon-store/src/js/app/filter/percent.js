/**
 * formart pages number
 * @param {number} total - data total count
 * @param {number} size - page display limit
 * @returns {number}
 **/
define(['angular'], function () {

    'use strict';

    function percent() {
        return function (dividend, divider) {
            if (typeof(dividend) != 'number' || typeof(divider) != 'number') {
                return '';
            }
            if(divider == 0){
                return 0;
            }else{
                return Math.ceil((dividend / divider) * 100);
            }
        }
    }

    angular.module('app.filter.percent', []).filter('percent', percent);
});