define(['base/BaseController', 'jquery.validator', 'service/UserService'], function (BaseController) {

    var RegisterController = BaseController.extend({

        $rootScope: null,
        $stateParams: null,
        $userService: null,

        init: function ($rootScope, $scope, $stateParams, $userService) {
            this.$rootScope = $rootScope;
            this.$stateParams = $stateParams;
            this.$uibModalInstance = $scope.$parent.$uibModalInstance;
            this.$userService = $userService;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function () {
            this._defineValidator();
            this._defineGetIdentifyingCode();
            this._defineRegister();
            this._defineGoSignIn();
            this._defineCancel();
        },

        _defineValidator: function () {
            var owner = this;
            this.$scope.userInfo = {};
            this.$scope.bindValidator = function (element) {
                $(element).validate({
                    rules: {
                        name: {
                            required: true,
                            username: true
                        },
                        phone: {
                            required: true,
                            phone: true
                        },
                        code: {
                            required: true,
                            identifyingCode: true
                        },
                        mail: {
                            required: true,
                            email: true
                        },
                        password: {
                            required: true,
                            password: true
                        }
                    },
                    messages: {
                        name: {
                            required: "请输入您的姓名",
                            username: '1-20位（不能含有特殊字符）'
                        },
                        phone: {
                            required: "请输入您的手机号",
                            phone: '请输入正确的手机号码'
                        },
                        code: {
                            required: "请输入验证码",
                            identifyingCode: ""
                        },
                        mail: {
                            required: "请输入您的邮件地址",
                            email: "请输入正确的邮件地址"
                        },
                        password: {
                            required: "请输入您的新密码",
                            password: "您的密码长度不正确(6到20个字符)"
                        }
                    },
                    submitHandler: function () {
                        owner.$scope.register();
                    }
                });
            };
        },

        _defineGetIdentifyingCode: function () {
            var owner = this;
            this.$scope.getIdentifyingCode = function () {
                owner.$scope.error = undefined;
                var phoneNo = owner.$scope.userInfo.phone;
                if (phoneNo && /^(1[3578][0-9]{9}|(\\d{3,4}-)\\d{7,8}(-\\d{1,4})?)$/.test(phoneNo)) {
                    owner.$scope.identifyingCodeDisabled = true;
                    var userInfo = {
                        phone: phoneNo
                    };
                    owner.$userService.phoneIdentifyingCode(userInfo).then(
                        function () {
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
                            if (resp && resp.status == 409) {
                                if (resp.data && resp.data.code == 'SRV_CONFLICT_USER_PHONE') {
                                    owner.$scope.error = "手机号已经被注册";
                                }
                            }
                            owner.$scope.identifyingCodeDisabled = undefined;
                        }
                    );
                } else {
                    owner.$scope.identifyingCodeDisabled = undefined;
                }
            };
        },

        _defineRegister: function () {
            var owner = this;
            this.$scope.qqLoginLink = "/login/qq?request=" + encodeURIComponent(window.location.href);
            this.$scope.weiboLoginLink = "/login/weibo?request=" + encodeURIComponent(window.location.href);
            this.$scope.wechatLoginLink = "/login/wechat/connect?request=" + encodeURIComponent(window.location.href);
            this.$scope.register = function () {
                owner.$scope.error = undefined;
                var userInfo = {};
                if (owner.$scope.phoneRegister) {
                    userInfo = {
                        name: owner.$scope.userInfo.name,
                        phone: owner.$scope.userInfo.phone,
                        password: owner.$scope.userInfo.password,
                        code: owner.$scope.userInfo.code
                    };
                } else {
                    userInfo = {
                        name: owner.$scope.userInfo.name,
                        mail: owner.$scope.userInfo.mail,
                        password: owner.$scope.userInfo.password
                    };
                }
                owner.$userService.register(owner.$scope.userInfo, owner.$scope.phoneRegister).then(
                    function (resp) {
                        if (resp && resp.status == 201) {
                            owner.$scope.registerSuccess = true;
                        }
                    },
                    function (resp) {
                        if (resp && resp.status == 409) {
                            if (resp.data && resp.data.code == 'SRV_CONFLICT_USER_NAME') {
                                owner.$scope.error = "用户已经存在";
                            }
                            if (resp.data && resp.data.code == 'SRV_CONFLICT_USER_MAIL') {
                                owner.$scope.error = "邮箱已经被注册";
                            }
                            if (resp.data && resp.data.code == 'SRV_CONFLICT_USER_PHONE') {
                                owner.$scope.error = "手机号已经被注册";
                            }
                        }
                        if (resp && resp.status == 400) {
                            if (resp.data && resp.data.code == 'SRV_ARG_INVALID_IDENTIFYING_CODE') {
                                owner.$scope.error = "验证码错误";
                            }
                        }
                    }
                );
            }
        },

        _defineCancel: function () {
            var owner = this;
            this.$scope.cancel = function () {
                owner.$uibModalInstance.dismiss("cancel");
            }
        },

        _defineGoSignIn: function () {
            var owner = this;
            this.$scope.goSignIn = function () {
                owner.$uibModalInstance.dismiss("sign-in");
                owner.$rootScope.signIn();
            }
        }
    });

    RegisterController.$inject = ['$rootScope', '$scope', '$stateParams', 'UserService'];

    angular.module('module.dialog.RegisterController', ['service.UserService']).controller('RegisterController', RegisterController);

});