define(['base/BaseController', 'jquery.morris', 'service/CouponService'], function (BaseController) {

    var CouponManagementController = BaseController.extend({

        init: function ($rootScope, $scope, $state, $stateParams, $uibModal, _couponService) {
            this.$rootScope = $rootScope;
            this.$state = $state;
            this.$stateParams = $stateParams;
            this.$uibModal = $uibModal;
            this.$couponService = _couponService;
            this._super($scope);

        },

        defineScope: function () {
            this._defineViewHandler();
            this._defineFunctions();
            this._refreshListView();
        },

        _defineViewHandler: function () {
            var owner = this;
            this.$scope.createCoupon = function () {
                var couponModel = owner.$uibModal.open({
                    windowTemplateUrl: 'views/dialog/modal-window.html',
                    windowTopClass: 'modal-default',
                    templateUrl: 'views/dialog/coupon.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                    }]
                });

                couponModel.result.then(
                    function () {
                        owner._refreshListView();
                    }
                );
            };
        },

        _defineFunctions: function() {
            this.$scope.goPage = function(page) {
                this._refreshListView(page);
            }.bind(this);
        },

        _refreshListView: function (page) {
            var owner = this;
            this.$couponService.couponList(owner.$stateParams.storeId, page || 0, 10).then(
                function (resp) {
                    owner.$scope.data = resp.data;
                }
            )
        }
    });

    CouponManagementController.$inject = ['$rootScope', '$scope', '$state', '$stateParams', '$uibModal', 'CouponService'];
    angular.module('module.store.CouponManagementController', ['service.CouponService']).controller('CouponManagementController', CouponManagementController);

});
