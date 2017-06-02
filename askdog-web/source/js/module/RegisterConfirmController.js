define(['base/BaseController', 'app/directive/adAnalytics', 'service/SearchService'], function (BaseController) {

    var RegisterConfirmController = BaseController.extend({

        init: function ($rootScope, $scope, $stateParams, $uibModal, $userService) {
            this.$rootScope = $rootScope;
            this.$stateParams = $stateParams;
            this.$uibModal = $uibModal;

            this.$userService = $userService;
            this._super($scope);
        },

        defineScope: function () {
            var owner = this;
            this.$scope.status = "verifying";
            this.$userService.confirm(this.$stateParams.token)
                .then(function () {
                    owner.$scope.status = "succeed";
                }, function () {
                    owner.$scope.status = "failed";
                });

            this.$scope.findPassword = function () {
                owner.$uibModal.open({
                    windowTemplateUrl: 'views/base/modal-window.html',
                    windowTopClass: 'lr-Modal',
                    templateUrl: 'views/dialog/reset.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                    }]
                });
            }
        }

    });

    RegisterConfirmController.$inject = ['$rootScope', '$scope', '$stateParams', '$uibModal', 'UserService'];
    angular.module('module.RegisterConfirmController', ['service.UserService']).controller('RegisterConfirmController', RegisterConfirmController);

});