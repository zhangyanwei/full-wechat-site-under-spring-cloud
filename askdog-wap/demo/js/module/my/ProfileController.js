define([
    '../../base/BaseController',
    'app/directive/adAnalytics',
    'service/UserService',
    'validate',
    'app/directive/vipApprove'
], function (BaseController) {
    var ProfileController = BaseController.extend({

        $stateParams: null,
        $channelService: null,

        /**
         * @Override
         * @param $scope
         * @param $stateParams
         * @param $channelService
         */
        init: function ($scope, $stateParams, $userService,$uibModal,$state) {
            this.$stateParams = $stateParams;
            this.$userService = $userService;
            this.$uibModal = $uibModal;
            this.$state = $state;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function () {
            this._getInfo();
            this._updateGender();
            this._getAvatar();
            this._bindValidator();
        },

        _updateInfo: function () {
            var owner = this;
            owner.$userService.updateInfo(owner.$scope.personalInfo).then(
                function(){
                    owner.$state.go('view-profile.zone');
                    var successModal = owner.$uibModal.open({
                        windowTemplateUrl: 'views/base/modal-window.html',
                        windowTopClass: 'status-modal',
                        templateUrl: 'views/dialog/success.html',
                        controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                            $scope.$uibModalInstance = $uibModalInstance;
                            $scope.message = '保存成功';
                        }]
                    });
                });
        },

        _updateGender: function () {
            var owner = this;
            owner.$scope.updateGender = function () {
                owner.$userService.updateInfo(owner.$scope.personalInfo)
                    .success(function () {
                    });
            }
        },

        _getInfo: function () {
            var owner = this;
            owner.$userService.getInfo()
                .success(function (data) {
                    owner.$scope.personalInfo = data;
                });
        },

        _getAvatar: function () {
            var owner = this;
            owner.$userService.getUserInfo()
                .success(function (data) {
                    owner.$scope.avatar = data;
                });
        },

        _bindValidator: function () {
            console.log(1);
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
                        console.log(1);
                        owner._updateInfo();
                    }
                });
            };
        }
    });

    ProfileController.$inject = ['$scope', '$stateParams', 'userService','$uibModal','$state'];

    angular.module('module.my.ProfileController', ['service.UserService']).controller('ProfileController', ProfileController);

});