define(['base/BaseController', 'angular-imgcrop'], function (BaseController) {

    var ImgCropController = BaseController.extend({

        init: function (_rootScope, _scope, _stateParams) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$uibModalInstance = _scope.$parent.$uibModalInstance;
            this._super(_scope);
        },

        defineScope: function () {
            this._defineSave();
            this._defineCancel();
            this._defineImageHandler();
        },

        _defineSave: function () {
            var owner = this;
            this.$scope.save = function () {
                var result = {
                    extension: owner.$scope.orginFileExtension,
                    file: owner._convertBase64UrlToBlob(owner.$scope.cropedImage)
                };
                owner.$uibModalInstance.close(result);
            };
        },

        _convertBase64UrlToBlob: function (imgData) {
            var bytes = window.atob(imgData.split(',')[1]);
            //处理异常,将ascii码小于0的转换为大于0
            var ab = new ArrayBuffer(bytes.length);
            var ia = new Uint8Array(ab);
            for (var i = 0; i < bytes.length; i++) {
                ia[i] = bytes.charCodeAt(i);
            }
            return new Blob([ab], {type: 'image/png'});
        },

        _defineCancel: function () {
            var owner = this;
            this.$scope.cancel = function () {
                owner.$uibModalInstance.dismiss("cancel");
            };
        },

        _defineImageHandler: function () {
            var owner = this;
            this.$scope.orginFileExtension = '';
            this.$scope.orginImage = '';
            this.$scope.cropedImage = '';
            this.$scope.saveDisabled = true;

            this.$scope.imageHandler = function (evt) {
                var file = evt.currentTarget.files[0];
                var imageCheck = AskDog.TokenUtil.isImage(file.name);
                if (!imageCheck.valid) {
                    owner.$scope.error = imageCheck.message;
                    owner.$scope.saveDisabled = true;
                    owner.$scope.orginImage = '';
                    owner.$scope.cropedImage = '';
                    owner.$scope.$digest();
                    return;
                } else {
                    owner.$scope.saveDisabled = false;
                    owner.$scope.error = undefined;
                    owner.$scope.$digest();
                }
                owner.$scope.orginFileExtension = file.name.split('.').pop().toLowerCase();

                var reader = new FileReader();
                reader.onload = function (evt) {
                    owner.$scope.$apply(
                        function ($scope) {
                            $scope.orginImage = evt.target.result;
                        }
                    );
                };
                reader.readAsDataURL(file);
            };

        }
    });

    ImgCropController.$inject = ['$rootScope', '$scope', '$stateParams'];

    angular.module('module.ImgCropController', ['ngImgCrop']).controller('ImgCropController', ImgCropController);

});