define([
    'base/BaseController',
    'jquery.validator',
    'app/directive/adAnalytics',
    'vod-sdk',
    'service/UserService',
    'service/CategoryService',
    'service/ExperienceService',
    'service/StorageService'
], function (BaseController) {

    var VideoController = BaseController.extend({

        _contextReadyListener: null,
        _uploader: null,
        _FILE_LIMIT: 1024, // file limit size 1GB

        init: function (_rootScope, _scope, _stateParams, _state, _uibModal, _userService, _categoryService, _experienceService, _storageService) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$state = _state;
            this.$uibModal = _uibModal;

            this.$userService = _userService;
            this.$categoryService = _categoryService;
            this.$experienceService = _experienceService;
            this.$storageService = _storageService;
            this._super(_scope);
        },

        /**
         * @Override
         */
        defineScope: function () {
            this._loadData();
            this._defineChannelSelect();
            this._defineCategorySelect();
            this._defineChoiceVideo();
            this._defineValidator();
            this._defineShareExperienceVideo();
            this._defineCreateChannel();
        },

        defineListeners: function () {
            var owner = this;
            this._contextReadyListener = this.$rootScope.$on('contextReady', function (event, userSelf) {
                if (userSelf) {
                    owner._loadOptionsData();
                }
            });
        },

        destroy: function () {
            this._contextReadyListener();
            this._contextReadyListener = null;
        },

        _loadData: function () {
            var owner = this;
            this.$scope.pureExperience = {
                content: {
                    price: "0"
                },
                activity: this.$stateParams.activity == 'MOON_FESTIVAL' ? 'MOON_FESTIVAL' : undefined
            };
            this.$scope.notFound = false;
            if (!this.$stateParams.expId) {
                this.$scope.notFound = false;
                owner._loadOptionsData();
            } else {
                this.$experienceService.detail(this.$stateParams.expId).then(
                    function (resp) {
                        owner.$scope.notFound = false;
                        owner.$scope.choiced = true;
                        owner.$scope.uploadComplete = true;
                        owner.$scope.amendExperience = resp.data;
                        owner.$scope.pureExperience.subject = owner.$scope.amendExperience.subject;
                        owner.$scope.pureExperience.channel_id = owner.$scope.amendExperience.channel.id;
                        owner.$scope.pureExperience.channel_name = owner.$scope.amendExperience.channel.name;
                        owner.$scope.pureExperience.category_id = owner.$scope.amendExperience.category.id;
                        owner.$scope.pureExperience.category_name = owner.$scope.amendExperience.category.name;
                        owner.$scope.pureExperience.content.price = "" + (owner.$scope.amendExperience.price / 100);
                        owner.$scope.pureExperience.content.video_link_id = owner.$scope.amendExperience.content.link_id;
                        owner._loadOptionsData();
                    },
                    function () {
                        owner.$scope.notFound = true;
                    }
                );
            }
        },

        _loadOptionsData: function () {
            var owner = this;
            this.$userService.ownedChannels(0, 100).then(
                function (resp) {
                    owner.$scope.ownedChannels = resp.data.result;
                }
            );
            // System categories data
            this.$categoryService.categoriesNested().then(
                function (resp) {
                    owner.$scope.sysCategories = resp.data;
                }
            );
        },

        _defineChannelSelect: function () {
            var owner = this;
            this.$scope.channelSelect = function (channel) {
                owner.$scope.pureExperience = owner.$scope.pureExperience || {};
                owner.$scope.pureExperience.channel_id = channel.id;
                owner.$scope.pureExperience.channel_name = channel.name;
                owner.$scope.error = undefined;
            }
        },

        _defineCategorySelect: function () {
            var owner = this;

            this.$scope.categoryOver = function (category, $event) {
                for (var index = 0; index < owner.$scope.sysCategories.length; index++) {
                    if (category.id == owner.$scope.sysCategories[index].id) {
                        owner.$scope.sysCategories[index].displayChildren = true;
                    } else {
                        owner.$scope.sysCategories[index].displayChildren = false;
                    }
                }
            };

            this.$scope.categorySelect = function (category, $event) {
                if (category.children.length == 0) {
                    $($event.target).closest(".btn-group").removeClass("open");
                    owner.$scope.pureExperience = owner.$scope.pureExperience || {};
                    owner.$scope.pureExperience.category_id = category.id;
                    owner.$scope.pureExperience.category_name = category.name;
                }
                owner.$scope.error = undefined;
            };
        },

        _defineChoiceVideo: function () {
            var owner = this;
            this.$scope.choiceVideo = function () {
                owner.$scope.fileError = undefined;
                $('#video-file').trigger('click');
            };
            this.$scope.reChoiceVideo = function () {
                if (owner._uploader) {
                    owner._uploader.stopUpload();
                }
                $("#video-file").val("");
                owner.$scope.choiced = false;
                owner.$scope.uploadComplete = false;
                $("#video-player").attr("src", "");
                $(".vedio-upload-info #percentage").text("上传 0%");
                $(".vedio-pic-progress .progress-bar").css("width", "0%");
            };
            this.$scope.videoLoad = function () {
                var file = $("#video-file")[0].files[0];
                var extension = file.name.split('.').pop().toLowerCase();
                if (extension != "mov" && extension != "mp4" && extension != "avi") {
                    owner.$scope.fileError = "视频文件格式不支持，支持mp4、mov、avi格式";
                    owner.$scope.$digest();
                    return false;
                }
                if ((file.size / 1024 / 1024) > owner._FILE_LIMIT) {
                    owner.$scope.fileError = "文件过大无法上传，最大支持1GB";
                    owner.$scope.$digest();
                    return false;
                }
                //Get token.
                owner.$storageService.getToken(AskDog.TokenUtil.type.EXPERIENCE_VIDEO, extension).then(
                    function (resp) {
                        var token = resp.data;
                        owner.$scope.choiced = true;
                        owner.$scope.pureExperience.content.video_link_id = token.link_id;
                        //file upload
                        owner._upload(token, file);
                    }
                );
            };
        },

        _upload: function (token, file) {
            var owner = this;
            var failedFile = undefined;
            this._uploader = new VODUpload({
                'onUploadFailed': function (fileName, code, message) {
                    failedFile = fileName;
                },
                'onUploadSucceed': function (fileName) {
                    owner.$scope.uploadComplete = true;
                    owner.$scope.$digest();
                    $("#video-player").attr("src", token.host + "/" + token.key);
                    $(".vedio-upload-info #percentage").text("上传 100%");
                    $(".vedio-pic-progress .progress-bar").css("width", "100%");
                },
                'onUploadProgress': function (fileName, totalSize, uploadedSize) {
                    var percentage = Math.ceil(uploadedSize * 100 / totalSize) + "%";
                    if (failedFile != fileName) {
                        $(".vedio-upload-info #percentage").text("上传 " + percentage);
                        $(".vedio-pic-progress .progress-bar").css("width", percentage);
                    }
                }
            });
            this._uploader.init(token.OSSAccessKeyId, token.secret_key);
            this._uploader.addFile(file, token.endpoint, token.bucket, token.key);
            this._uploader.startUpload();
        },

        _defineValidator: function () {
            var owner = this;
            this.$scope.bindValidator = function (element) {
                $(element).validate({
                    rules: {
                        subject: {
                            required: true,
                            maxlength: 40
                        }
                    },
                    messages: {
                        subject: {
                            required: '请输入标题',
                            maxlength: '标题40字以内'
                        }
                    },
                    submitHandler: function () {
                        if (!owner.$scope.pureExperience.channel_id) {
                            owner.$scope.error = "请选择频道";
                            return false;
                        }
                        if (!owner.$scope.pureExperience.category_id) {
                            owner.$scope.error = "请选择分类";
                            return false;
                        }
                        if (!owner.$scope.pureExperience.content.price) {
                            owner.$scope.error = "请选择价格";
                            return false;
                        }
                        var price = Number(owner.$scope.pureExperience.content.price);
                        if (isNaN(price)) {
                            owner.$scope.error = "请输入正确价格";
                            return false;
                        }
                        if (price == 0) {
                            owner.$scope.shareExperienceVideo();
                        } else {
                            var confirmModel = owner.$uibModal.open({
                                windowTemplateUrl: 'views/base/modal-window.html',
                                windowTopClass: 'confirm-modal',
                                templateUrl: 'views/dialog/confirm.html',
                                controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                                    $scope.$uibModalInstance = $uibModalInstance;
                                    $scope.confirmInfo = {
                                        title: "提醒",
                                        message: "您设置的视频经验价格为1元，请确认您是视频的原创者"
                                    }
                                }]
                            });
                            confirmModel.result.then(
                                function () {
                                    owner.$scope.shareExperienceVideo();
                                },
                                function () {
                                }
                            );
                        }
                    }
                });
            };
        },

        _defineShareExperienceVideo: function () {
            var owner = this;
            this.$scope.shareExperienceVideo = function () {
                owner.$scope.prevented = true;
                var price = Number(owner.$scope.pureExperience.content.price);
                var pureExperience = {
                    "subject": owner.$scope.pureExperience.subject,
                    "content": {
                        "type": "VIDEO",
                        "video_link_id": owner.$scope.pureExperience.content.video_link_id
                    },
                    "channel_id": owner.$scope.pureExperience.channel_id,
                    "category_id": owner.$scope.pureExperience.category_id,
                    "price": price * 100,
                    "activity": owner.$scope.pureExperience.activity
                };
                if (owner.$stateParams.expId) {
                    var amendExperience = pureExperience;
                    amendExperience.id = owner.$stateParams.expId;
                    owner.$experienceService.update(amendExperience.id, amendExperience).then(
                        function (resp) {
                            owner.$state.go('layout.view-default.exp', {experienceId: resp.data.id});
                        }
                    );
                } else {
                    owner.$experienceService.create(pureExperience).then(
                        function (resp) {
                            owner.$state.go('layout.view-default.exp', {experienceId: resp.data.id});
                        }
                    );
                }
            };
        },

        _defineCreateChannel: function () {
            var owner = this;
            this.$scope.showCreateChannel = function () {
                var createChannelModel = owner.$uibModal.open({
                    windowTemplateUrl: 'views/base/modal-window.html',
                    windowTopClass: 'new-channel-modal',
                    templateUrl: 'views/dialog/channel-create.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                    }]
                });
                createChannelModel.result.then(
                    function () {
                        //ok callback;
                        owner._loadOptionsData();
                    },
                    function () {
                        //cancel callback;
                    }
                );
            };
        }
    });

    VideoController.$inject = ['$rootScope', '$scope', '$stateParams', '$state', '$uibModal', 'UserService', 'CategoryService', 'ExperienceService', 'StorageService'];

    angular.module('module.VideoController', ['service.UserService', 'service.CategoryService', 'service.ExperienceService', 'service.StorageService']).controller('VideoController', VideoController);

});