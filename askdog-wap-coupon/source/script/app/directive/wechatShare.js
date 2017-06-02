define(['_global', 'angular'], function (_g) {

    function wechatShare() {

        function _loadWeChat(scope) {
            require(['wechat'], function (wx) {
                _initializeWechatShare(scope, wx);
            });
        }

        function _initializeWechatShare(scope, wx) {

            var option = angular.extend({
                title: '惠券 - 让美味更优惠',
                imgUrl: 'http://coupon.askdog.com/style/images/logo.jpg',
                desc: '在这里转发商户视频可以获得各种优惠抵值券，更多优惠正在加入中，请持续关注惠券',
                link: 'http://coupon.askdog.com'
            }, scope.option ? scope.option() : {});

            wx.ready(function () {
                var shareOptions = {
                    title: option.title,
                    imgUrl: option.imgUrl,
                    desc: option.desc,
                    link: option.link,
                    type: 'link',
                    success: function () {
                        scope.shared && (scope.shared());
                    }
                };
                wx.onMenuShareTimeline(shareOptions);
                wx.onMenuShareAppMessage(shareOptions);
                wx.onMenuShareQQ(shareOptions);
                wx.onMenuShareWeibo(shareOptions);
                wx.onMenuShareQZone(shareOptions);
            });
        }

        return {
            restrict: 'AE',
            scope: {
                option: '&?',
                shared: '&?',
                linkCallback: '&?'
            },
            link: function (scope, element, attributes) {
                if (_g.isWechat()) {
                    if (scope.linkCallback) {
                        // will initialize it manually by call the function which passed into linkCallback function.
                        scope.linkCallback({
                            link: function() {
                                _loadWeChat(scope);
                            }
                        });
                    } else {
                        _loadWeChat(scope);
                    }
                }
            }
        };
    }

    wechatShare.$inject = [];

    angular.module('app.directive.wechatShare', []).directive('wechatShare', wechatShare);
});