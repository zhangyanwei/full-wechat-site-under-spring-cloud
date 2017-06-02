/**
 * formart input number
 * @param {number} input
 * @param {string} symbol - placeholder
 * @param {number} max - max display number
 * @returns {string}
 **/
define(['angular'], function () {

    'use strict';

    function ellipsis() {
        return function (text, symbol, max) {
            if (!text) {
                return text;
            }

            if (!max) {
                max = 20;
            }

            if (!symbol) {
                symbol = '...';
            }

            if (text.length > max) {
                return text.substring(0, max) + symbol;
            } else {
                return text;
            }
        }
    }

    angular.module('app.filter.ellipsis', []).filter('ellipsis', ellipsis);
});