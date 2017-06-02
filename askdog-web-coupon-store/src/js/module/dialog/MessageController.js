define([
    'base/BaseController'
], function (BaseController) {

    var MessageController = BaseController.extend({

        init: function ($rootScope, $scope, $state, $stateParams, $uibModal) {
            this.$rootScope = $rootScope;
            this.$state = $state;
            this.$stateParams = $stateParams;
            this.$uibModalInstance = $scope.$parent.$uibModalInstance;
            this.$uibModal = $uibModal;
            this.$message = $scope.$parent.message;
            this._super($scope);
        },

        defineScope: function () {
            this._defineCancel();
            this._defineClose();
        },

        _defineCancel: function () {
            var owner = this;
            this.$scope.cancel = function () {
                owner.$uibModalInstance.dismiss("cancel");
            }
        },

        _defineClose: function () {
            var owner = this;
            this.$scope.close = function () {
                owner.$uibModalInstance.close();
            }
        },
    });

    MessageController.$inject = ['$rootScope', '$scope', '$state', '$stateParams', '$uibModal'];

    angular.module('module.dialog.MessageController', []).controller('MessageController', MessageController);

});
