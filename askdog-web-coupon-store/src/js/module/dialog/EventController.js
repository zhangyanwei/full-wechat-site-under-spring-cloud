define([
    'base/BaseController',
    'jquery.validator',
    'vod-sdk',
    'service/StorageService',
    'service/EventService',
    'service/CouponService'
], function (BaseController) {

    var EventController = BaseController.extend({

        init: function ($rootScope, $scope, $state, $stateParams, $storageService, $eventService, $couponService) {
            this.$rootScope = $rootScope;
            this.$state = $state;
            this.$stateParams = $stateParams;
            this.$storageService = $storageService;
            this.$eventService = $eventService;
            this.$couponService = $couponService;
            this.$uibModalInstance = $scope.$parent.$uibModalInstance;
            this.$storeId = $scope.$parent.storeId;
            this.$event = $scope.$parent.event;
            this._super($scope);
        },

        defineScope: function() {

            var owner = this;

            this.$event && this._copyEvent(this.$event);

            this.$scope.cancel = function() {
                this.$uibModalInstance.dismiss("cancel");
            }.bind(this);

            this.$scope.bindValidator = function (element) {
                $(element).validate({
                    rules: {
                        name: {
                            required: true,
                            maxlength: 15
                        },
                        content: {
                            required: false
                        },
                        description: {
                            required: true
                        },
                        period: {
                            required: true
                        },
                        poster: {
                            required: !owner.$event
                        },
                        video: {
                            required: !owner.$event
                        }
                    },
                    messages: {
                        name: {
                            required: '请填写活动名',
                            maxlength: '活动名太长了，请取短一点儿的名字'
                        },
                        content: {
                            required: '请填写活动内容，例如优惠套餐等'
                        },
                        description: {
                            required: '请填写活动说明，包含活动规则，参与条件等'
                        },
                        period: {
                            required: '请填写活动时间'
                        },
                        poster: {
                            required: '请选择活动海报，将作为视频封面显示'
                        },
                        video: {
                            required: '请选择活动视频，目前要求所有的活动都配上视频说明'
                        }
                    },
                    submitHandler: function () {
                        !!owner.$event ? owner._modifyEvent() : owner._createEvent();
                    }
                });
            };

            this.$scope.selectPoster = function(file) {
                if (file) {
                    var extension = file.name.split('.').pop().toLowerCase();
                    owner.$scope.posterUploadStatus = 'uploading';
                    owner.$storageService.getToken('EVENT_POSTER', extension).then(
                        function (resp) {
                            owner.$storageService.upload(resp.data, file).then(
                                function (resp) {
                                    owner.$scope.event || (owner.$scope.event = {});
                                    owner.$scope.event.poster = resp.data.linkId;
                                    owner.$scope.posterUploadStatus = 'succeed';
                                },
                                function() {
                                    owner.$scope.posterUploadStatus = 'failed';
                                }
                            );
                        }
                    );
                }
            };

            this.$scope.selectVideo = function(file) {
                if (file) {
                    var extension = file.name.split('.').pop().toLowerCase();
                    owner.$scope.validationMessage = null;
                    if (extension != "mov" && extension != "mp4" && extension != "avi") {
                        owner.$scope.validationMessage = "视频文件格式不支持，支持mp4、mov、avi格式";
                        return false;
                    }
                    if ((file.size / 1024 / 1024) > 1024) {
                        owner.$scope.validationMessage = "文件过大无法上传，最大支持1GB";
                        return false;
                    }

                    owner.$scope.videoUploadStatus = 'uploading';
                    owner.$storageService.getToken('EVENT_VIDEO', extension).then(
                        function (resp) {
                            var token = resp.data;
                            owner.$scope.event || (owner.$scope.event = {});
                            owner.$scope.event.video = token.resource_id;
                            //file upload
                            var uploader = new VODUpload({
                                'onUploadFailed': function (fileName, code, message) {
                                    owner.$scope.videoUploadStatus = 'failed';
                                    owner.$scope.$digest();
                                },
                                'onUploadSucceed': function (fileName) {
                                    owner.$scope.videoUploadStatus = 'succeed';
                                    owner.$scope.$digest();
                                },
                                'onUploadProgress': function (fileName, totalSize, uploadedSize) {
                                    owner.$scope.videoProgress = Math.ceil(uploadedSize * 100 / totalSize) + "%";
                                    owner.$scope.$digest();
                                }
                            });
                            uploader.init(token.OSSAccessKeyId, token.secret_key);
                            uploader.addFile(file, token.endpoint, token.bucket, token.key);
                            uploader.startUpload();
                        }
                    );
                }
            };

            this._loadCouponData();
        },

        _createEvent: function() {
            this.$scope.event.store = this.$storeId;
            this.$scope.event.coupons = this.$scope.coupon ? [this.$scope.coupon] : [];
            this.$eventService.createEvent(this.$scope.event).then(
                function(resp) {
                    this.$uibModalInstance.close();
                }.bind(this),
                function(resp) {
                    this.$scope.errorMessage = resp.data.message || '服务器正忙，请稍后再试';
                }.bind(this)
            );
        },

        _modifyEvent: function() {
            this.$scope.event.coupons = this.$scope.coupon ? [this.$scope.coupon] : [];
            this.$eventService.modifyEvent(this.$scope.event).then(
                function(resp) {
                    this.$uibModalInstance.close();
                }.bind(this),
                function(resp) {
                    this.$scope.errorMessage = resp.data.message || '服务器正忙，请稍后再试';
                }.bind(this)
            );
        },

        _loadCouponData: function () {
            var owner = this;
            this.$couponService.storeOwnedCoupons(this.$stateParams.storeId).then(
                function (resp) {
                    owner.$scope.coupons = [];
                    for (var index = 0; index < resp.data.result.length; index++) {
                        var item = resp.data.result[index];
                        if (item.type == 'NORMAL') {
                            owner.$scope.coupons.push(item);
                        }
                    }
                }
            )
        },

        _copyEvent: function(event) {
            var coupons = event.coupons.slice().map(function(item) {
                return item.id;
            });

            this.$scope.event = {
                id: event.id,
                name: event.name,
                content: event.content,
                period: event.period,
                description: event.description,
                coupons: coupons
            };

            this.$scope.coupon = coupons.length > 0 ? coupons[0] : undefined;
        }

    });

    EventController.$inject = ['$rootScope', '$scope', '$state', '$stateParams', 'StorageService', 'EventService', 'CouponService'];

    angular.module('module.EventController', ['service.StorageService', 'service.EventService', 'service.CouponService']).controller('EventController', EventController);

});
