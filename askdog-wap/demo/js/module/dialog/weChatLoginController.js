define([
    '../../base/BaseController',
    'app/directive/qrCode'
], function (BaseController) {
    var weChatLoginController = BaseController.extend({

        init: function ($scope, $stateParams, $window, $uibModal) {
            this.$stateParams = $stateParams;
            this.$window = $window;
            this.$uibModal = $uibModal;
            this.$uibModalInstance = $scope.$parent.$uibModalInstance;
            this.src = $scope.$parent.src;
            this.request = $scope.$parent.request;
            this.location = $scope.$parent.location;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function () {
            this._close();
            this._showLogin();
        },

        _close: function () {
           var owner = this;
            owner.$scope.close = function(){
                owner.$uibModalInstance.dismiss('close');
            }
        },

        _showLogin:function(){
            var owner = this;
            owner.$scope.showLogin = function () {
                window.location.href = "/login/wechat?request=" + encodeURIComponent(owner.request || owner.location);
                owner.$uibModalInstance.dismiss('close');
            }
        }


    });

    weChatLoginController.$inject = ['$scope', '$stateParams', '$window', '$uibModal'];

    angular.module('module.dialog.weChatLoginController', []).controller('weChatLoginController', weChatLoginController);

});