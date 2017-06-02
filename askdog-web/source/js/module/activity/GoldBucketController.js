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

    var GoldBucketController = BaseController.extend({

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
            owner.$scope.prevented = true;
            this.$userService.activityStatus().then(
                function (resp) {
                    if (resp.data === "NOT_REQUEST") {
                        owner.$scope.prevented = false;
                        owner._initData();
                    } else if (resp.data === "REVIEW_REJECTED") {
                        owner.$scope.info = "您的视频审核没有通过，您可以再次参与。";
                        owner.$scope.prevented = false;
                        owner._initData();
                    } else {
                        owner.$scope.prevented = true;
                        if (resp.data === "REVIEWING") {
                            owner.$scope.info = "您的视频正在审核中，请耐心等待我们的审核结果。";
                        }
                        if (resp.data === "REVIEW_PASSED") {
                            owner.$scope.info = "您的视频审核已经通过，继续分享更多经验吧。";
                        }
                    }
                }
            );
        },

        _initData: function () {
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
                owner.$scope.error = undefined;
                $('#video-file').trigger('click');
            };
            this.$scope.reChoiceVideo = function () {
                owner._uploader.stopUpload();
                $("#video-file").val("");
                owner.$scope.choiced = false;
                owner.$scope.uploadComplete = false;
                owner.$scope.error = undefined;
                $("#video-player").attr("src", "");
                $(".vedio-upload-info #percentage").text("0%");
                $(".vedio-pic-progress .progress-bar").css("width", "0%");
            };
            this.$scope.videoLoad = function () {
                var file = $("#video-file")[0].files[0];
                var extension = file.name.split('.').pop().toLowerCase();
                if (extension != "mov" && extension != "mp4" && extension != "avi") {
                    owner.$scope.error = "视频文件格式不支持，支持mp4、mov、avi格式";
                    owner.$scope.$digest();
                    return false;
                }
                if ((file.size / 1024 / 1024) > owner._FILE_LIMIT) {
                    owner.$scope.error = "文件过大无法上传，最大支持1GB";
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
            this._uploader = new VODUpload({
                'onUploadFailed': function (fileName, code, message) {
                    console.error("onUploadFailed: " + fileName + "," + code + "," + message);
                },
                'onUploadSucceed': function (fileName) {
                    owner.$scope.uploadComplete = true;
                    owner.$scope.$digest();
                    $("#video-player").attr("src", token.host + "/" + token.key);
                    $(".vedio-upload-info #percentage").text("100%");
                    $(".vedio-pic-progress .progress-bar").css("width", "100%");
                },
                'onUploadProgress': function (fileName, totalSize, uploadedSize) {
                    var percentage = Math.ceil(uploadedSize * 100 / totalSize) + "%";
                    $(".vedio-upload-info #percentage").text(percentage);
                    $(".vedio-pic-progress .progress-bar").css("width", percentage);
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
                        },
                        name: {
                            required: true,
                            username: true
                        },
                        phone: {
                            required: true,
                            phone: true
                        }
                    },
                    messages: {
                        subject: {
                            required: '请输入标题',
                            maxlength: '标题40字以内'
                        },
                        name: {
                            required: "请输入您的用户名",
                            username: '3-20位（不能含有特殊字符）'
                        },
                        phone: {
                            required: "请输入您的手机号",
                            phone: "请输入正确的手机号"
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
                        owner.$scope.shareExperienceVideo();
                    }
                });
            };
        },

        _defineShareExperienceVideo: function () {
            var owner = this;
            owner.$scope.pureExperience = {
                content: {}
            };
            this.$scope.shareExperienceVideo = function () {
                owner.$scope.prevented = true;
                var pureExperience = {
                    "subject": owner.$scope.pureExperience.subject,
                    "content": {
                        "type": "VIDEO",
                        "video_link_id": owner.$scope.pureExperience.content.video_link_id
                    },
                    "channel_id": owner.$scope.pureExperience.channel_id,
                    "category_id": owner.$scope.pureExperience.category_id,
                    "name": owner.$scope.pureExperience.name,
                    "phone": owner.$scope.pureExperience.phone,
                    "price": 0
                };
                owner.$userService.activityCreate(pureExperience).then(
                    function () {
                        owner.$scope.submitted = true;
                        owner._jumpIndex();
                    },
                    function (resp) {
                        if (resp && resp.status == 409) {
                            owner.$scope.error = "抱歉，您已经参加过本次活动";
                        }
                    }
                );
            };
        },

        _jumpIndex: function () {
            var owner = this;
            setTimeout(function () {
                owner.$state.go('layout.view-index.index');
            }, 2000);
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
                        owner._loadData();
                    },
                    function () {
                        //cancel callback;
                    }
                );
            };
        }
    });

    GoldBucketController.$inject = ['$rootScope', '$scope', '$stateParams', '$state', '$uibModal', 'UserService', 'CategoryService', 'ExperienceService', 'StorageService'];

    angular.module('module.GoldBucketController', ['service.UserService', 'service.CategoryService', 'service.ExperienceService', 'service.StorageService']).controller('GoldBucketController', GoldBucketController);

});