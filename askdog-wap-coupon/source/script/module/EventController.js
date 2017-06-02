define([
    'base/BaseController',
    '_global',
    'service/StoreService',
    'service/CouponService'
], function (BaseController, _g) {

    var EventController = BaseController.extend({

        init: function (_rootScope, _scope, _stateParams, _uibModal, _storeService, _couponService) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$uibModal = _uibModal;
            this.$storeService = _storeService;
            this.$couponService = _couponService;
            this._super(_scope);
        },

        defineScope: function () {
            this._defineViewHandler();
            this._refreshListView();
        },

        _defineViewHandler: function () {
            var owner = this;
            this.$scope.wantCoupon = function (id) {
                owner.$couponService.receiveCoupon(id).then(
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
                                $scope.$uibModalInstance = $uibModalInstance;
                                $scope.toast = {
                                    success: true,
                                    qrcode: true,
                                    message: '报名成功',
                                    remarks: '关注公众号查看活动券<br/>更多活动请持续关注惠券'
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
                                        message: '您已经领取过该活动券'
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
            };
            this.$scope.refresh = function() {
                owner._refreshListView();
            };
            this.$scope.wxShareOption = function() {
                var data = owner.$scope.detail;
                return {
                    title: data.name,
                    imgUrl: data.poster,
                    desc: data.description,
                    link: _g.loc.full(_g.loc.fragment())
                };
            };
            this.$scope.initWxShare = function(callback) {
                owner.$wxLink = callback;
            };
        },

        _refreshListView: function () {
            var owner = this;
            this.$scope.loaded = false;
            this.$storeService.eventDetail(owner.$stateParams.id).then(
                function (resp) {
                    owner.$scope.loaded = true;
                    owner.$scope.detail = resp.data;
                    owner.$wxLink && owner.$wxLink();
                },
                function (resp) {
                    owner.$scope.loaded = true;
                }
            );
        }
    });

    EventController.$inject = ['$rootScope', '$scope', '$stateParams', '$uibModal', 'StoreService', 'CouponService'];

    angular.module('module.EventController', ['service.StoreService', 'service.CouponService']).controller('EventController', EventController);

});