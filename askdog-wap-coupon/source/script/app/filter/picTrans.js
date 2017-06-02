define(['angular'], function () {

    'use strict';

    var urlMappings = [
        {
            from: "http://askdog.oss-cn-hangzhou.aliyuncs.com",
            to: "http://pic.askdog.cn"
        },
        {
            from: "http://askdog-video-out.oss-cn-hangzhou.aliyuncs.com",
            to: "http://vod.pic.askdog.cn"
        },
        {
            from: "http://img.askdog.cn",
            to: "http://pic.askdog.cn"
        },
        {
            from: "http://pic.askdog.cn",
            to: "http://pic.askdog.cn"
        },
        // for dev
        {
            from: "http://picdev.askdog.cn",
            to: "http://picdev.askdog.cn"
        }
    ];

    for (var i = 0; i < urlMappings.length; i++) {
        var pictureUrlMapping = urlMappings[i];
        var from = pictureUrlMapping.from;
        pictureUrlMapping.mdRegex = new RegExp("(?:(!\\[[^\\)\\]]*]\\()(" + from + ")([^\\s\\)\\]]*)(.*\\)))", "g");
    }

    function transform() {
        return function (link, imageParams) {

            if (!link) {
                return link;
            }

            for (var i = 0; i < urlMappings.length; i++) {
                var from = urlMappings[i].from;
                var to = urlMappings[i].to;

                if (link.startWith(from)) {
                    link = (from != to ? link.replace(from, to) : link) + '@' + imageParams;
                }
            }

            return link;
        }
    }

    angular.module('app.filter.picTrans', []).filter('picTrans', transform);
});


