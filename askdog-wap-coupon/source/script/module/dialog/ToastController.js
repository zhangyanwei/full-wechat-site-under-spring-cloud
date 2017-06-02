define([
    'base/BaseController'
], function (BaseController) {

    var ToastController = BaseController.extend({

        init: function ($rootScope, $scope, $stateParams, $state, $location, $uibModal) {
            this.$rootScope = $rootScope;
            this.$stateParams = $stateParams;
            this.$state = $state;
            this.$location = $location;
            this.$uibModal = $uibModal;

            this.$uibModalInstance = $scope.$parent.$uibModalInstance;
            this._super($scope);
        },

        defineScope: function () {
            this._defineCancel();
            this._defineDisappear();
        },

        _defineCancel: function () {
            var owner = this;
            this.$scope.cancel = function () {
                owner.$uibModalInstance.dismiss("cancel");
            }
        },

        _defineDisappear:function(){
            var owner = this;
            if($(".modal-content")){
               var time = 7;
                var setIntervalTime = setInterval(function(){
                    time--;
                    if(time == 0){
                        owner.$scope.cancel();
                        clearInterval(setIntervalTime);
                    }
                },1000);
            }
        }

    });

    ToastController.$inject = ['$rootScope', '$scope', '$stateParams', '$state', '$location', '$uibModal'];

    angular.module('module.dialog.ToastController', []).controller('ToastController', ToastController);

});