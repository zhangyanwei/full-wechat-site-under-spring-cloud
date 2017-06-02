define(['angular'], function () {

    /**
     * pageTitle - Directive for set Page title - mata title
     */
    function weShare($window, $http) {
        return {
            restrict: 'AE',
            scope: {
                detail: '='
            },
            link: function (scope, element, attributes) {
                scope.$watch(
                    function () {
                        return scope.detail;
                    },
                    function (detail) {
                        if (!detail) {
                            return;
                        }
                        var userAgent = $window.navigator.userAgent;
                        var matches = /MicroMessenger\/([^\s\/.]+)/.exec(userAgent);
                        if (matches && matches.length > 1) {
                            var version = matches[1];
                            if (version) {
                                require(['weixin'], function (wx) {
                                    $http.post('/api/wechat/jsapi/config', location.href.split('#')[0], {
                                        headers: {
                                            'Content-Type': 'text/plain'
                                        }
                                    }).success(
                                        function (config) {
                                            wx.config({
                                                appId: config.app_id,
                                                timestamp: config.timestamp,
                                                nonceStr: config.nonce_str,
                                                signature: config.signature,
                                                jsApiList: [
                                                    'onMenuShareTimeline',
                                                    'onMenuShareAppMessage',
                                                    'onMenuShareQQ',
                                                    'onMenuShareWeibo',
                                                    'onMenuShareQZone'
                                                ]
                                            });

                                            wx.ready(function () {
                                                var content = scope.detail.desc;
                                                if (!content) {
                                                    if (scope.detail.content && scope.detail.content.type == 'VIDEO') {
                                                        content = '视频来自ASKDOG经验分享社区';
                                                    } else {
                                                        content = '图文来自ASKDOG经验分享社区';
                                                    }
                                                }

                                                wx.onMenuShareTimeline({
                                                    title: scope.detail.subject,
                                                    imgUrl: scope.detail.thumbnail || AskDogExp.URL.base() + '/images/default-channel.png',
                                                    desc: scope.detail.desc || content,
                                                    link: scope.detail.link || AskDogExp.URL.full(),
                                                    type: 'link',
                                                    success: function () {
                                                        window.location = AskDogExp.URL.full();
                                                    }
                                                });

                                                wx.onMenuShareAppMessage({
                                                    title: scope.detail.subject,
                                                    desc: scope.detail.desc || content,
                                                    link: scope.detail.link || AskDogExp.URL.full(),
                                                    imgUrl: scope.detail.thumbnail || AskDogExp.URL.base() + '/images/default-channel.png',
                                                    type: 'link',
                                                    success: function () {
                                                        window.location = AskDogExp.URL.full();
                                                    }
                                                });

                                                wx.onMenuShareQQ({
                                                    title: scope.detail.subject,
                                                    desc: scope.detail.desc || content,
                                                    link: scope.detail.link || AskDogExp.URL.full(),
                                                    imgUrl: scope.detail.thumbnail || AskDogExp.URL.base() + '/images/default-channel.png',
                                                    type: 'link',
                                                    success: function () {
                                                        window.location = AskDogExp.URL.full();
                                                    }
                                                });

                                                wx.onMenuShareWeibo({
                                                    title: scope.detail.subject,
                                                    desc: scope.detail.desc || content,
                                                    link: scope.detail.link || AskDogExp.URL.full(),
                                                    imgUrl: scope.detail.thumbnail || AskDogExp.URL.base() + '/images/default-channel.png',
                                                    type: 'link',
                                                    success: function () {
                                                        window.location = AskDogExp.URL.full();
                                                    }
                                                });

                                                wx.onMenuShareQZone({
                                                    title: scope.detail.subject,
                                                    desc: scope.detail.desc || content,
                                                    link: scope.detail.link || AskDogExp.URL.full(),
                                                    imgUrl: scope.detail.thumbnail || AskDogExp.URL.base() + '/images/default-channel.png',
                                                    type: 'link',
                                                    success: function () {
                                                        window.location = AskDogExp.URL.full();
                                                    }
                                                });
                                            });

                                            wx.error(function (res) {
                                                //console.warn(res);
                                            });
                                        }
                                    );
                                });

                            }
                        }

                    });
            }
        }
    }

    weShare.$inject = ['$window', '$http'];
    angular.module('app.directive.weShare', []).directive('weShare', weShare);
});