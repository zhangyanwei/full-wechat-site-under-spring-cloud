define([
    '../../base/BaseController',
    'app/directive/adAnalytics',
    'service/ChannelService',
    'service/UserService',
    'validate'
], function (BaseController) {
    var PsdResetController = BaseController.extend({

        $stateParams: null,
        $channelService: null,

        /**
         * @Override
         * @param $scope
         * @param $stateParams
         * @param $channelService
         */
        init: function ($scope, $stateParams, $channelService,$userService) {
            this.$stateParams = $stateParams;
            this.$channelService = $channelService;
            this.$userService = $userService;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope:function(){

            this._bindValidator();
            this._cancelPassword();
            this._showPassword();
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

        _resetPassword:function(){
            var owner = this;
            owner.$userService.resetPwd(owner.$stateParams.token,owner.$scope.userInfo)
                .then(function(){
                    owner.$scope.status = "succeed";
                },function(){
                    owner.$scope.status = "failed";
                });
        },

        _bindValidator: function(){
            var owner = this;
            this.$scope.bindValidator = function (element) {
                $(element).validate({
                    rules:{
                        mailAddress:{
                            required:true,
                            email:true
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
                        mailAddress:{
                            required:'请填写邮箱地址',
                            email:"请填写正确的邮箱地址"
                        },
                        newPassword:{
                            required:'请填写新密码',
                            password:'密码长度不正确'
                        },
                        againPassword:{
                            required:'请填写确认密码',
                            password:'密码长度不正确',
                            equalTo:'两次密码输入的不匹配'
                        }

                    },
                    submitHandler:function(){
                        owner._resetPassword();
                    }
                });
            };
        },

        _cancelPassword:function(){
            var owner = this;
            owner.$scope.cancelNewPassword = function(){
                owner.$scope.userInfo.password = '';
            }
            owner.$scope.cancelAgainPassword = function(){
                owner.$scope.againPassword = '';
            }
        },

        _showPassword:function(){
            var owner = this;
            owner.$scope.showNewPassword = function(){
                "password" == $( "#newPassword" ).attr( "type" ) ?
                    $( "#newPassword"  ).attr( "type", "text" ) :
                    $( "#newPassword"  ).attr( "type", "password" );
            }
            owner.$scope.showAgainPassword = function(){
                "password" == $( "#againPassword" ).attr( "type" ) ?
                    $( "#againPassword"  ).attr( "type", "text" ) :
                    $( "#againPassword"  ).attr( "type", "password" );
            }
        }


    });

    PsdResetController.$inject = ['$scope', '$stateParams', 'channelService','userService'];

    angular.module('module.my.PsdResetController', ['service.ChannelService','service.UserService']).controller('PsdResetController', PsdResetController);

});