define([
    'base/BaseController',
    'service/CouponService',
    'service/UserService',
    'service/StoreService'
], function (BaseController) {

    var ConsumeConfirmController = BaseController.extend({

        init: function (_rootScope, _state, _scope, _stateParams, _couponService, _userService, _storeService) {
            this.$rootScope = _rootScope;
            this.$state = _state;
            this.$stateParams = _stateParams;
            this.$couponService = _couponService;
            this.$userService = _userService;
            this.$storeService = _storeService;
            this._super(_scope);
        },

        defineScope: function () {
            this._defineViewHandler();
            this._refreshListView();
        },

        _defineViewHandler: function () {
            var owner = this;
            this.$scope.confirm = function () {
                owner._consume();
            };

            this.$scope.cancel = function () {
                owner.$state.go('layout.view.index');
            }
        },

        _consume: function () {
            var owner = this;

            this.$scope.errorMessage = null;
            if (this.$scope.cashDetail.require_order_id && !this.$scope.orderId) {
                this.$scope.errorMessage = "请输入商户流水号";
                return;
            }

            this.$couponService.verify(this.$stateParams.id, this.$scope.orderId).then(
                function (resp) {
                    owner.$scope.successMessage = '消费成功';
                },
                function (resp) {
                    if (resp.data) {
                        owner.$scope.errorMessage = resp.data.message;
                        owner.$scope.errorCode = resp.data.code;
                    }
                }
            );
        },

        _refreshListView: function () {
            var owner = this;
            this.$couponService.cashDetail(owner.$stateParams.id).then(
                function (resp) {
                    owner.$scope.cashDetail = resp.data;
                }
            )
        }
    });

    ConsumeConfirmController.$inject = ['$rootScope', '$state', '$scope', '$stateParams', 'CouponService', 'UserService', 'StoreService'];

    angular.module('module.ConsumeConfirmController', ['service.CouponService']).controller('ConsumeConfirmController', ConsumeConfirmController);

});
