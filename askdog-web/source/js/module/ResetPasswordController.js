define(['base/BaseController', 'app/directive/adAnalytics', 'service/UserService', 'jquery.validator'], function (BaseController) {

    var ResetPasswordController = BaseController.extend({

        _contextChangeListener: null,

        init: function ($rootScope, $scope, $stateParams, $uibModal, $state, $userService) {
            this.$rootScope = $rootScope;
            this.$stateParams = $stateParams;
            this.$uibModal = $uibModal;
            this.$state = $state;

            this.$userService = $userService;
            this._super($scope);
        },

        defineScope: function () {
            this.$scope.token = {};
            this._initializeStatus();
            this._bindValidator();
            this._defineGoSignIn();
        },

        defineListeners: function () {
            var owner = this;
            this._contextChangeListener = this.$rootScope.$on('contextChange', function (event, userSelf) {
                if (userSelf) {
                    owner.$state.go('layout.view-index.index');
                }
            });
        },

        destroy: function () {
            this._contextChangeListener();
            this._contextChangeListener = null;
        },


        _initializeStatus: function () {
            var owner = this;
            this.$scope.status = "verifying";
            this.$userService.validatePasswordToken(this.$stateParams.token)
                .then(function (resp) {
                    owner.$scope.data = resp.data;
                    owner.$scope.status = resp.data.valid ? "valid" : "invalid";
                }, function () {
                    owner.$scope.status = "invalid";
                });
        },

        _bindValidator: function () {
            var owner = this;
            this.$scope.bindValidator = function (element) {
                $(element).validate({
                    rules: {
                        mail: {
                            required: true,
                            email: true
                        },
                        password: {
                            required: true,
                            password: true
                        },
                        confirm_password: {
                            required: true,
                            password: true,
                            equalTo: "#password"
                        }
                    },
                    messages: {
                        mail: {
                            required: "请输入您的邮件地址",
                            email: "请输入正确的邮件地址"
                        },
                        password: {
                            required: "请输入您的新密码",
                            password: "您的密码长度不正确(6到20个字符)"
                        },
                        confirm_password: {
                            required: "请再次输入您的新密码",
                            password: "您的密码长度不正确(6到20个字符)",
                            equalTo: '两次密码输入的不匹配'
                        }
                    },
                    submitHandler: function () {
                        owner._resetPassword();
                    }
                });
            }
        },

        _resetPassword: function () {
            var owner = this;
            this.$userService.resetPassword(this.$stateParams.token, this.$scope.token.mail, this.$scope.token.password)
                .then(function () {
                    owner.$scope.status = "succeed";
                }, function () {
                    owner.$scope.status = "failed";
                });
        },

        _defineGoSignIn: function () {
            var owner = this;
            this.$scope.goSignIn = function () {
                owner.$rootScope.signIn();
            }
        }

    });

    ResetPasswordController.$inject = ['$rootScope', '$scope', '$stateParams', '$uibModal', '$state', 'UserService'];
    angular.module('module.ResetPasswordController', ['service.UserService']).controller('ResetPasswordController', ResetPasswordController);

});