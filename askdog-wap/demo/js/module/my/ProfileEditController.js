define([
    '../../base/BaseController',
    'app/directive/adAnalytics',
    'service/UserService',
    'validate',
    'app/directive/autoHeight'
], function (BaseController) {
    var ProfileEditController = BaseController.extend({
        init: function ($scope, $stateParams, $userService, $state) {
            this.$stateParams = $stateParams;
            this.$userService = $userService;
            this.$state = $state;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function () {
            this._defineUserInfo();
            this._bindValidator();
        },

        _defineUserInfo: function () {
            var owner = this;
            owner.$userService.getInfo()
                .success(function (data) {
                    owner.$scope.personalInfo = data;
                    owner.$scope.personalInfo.signature =  owner.$scope.personalInfo.signature.replace(/[\r\n]/g,"");
                });
        },

        _bindValidator: function () {
            var owner = this;
            owner.$scope.bindValidator = function (element) {
                $(element).validate({
                    rules: {
                        phone: "phone",
                        zhanghao: {
                            username: true,
                            required: true
                        }
                    },
                    messages: {
                        phone: "请填写正确的手机号",
                        zhanghao: {
                            username: '姓名格式不正确',
                            required: '请填写姓名'
                        }
                    },
                    submitHandler: function () {
                        owner._updateInfo();
                    }
                });
            };
        },

        _updateInfo: function () {
            var owner = this;
            owner.$userService.updateInfo(owner.$scope.personalInfo).then(
                function () {
                    window.history.go(-1);
                }
            );
        }
    });

    ProfileEditController.$inject = ['$scope', '$stateParams', 'userService', '$state'];

    angular.module('module.my.ProfileEditController', ['service.UserService']).controller('ProfileEditController', ProfileEditController);

});