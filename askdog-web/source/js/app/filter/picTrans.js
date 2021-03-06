define(['angular'], function () {

    'use strict';

    function transform() {
        return function (input, imageParams) {

            if (!input) {
                return input;
            }

            for (var i = 0; i < AskDog.Constants.PICTRUE_URL_MAPPINS.length; i++) {
                var from = AskDog.Constants.PICTRUE_URL_MAPPINS[i].from;
                var to = AskDog.Constants.PICTRUE_URL_MAPPINS[i].to;

                if (input.startWith(from)) {
                    input = input.replace(from, to) + '@' + imageParams;
                }
            }

            return input;
        }
    }

    angular.module('app.filter.picTrans', []).filter('picTrans', transform);
});


