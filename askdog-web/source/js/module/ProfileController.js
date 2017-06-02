define([
    'base/BaseController',
    'jquery.validator',
    'app/directive/adAnalytics',
    'service/UserService',
    'service/StorageService'
], function (BaseController) {

    var ProfileController = BaseController.extend({

        _contextReadyListener: null,

        init: function (_rootScope, _scope, _stateParams, _uibModal, _userService, _storageService) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$uibModal = _uibModal;

            this.$userService = _userService;
            this.$storageService = _storageService;
            this._super(_scope);
        },

        /**
         * @Override
         */
        defineScope: function () {
            this._loadData();
            this._defineValidator();
            this._defineOpenImageCropDialog();
            this._defineAvatarHandler();
            this._defineEditInfo();
        },

        defineListeners: function () {
            var owner = this;
            this._contextReadyListener = this.$rootScope.$on('contextReady', function (event, userSelf) {
                if (userSelf && !owner.$scope.personalInfo) {
                    owner._loadData();
                }
            });
        },

        destroy: function () {
            this._contextReadyListener();
            this._contextReadyListener = null;
        },

        _loadData: function () {
            var owner = this;
            this.$userService.personalInfo().then(
                function (resp) {
                    owner.$scope.personalInfo = resp.data;
                }
            );
        },

        _defineOpenImageCropDialog: function () {
            var owner = this;
            this.$scope.openImageCropDialog = function () {
                var imageCropModal = owner.$uibModal.open({
                    windowTemplateUrl: 'views/base/modal-window.html',
                    windowTopClass: 'modal-default',
                    templateUrl: 'views/dialog/img-crop.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                        $scope.imageCropInfo = {
                            title: "修改头像",
                            fileSelectTitle: '选择头像',
                            areaType: 'circle'
                        }
                    }]
                });
                imageCropModal.result.then(
                    function (result) {
                        //ok callback;
                        owner.$scope.avatarLoad(result.extension, result.file);
                    },
                    function () {
                        //cancel callback;
                    }
                );
            }
        },

        _defineAvatarHandler: function () {
            var owner = this;
            this.$scope.avatarLoad = function (extension, file) {
                owner.$scope.uploadCompleted = false;
                owner.$scope.getAvatarToken(extension, file);
            };
            this.$scope.getAvatarToken = function (extension, file) {
                //Get token.
                owner.$storageService.getToken(AskDog.TokenUtil.type.USER_AVATAR, extension).then(
                    function (resp) {
                        owner.$scope.uploadAvatar(resp.data, file);
                    }
                );
            };
            this.$scope.uploadAvatar = function (token, file) {
                owner.$storageService.upload(token, file).then(
                    function (resp) {
                        if (resp && resp.status == 200) {
                            owner.$scope.avatarCache = {
                                linkId: resp.data.linkId,
                                avatar: resp.data.url
                            };
                            owner.$scope.saveAvatar(resp.data.linkId, resp.data.url)
                        }
                    }
                );
            };
            this.$scope.saveAvatar = function (linkId, avatar) {
                owner.$userService.saveAvatar(linkId).then(
                    function () {
                        owner.$scope.uploadCompleted = true;
                        if (owner.$scope.personalInfo) {
                            owner.$scope.personalInfo.avatar = avatar;
                        }
                        if (owner.$rootScope.userSelf) {
                            owner.$rootScope.userSelf.avatar = avatar;
                        }
                    },
                    function () {
                        owner.$rootScope.signIn(
                            function () {
                                owner.$scope.saveAvatar(owner.$scope.avatarCache.linkId, owner.$scope.avatarCache.avatar);
                            },
                            function () {
                                owner.$scope.uploadCompleted = true;
                            }
                        );
                    }
                );
            };
        },

        _defineValidator: function () {
            var owner = this;
            this.$scope.bindValidator = function (element) {
                $(element).validate({
                    rules: {
                        name: {
                            required: true,
                            username: true
                        },
                        phone: {
                            phone: true
                        }
                    },
                    messages: {
                        name: {
                            required: '请输入您的用户名',
                            username: '3-20位（不能含有特殊字符）'
                        },
                        phone: {
                            phone: '请输入正确的手机号码'
                        }
                    },
                    submitHandler: function () {
                        owner.$scope.saveInfo();
                    }
                });
            };

            this.$scope.bindSignatureValidator = function (element) {
                $(element).validate({
                    rules: {
                        signature: {
                            maxlength: 100
                        }
                    },
                    messages: {
                        signature: {
                            maxlength: '签名内容不能超过100字'
                        }
                    },
                    submitHandler: function () {
                        owner.$scope.saveSignature();
                    }
                });
            };
        },

        _defineEditInfo: function () {
            var owner = this;
            this.$scope.toggleInfo = function () {
                $(".info-panel").toggle();
                $(".info-form").toggle();
            };

            this.$scope.toggleSignature = function () {
                $(".signature-panel").toggle();
                $(".signature-form").toggle();
            };

            this.$scope.saveInfo = function () {
                owner.$userService.savePersonalInfo(owner.$scope.personalInfo).then(
                    function (resp) {
                        owner.$scope.toggleInfo();
                    }
                );
            };
            this.$scope.saveSignature = function () {
                owner.$userService.savePersonalInfo(owner.$scope.personalInfo).then(
                    function (resp) {
                        owner.$scope.toggleSignature();
                    }
                );
            };
        }

    });

    ProfileController.$inject = ['$rootScope', '$scope', '$stateParams', '$uibModal', 'UserService', 'StorageService'];

    angular.module('module.ProfileController', ['service.UserService', 'service.StorageService']).controller('ProfileController', ProfileController);

});