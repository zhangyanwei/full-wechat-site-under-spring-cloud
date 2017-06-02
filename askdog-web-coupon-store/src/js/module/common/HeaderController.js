define([
    'base/BaseController',
    'service/UserService'
], function (BaseController) {

    var HeaderController = BaseController.extend({

        init: function ($rootScope, $scope, $state, $stateParams, $userService, $uibModal) {
            this.$rootScope = $rootScope;
            this.$state = $state;
            this.$stateParams = $stateParams;
            this.$userService = $userService;
            this.$uibModal = $uibModal;
            this._super($scope);
        },

        defineScope: function () {
            this._defineSignOut();
            this._defineModifyPassword();
        },

        _defineSignOut: function () {
            var owner = this;
            this.$rootScope.signOut = function () {
                owner.$userService.logout().then(
                    function () {
                        window.session = {user: undefined};
                        window.location.href = "/";
                    }
                );
            }
        },

        _defineModifyPassword: function () {
            var owner = this;
            this.$rootScope.modifyPsw = function () {
                var couponModel = owner.$uibModal.open({
                    windowTemplateUrl: 'views/dialog/modal-window.html',
                    windowTopClass: 'modal-default',
                    templateUrl: 'views/dialog/modify-password.html',
                    controller: ''
                    // controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                    //     $scope.$uibModalInstance = $uibModalInstance;
                    // }]
                });

                // couponModel.result.then(
                //     function () {
                //     },
                //     function () {
                //     }
                // );
            };
        }
    });

    HeaderController.$inject = ['$rootScope', '$scope', '$state', '$stateParams', 'UserService', '$uibModal'];
    angular.module('module.HeaderController', ['service.UserService']).controller('HeaderController', HeaderController);

});
