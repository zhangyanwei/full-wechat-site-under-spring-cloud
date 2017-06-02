define(['base/BaseController', 'service/UserCouponService', 'service/StoreService', 'jquery.validator', 'bootstrap.datetimepicker'], function (BaseController) {

    var CouponHistoryController = BaseController.extend({

        init: function ($rootScope, $scope, $state, $stateParams, $userCouponService, $storeService) {
            this.$rootScope = $rootScope;
            this.$state = $state;
            this.$stateParams = $stateParams;
            this.$userCouponService = $userCouponService;
            this.$storeService = $storeService;
            this._super($scope);
        },

        defineScope: function () {
            this._defineRenders();
            this._defineEmployees();
            this._defineSearch();
            this._defineExport()
        },

        _defineRenders: function () {
            this.$scope.datetimepicker = function () {
                $(this).datetimepicker({
                    language: 'zh-CN',
                    weekStart: 1,
                    todayBtn: 1,
                    autoclose: 1,
                    todayHighlight: 1,
                    startView: 2,
                    forceParse: 0,
                    showMeridian: 1
                });
            };
        },

        _defineEmployees: function () {
            this.$scope.selectEmployee = function (employee) {
                this.$scope.verifier = employee;
            }.bind(this);
            this.$storeService.getEmployees(this.$stateParams.storeId, 0, Number.MAX_VALUE).then(
                function (resp) {
                    this.$scope.employees = resp.data.result;
                }.bind(this),
                function (resp) {
                    this.$scope.message = resp.data.message || '服务器正忙，请稍后再试';
                }.bind(this)
            );
        },

        _defineSearch: function () {
            this.$scope.search = function () {
                var owner = this;
                var query = {}, inputs = $('form').serializeArray();
                $.each(inputs, function (i, input) {
                    input.value !== "" && (query[input.name] = input.value);
                });
                owner.$stateParams.storeId !== "" && (query.storeId = owner.$stateParams.storeId);
                this.$scope.query = query;
                this.$scope.goPage(0);
            }.bind(this);

            this.$scope.goPage = function (page) {
                this.$scope.message = undefined;
                this.$userCouponService.usedCoupons(this.$scope.query, page || 0, 10).then(
                    function (resp) {
                        this.$scope.data = resp.data;
                    }.bind(this),
                    function (resp) {
                        this.$scope.message = resp.data.message || '服务器正忙，请稍后再试';
                    }.bind(this)
                );
            }.bind(this);
        },

        _defineExport: function () {
            this.$scope.export = function () {
                var owner = this;
                var query = {}, inputs = $('form').serializeArray();
                $.each(inputs, function (i, input) {
                    input.value !== "" && (query[input.name] = input.value);
                });
                owner.$stateParams.storeId !== "" && (query.storeId = owner.$stateParams.storeId);
                this.$scope.query = query;
                this.$scope.message = undefined;
                this.$userCouponService.exportSearchResult(this.$scope.query);
            }.bind(this)
        }
    });

    CouponHistoryController.$inject = ['$rootScope', '$scope', '$state', '$stateParams', 'UserCouponService', 'StoreService'];
    angular.module('module.CouponHistoryController', ['service.UserCouponService', 'service.StoreService']).controller('CouponHistoryController', CouponHistoryController);

});
