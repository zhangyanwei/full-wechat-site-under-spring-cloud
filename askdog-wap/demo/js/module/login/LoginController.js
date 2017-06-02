define([
    '../../base/BaseController',
    'app/directive/adAnalytics',
    'service/UserService',
    'validate'
], function (BaseController) {
    var LoginController = BaseController.extend({

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
            this._showPassword();
            this._bindValidator();
            this._otherLogin();
            this._showForgetPwd();
            this._defineRecover();
            this._defineSendCode();
            this._defineShowLogin();
            this.$scope.loginTitle = '登录';
            this.$rootScope.forgetPassword = false;
        },

        _otherLogin: function () {
            this.$scope.qqLoginLink = "/login/qq?request=" + encodeURIComponent(this.$stateParams.request || window.location.origin);
            this.$scope.weiboLoginLink = "/login/weibo?request=" + encodeURIComponent(this.$stateParams.request || window.location.origin);
            this.$scope.weixinLoginLink = "/login/wechat/connect?request=" + encodeURIComponent(this.$stateParams.request || window.location.origin);
        },

        _signIn: function () {
            var owner = this;
            owner.$userService.login(owner.$scope.userInfo.username, owner.$scope.userInfo.password)
                .then(function (resp) {
                    var location =owner.$stateParams.request;
                    owner.$rootScope.user = resp.data;
                    if (resp.data && resp.data.category_codes == undefined) {
                        owner.$rootScope.$broadcast('noSubscribe');
                    } else if (location) {
                        window.location.href = owner.$stateParams.request;
                    }
                    else {
                        var hash = owner.$stateParams.hash;
                        window.location = hash || '/';
                    }
                }, function (resp) {
                    owner.$scope.error = resp.data ? resp.data.message : '服务器正忙，请稍后再试';
                });

        },

        _defineRecover: function () {
            var owner = this;
            owner.$scope.recoverPassword = function(){
                owner.$userService.recover(owner.$scope.recoverMail).then(
                    function (resp) {
                        if (resp && resp.status == 200) {
                            owner.$rootScope.resetPwdSuccess = true;
                        }
                    },
                    function (resp) {
                        if (resp && resp.status == 404) {
                            if (resp.data.code == 'SRV_NOT_FOUND_USER') {
                                owner.$scope.psdResetError = "该邮箱未注册";
                            }
                        }
                    }
                );
            };
        },

        _bindValidator: function () {
            var owner = this;
            this.$scope.bindValidator = function (element) {
                $(element).validate({
                    rules: {
                        loginUsername: {
                            required: true
                        },
                        loginPassword: "required"

                    },
                    messages: {
                        loginUsername: {
                            required: '请填写账号'
                        },
                        loginPassword: '请填写密码'
                    },
                    submitHandler: function () {
                        owner._signIn();
                    }
                });
            };
            this.$scope.findValidator = function(element){
                $(element).validate({
                    rules:{
                        recoverMail:{
                            required:true,
                            emailOrPhone:true
                        }
                    },
                    messages:{
                        recoverMail:{
                            required:'请输入邮箱或者手机号',
                            emailOrPhone:'请输入正确的邮箱或者手机号'
                        }
                    },
                    submitHandler:function(){
                        if(owner.$scope.recoverMail.match(/^(1[3578][0-9]{9}|(\\d{3,4}-)\\d{7,8}(-\\d{1,4})?)$/)){
                            owner.$scope.phoneForgetPassword = true;
                            owner.$scope.$digest();
                        }else{
                            owner.$scope.recoverPassword();
                        }
                    }
                });
            };
            this.$scope.phoneForgetValidator = function(element){
                $(element).validate({
                    rules:{
                        code:{
                            required:true
                        }
                    },
                    messages:{
                        code:{
                            required:'请输入验证码'
                        }
                    },
                    submitHandler:function(){
                       owner.$scope.validatorPhoneOrCode();
                    }
                });
            };
            this.$scope.resetBindValidator = function(element){
                $(element).validate({
                    rules:{
                        phoneNewPassword:{
                            required:true,
                            password:true
                        },
                        phoneAgainPassword:{
                            required:true,
                            password:true,
                            equalTo:"#phoneResetPassword"
                        }
                    },
                    messages:{
                        phoneNewPassword:{
                            required:'请输入新密码',
                            password:'密码长度不正确'
                        },
                        phoneAgainPassword:{
                            required:"请输入确认密码",
                            password:'两次密码输入不一致',
                            equalTo:"两次密码输入不一致"
                        }
                    },
                    submitHandler:function(){
                        owner.$scope.updatePassword();
                    }
                });
            }
        },

        _showPassword: function () {
            var owner = this;
            owner.$scope.showPassword = function () {
                var password = $("#password").prop('type');
                "password" == $("#password").attr("type") ?
                    $("#password").attr("type", "text") :
                    $("#password").attr("type", "password");
            }
        },

        _showForgetPwd:function(){
            var owner = this;
            owner.$scope.showForgetPwd = function(){
                owner.$rootScope.forgetPassword = true;
                owner.$scope.phoneResetPwdSuccess = false;
                owner.$scope.loginTitle = '密码重置';
            }
        },

        _defineSendCode:function(){
            var owner = this;
            var time;
            owner.$scope.sendCode = function(){
                if( $(".phone-reset-send a").hasClass("disabled")){
                    return;
                }
                $(".phone-reset-send a").text("等待60秒");
                $(".phone-reset-send a").addClass("disabled");
                var number = 59;
                 time = setInterval(function(){
                    $(".phone-reset-send a").text('等待' + number + '秒');
                    number -- ;
                    if(number == 0 || owner.$scope.phoneRegisterSuccess){
                        clearInterval(time);
                        $(".phone-reset-send a").text("重新发送");
                        $(".phone-reset-send a").removeClass("disabled");
                    }
                },1000);
                owner.$userService.phoneRecover(owner.$scope.recoverMail).then(function(){
                   owner.$userService.phoneRetSend(owner.$scope.recoverMail).then(function(){
                       console.log("验证码发送成功了");
                       owner.$scope.sendCodeSuccess = true;
                   });
                },
                function(resp){
                    if(resp && resp.status == 404){
                        owner.$scope.error = '用户不存在';
                        clearInterval(time);
                        $(".send-code a").text("发送验证码");
                        $(".send-code a").removeClass("disabled");
                    }
                });
            };
            owner.$scope.validatorPhoneOrCode = function(){

                var phoneInfo = {
                    phone:owner.$scope.recoverMail,
                    code:owner.$scope.phoneInfo.code
                }
                owner.$userService.phoneOrCodeValidator(phoneInfo).then(
                    function(){
                        console.log("验证码验证成功");
                        clearInterval(time);
                        $(".send-code a").text("发送验证码");
                        $(".send-code a").removeClass("disabled");
                        owner.$scope.codeValidator = true;
                        owner.$scope.phoneForgetPassword = false;
                    },
                    function(resp){
                        if(resp && resp.status == 400){
                            owner.$scope.validatorError = "验证码不正确";
                        }
                    }
                );
            };
            owner.$scope.updatePassword = function(){
                var data = {
                    phone:owner.$scope.recoverMail,
                    code:owner.$scope.phoneInfo.code,
                    password:owner.$scope.phoneReset.newPassword
                }
                owner.$userService.updatePhonePassword(data).then(
                    function(){
                       owner.$scope.codeValidator = false;
                        owner.$scope.phoneResetPwdSuccess = true;
                        owner.$scope.recoverMail = '';
                        owner.$scope.validatorError = false;
                        owner.$scope.phoneInfo.code = '';
                        owner.$scope.phoneReset='';
                    }
                );
            }
        },

        _defineShowLogin:function(){
            var time = setInterval(function(){
                if($(".user-login-box")){
                    clearInterval(time);
                    var height = screen.height;
                    $(".user-login-box").height(height);
                }
            },100);
            var owner = this;
            owner.$scope.cancelNewPassword = function(){
                owner.$scope.phoneReset.newPassword = '';
            }
            owner.$scope.cancelAgainPassword = function(){
                owner.$scope.phoneReset.againPassword = '';
            }
            owner.$scope.showNewPassword = function(){
                "password" == $( "#phoneResetPassword" ).attr( "type" ) ?
                    $( "#phoneResetPassword"  ).attr( "type", "text" ) :
                    $( "#phoneResetPassword"  ).attr( "type", "password" );
            }
            owner.$scope.showAgainPassword = function(){
                "password" == $( "#phoneResetAgainPsd" ).attr( "type" ) ?
                    $( "#phoneResetAgainPsd"  ).attr( "type", "text" ) :
                    $( "#phoneResetAgainPsd"  ).attr( "type", "password" );
            }
        }
    });

    LoginController.$inject = ['$rootScope', '$scope', '$state', '$stateParams', '$window', 'userService'];

    angular.module('module.login.LoginController', ['service.UserService']).controller('LoginController', LoginController);

});