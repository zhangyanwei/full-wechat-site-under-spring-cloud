define([
    'base/BaseController',
    'service/ProductService'
], function (BaseController) {

    var ProductManagementController = BaseController.extend({

        init: function ($rootScope, $scope, $state, $stateParams, $uibModal, $productService) {
            this.$rootScope = $rootScope;
            this.$state = $state;
            this.$stateParams = $stateParams;
            this.$uibModal = $uibModal;
            this.$productService = $productService;
            this._super($scope);
        },

        defineScope: function () {
            this._defineViewHandler();
            this._defineFunctions();
            this._loadProductList();
        },

        _defineViewHandler: function () {
            var owner = this;
            this.$scope.openCreateModal = function () {
                var productModel = owner.$uibModal.open({
                    windowTemplateUrl: 'views/dialog/modal-window.html',
                    windowTopClass: 'modal-default',
                    templateUrl: 'views/dialog/product.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                        $scope.message={
                            title:'添加商品',
                            button:'添加'
                        }
                    }]
                });

                productModel.result.then(
                    function () {
                        owner._loadProductList();
                    }
                );
            };
            owner.$scope.deleteProduct = function(id){
                var messageModel = owner.$uibModal.open({
                    windowTemplateUrl: 'views/dialog/modal-window.html',
                    windowTopClass: 'modal-default',
                    templateUrl: 'views/dialog/message.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                        $scope.message = '确定要删除该商品？';
                    }]
                });

                messageModel.result.then(
                    function () {
                        owner.$productService.deleteProduct(id)
                            .then(function(){
                                for(var i = 0;i<owner.$scope.data.result.length;i++){
                                    if(owner.$scope.data.result[i].id == id){
                                        owner.$scope.data.result[i].deleted = true;
                                    }
                                }
                            });
                    }
                );
            };
            owner.$scope.updateStore = function(id){
                owner.$productService.getProductDetail(id)
                    .then(function(resp){
                        owner.$scope.productDetail = resp.data;
                        var productModel = owner.$uibModal.open({
                            windowTemplateUrl: 'views/dialog/modal-window.html',
                            windowTopClass: 'modal-default',
                            templateUrl: 'views/dialog/product.html',
                            controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                                $scope.$uibModalInstance = $uibModalInstance;
                                $scope.amendProduct = owner.$scope.productDetail;
                                $scope.message={
                                    title:'修改商品',
                                    button:'修改'
                                }
                            }]
                        });

                        productModel.result.then(
                            function () {
                                owner._loadProductList(owner.$scope.page);
                            }
                        );
                    });
            }
        },

        _defineFunctions: function() {
            this.$scope.goPage = function(page) {
                this._loadProductList(page);
            }.bind(this);
        },

        _loadProductList: function (page) {
            var owner = this;
            this.$productService.products(this.$stateParams.storeId, page || 0, 10).then(
                function (resp) {
                    owner.$scope.data = resp.data;
                }
            );
        }

    });

    ProductManagementController.$inject = ['$rootScope', '$scope', '$state', '$stateParams', '$uibModal', 'ProductService'];

    angular.module('module.ProductManagementController', ['service.ProductService']).controller('ProductManagementController', ProductManagementController);

});
