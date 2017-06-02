define([
    '../../base/BaseController',
    'app/directive/adAnalytics',
    'service/UserService',
    'validate'
], function (BaseController) {
    var RegisterController = BaseController.extend({

        init: function ($rootScope, $scope, $state, $stateParams, $window, $userService) {
            this.$rootScope = $rootScope;
            this.$state = $state;
            this.$stateParams = $stateParams;
            this.$window = $window;
            this.$userService = $userService;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function () {
            this._cancelPassword();
            this._defineRegister();
            this._showPassword();
            this._bindValidator();
            this._definePhoneRegister();
            this._defineShowLogin();
        },

        _defineRegister: function () {
            var owner = this;
            owner.$scope.registerSuccess = false;
            owner.$scope.register = function () {
                owner.$userService.register(owner.$scope.userInfo).then(
                    function (resp) {
                        if (resp && resp.status == 201) {
                            owner.$scope.registerSuccess = true;
                            owner.$scope.mailRegisterSuccess = true;
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
                        }
                    }
                );
            };
        },

        _definePhoneRegister: function () {
            var owner = this;
            owner.$scope.phoneSendCode = function(){
                if( $(".phone-register-send-code a").hasClass("disabled")){
                    return;
                }
                $(".phone-register-send-code a").text("等待60秒");
                $(".phone-register-send-code a").addClass("disabled");
                var number = 59;
                var time = setInterval(function(){
                    $(".phone-register-send-code a").text('等待' + number + '秒');
                    number -- ;
                    if(number == 0 || owner.$scope.phoneRegisterSuccess){
                        clearInterval(time);
                        $(".phone-register-send-code a").text("重新发送");
                        $(".phone-register-send-code a").removeClass("disabled");
                    }
                },1000);
                var phone = owner.$scope.phoneInfo.phone;
               if(phone && /^(1[3578][0-9]{9}|(\\d{3,4}-)\\d{7,8}(-\\d{1,4})?)$/.test(phone)){
                   var phoneInfo = {
                       phone:phone
                   }
                   owner.$userService.phoneRegisterCode(phoneInfo).then(
                       function (resp) {
                           console.log("验证码发送成功");
                       },
                       function(resp){
                           if (resp && resp.status == 409) {
                               owner.$scope.error = "用户的手机号已经被使用了";
                               clearInterval(time);
                               $(".phone-register-send-code a").text("发送验证码");
                               $(".phone-register-send-code a").removeClass("disabled");
                           }
                       });
               }
            };
            owner.$scope.phoneRegister = function () {
                owner.$userService.phoneRegister(owner.$scope.phoneInfo).then(
                    function (resp) {
                        if (resp && resp.status == 201) {
                            owner.$scope.phoneRegisterShow = false;
                            owner.$scope.registerSuccess = true;
                            owner.$scope.phoneRegisterSuccess = true;
                        }
                    },
                    function (resp) {
                        if (resp && resp.status == 409) {
                            if (resp.data && resp.data.code == 'SRV_CONFLICT_USER_NAME') {
                                owner.$scope.error = "用户已经存在";
                            }
                            if (resp.data && resp.data.code == 'SRV_CONFLICT_USER_MAIL') {
                                owner.$scope.error = "手机号已经被注册";
                            }
                        }
                        if(resp && resp.status == 400){
                            if (resp.data && resp.data.code == 'SRV_ARG_INVALID_IDENTIFYING_CODE') {
                                owner.$scope.error = "验证码错误";
                            }
                        }
                    }
                );
            };
        },

        _cancelPassword: function () {
            var owner = this;
            owner.$scope.cancelPassword = function () {
                if(owner.$scope.userInfo){
                    owner.$scope.userInfo.password = '';
                }else{
                    owner.$scope.phoneInfo.password = '';
                }
            }
        },

        _showPassword: function () {
            var owner = this;
            owner.$scope.showPassword = function () {
                var password = $("#inputPassword3").prop('type');
                "password" == $("#inputPassword3").attr("type") ?
                    $("#inputPassword3").attr("type", "text") :
                    $("#inputPassword3").attr("type", "password");

                var phonePassword = $("#inputPassword4").prop('type');
                "password" == $("#inputPassword4").attr("type") ?
                    $("#inputPassword4").attr("type", "text") :
                    $("#inputPassword4").attr("type", "password");
            }
        },

        _bindValidator: function () {
            var owner = this;
            this.$scope.bindValidator = function (element) {
                $(element).validate({
                    rules: {
                        username: {
                            required: true,
                            username: true
                        },
                        email: {
                            required: true,
                            email: true
                        },
                        password: {
                            required: true,
                            password: true
                        }
                    },
                    messages: {
                        username: {
                            required: '请填写姓名',
                            username: '包含特殊字符'
                        },
                        email: {
                            required: '请填写邮箱',
                            email: '邮箱格式不正确'
                        },
                        password: {
                            required: '请填写密码',
                            password: '密码长度不正确'
                        }

                    },
                    submitHandler: function () {
                        owner.$scope.register();
                    }
                });
            };
            this.$scope.phoneRegisterValidator = function (element) {
                $(element).validate({
                    rules: {
                        username: {
                            required: true,
                            username: true
                        },
                        phone: {
                            required: true,
                            phone: true
                        },
                        password: {
                            required: true,
                            password: true
                        },
                        code:{
                            required: true
                        }
                    },
                    messages: {
                        username: {
                            required: '请填写姓名',
                            username: '包含特殊字符'
                        },
                        phone: {
                            required: '请填写手机号',
                            phone: '请填写正确的手机号'
                        },
                        password: {
                            required: '请填写密码',
                            password: '密码长度不正确'
                        },
                        code:{
                            required:"请输入验证码"
                        }

                    },
                    submitHandler: function () {
                        owner.$scope.phoneRegister();
                    }
                });
            }
        },

        _defineShowLogin:function() {
            var time = setInterval(function () {
                if ($(".user-login-box")) {
                    clearInterval(time);
                    var height = screen.height;
                    $(".user-login-box").height(height);
                }
            }, 100);
        }
    });

    RegisterController.$inject = ['$rootScope', '$scope', '$state', '$stateParams', '$window', 'userService'];

    angular.module('module.my.RegisterController', ['service.UserService']).controller('RegisterController', RegisterController);

});