define([
    'base/BaseController',
    'jquery.validator',
    'app/directive/adAnalytics',
    'editor-md',
    'service/UserService',
    'service/CategoryService',
    'service/ExperienceService',
    'service/StorageService'
], function (BaseController) {

    var WordController = BaseController.extend({

        _contextReadyListener: null,
        _EDITOR_ID: 'word-textarea',
        _EDITOR: null,

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
            var owner = this;
            var interval = setInterval(function () {
                if (owner._initializeEditor()) {
                    clearInterval(interval);
                }
            }, 50);
        },

        _initializeEditor: function () {
            var owner = this;
            if (this.$stateParams.expId && !this.$scope.amendExperience) {
                return false;
            }
            if ($("#" + this._EDITOR_ID)) {
                this._EDITOR = editormd(this._EDITOR_ID, {
                    width: "100%",
                    height: 640,
                    placeholder: "这里可以编辑您的图文经验",
                    syncScrolling: "single", //|false
                    path: "lib/editormd/lib/",

                    //htmlDecode: "style,script,iframe|on*",        // 开启 HTML 标签解析，为了安全性，默认不开启
                    codeFold: true,                               // 开启代码折叠功能
                    emoji: true,
                    taskList: true,
                    tocm: true,                                   // Using [TOCM]
                    //TODO resolve the conflicts
                    //tex: true,                                  // 开启科学公式TeX语言支持，默认关闭
                    //flowChart: true,                            // 开启流程图支持，默认关闭
                    //sequenceDiagram: true,                      // 开启时序/序列图支持，默认关闭,
                    imageUpload: true,
                    imageFormats: ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
                    markdown: owner.$scope.amendExperience ? owner.$scope.amendExperience.content.content : "",
                    imageUploadPointcut: function (file, callback) {
                        owner._imageUploadPointcut(file, callback);
                    },
                    onload: function () {
                    }
                });
                return true;
            }
            return false;
        },

        _imageUploadPointcut: function (file, callback) {
            var owner = this;
            var extension = file.name.split('.').pop().toLowerCase();
            this.$storageService.getToken(AskDog.TokenUtil.type.EXPERIENCE_PICTURE, extension).then(
                function (resp) {
                    owner.$storageService.upload(resp.data, file).then(
                        function (resp) {
                            if (resp && resp.status == 200) {
                                if (callback) {
                                    //image link id
                                    owner.$scope.picture_link_ids = owner.$scope.picture_link_ids || [];
                                    if (resp.data.linkId) {
                                        owner.$scope.picture_link_ids.push(resp.data.linkId);
                                    }
                                    //image link url
                                    callback(resp.data.url);
                                }
                            }
                        }
                    );
                }
            );
        },

        defineScope: function () {
            this._loadData();
            this._defineChannelSelect();
            this._defineCategorySelect();
            this._defineValidator();
            this._defineShareExperienceWord();
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
            this.$scope.pureExperience = {};
            this.$scope.notFound = true;
            if (!this.$stateParams.expId) {
                this.$scope.notFound = false;
                owner._loadOptionsData();
            } else {
                this.$experienceService.detail(this.$stateParams.expId).then(
                    function (resp) {
                        owner.$scope.notFound = false;
                        owner.$scope.amendExperience = resp.data;
                        owner.$scope.pureExperience = owner.$scope.pureExperience || {};
                        owner.$scope.pureExperience.subject = owner.$scope.amendExperience.subject;
                        owner.$scope.pureExperience.channel_id = owner.$scope.amendExperience.channel.id;
                        owner.$scope.pureExperience.channel_name = owner.$scope.amendExperience.channel.name;
                        owner.$scope.pureExperience.category_id = owner.$scope.amendExperience.category.id;
                        owner.$scope.pureExperience.category_name = owner.$scope.amendExperience.category.name;
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
            // Owned channels data
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
                        owner.$scope.shareExperienceWord();
                    }
                });
            };
        },

        _defineShareExperienceWord: function () {
            var owner = this;
            this.$scope.shareExperienceWord = function () {

                if (owner._EDITOR.getMarkdown() == "") {
                    owner.$scope.error = "请填写内容";
                    return false;
                }

                if (!owner.$scope.pureExperience.channel_id) {
                    owner.$scope.error = "请选择频道";
                    return false;
                }
                if (!owner.$scope.pureExperience.category_id) {
                    owner.$scope.error = "请选择分类";
                    return false;
                }

                owner.$scope.prevented = true;
                var pureExperience = {
                    "subject": owner.$scope.pureExperience.subject,
                    "content": {
                        "type": "TEXT",
                        "content": owner._EDITOR.getMarkdown(),
                        "picture_link_ids": owner.$scope.picture_link_ids || []
                    },
                    "channel_id": owner.$scope.pureExperience.channel_id,
                    "category_id": owner.$scope.pureExperience.category_id,
                    "price": 0
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
                    console.log("create", pureExperience);
                    owner.$experienceService.create(pureExperience).then(
                        function (resp) {
                            owner.$state.go('layout.view-default.exp', {experienceId: resp.data.id});
                        }
                    );
                }
            }

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

    WordController.$inject = ['$rootScope', '$scope', '$stateParams', '$state', '$uibModal', 'UserService', 'CategoryService', 'ExperienceService', 'StorageService'];

    angular.module('module.WordController', ['service.UserService', 'service.CategoryService', 'service.ExperienceService', 'service.StorageService']).controller('WordController', WordController);

});