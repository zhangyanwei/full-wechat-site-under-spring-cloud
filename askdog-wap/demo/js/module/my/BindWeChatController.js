define([
    '../../base/BaseController',
    'service/UserService'
], function (BaseController) {
    var BindWeChatController = BaseController.extend({

        init: function ($scope, $stateParams, $userService, $state) {
            this.$stateParams = $stateParams;
            this.$userService = $userService;
            this.$state = $state;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope:function(){
            var token = this.$stateParams.token;
            this._validateToken(token);
            this._bindRevenueAccount(token)
            this._defineBindCancel();
        },

        _validateToken:function(token){
            var owner = this;
            this.$userService.tokenValidate(token)
                .then(function(resp){
                    if(resp.data){
                        owner.$scope.userInfo = resp.data;
                    }
                },function(resp){
                    if(resp.status == 400){
                        owner.$scope.error = '链接失效，请重新扫描二维码';
                    }
                });
        },

        _bindRevenueAccount:function(token){
            var owner = this;
            owner.$scope.bindWechat = function() {
                owner.$userService.bindWechat(token)
                    .then(function(){
                        owner.$scope.bindSuccess = true;
                    }, function() {
                        owner.$scope.bindFail = true;
                    });
            }
        },

        _defineBindCancel:function(){
            var owner = this;
            owner.$scope.bindCancel = function(){
                owner.$scope.cancelBind = true;
            }
        }
    });

    BindWeChatController.$inject = ['$scope', '$stateParams', 'userService', '$state'];

    angular.module('module.my.BindWeChatController', ['service.UserService']).controller('BindWeChatController', BindWeChatController);

});