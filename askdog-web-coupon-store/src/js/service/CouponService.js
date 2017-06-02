define(['base/BaseService'], function (BaseService) {

    var CouponService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        verify: function (user_coupon_id) {
            var verifyURI = "/api/coupon/{user_coupon_id}".format({
                "user_coupon_id": user_coupon_id
            });
            return this.put(verifyURI);
        },

        statistics: function (storeId) {
            var uri = '/api/coupon/statistics?storeId={storeId}'.format({
                storeId: storeId
            });
            return this.get(uri)
        },

        couponList: function (id, page, size) {
            return this.get("/api/coupon?storeId={id}&page={page}&size={size}".format({
                "id": id,
                "page": page,
                "size": size
            }));
        },

        storeOwnedCoupons: function (storeId) {
            // TODO
            var uri = '/api/coupon?storeId={storeId}&page=0&size=100'.format({
                'storeId': storeId
            });
            return this.get(uri);
        },

        createCoupons: function (pureCoupon) {
            return this.post('/api/coupon', pureCoupon);
        }
    });

    angular.module('service.CouponService', [])
        .provider('CouponService', function () {
            this.$get = ['$http', '$rootScope', function (http, scope) {
                return new CouponService(http, scope);
            }];
        });

});