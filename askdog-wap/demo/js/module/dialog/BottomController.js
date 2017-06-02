define([
    '../../base/BaseController'
], function (BaseController) {
    var BottomController = BaseController.extend({

        init: function ($scope, $stateParams, $uibModal) {
            this.$stateParams = $stateParams;
            this.$uibModal = $uibModal;
            this.$experienceId = $scope.$parent.experienceId;
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

    BottomController.$inject = ['$scope', '$stateParams', '$uibModal'];

    angular.module('module.dialog.BottomController', []).controller('BottomController', BottomController);

});