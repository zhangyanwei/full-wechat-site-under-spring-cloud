define([
    'base/BaseController',
    'service/StoreService',
    'jquery.validator',
    'app/directive/qrCode'
], function (BaseController) {

    var StoreManagementController = BaseController.extend({

        init: function ($rootScope, $scope, $state, $stateParams, $storeService, $uibModal) {
            this.$rootScope = $rootScope;
            this.$state = $state;
            this.$stateParams = $stateParams;
            this.$storeService = $storeService;
            this.$uibModal = $uibModal;
            this._super($scope);
        },

        defineScope: function () {
            this._defineViewHandler();
            this._defineFunctions();
            this._refreshListView();
        },

        _defineViewHandler: function () {
            var owner = this;

            this.$scope.toggleDetail = function(id, $event){
                $event.stopPropagation();
                owner.$scope.showDetailId = owner.$scope.showDetailId == id ? null : id;
                return false;
            };

            this.$scope.deleteStore = function (id, $event) {
                $event.stopPropagation();
                var messageModel = owner.$uibModal.open({
                    windowTemplateUrl: 'views/dialog/modal-window.html',
                    windowTopClass: 'modal-default',
                    templateUrl: 'views/dialog/message.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                        $scope.message = '确定要删除该商户？';
                    }]
                });
                messageModel.result.then(function () {
                    owner.$storeService.deleteStoreInfo(id)
                        .then(function () {
                            owner._refreshListView(owner.$scope.page);
                        });
                });

            };
            this.$scope.addStore = function () {
                var storeInfoModal = owner.$uibModal.open({
                    windowTemplateUrl: 'views/dialog/modal-window.html',
                    windowTopClass: 'pg-show-modal',
                    templateUrl: 'views/dialog/store-info.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                        $scope.message = {
                            title: '添加商户',
                            addStore: true,
                            active: '添加'
                        }
                    }]
                });
                storeInfoModal.result.then(function () {
                    owner._refreshListView();
                });
            };
            this.$scope.updateStore = function (id, $event) {
                $event.stopPropagation();
                owner.$storeService.getStoreDetail(id)
                    .then(function (resp) {
                        var storeInfoModal = owner.$uibModal.open({
                            windowTemplateUrl: 'views/dialog/modal-window.html',
                            windowTopClass: 'pg-show-modal',
                            templateUrl: 'views/dialog/store-info.html',
                            controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                                $scope.$uibModalInstance = $uibModalInstance;
                                $scope.message = {
                                    title: '修改商户信息',
                                    addStore: false,
                                    active: '保存'
                                };
                                $scope.storeDetail = resp.data;
                            }]
                        });
                        storeInfoModal.result.then(function () {
                            owner._refreshListView(owner.$scope.page);
                        });
                    });
            };

            this.$scope.configStore = function (id, $event) {
                $event.stopPropagation();
                owner.$storeService.getStoreDetail(id).then(function (resp) {
                        owner.$uibModal.open({
                            windowTemplateUrl: 'views/dialog/modal-window.html',
                            windowTopClass: 'pg-show-modal',
                            templateUrl: 'views/dialog/store-settings.html',
                            controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                                $scope.$uibModalInstance = $uibModalInstance;
                                $scope.message = {
                                    title: '商户配置',
                                    active: '保存'
                                };
                                $scope.storeDetail = resp.data;
                            }]
                        });
                    }
                );
            };

            this.$scope.showQrcode = function (store, $event) {
                $event.stopPropagation();

                owner.$uibModal.open({
                    windowTemplateUrl: 'views/dialog/modal-window-pure.html',
                    windowTopClass: 'pure-modal',
                    templateUrl: 'views/dialog/qrcode-store.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                        $scope.store = store;

                        $scope.cancel = function () {
                            $uibModalInstance.dismiss("cancel");
                        }
                    }]
                });
            };
        },

        _defineFunctions: function() {
            this.$scope.goPage = function(page) {
                this._refreshListView(page);
            }.bind(this);
        },

        _refreshListView: function (page) {
            var owner = this;
            this.$storeService.storeList(page || 0, 12).then(
                function (resp) {
                    owner.$scope.data = resp.data;
                }
            );
        }

    });

    StoreManagementController.$inject = ['$rootScope', '$scope', '$state', '$stateParams', 'StoreService', '$uibModal'];

    angular.module('module.StoreManagementController', ['service.StoreService']).controller('StoreManagementController', StoreManagementController);

});
