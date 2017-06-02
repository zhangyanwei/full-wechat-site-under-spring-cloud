define([
    '../../base/BaseController',
    'app/directive/qrCode'
], function (BaseController) {
    var shareController = BaseController.extend({

         thumbnail : {
            LIFE: 'http://m.askdog.com/images/channel/channel-pic-12.png',
            HEALTH: 'http://m.askdog.com/images/channel/channel-pic-2.png',
            LAW: 'http://m.askdog.com/images/channel/channel-pic-3.png',
            ECONOMIC: 'http://m.askdog.com/images/channel/channel-pic-4.png',
            CULTURE: 'http://m.askdog.com/images/channel/channel-pic-5.png',
            SCIENCE: 'http://m.askdog.com/images/channel/channel-pic-6.png',
            ART: 'http://m.askdog.com/images/channel/channel-pic-7.png',
            ENTERTAINMENT: 'http://m.askdog.com/images/channel/channel-pic-11.png',
            EDUCATION: 'http://m.askdog.com/images/channel/channel-pic-9.png',
            IT: 'http://m.askdog.com/images/channel/channel-pic-10.png',
            GUITAR:'http://m.askdog.com/images/channel/channel-pic-8.png',
            GUITAR_TUTORIAL:'http://m.askdog.com/images/channel/channel-pic-8.png',
            GUITAR_SHOW:'http://m.askdog.com/images/channel/channel-pic-8.png',
            CATE:'http://m.askdog.com/images/channel/channel-pic-1.png',
            CATE_COLUMN:'http://m.askdog.com/images/channel/channel-pic-1.png',
            CATE_SHARE:'http://m.askdog.com/images/channel/channel-pic-1.png',
        },

        init: function ($scope, $stateParams, $window, $uibModal) {
            this.$stateParams = $stateParams;
            this.$window = $window;
            this.$uibModal = $uibModal;
            this.$uibModalInstance = $scope.$parent.$uibModalInstance;
            this.$detail = $scope.$parent.detail;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function () {
            var code = this.$detail.category.code;
            this._close();
            this._defineShareQZone(code);
            this._defineShareWeiBo(code);
            this._defineShareWeChat();
        },

        _close: function () {
            var owner = this;
            owner.$scope.close = function () {
                owner.$uibModalInstance.dismiss('close');
            }
        },

        _defineShareQZone: function (code) {
            var qZoneLink = "http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?";
            var linkParams = {
                url: 'http://' + window.location.hostname + '/#/exp/' + this.$stateParams.id,
                desc: '我在ASKDOG分享了好玩的内容，赶紧过来看！！！#ASKDOG#',
                title: this.$detail.subject,
                summary: '分享摘要',
                pics: this.$detail.thumbnail || this.thumbnail[code]
            };
            for (var prop in linkParams) {
                qZoneLink = qZoneLink + prop + '=' + (encodeURIComponent(linkParams[prop]) || '') + "&";
            }
            this.$scope.shareToQZone = function () {
                window.open(qZoneLink);
            }
        },

        _defineShareWeiBo: function (code) {
            var weiBoLink = "http://service.weibo.com/share/share.php?";
            // TODO app key
            var linkParams = {
                url: 'http://' + window.location.hostname + '/#/exp/' + this.$stateParams.id,
                appkey: '2706904596',
                ralateUid: '5943071831',
                language: 'zh_cn',
                searchPic: true,
                title: '我在ASKDOG分享了好玩的内容，赶紧过来看！！！#ASKDOG#' + '《' + this.$detail.subject + '》',
                pic: this.$detail.thumbnail || this.thumbnail[code]
            };
            for (var prop in linkParams) {
                weiBoLink = weiBoLink + prop + '=' + (encodeURIComponent(linkParams[prop]) || '') + "&";
            }
            this.$scope.shareToWeiBo = function () {
                window.open(weiBoLink);
            };
        },

        _defineShareWeChat: function () {
            var owner = this;

            owner.$scope.qrLink = 'http://' + window.location.hostname + '/#/exp/' + owner.$stateParams.id;
            this.$scope.shareToWeChat = function () {
                var userAgent = owner.$window.navigator.userAgent;
                var matches = /MicroMessenger\/([^\s\/.]+)/.exec(userAgent);
                if (matches && matches.length > 1) {
                    var version = matches[1];
                    if (version >= 5) {
                        owner.$uibModalInstance.dismiss('close');
                        var wechatShare = owner.$uibModal.open({
                            windowTemplateUrl: 'views/base/modal-window.html',
                            windowTopClass: 'wechat-modal',
                            templateUrl: 'views/dialog/wechat-share.html',
                            controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                                $scope.$uibModalInstance = $uibModalInstance;
                            }]
                        });
                    }
                } else {
                    owner.$scope.phoneShare = true;
                }

            };
        }

    });

    shareController.$inject = ['$scope', '$stateParams', '$window', '$uibModal'];

    angular.module('module.dialog.shareController', []).controller('shareController', shareController);

});