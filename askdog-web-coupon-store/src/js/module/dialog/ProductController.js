define([
    'base/BaseController',
    'jquery.validator',
    'vod-sdk',
    'service/StorageService',
    'service/CouponService',
    'service/ProductService'
], function (BaseController) {

    var ProductController = BaseController.extend({

        _PICTURE_LIMIT: 5,

        init: function ($rootScope, $scope, $state, $stateParams, $storageService, $couponService, $productService) {
            this.$rootScope = $rootScope;
            this.$state = $state;
            this.$stateParams = $stateParams;
            this.$uibModalInstance = $scope.$parent.$uibModalInstance;

            this.$amendProduct = $scope.$parent.amendProduct;

            this.$storageService = $storageService;
            this.$couponService = $couponService;
            this.$productService = $productService;

            this._super($scope);
        },

        defineScope: function () {
            var owner = this;
            this.pictures = [];
            if (this.$amendProduct) {
                var couponNormal, couponForward;
                for (var i = 0; i < this.$amendProduct.coupons.length; i++) {
                    if (this.$amendProduct.coupons[i].type == 'FORWARDED') {
                        couponForward = this.$amendProduct.coupons[i];
                    }
                    if (this.$amendProduct.coupons[i].type == 'NORMAL') {
                        couponNormal = this.$amendProduct.coupons[i];
                    }
                }
                owner.$scope.product = {
                    id: owner.$amendProduct.id,
                    name: owner.$amendProduct.name,
                    couponNormal: couponNormal,
                    couponForward: couponForward,
                    description: this.$amendProduct.description,
                    special: this.$amendProduct.tags[0] == 'SPECIAL' ? true : false
                };
            } else {
                this.$scope.product = {
                    special: true
                };
            }

            this._bindValidator();
            this._defineRefreshView();
            this._defineViewHandler();
        },


        _bindValidator: function () {
            var owner = this;
            this.$scope.bindValidator = function (element) {
                $(element).validate({
                    rules: {
                        productName: {
                            required: true,
                            maxlength: 20
                        },
                        // couponNormal: {
                        //     required: true
                        // },
                        // couponForward: {
                        //     required: true
                        // },
                        productVideo: {
                            required: false
                        },
                        description: {
                            required: true
                        }

                    },
                    messages: {
                        productName: {
                            required: '',
                            maxlength: '产品名称不能超过20个文字'
                        },
                        // couponNormal: {
                        //     required: ''
                        // },
                        // couponForward: {
                        //     required: ''
                        // },
                        productVideo: {
                            required: ''
                        },
                        description: {
                            required: ''
                        }
                    },
                    submitHandler: function () {
                        if (owner.$amendProduct) {
                            owner._modifyProduct();
                        } else {
                            owner._createProduct();
                        }
                    }
                });
            }
        },

        _defineRefreshView: function () {
            this._loadCouponData();
        },

        _loadCouponData: function () {
            var owner = this;
            this.$couponService.storeOwnedCoupons(this.$stateParams.storeId).then(
                function (resp) {
                    owner.$scope.couponNormalList = [];
                    owner.$scope.couponForwardList = [];
                    for (var index = 0; index < resp.data.result.length; index++) {
                        var item = resp.data.result[index];
                        if (item.type == 'NORMAL') {
                            owner.$scope.couponNormalList.push(item);
                        }
                        if (item.type == 'FORWARDED') {
                            owner.$scope.couponForwardList.push(item);
                        }
                    }
                }
            )
        },

        _defineViewHandler: function () {
            var owner = this;
            this.$scope.cancel = function () {
                owner.$uibModalInstance.dismiss("cancel");
            };

            this.$scope.videoLoad = function () {
                owner.$scope.validationMessage = undefined;
                var file = $("#video-file")[0].files[0];
                var extension = file.name.split('.').pop().toLowerCase();
                if (extension != "mov" && extension != "mp4" && extension != "avi") {
                    owner.$scope.validationMessage = "视频文件格式不支持，支持mp4、mov、avi格式";
                    owner.$scope.$digest();
                    return false;
                }
                if ((file.size / 1024 / 1024) > owner._FILE_LIMIT) {
                    owner.$scope.validationMessage = "文件过大无法上传，最大支持1GB";
                    owner.$scope.$digest();
                    return false;
                }
                //Get token.
                owner.$storageService.getToken('PRODUCT_VIDEO', extension).then(
                    function (resp) {
                        var token = resp.data;
                        owner.$scope.product.video_id = token.resource_id;
                        //file upload
                        owner._upload(token, file);
                    }
                );
            };
            this.$scope.imageLoad = function (index) {
                var file = $(".product-image")[index].files[0];
                if (file) {
                    var extension = file.name.split('.').pop().toLowerCase();
                    owner.$storageService.getToken('PRODUCT_COVER', extension).then(
                        function (resp) {
                            var token = resp.data;
                            owner.$scope._imageUpload(token, file, index);
                        }
                    );
                }
            };
            this.$scope._imageUpload = function (token, file, index) {
                owner.$storageService.upload(token, file).then(
                    function (resp) {
                        if (index == 0) {
                            owner.cover_id = resp.data.linkId;
                        } else {
                            var picture = {
                                "index": index,
                                "link_id": resp.data.linkId
                            };
                            var pictureModify = false;
                            for (var i = 0; i < owner.pictures.length; i++) {
                                if (owner.pictures[i].index == index) {
                                    owner.pictures[i] = picture;
                                    pictureModify = true;
                                    break;
                                }
                            }
                            if (!pictureModify) {
                                owner.pictures.push(picture);
                            }
                        }
                    }
                );
            };
        },

        _upload: function (token, file) {
            var owner = this;
            this.$scope.errorMessage = undefined;
            this.$scope.uploadComplete = false;
            this._uploader = new VODUpload({
                'onUploadFailed': function (fileName, code, message) {
                },
                'onUploadSucceed': function (fileName) {
                    owner.$scope.uploadComplete = true;
                    owner.$scope.$digest();
                    $("#video-progress .progress-bar").css("width", "100%");
                    $("#video-progress .progress-bar").text("100%");
                },
                'onUploadProgress': function (fileName, totalSize, uploadedSize) {
                    var percentage = Math.ceil(uploadedSize * 100 / totalSize) + "%";
                    $("#video-progress .progress-bar").css("width", percentage);
                    $("#video-progress .progress-bar").text(percentage);
                }
            });
            this._uploader.init(token.OSSAccessKeyId, token.secret_key);
            this._uploader.addFile(file, token.endpoint, token.bucket, token.key);
            this._uploader.startUpload();
        },

        _createProduct: function () {
            var owner = this;
            if (this.$scope.product.special) {
                if (owner.$scope.product.video_id == undefined) {
                    owner.$scope.errorMessage = '请上传视频';
                    owner.$scope.$digest();
                    return;
                }
            }
            for (var i = 0; i < owner.pictures.length; i++) {
                var pictureIndex = owner.pictures[i].index;
                if (owner.$scope.product.pictures && owner.$scope.product.pictures['name' + pictureIndex]) {
                    owner.pictures[i].name = owner.$scope.product.pictures['name' + pictureIndex];
                } else {
                    owner.pictures[i].name = '';
                }
            }

            var coupons = [];
            if (this.$scope.product.couponNormal && this.$scope.product.couponNormal.id) {
                coupons.push(this.$scope.product.couponNormal.id);
            }
            if (this.$scope.product.couponForward && this.$scope.product.couponForward.id) {
                coupons.push(this.$scope.product.couponForward.id);
            }
            var pureProduct = {
                name: this.$scope.product.name,
                description: this.$scope.product.description,
                video_id: this.$scope.product.video_id,
                cover_id: owner.cover_id,
                store_id: this.$stateParams.storeId,
                coupons: coupons,
                pictures: owner.pictures,
                tags: this.$scope.product.special ? ['SPECIAL'] : null
            };
            this.$productService.createProduct(pureProduct).then(
                function (resp) {
                    owner.$uibModalInstance.close();
                }
            );
        },

        _modifyProduct: function () {
            var owner = this;
            for (var i = 0; i < owner.pictures.length; i++) {
                var pictureIndex = owner.pictures[i].index;
                if (owner.$scope.product.pictures && owner.$scope.product.pictures['name' + pictureIndex]) {
                    owner.pictures[i].name = owner.$scope.product.pictures['name' + pictureIndex];
                } else {
                    owner.pictures[i].name = '';
                }
            }

            var coupons = [];
            if (this.$scope.product.couponNormal && this.$scope.product.couponNormal.id) {
                coupons.push(this.$scope.product.couponNormal.id);
            }
            if (this.$scope.product.couponForward && this.$scope.product.couponForward.id) {
                coupons.push(this.$scope.product.couponForward.id);
            }
            var amendProduct = {
                name: this.$scope.product.name,
                description: this.$scope.product.description,
                video_id: this.$scope.product.video_id,
                cover_id: owner.cover_id,
                store_id: this.$stateParams.storeId,
                coupons: coupons,
                pictures: owner.pictures.length == 0 ? null: owner.pictures,
                tags: this.$scope.product.special ? ['SPECIAL'] : null
            };
            this.$productService.updateProduct(owner.$scope.product.id, amendProduct).then(
                function (resp) {
                    owner.$uibModalInstance.close();
                }
            );
        }

    });

    ProductController.$inject = ['$rootScope', '$scope', '$state', '$stateParams', 'StorageService', 'CouponService', 'ProductService'];

    angular.module('module.ProductController', ['service.StorageService', 'service.CouponService', 'service.ProductService']).controller('ProductController', ProductController);

});
