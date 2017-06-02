define([
    'base/BaseService',
    'angular',
    'class'
], function (BaseService) {

    var OrderService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },
        getPayStatus: function (experienceId) {
            return this.get("/api/orders/experience/" + experienceId);
        }

    });

    angular.module('service.OrderService', [])
        .provider('orderService', function () {
            this.$get = ['$http', '$rootScope', function (http, scope) {
                return new OrderService(http, scope);
            }];
        });

});