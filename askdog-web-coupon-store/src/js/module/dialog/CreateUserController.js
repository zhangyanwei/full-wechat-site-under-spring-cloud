define([
    'base/BaseController',
    'jquery.validator',
    'service/UserService'
], function (BaseController) {

    var CreateUserController = BaseController.extend({

        init: function ($rootScope, $scope, $state, $stateParams, $uibModal, $userService) {
            this.$rootScope = $rootScope;
            this.$state = $state;
            this.$stateParams = $stateParams;
            this.$uibModalInstance = $scope.$parent.$uibModalInstance;
            this.$uibModal = $uibModal;
            this.$userService = $userService;
            this.$message = $scope.$parent.message;
            this._super($scope);
        },

        defineScope: function () {
            this._defineCancel();
            this._defineBindValidator();
            this._defineViewHandler();
        },

        _defineCancel: function () {
            var owner = this;
            this.$scope.cancel = function () {
                owner.$uibModalInstance.dismiss("cancel");
            }
        },

        _defineViewHandler:function(){
            var owner = this;
            owner.$scope.createUser = function(e){
                owner.$userService.create(owner.$scope.user)
                    .then(function(resp) {
                        owner.$scope.createUserCallback(resp.data);
                        owner.$uibModalInstance.close();
                    }, function(resp) {
                        owner.$scope.error = resp.data.message || '服务器正忙，请稍后再试';
                    });
            };
        },

        _defineBindValidator:function(){
            var owner = this;
            this.$scope.bindValidator = function(element){
                $(element).validate({
                    rules: {
                        nickname: {
                            required: true,
                            nickname: true
                        },
                        username: {
                            required: true,
                            username: true
                        },
                        password: {
                            required: true,
                            password: true
                        },
                        confirm_password:{
                            required: true,
                            equalTo: '#user_password'
                        }
                    },
                    messages: {
                        nickname: {
                            required: '',
                            nickname: '请输入的格式不正确'
                        },
                        username: {
                            required: '',
                            username: '请以字母开头，允许4~19位字符'
                        },
                        password: {
                            required: '',
                            password: '请输入6~20位的密码'
                        },
                        confirm_password:{
                            required: '',
                            password: '请输入6~20位的密码',
                            equalTo: '您两次输入的密码不匹配'
                        }
                    },
                    submitHandler: function () {
                        owner.$scope.createUser();
                    }
                });
            }
        }
    });

    CreateUserController.$inject = ['$rootScope', '$scope', '$state', '$stateParams', '$uibModal', 'UserService'];

    angular.module('module.dialog.CreateUserController', ['service.UserService']).controller('CreateUserController', CreateUserController);

});
