define([
    'base/BaseController',
    'jquery.validator',
    'service/UserService'
], function (BaseController) {

    var ModifyPasswordController = BaseController.extend({

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

        _defineViewHandler: function () {
            var owner = this;
            owner.$scope.modifyPassword = function (change_password) {
                console.log(change_password);
                owner.$userService.modifyPassword(change_password)
                    .then(function () {
                        owner.$scope.successMessage = "修改成功";
                        owner.$uibModalInstance.close();
                    }, function (resp) {
                        owner.$scope.error = resp.data.message || '服务器正忙，请稍后再试';
                    });
            };
        },

        _defineBindValidator: function () {
            var owner = this;
            this.$scope.bindValidator = function (element) {
                $(element).validate({
                    rules: {
                        old_password: {
                            required: true,
                            password: true
                        },
                        new_password: {
                            required: true,
                            password: true
                        },
                        confirm_password: {
                            required: true,
                            equalTo: '#new_password'
                        }
                    },
                    messages: {
                        old_password: {
                            required: '',
                            password: '请输入6~20位的密码'
                        },
                        new_password: {
                            required: '',
                            password: '请输入6~20位的密码'
                        },
                        confirm_password: {
                            required: '',
                            password: '请输入6~20位的密码',
                            equalTo: '您两次输入的密码不匹配'
                        }
                    },
                    submitHandler: function () {
                        owner.$scope.modifyPassword();
                    }
                });
            }
        }
    });

    ModifyPasswordController.$inject = ['$rootScope', '$scope', '$state', '$stateParams', '$uibModal', 'UserService'];

    angular.module('module.dialog.ModifyPasswordController', ['service.UserService']).controller('ModifyPasswordController', ModifyPasswordController);

});
