define(['base/BaseService'], function (BaseService) {

    var UserCouponService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        usedCoupons: function (query, page, size) {
            var query_string = "";
            for (var name in query) {
                if (query.hasOwnProperty(name)) {
                    query[name] && (query_string += "&" + name + "=" + encodeURIComponent(query[name]));
                }
            }
            query_string && (query_string += "&");
            return this.get("/api/usercoupons/history?page={0}&size={1}".format(page, size) + query_string);
        },

        exportSearchResult: function (query) {
            var query_string = "";
            for (var name in query) {
                if (query.hasOwnProperty(name)) {
                    query[name] && (query_string += "&" + name + "=" + encodeURIComponent(query[name]));
                }
            }
            query_string && (query_string += "&");
            window.open(
                "http://store.coupon.askdog.com/api/usercoupons/history/excel?" + query_string,
                "_parent"
            );
        }
    });

    angular.module('service.UserCouponService', [])
        .provider('UserCouponService', function () {
            this.$get = ['$http', '$rootScope', function (http, scope) {
                return new UserCouponService(http, scope);
            }];
        });

});