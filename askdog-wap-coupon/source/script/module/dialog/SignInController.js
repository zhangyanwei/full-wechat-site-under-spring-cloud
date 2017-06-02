define([
    'base/BaseController',
    'jquery.validator',
    'service/UserService'
], function (BaseController) {

    var SignInController = BaseController.extend({

        init: function ($rootScope, $scope, $stateParams, $state, $location, $uibModal, $userService) {
            this.$rootScope = $rootScope;
            this.$stateParams = $stateParams;
            this.$state = $state;
            this.$location = $location;
            this.$uibModal = $uibModal;

            this.$uibModalInstance = $scope.$parent.$uibModalInstance;
            this.$userService = $userService;
            this._super($scope);
        },

        defineScope: function () {
            this._defineValidator();
            this._defineLogin();
            this._defineCancel();
        },

        _defineValidator: function () {
            var owner = this;
            this.$scope.bindValidator = function (element) {
                $(element).validate({
                    rules: {
                        username: {
                            required: true,
                            emailOrPhone: true
                        },
                        password: {
                            required: true,
                            password: true
                        }
                    },
                    messages: {
                        username: {
                            required: "请输入您的手机/邮件地址",
                            emailOrPhone: "请输入正确的手机/邮件地址"
                        },
                        password: {
                            required: "请输入您的新密码",
                            password: "您的密码长度不正确(6到20个字符)"
                        }
                    },
                    submitHandler: function () {
                        owner.$scope.login();
                    }
                });
            };
        },

        _defineLogin: function () {
            var owner = this;
            this.$scope.login = function () {
                owner.$userService.login(owner.$scope.userInfo).then(
                    function (resp) {
                        owner.$uibModalInstance.close();
                        owner.$rootScope.$broadcast('contextChange', resp.data);
                    },
                    function (resp) {
                        owner.$scope.error = resp.data ? resp.data.message : '服务器正忙，请稍后再试';
                    }
                );
            }
        },

        _defineCancel: function () {
            var owner = this;
            this.$scope.cancel = function () {
                owner.$uibModalInstance.dismiss("cancel");
            }
        }

    });

    SignInController.$inject = ['$rootScope', '$scope', '$stateParams', '$state', '$location', '$uibModal', 'UserService'];
    angular.module('module.dialog.SignInController', ['service.UserService']).controller('SignInController', SignInController);

});