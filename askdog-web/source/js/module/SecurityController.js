define([
    'base/BaseController',
    'jquery.validator',
    'app/directive/adAnalytics',
    'service/UserService'
], function (BaseController) {

    var SecurityController = BaseController.extend({

        _contextReadyListener: null,
        _WIDTH: 800,
        _HEIGHT: 800,
        _BIND_TOKEN_LINK: "/api/users/bind/wx/token?width={width}&height={height}&stamp={stamp}",

        init: function (_rootScope, _scope, _stateParams, _userService) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$userService = _userService;
            this._super(_scope);
        },

        defineScope: function () {
            this._loadAccounts();
            this._defineGenerateQRCode();
            this._defineUnbind();
            this._defineValidator();
            this._defineChangePassword();
        },

        defineListeners: function () {
            var owner = this;
            this._contextReadyListener = this.$rootScope.$on('contextReady', function (event, userSelf) {
                if (userSelf && !owner.$scope.accounts) {
                    owner._loadAccounts();
                }
            });
        },

        destroy: function () {
            this._contextReadyListener();
            this._contextReadyListener = null;
        },

        _loadAccounts: function () {
            var owner = this;
            this.$userService.accounts().then(
                function (resp) {
                    if (resp && resp.data && resp.data.length > 0) {
                        var accounts = resp.data;
                        for (var index = 0; index < accounts.length; index++) {
                            if (accounts[index].provider == "WECHAT") {
                                owner.$scope.isBind = true;
                                owner.$scope.accounts = accounts;
                                break;
                            }
                        }
                        if (!owner.$scope.isBind) {
                            owner._genderQRCode();
                        }
                    } else {
                        owner._genderQRCode();
                    }
                },
                function (resp) {
                    if (resp && resp.status == 400 && resp.data.code == 'SRV_NOT_FOUND_USER_REVENUE_ACCOUNT') {
                        owner._genderQRCode();
                    }
                }
            );
        },

        _defineGenerateQRCode: function () {
            var owner = this;
            this.$scope.generateQRCode = function () {
                owner._genderQRCode();
            };
        },

        _defineUnbind: function () {
            var owner = this;
            this.$scope.unbind = function (account) {
                owner.$userService.unbind(account.external_user_id, account.provider).then(
                    function (resp) {
                        owner._genderQRCode();
                    }
                );
            };
        },

        _genderQRCode: function () {
            this.$scope.isBind = false;
            this.$scope.bind_token_link = this._BIND_TOKEN_LINK.format({
                "width": this._WIDTH,
                "height": this._HEIGHT,
                "stamp": new Date().getTime()
            });
        },

        _defineValidator: function () {
            var owner = this;
            this.$scope.bindValidator = function (element) {
                $(element).validate({
                    rules: {
                        origin_password: {
                            required: true,
                            password: true
                        },
                        new_password: {
                            required: true,
                            password: true
                        },
                        confirm_password: {
                            required: true,
                            password: true,
                            equalTo: '#password'
                        }
                    },
                    messages: {
                        origin_password: {
                            required: '请输入您的原密码',
                            password: '您的密码长度不正确(6到20个字符)'
                        },
                        new_password: {
                            required: '请输入您的新密码',
                            password: '您的密码长度不正确(6到20个字符)'
                        },
                        confirm_password: {
                            required: '请再次输入您的新密码',
                            password: '您的密码长度不正确(6到20个字符)',
                            equalTo: '两次输入的密码不一致'
                        }
                    },
                    submitHandler: function () {
                        owner.$scope.changePassword();
                    }
                });
            };
        },

        _defineChangePassword: function () {
            var owner = this;
            owner.$scope.amendPassword = {};
            this.$scope.changePassword = function () {
                var amendPassword = {
                    origin_password: owner.$scope.amendPassword.origin_password,
                    new_password: owner.$scope.amendPassword.new_password
                };
                owner.$userService.changePassword(amendPassword).then(
                    function () {
                        owner.$scope.info = "修改成功";
                        owner.$scope.amendPassword = {};
                    },
                    function (resp) {
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
                    }
                )
            }
        }

    });

    SecurityController.$inject = ['$rootScope', '$scope', '$stateParams', 'UserService'];

    angular.module('module.SecurityController', ['service.UserService']).controller('SecurityController', SecurityController);

});