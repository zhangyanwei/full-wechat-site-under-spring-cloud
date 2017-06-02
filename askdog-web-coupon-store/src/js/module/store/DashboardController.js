define([
    'base/BaseController',
    'jquery.morris',
    'service/CouponService'
], function (BaseController) {

    var DashboardController = BaseController.extend({

        init: function ($rootScope, $scope, $state, $stateParams, $couponService) {
            this.$rootScope = $rootScope;
            this.$state = $state;
            this.$stateParams = $stateParams;

            this.$couponService = $couponService;
            this._super($scope);

            this._defineDrawRenders();
        },

        _defineDrawRenders: function () {
            this.$couponService.statistics(this.$stateParams.storeId).then(
                function (resp) {
                    var time = setInterval(function () {
                        if ($("#pick-coupon")) {
                            clearInterval(time);
                            Morris.Donut({
                                element: 'forward-coupon',
                                data: [
                                    {label: "未使用", value: resp.data.FORWARDED.NOT_USED},
                                    {label: "已使用", value: resp.data.FORWARDED.USED}
                                ]
                            });

                            Morris.Donut({
                                element: 'pick-coupon',
                                data: [
                                    {label: "未使用", value: resp.data.NORMAL.NOT_USED},
                                    {label: "已使用", value: resp.data.NORMAL.USED}
                                ]
                            });
                        }
                    }, 100);
                }
            );


        }
    });

    DashboardController.$inject = ['$rootScope', '$scope', '$state', '$stateParams', 'CouponService'];
    angular.module('module.DashboardController', ['service.CouponService']).controller('DashboardController', DashboardController);

});
