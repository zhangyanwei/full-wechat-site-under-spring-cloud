/**
 * formart duration number
 * @param {number} duration
 * @returns {string}
 **/
define(['angular'], function () {

    'use strict';

    function durationFix() {
        return function (duration) {
            var durationText = "";
            duration = Number(duration);
            if (isNaN(duration)) {
                return "0:00";
            }
            if (duration < 1) {
                duration = 1;
            }
            var decimal = 0;
            duration = duration.toFixed(decimal);
            var hour = Math.floor(duration / 3600);
            var remainder = duration % 3600;
            var minute = Math.floor(remainder / 60);
            var second = (remainder % 60).toFixed(0);

            if (hour > 0) {
                durationText = hour + ":";
            }
            if (minute > 0) {
                durationText = durationText + minute + ":";
            } else {
                durationText = durationText + "00:";
            }
            if (second < 10) {
                second = "0" + second;
            }
            durationText = durationText + second;
            return durationText;
        }
    }

    angular.module('app.filter.durationFix', []).filter('durationFix', durationFix);
});