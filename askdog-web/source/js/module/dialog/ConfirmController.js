define(['base/BaseController'], function (BaseController) {

    var ConfirmController = BaseController.extend({

        init: function (_rootScope, _scope, _stateParams) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$uibModalInstance = _scope.$parent.$uibModalInstance;
            this._super(_scope);
        },

        defineScope: function () {
            this._defineClose();
            this._defineCancel();
        },

        _defineClose: function () {
            var owner = this;
            this.$scope.close = function () {
                owner.$uibModalInstance.close();
            };
        },

        _defineCancel: function () {
            var owner = this;
            this.$scope.cancel = function () {
                owner.$uibModalInstance.dismiss("cancel");
            };
        }

    });

    ConfirmController.$inject = ['$rootScope', '$scope', '$stateParams'];

    angular.module('module.dialog.ConfirmController', []).controller('ConfirmController', ConfirmController);

});