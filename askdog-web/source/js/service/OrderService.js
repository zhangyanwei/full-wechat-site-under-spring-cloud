define(['base/BaseService'], function (BaseService) {

    var OrderService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        getOrder: function (experienceId, orderInfo) {
            var orderUri = "/api/orders/experience/{experienceId}/pay".format({
                "experienceId": experienceId
            });
            return this.post(orderUri, orderInfo);
        },

        getQRCode: function (orderId) {
            var qrCodeUri = "/api/orders/{orderId}/wxqrcode?width=800&height=800".format({
                "orderId": orderId
            });
            return this.get(qrCodeUri);
        }

    });

    angular.module('service.OrderService', [])
        .provider('OrderService', function () {
            this.$get = ['$http', '$rootScope', function (http, scope) {
                return new OrderService(http, scope);
            }];
        });

});