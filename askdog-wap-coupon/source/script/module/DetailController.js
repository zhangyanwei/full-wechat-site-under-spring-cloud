define([
    'base/BaseController',
    'app/directive/wechatShare',
    'service/ProductService',
    'service/CouponService',
    'app/directive/videoView'
], function (BaseController) {

    var DetailController = BaseController.extend({

        _VIDEO: null,

        init: function (_rootScope, _scope, _stateParams, _uibModal, _productService, _couponService) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$uibModal = _uibModal;
            this.$productService = _productService;
            this.$couponService = _couponService;
            this._super(_scope);
        },

        defineScope: function () {
            this._defineViewHandler();
            this._refreshListView();
        },

        defineListeners: function () {
            // TODO
        },

        destroy: function () {
            // TODO
        },

        _defineViewHandler: function () {
            var owner = this;
            var id = '';
            owner.$scope.receiveCoupon = function () {
                var receiveCoupon = undefined;
                for (var i = 0; i < owner.$scope.detail.coupons.length; i++) {
                    if (owner.$scope.detail.coupons[i].type == 'NORMAL') {
                        receiveCoupon = owner.$scope.detail.coupons[i];
                        break;
                    }
                }
                if (receiveCoupon) {
                    owner.$couponService.receiveCoupon(receiveCoupon.id).then(
                        function (resp) {
                            var toastModal = owner.$uibModal.open({
                                windowTemplateUrl: 'views/dialog/modal-window.html',
                                windowTopClass: 'modal-toast',
                                templateUrl: 'views/dialog/toast.html',
                                controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                                    $scope.$uibModalInstance = $uibModalInstance;
                                    $scope.toast = {
                                        success: true,
                                        qrcode: true,
                                        message: '领取成功, 关注公众号可随时查看您的优惠券',
                                        remarks: '转发视频领取' + owner.$scope.detail.coupon_forwarded + '元优惠券'
                                    };
                                }]
                            });

                            toastModal.opened.then(function() {
                                $("video").hide();
                            });

                            toastModal.closed.then(function() {
                                $("video").show();
                            });
                        },
                        function (resp) {
                            if (resp.status == 409) {
                                var toastModal = owner.$uibModal.open({
                                    windowTemplateUrl: 'views/dialog/modal-window.html',
                                    windowTopClass: 'modal-toast',
                                    templateUrl: 'views/dialog/toast.html',
                                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                                        $scope.$uibModalInstance = $uibModalInstance;
                                        $scope.toast = {
                                            message: '您已经领取过该优惠券'
                                        };
                                    }]
                                });

                                toastModal.opened.then(function() {
                                    $("video").hide();
                                });

                                toastModal.closed.then(function() {
                                    $("video").show();
                                });
                            }
                        }
                    );
                }
            };
        },

        _refreshListView: function () {
            // TODO
            var owner = this;
            owner.$scope.loadingCompleted = false;
            owner.$productService.productDetail(owner.$stateParams.id).then(
                function (resp) {
                    owner.$scope.loadingCompleted = true;
                    owner.$scope.detail = resp.data;
                    owner._couponView(owner.$scope.detail);
                }
            );
        },

        _couponView: function (detail) {
            var owner = this;
            if (detail.coupons) {
                for (var i = 0; i < detail.coupons.length; i++) {
                    if (detail.coupons[i].type == 'NORMAL') {
                        owner.$scope.detail.coupon_normal = detail.coupons[i].rule;
                    }
                    if (detail.coupons[i].type == 'FORWARDED') {
                        owner.$scope.detail.coupon_forwarded = detail.coupons[i].rule;
                    }
                }

                // this is temporary solution, should be tidy up.
                var updatedDetail = owner.$scope.detail;
                if (updatedDetail.coupon_normal || updatedDetail.coupon_forwarded) {
                    $("#a-my-coupons").css("bottom", "60px");
                }
            }

        }

    });

    DetailController.$inject = ['$rootScope', '$scope', '$stateParams', '$uibModal', 'ProductService', 'CouponService'];

    angular.module('module.DetailController', ['service.ProductService', 'service.CouponService']).controller('DetailController', DetailController);

});