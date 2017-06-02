define([
    'base/BaseController',
    'jquery.validator',
    'service/CouponService',
    'bootstrap.datetimepicker'
], function (BaseController) {

    var CouponController = BaseController.extend({

        init: function ($rootScope, $scope, $state, $stateParams, $couponService) {
            this.$rootScope = $rootScope;
            this.$state = $state;
            this.$stateParams = $stateParams;
            this.$uibModalInstance = $scope.$parent.$uibModalInstance;
            this.$couponService = $couponService;
            this._super($scope);
        },

        defineScope: function () {
            this._defineRenders();
            this._defineCancel();
            this._bindValidator();
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
                    showMeridian: 1,
                    minView: 2
                });
            };
        },

        _defineCancel: function () {
            var owner = this;
            this.$scope.cancel = function () {
                owner.$uibModalInstance.dismiss("cancel");
            }
        },

        _createCoupon: function () {
            var owner = this;

            this.$scope.coupon.expire_time_from = new Date($("#expire_time_from").val()).getTime();
            this.$scope.coupon.expire_time_to = new Date($("#expire_time_to").val()).getTime();

            this.$couponService.createCoupons(this.$scope.coupon).then(
                function () {
                    owner.$uibModalInstance.close();
                }
            );
        },

        _bindValidator: function () {
            var owner = this;
            this.$scope.couponTypeData = [
                {type: 'NORMAL', typeName: '普通优惠券'},
                {type: 'FORWARDED', typeName: '转发升级优惠券'}
            ];
            this.$scope.couponExpireUnit = [
                {type: 'DAY', typeName: '天'},
                {type: 'WEEK', typeName: '周'},
                {type: 'MONTH', typeName: '月'}
            ];
            this.$scope.coupon = {
                store_id: this.$stateParams.storeId
            };
            this.$scope.bindValidator = function (element) {
                $(element).validate({
                    rules: {
                        couponName: {
                            required: true,
                            maxlength: 20
                        },
                        couponType: {
                            required: true
                        },
                        couponRule: {
                            required: true,
                            number: true
                        },
                        couponExpireValue: {
                            required: true,
                            number: true
                        },
                        couponExpireUnit: {
                            required: true
                        },
                        expire_time_from: {
                            required: true
                        },
                        expire_time_to: {
                            required: true
                        }
                    },
                    messages: {
                        couponName: {
                            required: '',
                            maxlength: '优惠券名不能超过20个字'
                        },
                        couponType: {
                            required: ''
                        },
                        couponRule: {
                            required: '',
                            number: "请输入正确优惠额"
                        },
                        couponExpireValue: {
                            required: '',
                            number: "请输入正确有效期"
                        },
                        couponExpireUnit: {
                            required: ''
                        },
                        expire_time_from: {
                            required: ''
                        },
                        expire_time_to: {
                            required: ''
                        }
                    },
                    submitHandler: function () {
                        owner._createCoupon();
                    }
                });
            }
        }

    });

    CouponController.$inject = ['$rootScope', '$scope', '$state', '$stateParams', 'CouponService'];

    angular.module('module.CouponController', ['service.CouponService']).controller('CouponController', CouponController);

});
