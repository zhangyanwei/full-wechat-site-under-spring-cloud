define([
    'base/BaseController'
], function (BaseController) {

    var MessageController = BaseController.extend({

        init: function ($rootScope, $scope, $stateParams) {
            this.$rootScope = $rootScope;
            this.$stateParams = $stateParams;
            this.$uibModalInstance = $scope.$parent.$uibModalInstance;
            this._super($scope);
        },

        defineScope: function () {
            this._defineClose();
            this._defineCancel();
        },

        _defineClose: function () {
            var owner = this;
            this.$scope.close = function () {
                owner.$uibModalInstance.close();
            }
        },

        _defineCancel: function () {
            var owner = this;
            this.$scope.cancel = function () {
                owner.$uibModalInstance.dismiss("cancel");
            }
        }

    });

    MessageController.$inject = ['$rootScope', '$scope', '$stateParams'];

    angular.module('module.dialog.MessageController', []).controller('MessageController', MessageController);

});