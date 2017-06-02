define([
    '../../base/BaseController'
], function (BaseController) {
    var FooterController = BaseController.extend({

        $stateParams: null,

        /**
         * @Override
         * @param $scope
         * @param $stateParams
         */
        init: function ($scope, $stateParams,$location,$uibModal) {
            this.$stateParams = $stateParams;
            this.$location = $location;
            this.$uibModal = $uibModal;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope:function() {
            this._defineShowDialog();
        },

        _defineShowDialog:function(){
            var owner = this;
            owner.$scope.showDialog = function(){
                var messageModal = owner.$uibModal.open({
                    windowTemplateUrl: 'views/base/modal-window.html',
                    windowTopClass: 'out-Modal',
                    templateUrl: 'views/dialog/messageDialog.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                        $scope.message = '请到web端分享经验';
                    }]
                });
            }
        }


    });

    FooterController.$inject = ['$scope', '$stateParams','$location','$uibModal'];

    angular.module('module.header.FooterController',[]).controller('FooterController', FooterController);

});