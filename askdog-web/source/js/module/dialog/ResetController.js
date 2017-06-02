define(['base/BaseController', 'jquery.validator', 'service/UserService'], function (BaseController) {

    var ResetController = BaseController.extend({

        init: function ($rootScope, $scope, $stateParams, $state, $uibModal, $userService) {
            this.$rootScope = $rootScope;
            this.$stateParams = $stateParams;
            this.$state = $state;
            this.$uibModal = $uibModal;

            this.$uibModalInstance = $scope.$parent.$uibModalInstance;
            this.$userService = $userService;
            this._super($scope);
        },

        defineScope: function () {
            this._defineValidator();
            this._defineRecoverStepHandler();
            this._defineGetIdentifyingCode();
            this._defineCancel();
            this._defineGoSignIn();
        },

        _defineCancel: function () {
            var owner = this;
            this.$scope.cancel = function () {
                owner.$uibModalInstance.dismiss("cancel");
            }
        },

        _defineValidator: function () {
            var owner = this;
            this.$scope.voucherValidator = function (element) {
                $(element).validate({
                    rules: {
                        voucher: {
                            required: true,
                            emailOrPhone: true
                        }
                    },
                    messages: {
                        voucher: {
                            required: '请输入您的手机号/邮件地址',
                            emailOrPhone: '请输入正确的手机号/邮件地址'
                        }
                    },
                    submitHandler: function () {
                        owner.$scope.recoverStep1Handler();
                    }
                });
            };

            this.$scope.codeValidator = function (element) {
                $(element).validate({
                    rules: {
                        code: {
                            required: true,
                            identifyingCode: true
                        }
                    },
                    messages: {
                        code: {
                            required: "请输入验证码",
                            identifyingCode: ""
                        }
                    },
                    submitHandler: function () {
                        owner.$scope.recoverStep2Handler();
                    }
                });
            };

            this.$scope.recoverValidator = function (element) {
                $(element).validate({
                    rules: {
                        password: {
                            required: true,
                            password: true
                        },
                        confirmPassword: {
                            required: true,
                            password: true,
                            equalTo: "#recoverPassword"
                        }
                    },
                    messages: {
                        password: {
                            required: '请输入您的新密码',
                            password: '您的密码长度不正确(6到20个字符)'
                        },
                        confirmPassword: {
                            required: '请再次输入您的新密码',
                            password: '您的密码长度不正确(6到20个字符)',
                            equalTo: '两次输入的密码不一致'
                        }
                    },
                    submitHandler: function () {
                        owner.$scope.recoverStep3Handler();
                    }
                });
            }
        },

        _defineRecoverStepHandler: function () {
            var owner = this;
            this.$scope.recoverInfo = {
                step: 1,
                voucher: undefined,
                type: undefined
            };
            this.$scope.recoverStep1Handler = function () {
                owner.$scope.error = undefined;
                var voucher = owner.$scope.recoverInfo.voucher;
                owner.$scope.recoverInfo.type = owner._checkRecoverType(voucher);
                if (owner.$scope.recoverInfo.type == 'phone') {
                    owner.$scope.requireIdentifyingCode(
                        function () {
                            owner.$scope.recoverInfo.step = 2;
                        }, function () {
                        }
                    );
                } else if (owner.$scope.recoverInfo.type == 'mail') {
                    owner.$userService.recoverByMail(voucher).then(
                        function () {
                            owner.$scope.recoverInfo.step = 2;
                        },
                        function (resp) {
                            if (resp && resp.data.code == "SRV_NOT_FOUND_USER") {
                                owner.$scope.error = "该邮箱没有被注册";
                            }
                        }
                    );
                }
            };

            this.$scope.recoverStep2Handler = function () {
                owner.$scope.error = undefined;
                var voucher = owner.$scope.recoverInfo.voucher;
                var code = owner.$scope.recoverInfo.code;
                if (owner.$scope.recoverInfo.type == 'phone') {
                    owner.$userService.recoverCheckToken(voucher, code).then(
                        function () {
                            owner.$scope.recoverInfo.step = 3;
                        },
                        function (resp) {
                            if (resp && resp.status == 400) {
                                if (resp.data && resp.data.code == 'SRV_ARG_INVALID_IDENTIFYING_CODE') {
                                    owner.$scope.error = "验证码错误";
                                }
                            }
                        }
                    );
                } else if (owner.$scope.recoverInfo.type == 'mail') {

                }
            };

            this.$scope.recoverStep3Handler = function () {
                owner.$scope.error = undefined;
                var recoverInfo = {
                    "phone": owner.$scope.recoverInfo.voucher,
                    "code": owner.$scope.recoverInfo.code,
                    "password": owner.$scope.recoverInfo.password
                };

                if (owner.$scope.recoverInfo.type == 'phone') {
                    owner.$userService.recoverByPhone(recoverInfo).then(
                        function () {
                            owner.$scope.recoverInfo.step = 4;
                        },
                        function (resp) {
                            if (resp && resp.status == 400) {
                                if (resp.data && resp.data.code == 'SRV_ARG_INVALID_IDENTIFYING_CODE') {
                                    owner.$scope.error = "验证码错误";
                                }
                            }
                        }
                    );
                } else if (owner.$scope.recoverInfo.type == 'mail') {

                }
            };
        },

        _defineGetIdentifyingCode: function () {
            var owner = this;
            this.$scope.requireIdentifyingCode = function (successCallback, failCallback) {
                owner.$scope.error = undefined;
                var voucher = owner.$scope.recoverInfo.voucher;
                owner.$userService.recoverIdentifyingCode(voucher).then(
                    function (resp) {
                        if (successCallback) {
                            successCallback(resp);
                        }
                        owner.$scope.countdown = 60;
                        owner.$scope.identifyingCodeDisabled = true;
                        var interval = setInterval(function () {
                            if (owner.$scope.countdown == 1) {
                                owner.$scope.identifyingCodeDisabled = false;
                                clearInterval(interval);
                            } else {
                                owner.$scope.countdown--;
                            }
                            owner.$scope.$digest();
                        }, 1000);
                    },
                    function (resp) {
                        if (failCallback) {
                            failCallback(resp);
                        }
                        if (resp && resp.status == 404) {
                            if (resp.data && resp.data.code == 'SRV_NOT_FOUND_USER') {
                                owner.$scope.error = "用户没有被注册";
                                owner.$scope.identifyingCodeDisabled = true;
                            }
                        } else {
                            owner.$scope.identifyingCodeDisabled = false;
                        }
                    }
                );
            };
        },

        _checkRecoverType: function (voucher) {
            if (/^(1[3578][0-9]{9}|(\\d{3,4}-)\\d{7,8}(-\\d{1,4})?)$/.test(voucher)) {
                return 'phone';
            } else if (/^[^@\s]+@(?:[^@\s.]+)(?:\.[^@\s.]+)+$/.test(voucher)) {
                return 'mail';
            }
            return undefined;
        },

        _defineGoSignIn: function () {
            var owner = this;
            this.$scope.goSignIn = function () {
                owner.$uibModalInstance.dismiss("cancel");
                owner.$rootScope.signIn();
            }
        }

    });

    ResetController.$inject = ['$rootScope', '$scope', '$stateParams', '$state', '$uibModal', 'UserService'];
    angular.module('module.dialog.ResetController', ['service.UserService']).controller('ResetController', ResetController);

});