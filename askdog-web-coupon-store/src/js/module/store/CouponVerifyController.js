define(['base/BaseController', 'jquery.validator', 'service/CouponService'], function (BaseController) {

    var CouponVerifyController = BaseController.extend({

        init: function ($rootScope, $scope, $state, $stateParams, $couponService) {
            this.$rootScope = $rootScope;
            this.$state = $state;
            this.$stateParams = $stateParams;
            this.$couponService = $couponService;
            this._super($scope);
        },

        defineScope: function () {
            this._defineValidator();
            this._defineViewHandler();
        },

        _defineViewHandler: function () {
            var owner = this;
            this.$scope.verify = function () {
                owner.$couponService.verify(owner.$scope.couponNO).then(
                    function (resp) {
                        owner._refreshView(resp.data);
                    },
                    function (resp) {
                        if (resp.status == 400 || resp.status == 500) {
                            owner.$scope.errorMessage = "无效的优惠券!"
                        }else {
                            owner.$scope.errorMessage = resp.data.message;
                        }
                    }
                );
            };
        },

        _refreshView: function (verifyInfo) {
            this.$scope.verifyList || (this.$scope.verifyList = []);
            this.$scope.verifyList.unshift(verifyInfo);
        },

        _defineValidator: function () {
            var owner = this;
            this.$scope.bindValidator = function (element) {
                $(element).validate({
                    rules: {
                        couponNO: {
                            required: true,
                            couponNO: true
                        }
                    },
                    messages: {
                        couponNO: {
                            required: '请输入优惠券号',
                            couponNO: '无效的优惠券号'
                        }
                    },
                    submitHandler: function () {
                        owner.$scope.verify();
                    }
                });
            };
        }

    });

    CouponVerifyController.$inject = ['$rootScope', '$scope', '$state', '$stateParams', 'CouponService'];
    angular.module('module.CouponVerifyController', ['service.CouponService']).controller('CouponVerifyController', CouponVerifyController);

});
