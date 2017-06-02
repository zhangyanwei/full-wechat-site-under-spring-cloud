define(['base/BaseController', 'jquery.validator', 'service/UserService'], function (BaseController) {

    var LoginController = BaseController.extend({

        init: function ($rootScope, $scope, $stateParams, $state, $location, $uibModal, $userService) {
            this.$rootScope = $rootScope;
            this.$stateParams = $stateParams;
            this.$state = $state;
            this.$location = $location;
            this.$uibModal = $uibModal;

            this.$uibModalInstance = $scope.$parent.$uibModalInstance;
            this.$userService = $userService;
            this._super($scope);
        },

        defineScope: function () {
            this._defineValidator();
            this._defineLogin();
            this._defineCancel();
            this._defineGoSignUp();
            this._defineOpenResetPwdDialog();
        },

        _defineValidator: function () {
            var owner = this;
            this.$scope.bindValidator = function (element) {
                $(element).validate({
                    rules: {
                        username: {
                            required: true,
                            emailOrPhone: true
                        },
                        password: {
                            required: true,
                            password: true
                        }
                    },
                    messages: {
                        username: {
                            required: "请输入您的手机/邮件地址",
                            emailOrPhone: "请输入正确的手机/邮件地址"
                        },
                        password: {
                            required: "请输入您的新密码",
                            password: "您的密码长度不正确(6到20个字符)"
                        }
                    },
                    submitHandler: function () {
                        owner.$scope.login();
                    }
                });
            };
        },

        _defineLogin: function () {
            var owner = this;
            this.$scope.qqLoginLink = "/login/qq?request=" + encodeURIComponent(window.location.href);
            this.$scope.weiboLoginLink = "/login/weibo?request=" + encodeURIComponent(window.location.href);
            this.$scope.wechatLoginLink = "/login/wechat/connect?request=" + encodeURIComponent(window.location.href);

            this.$scope.login = function () {
                owner.$userService.login(owner.$scope.userInfo).then(
                    function (resp) {
                        owner.$uibModalInstance.close();
                        owner.$rootScope.$broadcast('contextChange', resp.data);
                        if (owner.$location.$$path.startWith('/reg/')) {
                            owner.$state.go('layout.view-index.index');
                        }
                        var userSelf = resp.data;
                        if (userSelf && userSelf.category_codes == undefined) {
                            // User first login
                            var interestModel = owner.$uibModal.open({
                                windowTemplateUrl: 'views/base/modal-window.html',
                                windowTopClass: 'select-book-modal',
                                templateUrl: 'views/dialog/interest-select.html',
                                size: 'lg',
                                controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                                    $scope.$uibModalInstance = $uibModalInstance;
                                }]
                            });
                            interestModel.result.then(
                                function () {
                                    //ok callback;
                                    owner.$rootScope.$broadcast('sidebarRefresh');
                                },
                                function () {
                                    //cancel callback;
                                    owner.$userService.subscribeCategory({"category_codes": []}).then(
                                        function () {
                                            owner.$rootScope.$broadcast('sidebarRefresh');
                                        }
                                    );
                                }
                            );
                        }
                    },
                    function (resp) {
                        owner.$scope.error = resp.data ? resp.data.message : '服务器正忙，请稍后再试';
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

        _defineGoSignUp: function () {
            var owner = this;
            this.$scope.goSignUp = function () {
                owner.$uibModalInstance.dismiss("sign-up");
                owner.$rootScope.signUp()
            }
        },

        _defineOpenResetPwdDialog: function () {
            var owner = this;
            this.$scope.openResetPwd = function () {
                owner.$uibModalInstance.dismiss("cancel");
                owner.$uibModal.open({
                    windowTemplateUrl: 'views/base/modal-window.html',
                    windowTopClass: 'lr-Modal',
                    templateUrl: 'views/dialog/reset.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                    }]
                });
            }
        }
    });

    LoginController.$inject = ['$rootScope', '$scope', '$stateParams', '$state', '$location', '$uibModal', 'UserService'];
    angular.module('module.dialog.LoginController', ['service.UserService']).controller('LoginController', LoginController);

});