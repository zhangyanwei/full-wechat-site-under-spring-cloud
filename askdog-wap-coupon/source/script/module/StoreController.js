define([
    'base/BaseController',
    '_global',
    'service/StoreService',
    'service/ProductService',
    'service/CouponService',
    'app/directive/swiper'
], function (BaseController, _g) {

    var cookieName = "require-upgrade-coupon";

    function expires(minutes) {
        var expires = new Date();
        expires.setMinutes(expires.getMinutes() + minutes);
        return expires;
    }

    var StoreController = BaseController.extend({

        init: function (_rootScope, _scope, _stateParams, _uibModal, _cookies, _storeService, _productService, _couponService) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$uibModal = _uibModal;
            this.$cookies = _cookies;
            this.$storeService = _storeService;
            this.$productService = _productService;
            this.$couponService = _couponService;
            this._super(_scope);
        },

        defineScope: function () {
            this._defineViewHandler();
            this._refreshListView();
            this._continueUpgrade();
        },

        _defineViewHandler: function () {
            var owner = this;
            this.$scope.receiveCoupon = function (id) {
                var receiveCoupon = undefined;
                for (var i = 0; i < owner.$scope.detail.special_product.coupons.length; i++) {
                    if (owner.$scope.detail.special_product.coupons[i].type == 'NORMAL') {
                        receiveCoupon = owner.$scope.detail.special_product.coupons[i];
                        break;
                    }
                }
                if (receiveCoupon) {
                    owner.$couponService.receiveCoupon(receiveCoupon.id, owner.$scope.detail.special_product.id).then(
                        function (resp) {
                            if (owner.$rootScope.modalOpened) {
                                return;
                            }
                            owner.$rootScope.modalOpened = true;
                            var toastModal = owner.$uibModal.open({
                                windowTemplateUrl: 'views/dialog/modal-window.html',
                                windowTopClass: 'modal-toast',
                                templateUrl: 'views/dialog/toast.html',
                                controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                                    var forwardedCoupon = owner.$scope.detail.coupon_forwarded;
                                    $scope.$uibModalInstance = $uibModalInstance;
                                    $scope.toast = {
                                        success: true,
                                        qrcode: true,
                                        message: '领取成功',
                                        remarks: '关注公众号查看优惠券'  + (forwardedCoupon ? '<br />转发视频领取{0}元优惠券'.format(forwardedCoupon) : '')
                                    };
                                }]
                            });

                            toastModal.opened.then(function () {
                                $("video").hide();
                            });

                            toastModal.closed.then(function () {
                                $("video").show();
                                owner.$rootScope.modalOpened = false;
                            });
                        },
                        function (resp) {
                            if (resp.status == 409) {
                                if (owner.$rootScope.modalOpened) {
                                    return;
                                }
                                owner.$rootScope.modalOpened = true;
                                var toastModal = owner.$uibModal.open({
                                    windowTemplateUrl: 'views/dialog/modal-window.html',
                                    windowTopClass: 'modal-toast',
                                    templateUrl: 'views/dialog/toast.html',
                                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                                        $scope.$uibModalInstance = $uibModalInstance;
                                        $scope.toast = {
                                            success: false,
                                            qrcode: false,
                                            message: '您已经领取过该优惠券'
                                        };
                                    }]
                                });

                                toastModal.opened.then(function () {
                                    $("video").hide();
                                });

                                toastModal.closed.then(function () {
                                    $("video").show();
                                    owner.$rootScope.modalOpened = false;
                                });
                            }
                        }
                    );
                }
            };
            this.$scope.vote = function () {
                owner.$productService.createVote(owner.$scope.detail.special_product.id, 'UP').then(
                    function (resp) {
                        owner.$scope.detail.special_product.statistics.up_vote_count++;
                        owner.$scope.detail.special_product.vote = 'UP';
                        owner._voteAnimation();
                    },
                    function (resp) {
                        if (resp && resp.status == 409) {
                            owner.$productService.createVote(owner.$scope.detail.special_product.id, 'DOWN')
                                .then(function () {
                                    owner.$scope.detail.special_product.statistics.up_vote_count--;
                                    owner.$scope.detail.special_product.vote = 'DOWN';
                                    owner._voteAnimation();
                                });
                        }
                    }
                );
            };
            this.$scope.initWxShare = function(callback) {
                owner._initWxCallback = callback;
            };
            this.$scope.wxShareOption = function() {
                var data = owner.$scope.detail,
                    coupons = data.special_product.coupons;
                var discount = 0;
                for (var i = 0; i < coupons.length; i++) {
                    discount += Number(coupons[i].rule) || 0;
                }
                return {
                    title: "厉害了word【{0}】，最大优惠{1}元！".format(data.name, discount),
                    imgUrl: data.cover_image,
                    desc: data.description,
                    link: _g.loc.full(_g.loc.fragment())
                };
            };
            this.$scope.wxShared = function() {
                var data = owner.$scope.detail,
                    coupons = data.special_product.coupons;

                var upgradeCoupon = undefined;
                for (var i = 0; i < coupons.length; i++) {
                    if (coupons[i].type == 'FORWARDED') {
                        upgradeCoupon = coupons[i];
                        break;
                    }
                }

                upgradeCoupon && owner._upgradeCoupon(upgradeCoupon.id, true);
            };
        },

        _refreshListView: function () {
            var owner = this;
            this.$scope.loadingCompleted = false;
            this.$storeService.storeDetail(owner.$stateParams.id).then(
                function (resp) {
                    owner.$scope.loadingCompleted = true;
                    owner.$scope.detail = resp.data;
                    if (owner.$scope.detail.special_product) {
                        var detail = owner.$scope.detail.special_product.coupons;
                        for (var i = 0; i < detail.length; i++) {
                            if (detail[i].type == 'NORMAL') {
                                owner.$scope.detail.coupon_normal = detail[i].rule;
                            } else if (detail[i].type == 'FORWARDED') {
                                owner.$scope.detail.coupon_forwarded = detail[i].rule;
                            }
                        }
                    }
                    // only available if working in wechat browser.
                    owner._initWxCallback && owner._initWxCallback();
                });
        },

        _continueUpgrade: function() {
            var couponId = this.$cookies.get(cookieName);
            if (couponId) {
                this.$cookies.remove(cookieName);
                this._upgradeCoupon(couponId);
            }
        },

        _showMessage: function(option) {
            var toastModal = this.$uibModal.open({
                windowTemplateUrl: 'views/dialog/modal-window.html',
                windowTopClass: 'modal-toast',
                templateUrl: 'views/dialog/toast.html',
                controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                    $scope.$uibModalInstance = $uibModalInstance;
                    $scope.toast = option;
                }]
            });

            this._coverVideo(toastModal);
        },

        _authRequired: function(couponId) {
            var messageModal = this.$uibModal.open({
                windowTemplateUrl: 'views/dialog/modal-window.html',
                windowTopClass: 'modal-default',
                templateUrl: 'views/dialog/message.html',
                controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                    $scope.$uibModalInstance = $uibModalInstance;
                    $scope.message = '<i class="color-dark-green icon-success iconfont" style="margin-right: 5px"></i>转发成功</br>通过微信登录后可领取优惠券';
                }]
            });

            this._coverVideo(messageModal);
            messageModal.result.then(function () {
                this.$cookies.put(cookieName, couponId, {"expires" : expires(3)});
                _g.wxAuth();
            }.bind(this));
        },

        _coverVideo: function(modal) {
            modal.opened.then(function () {
                $("video").hide();
            });

            modal.closed.then(function () {
                $("video").show();
            });
        },

        _upgradeCoupon: function(couponId, auth) {
            var owner = this;
            var promise = this.$couponService.upgradeCoupon(couponId);
            promise.disableAutoAuthenticate = true;
            promise.then(
                function () {
                    owner._showMessage({
                        success: true,
                        qrcode: true,
                        message: '领取成功, 关注公众号可随时查看您的优惠券'
                    });
                },
                function (resp) {
                    if (resp.data) {
                        if (resp.data.code == 'SRV_CONFLICT_USER_COUPON') {
                            owner._showMessage({
                                message: '感谢您分享</br>您已经领取过该优惠券</br>请使用后再领取'
                            });
                        } else if (auth && resp.data.code == 'WEB_ACCESS_AUTH_REQUIRED') {
                            owner._authRequired(couponId);
                        }
                    }
                }
            );
        },

        _voteAnimation: function() {
            var owner = this;
            this.$scope.voteAnimation = { 'animation': 'font-flare 200ms ease-out' };
            setTimeout(function() {
                owner.$scope.voteAnimation = { 'animation': '' };
                owner.$scope.$digest();
            }, 200);
        }
    });

    StoreController.$inject = ['$rootScope', '$scope', '$stateParams', '$uibModal', '$cookies', 'StoreService', 'ProductService', 'CouponService'];

    angular.module('module.StoreController', ['ngCookies', 'app.directive.swiper', 'service.StoreService', 'service.ProductService', 'service.CouponService']).controller('StoreController', StoreController);

});