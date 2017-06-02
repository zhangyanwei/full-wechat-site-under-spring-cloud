/**
 * formart input time
 * @param {number} input - timestamp
 * @returns {string} formated time with suffix
 **/
define(['angular'], function () {

    'use strict';

    var formats = {
        'piece': 1,
        'tenthousand': 10000,
        'hundredmillion': 100000000
    };

    var suffixes = {
        'piece': '',
        'tenthousand': '万',
        'hundredmillion': '亿'
    };

    function count() {
        return function (input) {
            if (isNaN(input)) {
                return input;
            }

            var unitlist = ['piece', 'tenthousand', 'hundredmillion'];
            var unit = unitlist.shift();
            var result = parseInt(input / formats[unit]);

            while (unitlist.length && (input / formats[unitlist[0]] > 1)) {
                unit = unitlist.shift();
                result = parseInt(input / formats[unit]);
            }

            return result.toString() + suffixes[unit];
        }
    }

    angular.module('app.filter.count', []).filter('count', count);
});


