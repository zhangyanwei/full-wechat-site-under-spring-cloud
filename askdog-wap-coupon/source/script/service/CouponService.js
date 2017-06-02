define(['base/BaseService'], function (BaseService) {

    var CouponService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        cashList: function (page, size,state) {
            return this.get("/api/usercoupons?page={page}&size={size}&state={state}".format({"page": page, "size": size,"state":state}));
        },

        cashDetail: function (id) {
            return this.get("/api/usercoupons/" + id);
        },

        receiveCoupon: function (coupon_id) {
            var data = {
                coupon_id: coupon_id
            };
            return this.post('/api/usercoupons', data);
        },

        upgradeCoupon: function (coupon_id) {
            var data = {
                coupon_id: coupon_id
            };
            return this.put('/api/usercoupons', data);
        },

        verify: function (user_coupon_id, orderId) {

            var param = {
                "user_coupon_id": user_coupon_id,
                "order_id": orderId
            };

            var verifyURI = (orderId
                ? "/api/coupon/{user_coupon_id}?order_id={order_id}"
                : "/api/coupon/{user_coupon_id}")
                .format(param);

            return this.put(verifyURI);
        }
    });

    angular.module('service.CouponService', [])
        .provider('CouponService', function () {
            this.$get = ['$http', '$rootScope', function (http, scope) {
                return new CouponService(http, scope);
            }];
        });

});