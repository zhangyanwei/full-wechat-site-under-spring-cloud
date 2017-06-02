define([
    'base/BaseController',
    'jquery.morris',
    'service/StoreService'
], function (BaseController) {

    var StoreDataController = BaseController.extend({

        init: function ($rootScope, $scope, $state, $stateParams, $storeService) {
            this.$rootScope = $rootScope;
            this.$state = $state;
            this.$stateParams = $stateParams;
            this.$storeService = $storeService;
            this._super($scope);
        },

        defineScope: function () {
            var owner = this;
            var time = setInterval(function () {
                if ($("#experience-creation-chart")) {
                    clearInterval(time);
                    owner._defineData();
                }
            }, 100);
        },

        _defineData: function () {
            var owner = this;
            owner.$storeService.getStoreData()
                .then(function (resp) {
                    owner._renderStoreData(resp);
                });
            owner.$storeService.getCouponData()
                .then(function (resp) {
                    owner._renderCouponData(resp);
                });
        },

        _renderStoreData: function (resp) {
            var data = [], map = {}, cachedItem, regSum = 0;
            resp.data.store_registration_trend.forEach(function (item) {
                cachedItem = {
                    time: item.time,
                    count: item.count
                };
                map[item.time] = cachedItem;
                data.push(cachedItem);
            });
            Morris.Line({
                element: 'agent-store-data',
                data: data,
                xkey: 'time',
                ykeys: ['count'],
                dateFormat: function (date) {
                    return new Date(date).format("yyyy-MM-dd");
                },
                labels: ['注册数量']
            });
        },

        _renderCouponData: function (resp) {
            var data = [], map = {}, cachedItem, regSum = 0;
            resp.data.coupon_creation_trend.forEach(function (item) {
                cachedItem = {
                    time: item.time,
                    count: item.count,
                    used_count: item.used_count
                };
                map[item.time] = cachedItem;
                data.push(cachedItem);
            });
            Morris.Line({
                element: 'agent-coupon-data',
                data: data,
                xkey: 'time',
                ykeys: ['count','used_count'],
                dateFormat: function (date) {
                    return new Date(date).format("yyyy-MM-dd");
                },
                labels: ['领取数量', '已使用数量']
            });
        }
    });

    StoreDataController.$inject = ['$rootScope', '$scope', '$state', '$stateParams', 'StoreService'];
    angular.module('module.StoreDataController', ['service.StoreService']).controller('StoreDataController', StoreDataController);

});
