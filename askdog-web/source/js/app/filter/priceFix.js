/**
 * formart price number
 * @param {number} price
 * @param {number} decimal - decimal places
 * @returns {number}
 **/
define(['angular'], function () {

    'use strict';

    function priceFix() {
        return function (price, decimal, unit) {

            if (!price) {
                return price;
            }
            price = Number(price);
            if (isNaN(price)) {
                return price;
            }
            if (!decimal) {
                decimal = 2;
            }
            if (!unit) {
                unit = 0.01
            }

            price = (price * unit).toFixed(decimal);
            return price;
        }
    }

    angular.module('app.filter.priceFix', []).filter('priceFix', priceFix);
});