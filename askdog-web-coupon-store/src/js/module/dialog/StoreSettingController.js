define([
    'base/BaseController',
    'jquery.validator',
    'service/StoreService'
], function (BaseController) {

    var StoreSettingController = BaseController.extend({

        init: function ($rootScope, $scope, $state, $stateParams, $uibModal, $storeService) {
            this.$rootScope = $rootScope;
            this.$state = $state;
            this.$stateParams = $stateParams;
            this.$uibModalInstance = $scope.$parent.$uibModalInstance;
            this.$uibModal = $uibModal;
            this.$message = $scope.$parent.message;
            this.$storeService = $storeService;
            this._super($scope);
        },

        defineScope: function () {
            this.$scope.storeDetail = this.$scope.$parent.storeDetail || {};
            this._initData();
            this._defineCancel();
            this._defineBindValidator();
            this._defineUpdateSetting();
        },

        _initData: function() {
            var owner = this;
            this.$storeService.getStoreSetting(this.$scope.storeDetail.id).then(
                function (resp) {
                    owner.$scope.setting = resp.data;
                }
            );
        },

        _defineCancel: function () {
            var owner = this;
            this.$scope.cancel = function () {
                owner.$uibModalInstance.dismiss("cancel");
            }
        },

        _defineBindValidator: function () {
            var owner = this;
            this.$scope.bindValidator = function (element) {
                $(element).validate({
                    rules: {},
                    messages: {},
                    submitHandler: function () {
                        owner.$scope.updateStoreSetting();
                    }
                });
            }
        },

        _defineUpdateSetting: function () {
            var owner = this;
            this.$scope.updateStoreSetting = function () {
                owner.$storeService.updateStoreSetting(owner.$scope.storeDetail.id, owner.$scope.setting).then(
                    function (resp) {
                        owner.$uibModalInstance.close();
                    }
                );
            }
        }
    });

    StoreSettingController.$inject = ['$rootScope', '$scope', '$state', '$stateParams', '$uibModal', 'StoreService'];

    angular.module('module.dialog.StoreInfoController', ['service.StoreService']).controller('StoreSettingController', StoreSettingController);

});
