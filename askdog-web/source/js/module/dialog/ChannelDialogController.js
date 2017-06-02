define(['base/BaseController', 'jquery.validator', 'service/ChannelService', 'service/StorageService'], function (BaseController) {

    var ChannelDialogController = BaseController.extend({

        init: function (_rootScope, _scope, _stateParams, _uibModal, _channelService, _storageService) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$uibModal = _uibModal;

            this.$uibModalInstance = _scope.$parent.$uibModalInstance;
            this.$amendChannel = _scope.amendChannel;
            this.$thumbnail = _scope.amendChannel;

            this.$channelService = _channelService;
            this.$storageService = _storageService;
            this._super(_scope);
        },

        defineScope: function () {
            this._defineOpenImageCropDialog();
            this._defineSelectFile();
            this._defineValidator();
            this._defineCreateChannel();
            this._defineUpdateChannel();
            this._defineCancel();
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
                            title: "上传图标",
                            fileSelectTitle: '选择图片',
                            areaType: 'square'
                        }
                    }]
                });
                imageCropModal.opened.then(
                    function () {
                        $(".modal:last").addClass("hidden");
                        $(".modal-backdrop").addClass("hidden");
                        $(".modal-backdrop").after('<div class="modal-backdrop custom-backdrop fade in" ></div>');
                    }
                );
                imageCropModal.closed.then(
                    function () {
                        $(".modal").removeClass("hidden");
                        $(".modal-backdrop").removeClass("hidden");
                        $(".modal-backdrop.custom-backdrop").remove();
                    }
                );
                imageCropModal.result.then(
                    function (result) {
                        //ok callback;
                        console.log(result);
                        owner.$scope.thumbnailLoad(result.extension, result.file);
                    },
                    function () {
                        //cancel callback;
                    }
                );
            }
        },


        _defineSelectFile: function () {
            var owner = this;
            this.$scope.thumbnailLoad = function (extension, file) {
                owner.$scope.uploadCompleted = false;
                //Get token.
                owner.$storageService.getToken(AskDog.TokenUtil.type.CHANNEL_THUMBNAIL, extension).then(
                    function (resp) {
                        owner.$storageService.upload(resp.data, file).then(
                            function (resp) {
                                if (resp && resp.status == 200) {
                                    owner.$scope.cover_image_link_id = resp.data.linkId;
                                    owner.$scope.thumbnail = resp.data.url;
                                    owner.$scope.uploadCompleted = true;
                                }
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
                        subject: {
                            required: true,
                            maxlength: 14
                        },
                        description: {
                            required: false,
                            maxlength: 100
                        }
                    },
                    messages: {
                        subject: {
                            required: "请输入标题",
                            maxlength: "标题不能超过14个字"
                        },
                        description: {
                            maxlength: "描述不能超过100个字"
                        }
                    },
                    submitHandler: function () {
                        if (owner.$amendChannel) {
                            owner.$scope.updateChannel();
                        } else {
                            owner.$scope.createChannel();
                        }
                    }
                });
            };
        },

        _defineCreateChannel: function () {
            var owner = this;
            this.$scope.createChannel = function () {
                owner.$scope.prevented = true;
                owner.$scope.$digest();
                var pureChannel = {
                    "name": owner.$scope.pureChannel.name,
                    "description": owner.$scope.pureChannel.description,
                    "cover_image_link_id": owner.$scope.cover_image_link_id
                };
                owner.$channelService.create(pureChannel).then(
                    function () {
                        owner.$uibModalInstance.close();
                    }
                );
            }
        },

        _defineUpdateChannel: function () {
            var owner = this;
            this.$scope.updateChannel = function () {
                owner.$scope.prevented = true;
                owner.$scope.$digest();
                var amendChannel = {
                    "name": owner.$scope.amendChannel.name,
                    "description": owner.$scope.amendChannel.description
                };
                if (owner.$scope.cover_image_link_id) {
                    amendChannel.cover_image_link_id = owner.$scope.cover_image_link_id;
                }
                owner.$channelService.update(owner.$scope.amendChannel.id, amendChannel).then(
                    function () {
                        owner.$uibModalInstance.close();
                    }
                );
            }
        },

        _defineCancel: function () {
            var owner = this;
            this.$scope.cancel = function () {
                owner.$uibModalInstance.dismiss("cancel");
            };
        }

    });

    ChannelDialogController.$inject = ['$rootScope', '$scope', '$stateParams', '$uibModal', 'ChannelService', 'StorageService'];

    angular.module('module.dialog.ChannelDialogController', ['service.ChannelService', 'service.StorageService']).controller('ChannelDialogController', ChannelDialogController);

});