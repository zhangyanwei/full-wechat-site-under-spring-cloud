define(['base/BaseController', 'jquery.qrcode'], function (BaseController) {

    var ShareController = BaseController.extend({

        thumbnail: {
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
            GUITAR: 'http://m.askdog.com/images/channel/channel-pic-8.png',
            GUITAR_TUTORIAL: 'http://m.askdog.com/images/channel/channel-pic-8.png',
            GUITAR_SHOW: 'http://m.askdog.com/images/channel/channel-pic-8.png',
            CATE: 'http://m.askdog.com/images/channel/channel-pic-1.png',
            CATE_COLUMN: 'http://m.askdog.com/images/channel/channel-pic-1.png',
            CATE_SHARE: 'http://m.askdog.com/images/channel/channel-pic-1.png',
        },

        init: function (_rootScope, _scope, _stateParams) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$uibModalInstance = _scope.$parent.$uibModalInstance;
            this.$experienceDetail = _scope.$parent.experienceDetail;
            this._super(_scope);
        },

        defineScope: function () {
            var code = this.$experienceDetail.category.code;
            this._defineShareQZone(code);
            this._defineShareWeChat();
            this._defineShareWeiBo(code);
            this._defineCancel();
        },

        _defineShareQZone: function (code) {
            var qZoneLink = "http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?";

            var linkParams = {
                url: 'http://' + window.location.hostname + '/#/exp/' + this.$experienceDetail.id,
                desc: '我在ASKDOG分享了好玩的内容，赶紧过来看！！！#ASKDOG#',
                title: this.$experienceDetail.subject,
                summary: '分享摘要',
                pics: this.$experienceDetail.thumbnail || this.thumbnail[code]
            };
            for (var prop in linkParams) {
                qZoneLink = qZoneLink + prop + '=' + (encodeURIComponent(linkParams[prop]) || '') + "&";
            }
            this.$scope.shareToQZone = function () {
                window.open(qZoneLink);
            };
        },

        _defineShareWeChat: function () {
            //TODO share host
            var qrLink = 'http://m.askdog.com/#/exp/' + this.$experienceDetail.id;
            var generated = false;
            this.$scope.shareToWeChat = function () {
                if (!generated) {
                    $('#experience-qrcode').qrcode({width: 120, height: 120, text: qrLink});
                    generated = true;
                }
            };
        },

        _defineShareWeiBo: function (code) {
            var weiBoLink = "http://service.weibo.com/share/share.php?";
            // TODO app key
            var linkParams = {
                url: 'http://' + window.location.hostname + '/#/exp/' + this.$experienceDetail.id,
                appkey: '2706904596',
                ralateUid: '5994848456',
                language: 'zh_cn',
                searchPic: true,
                title: '我在ASKDOG分享了好玩的内容，赶紧过来看！！！#ASKDOG#' + '《' + this.$experienceDetail.subject + '》',
                pic: this.$experienceDetail.thumbnail || this.thumbnail[code]
            };
            for (var prop in linkParams) {
                weiBoLink = weiBoLink + prop + '=' + (encodeURIComponent(linkParams[prop]) || '') + "&";
            }
            this.$scope.shareToWeiBo = function () {
                window.open(weiBoLink);
            };
        },

        _defineCancel: function () {
            var owner = this;
            this.$scope.cancel = function () {
                owner.$uibModalInstance.dismiss("cancel");
            };
        }
    });

    ShareController.$inject = ['$rootScope', '$scope', '$stateParams'];

    angular.module('module.dialog.ShareController', []).controller('ShareController', ShareController);

});