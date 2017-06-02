define([
    'base/BaseController',
    'app/directive/adAnalytics',
    'service/UserService',
    'validate'
], function (BaseController) {
    var SecurityCenterController = BaseController.extend({

        init: function ($scope, $stateParams, $userService) {
            this.$stateParams = $stateParams;
            this.$userService = $userService;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function(){
            this._bindValidator();
            this._bindAccounts();
            this._showModal();
            this._showPasswordForm();
        },

        _updatePassword: function(){
            var owner = this;
            var oldPassword = owner.$scope.oldPassword;
            var newPassword = owner.$scope.newPassword;
            owner.$userService.updatePassword(oldPassword,newPassword).then(function(){
                owner.$scope.updatePasswordSuccess = true;
                owner.$scope.showPasswordForm = false;
            },function(resp){
                if (resp && resp.status == 400) {
                    if (resp.data.code == 'SRV_ARG_INVALID_ORIGIN_PASSWORD') {
                        owner.$scope.error = "原密码不正确";
                    }
                    if (resp.data.code == 'Pattern.changePassword.originPassword') {
                        owner.$scope.error = "原密码不匹配密码规则";
                    }
                    if (resp.data.code == 'Pattern.changePassword.newPassword') {
                        owner.$scope.error = "新密码不匹配密码规则";
                    }
                }
            });


        },

        _bindValidator: function(){
            var owner = this;
            this.$scope.bindValidator = function (element) {
                $(element).validate({
                    rules:{
                        oldPassword:{
                            required:true,
                            password:true
                        },
                        newPassword:{
                            required:true,
                            password:true
                        },
                        againPassword:{
                            required:true,
                            equalTo:"#newPassword"
                        }
                    },
                    messages:{
                        oldPassword:{
                            required:'请填写原始密码',
                            password:'密码长度不正确'
                        },
                        newPassword:{
                            required:'请填写新密码',
                            password:'密码长度不正确'
                        },
                        againPassword:{
                            required:'请填写确认密码',
                            password:'两次密码输入的不匹配',
                            equalTo:'两次密码输入的不匹配'
                        }

                    },
                    submitHandler:function(){
                        owner._updatePassword();
                    }
                });
            };
        },

        _bindAccounts:function(){
            var owner = this;
                owner.$userService.bindAccounts()
                    .success(function(data){
                        if(data.length != 0){
                            owner.$scope.userAvatar = data[0];
                            owner.$scope.bind = true;
                        }
                    });

        },

        _showModal:function(){
            var owner = this;
            owner.$scope.showModal = function(){
                if(!owner.$scope.bind){
                    $("#bindWeChat").modal("show");
                }
            }
        },

        _showPasswordForm:function(){
            var owner = this;
            owner.$scope.showForm = function(){
                owner.$scope.showPasswordForm = true;
            }
        }
    });

    SecurityCenterController.$inject = ['$scope', '$stateParams', 'userService'];

    angular.module('module.my.SecurityCenterController', ['service.UserService'])
        .controller('SecurityCenterController', SecurityCenterController);

});