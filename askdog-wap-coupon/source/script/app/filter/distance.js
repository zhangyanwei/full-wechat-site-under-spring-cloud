define(['angular'], function () {

    'use strict';

    function distance() {
        return function (distance) {
            distance  = Math.round(distance * 1000);

            if (distance < 1000) {
                return distance + " m";
            }

            distance = Math.round(distance / 100) / 10;

            if (distance > 10) {
                distance = Math.round(distance);
            }

            return distance + " km";
        }
    }

    angular.module('app.filter.distance', []).filter('distance', distance);
});


