define(['base/BaseController', 'service/EventService'], function (BaseController) {

    var EventManagementController = BaseController.extend({

        init: function ($rootScope, $scope, $state, $stateParams, $uibModal, $eventService) {
            this.$rootScope = $rootScope;
            this.$state = $state;
            this.$stateParams = $stateParams;
            this.$uibModal = $uibModal;
            this.$eventService = $eventService;
            this._super($scope);
        },

        defineScope: function() {
            var owner = this;

            this.$scope.goPage = function(page) {
                owner.$scope.errorMessage = null;
                owner.$eventService.search(this.$stateParams.storeId, page || 0, 10).then(
                    function(resp) {
                        owner.$scope.data = resp.data;
                    },
                    function(resp) {
                        owner.$scope.errorMessage = resp.data.message || "服务器正忙，请稍后再试";
                    }
                );
            };

            this.$scope.createEvent = function() {
                owner.$scope.errorMessage = null;
                var productModel = owner.$uibModal.open({
                    windowTemplateUrl: 'views/dialog/modal-window.html',
                    windowTopClass: 'modal-default',
                    templateUrl: 'views/dialog/event.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                        $scope.storeId = owner.$stateParams.storeId;
                        $scope.message={
                            title:'添加活动',
                            button:'添加'
                        }
                    }]
                });

                productModel.result.then(
                    function () {
                        owner.$scope.goPage();
                    }
                );
            };

            this.$scope.updateEvent = function(event) {
                owner.$scope.errorMessage = null;
                var modal = owner.$uibModal.open({
                    windowTemplateUrl: 'views/dialog/modal-window.html',
                    windowTopClass: 'modal-default',
                    templateUrl: 'views/dialog/event.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                        $scope.storeId = owner.$stateParams.storeId;
                        $scope.event = event;
                        $scope.message={
                            title:'编辑活动',
                            button:'编辑'
                        }
                    }]
                });

                modal.result.then(
                    function () {
                        owner.$scope.goPage(owner.$scope.page);
                    }
                );
            };

            this.$scope.deleteEvent = function(id) {
                owner.$scope.errorMessage = null;
                var modal = owner.$uibModal.open({
                    windowTemplateUrl: 'views/dialog/modal-window.html',
                    windowTopClass: 'modal-default',
                    templateUrl: 'views/dialog/message.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                        $scope.message = '确定要删除该活动？';
                    }]
                });

                modal.result.then(
                    function () {
                        owner.$eventService.deleteEvent(id).then(
                            function(resp) {
                                owner.$scope.data.result.remove(function(item) {
                                    return item && item.id == id;
                                });
                            },
                            function(resp) {
                                owner.$scope.errorMessage = resp.data.message || "服务器正忙，请稍后再试";
                            }
                        );
                    }
                );
            };

            // load the data
            this.$scope.goPage();
        }

    });

    EventManagementController.$inject = ['$rootScope', '$scope', '$state', '$stateParams', '$uibModal', 'EventService'];

    angular.module('module.EventManagementController', ['service.EventService']).controller('EventManagementController', EventManagementController);

});
