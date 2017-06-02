define(['angular'], function () {

    'use strict';

    function categoryLink() {
        return function (categoryCode) {
            var link;
            if (!categoryCode) {
                return link;
            }
            var index = categoryCode.indexOf('_');
            if (index < 0) {
                link = '/' + categoryCode;
            } else {
                var category = categoryCode.substring(0, index);
                link = '/' + category + '/' + categoryCode;
            }
            return link;
        }
    }

    angular.module('app.filter.categoryLink', []).filter('categoryLink', categoryLink);
});