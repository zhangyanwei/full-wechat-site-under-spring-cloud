(function (AskDogExp) {

    // String formatter
    String.prototype.format = function (a) {
        var args = typeof a == "object" ? a : arguments;
        return this.replace(/\{(\w+)\}/g, function (m, k) {
            return args[k];
        });
    };

    if (!String.prototype.startWith) {
        String.prototype.startWith = function (str) {
            if (str == null || str == "" || this.length == 0 || str.length > this.length) {
                return false;
            }

            return this.substr(0, str.length) == str;
        };
    }

    // Array useful functions
    if (!Array.prototype.remove) {
        Array.prototype.remove = function (predicate) {
            if (this == null) {
                throw new TypeError('Array.prototype.remove called on null or undefined');
            }
            if (typeof predicate !== 'function') {
                throw new TypeError('predicate must be a function');
            }
            var list = Object(this);
            var length = list.length >>> 0;
            var thisArg = arguments[1];
            var value;

            for (var i = 0; i < length; i++) {
                value = list[i];
                if (predicate.call(thisArg, value, i, list)) {
                    list.splice(i, 1);
                }
            }

            return undefined;
        };
    }

    if (!Array.prototype.find) {
        Array.prototype.find = function (predicate) {
            if (this == null) {
                throw new TypeError('Array.prototype.find called on null or undefined');
            }
            if (typeof predicate !== 'function') {
                throw new TypeError('predicate must be a function');
            }
            var list = Object(this);
            var length = list.length >>> 0;
            var thisArg = arguments[1];
            var value;

            for (var i = 0; i < length; i++) {
                value = list[i];
                if (predicate.call(thisArg, value, i, list)) {
                    return value;
                }
            }
            return undefined;
        };
    }

    if (!Date.prototype.format) {
        Date.prototype.format = function (fmt) {
            var o = {
                "M+": this.getMonth() + 1,
                "d+": this.getDate(),
                "h+": this.getHours(),
                "m+": this.getMinutes(),
                "s+": this.getSeconds(),
                "q+": Math.floor((this.getMonth() + 3) / 3),
                "S": this.getMilliseconds()
            };
            if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        };
    }

    var URL = (function () {
        var matches = /(\w+:\/\/[^/#?]+)(\/[^?#]+)?(?:#([^?]+))?(?:\?(.+))?/.exec(window.location.href);
        var _urlBase = matches[1],
            _uri = matches[2],
            _urlPath = matches[3] || '/',
            _urlParams = {};
        if (matches[4] !== undefined) {
            var params = matches[4];
            if (params.length > 0) {
                var pairs = params.split('&');
                for (var index in pairs) {
                    if (typeof pairs[index] !== 'string') {
                        continue;
                    }

                    var pair = pairs[index].split('=');
                    _urlParams[pair[0]] = pair.length > 1 ? pair[1] : "";
                }
            }
        }

        return {
            full: function () {
                return window.location.href;
            },
            uri: function () {
                return _uri;
            },
            base: function () {
                return _urlBase;
            },
            param: function (name) {
                return _urlParams[name];
            },
            path: function () {
                return _urlPath;
            }
        };
    })();
    // Utilities
    var Util = {
        LIB_PATH: '../lib',
        libPath: function (lib) {
            return Util.LIB_PATH + '/' + lib;
        }
    };

    var ApiUtil = {
        BASE_DOMAIN: 'askdog.com',
        API_DOMAIN: 'api.askdog.com',
        apiUrl: function (endpoint) {
            if (endpoint.startWith('http')) {
                return endpoint;
            }
            return 'http://' + ApiUtil.API_DOMAIN + endpoint;
        }
    };

    // Token  utilities
    var TokenUtil = {
        type: {
            USER_AVATAR: 'USER_AVATAR',
            EXPERIENCE_PICTURE: 'EXPERIENCE_PICTURE',
            EXPERIENCE_VIDEO: 'EXPERIENCE_VIDEO',
            CHANNEL_THUMBNAIL: 'CHANNEL_THUMBNAIL'
        },
        TOKEN_URI: '/api/storage/access_token?type={type}&extention={extension}', // USER_AVATAR,EXPERIENCE_PICTURE,CHANNEL_THUMBNAIL
        getUri: function (type, extension) {
            return TokenUtil.TOKEN_URI.format({
                "type": type,
                "extension": extension
            })
        }
    };

    // precondition check
    var Precondition = {

        /**
         * Ensures the truth of an expression involving one or more parameters to the calling method.
         * @param expression
         * @param errorMsg
         */
        checkArgument: function (expression, errorMsg) {
            if (!expression) {
                throw new Error(errorMsg);
            }
        }
    };

    var Constants = {
        // TODO change to production url "http://pic.askdog.cn"
        PICTURE_URL_PREFIX: "http://pic.askdog.cn",
        PICTURE_URL_VOD_PREFIX: "http://vod.pic.askdog.cn",
        PICTURE_URL_DEV_PREFIX: "http://picdev.askdog.cn",
        PICTURE_URL_DEV_VOD_PREFIX: "http://vod.picdev.askdog.cn",
        CATEGORY_THUMBNAIL: {
            ART: {
                abstract: "http://pic.askdog.cn/resources/category/thumbnail/abstract/art.png",
                specific: "http://pic.askdog.cn/resources/category/thumbnail/specific/art.png"
            },
            CULTURE: {
                abstract: "http://pic.askdog.cn/resources/category/thumbnail/abstract/culture.png",
                specific: "http://pic.askdog.cn/resources/category/thumbnail/specific/culture.png"
            },
            ECONOMIC: {
                abstract: "http://pic.askdog.cn/resources/category/thumbnail/abstract/economy.png",
                specific: "http://pic.askdog.cn/resources/category/thumbnail/specific/economy.png"
            },
            EDUCATION: {
                abstract: "http://pic.askdog.cn/resources/category/thumbnail/abstract/education.png",
                specific: "http://pic.askdog.cn/resources/category/thumbnail/specific/education.png"
            },
            ENTERTAINMENT: {
                abstract: "http://pic.askdog.cn/resources/category/thumbnail/abstract/entertainment.png",
                specific: "http://pic.askdog.cn/resources/category/thumbnail/specific/entertainment.png"
            },
            HEALTH: {
                abstract: "http://pic.askdog.cn/resources/category/thumbnail/abstract/health.png",
                specific: "http://pic.askdog.cn/resources/category/thumbnail/specific/health.png"
            },
            IT: {
                abstract: "http://pic.askdog.cn/resources/category/thumbnail/abstract/it.png",
                specific: "http://pic.askdog.cn/resources/category/thumbnail/specific/it.png"
            },
            LAW: {
                abstract: "http://pic.askdog.cn/resources/category/thumbnail/abstract/law.png",
                specific: "http://pic.askdog.cn/resources/category/thumbnail/specific/law.png"
            },
            LIFE: {
                abstract: "http://pic.askdog.cn/resources/category/thumbnail/abstract/life.png",
                specific: "http://pic.askdog.cn/resources/category/thumbnail/specific/life.png"
            },
            SCIENCE: {
                abstract: "http://pic.askdog.cn/resources/category/thumbnail/abstract/science.png",
                specific: "http://pic.askdog.cn/resources/category/thumbnail/specific/science.png"
            },
            GUITAR: {
                abstract: "http://pic.askdog.cn/resources/category/thumbnail/abstract/guitar.png",
                specific: "http://pic.askdog.cn/resources/category/thumbnail/specific/guitar.png"
            },
            GUITAR_TUTORIAL: {
                abstract: "http://pic.askdog.cn/resources/category/thumbnail/abstract/guitar.png",
                specific: "http://pic.askdog.cn/resources/category/thumbnail/specific/guitar.png"
            },
            GUITAR_SHOW: {
                abstract: "http://pic.askdog.cn/resources/category/thumbnail/abstract/guitar.png",
                specific: "http://pic.askdog.cn/resources/category/thumbnail/specific/guitar.png"
            },
            CATE: {
                abstract: "http://pic.askdog.cn/resources/category/thumbnail/abstract/cate.png",
                specific: "http://pic.askdog.cn/resources/category/thumbnail/specific/cate.png"
            },
            CATE_COLUMN: {
                abstract: "http://pic.askdog.cn/resources/category/thumbnail/abstract/cate.png",
                specific: "http://pic.askdog.cn/resources/category/thumbnail/specific/cate.png"
            },
            CATE_SHARE: {
                abstract: "http://pic.askdog.cn/resources/category/thumbnail/abstract/cate.png",
                specific: "http://pic.askdog.cn/resources/category/thumbnail/specific/cate.png"
            }
        }
    };
    Constants.PICTRUE_URL_MAPPINS = [
        {
            from: Constants.PICTURE_URL_PREFIX,
            to: Constants.PICTURE_URL_PREFIX
        },
        {
            from: "http://askdog.oss-cn-hangzhou.aliyuncs.com",
            to: Constants.PICTURE_URL_PREFIX
        },
        {
            from: "http://askdog-video-out.oss-cn-hangzhou.aliyuncs.com",
            to: Constants.PICTURE_URL_VOD_PREFIX
        },
        {
            from: "http://img.askdog.cn",
            to: Constants.PICTURE_URL_PREFIX
        },
        {
            from: Constants.PICTURE_URL_DEV_PREFIX,
            to: Constants.PICTURE_URL_DEV_PREFIX
        },
        {
            from: "http://ad-dev.oss-cn-hangzhou.aliyuncs.com",
            to: Constants.PICTURE_URL_DEV_PREFIX
        },
        {
            from: "http://ad-exp-vod-dev.oss-cn-hangzhou.aliyuncs.com",
            to: Constants.PICTURE_URL_DEV_VOD_PREFIX
        },
        {
            from: "http://imgdev.askdog.cn",
            to: Constants.PICTURE_URL_DEV_PREFIX
        }
    ];

    for (var i = 0; i < Constants.PICTRUE_URL_MAPPINS.length; i++) {
        var pictureUrlMapping = Constants.PICTRUE_URL_MAPPINS[i];
        var from = pictureUrlMapping.from;
        pictureUrlMapping.mdRegex = new RegExp("(?:(!\\[[^\\)\\]]*]\\()(" + from + ")([^\\s\\)\\]]*)(.*\\)))", "g");
    }

    AskDogExp.ApiUtil = ApiUtil;
    AskDogExp.URL = URL;
    AskDogExp.Util = Util;
    AskDogExp.TokenUtil = TokenUtil;
    AskDogExp.Precondition = Precondition;
    AskDogExp.Constants = Constants;

})(window.AskDogExp || (window.AskDogExp = function () {
    }));


