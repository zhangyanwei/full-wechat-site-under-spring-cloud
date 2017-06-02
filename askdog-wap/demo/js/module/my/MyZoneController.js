define([
    '../../base/BaseController',
    'app/directive/adAnalytics',
    'service/UserService',
    'app/directive/vipApprove'
], function (BaseController) {
    var MyZoneController = BaseController.extend({

        $stateParams: null,
        $userService: null,

        /**
         * @Override
         * @param $scope
         * @param $stateParams
         * @param $userService
         */
        init: function ($scope, $stateParams, $userService,$uibModal,$rootScope) {
            this.$stateParams = $stateParams;
            this.$userService = $userService;
            this.$uibModal = $uibModal;
            this.$rootScope = $rootScope;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope:function(){
            this._defineSearchUserInfo();
        },

        _defineSearchUserInfo: function () {
            var owner = this;
            owner.$userService.getStates()
                .success(function (data) {
                    if (data == 'AUTHENTICATED') {
                        owner.$scope.userSelf = true;
                        owner._loginOut();
                        owner._getUserInfo();
                        owner._defineOutLogin();
                        owner.$userService.getUserInfo()
                            .then(function(resp){
                                owner.$scope.notice = resp.data;
                            });
                    }
                });
        },

        _loginOut:function(){
            var owner = this;
            owner.$scope.signOut = function(){
                owner.$userService.logout()
                    .success(function(){
                        window.location = '/';
                    });
            }
        },

        _getUserInfo:function(){
            var owner = this;
            this.$userService.getInfo()
                .success(function(data){
                    console.log(data);
                    owner.$scope.userInfo = data;
                });
        },

        _defineOutLogin:function(){
            var owner = this;
            owner.$scope.outLogin = function(){
                var selectModal = owner.$uibModal.open({
                    windowTemplateUrl: 'views/base/modal-window.html',
                    windowTopClass: 'out-Modal',
                    templateUrl: 'views/dialog/selectDialog.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                        $scope.message = '确定退出当前账号吗？'
                    }]
                });
                selectModal.result.then(function(){
                    owner.$scope.signOut();
                });
            }
        }

    });

    MyZoneController.$inject = ['$scope', '$stateParams', 'userService','$uibModal','$rootScope'];

    angular.module('module.my.MyZoneController', ['service.UserService']).controller('MyZoneController', MyZoneController);

});