define([
    '../../base/BaseController'
], function (BaseController) {
    var SelectController = BaseController.extend({

        init: function ($scope, $stateParams, $uibModal) {
            this.$stateParams = $stateParams;
            this.$uibModal = $uibModal;
            this.$uibModalInstance = $scope.$parent.$uibModalInstance;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function () {
            this._defineClose();
            this._defineCancel();
        },

        _defineClose:function(){
            var owner = this;
            this.$scope.close = function () {
                owner.$uibModalInstance.close();
            };
        },

        _defineCancel:function(){
            var owner = this;
            this.$scope.cancel = function () {
                owner.$uibModalInstance.dismiss("cancel");
            };
        }

    });

    SelectController.$inject = ['$scope', '$stateParams', '$uibModal'];

    angular.module('module.dialog.SelectController', []).controller('SelectController', SelectController);

});