define(['base/BaseService'], function (BaseService) {

    var QQMapService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        address: function (location) {
            var url = "http://apis.map.qq.com/ws/geocoder/v1/?key=EIGBZ-WMIWF-M4TJ7-NPBLX-TO4SS-GHB3U&location={0}%2C{1}&output=jsonp&callback=JSON_CALLBACK".format(location.latitude, location.longitude);
            return this.jsonp(url);
        }

    });

    angular.module('service.QQMapService', [])
        .provider('QQMapService', function () {
            this.$get = ['$http', '$rootScope', function (http, scope) {
                return new QQMapService(http, scope);
            }];
        });

});