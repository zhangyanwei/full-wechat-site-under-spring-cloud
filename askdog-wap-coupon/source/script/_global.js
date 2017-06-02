define(function () {

    function http() {
        var request = function(method, url, data, callback) {
            var xhr = new XMLHttpRequest();
            xhr.withCredentials = true;
            xhr.addEventListener("readystatechange", function () {
                if (this.status > 500) {
                    window.location.hash = "/maintenance"
                } else if (this.readyState === 4) {
                    callback.call(this)
                }
            });
            xhr.open(method, url);
            xhr.setRequestHeader("content-type", "application/json; charset=UTF-8");
            xhr.setRequestHeader("cache-control", "no-cache");
            xhr.send(data);
        };

        this.post = function(url, data, callback) {
            request("POST", url, data, callback);
        };

        this.get = function (url, callback) {
            request("GET", url, null, callback);
        };

        return this;
    }

    function auth(hash, callback) {
        _g.http().get("/api/users/me/status", function() {
            if (this.responseText != '"AUTHENTICATED"') {
                document.getElementsByTagName("body")[0].style.display = "none";
                _g.wxAuth(hash);
            } else {
                typeof callback == "function" && callback();
            }
        });
    }

    var traceView, currentTraceMessage;

    function showTrace(message) {
        setTimeout(function () {
            var traceMessage = currentTraceMessage = traceView.insertBefore(document.createElement("div"), currentTraceMessage);
            traceMessage.textContent = message;
            traceMessage.className = "trace-message";
            traceMessage.style.animation = "trace-message 2s ease-out";
            setTimeout(function() {
                traceMessage.remove();
                (currentTraceMessage === traceMessage) && (currentTraceMessage = undefined);
            }, 2000);
        }, 0);
    }

    function trace(message) {
        if (traceView) {
            showTrace(message);
        } else {
            traceView = document.body.appendChild(document.createElement("div"));
            traceView.className = "trace-message-wrapper";
            showTrace(message);
        }
    }

    function wxConfig(callback) {
        http().post("/api/wechat/jsapi/config", location.href.split('#')[0], function () {
            var wxConfig = JSON.parse(this.responseText);
            callback(wxConfig);
        });
    }

    var _g = this;

    this.http = http;

    this.trace = trace;

    this.cities = function(callback) {

        var owner = this;

        if (this.cityList) {
            callback.call(this, this.cityList);
            return;
        }

        http().get("/api/stores/cities", function () {
            var cities = JSON.parse(this.responseText).result;
            owner.cityList = cities;
            callback.call(this, cities);
        });
    };

    this.loc = (function () {
        var matches = /(\w+:\/\/[^/#?]+)(\/[^?#]*)?(?:\?([^#]*))?(?:#([^?]+))?(?:\?(.+))?/.exec(window.location.href);
        var _urlBase = matches[1],
            _path = matches[2] || '/',
            _queries = matches[3],
            _fragment = matches[4],
            _urlParams = {};
        if (_queries !== undefined) {
            if (_queries.length > 0) {
                var pairs = _queries.split('&');
                for (var i = 0;i < pairs.length; i++) {
                    var pair = pairs[i];
                    if (typeof pair !== 'string') {
                        continue;
                    }
                    var param = pair.split('=');
                    _urlParams[param[0]] = param.length > 1 ? param[1] : "";
                }
            }
        }
        return {
            full: function (hash, keepQuery) {
                var query = keepQuery && _queries ? ("?" + _queries)  : "";
                return hash ? "{0}/{1}#{2}".format(_urlBase, query, hash) : window.location.href;
            },
            path: function () {
                return _path;
            },
            query: function() {
                return _queries;
            },
            base: function () {
                return _urlBase;
            },
            param: function (name) {
                var value = _urlParams[encodeURIComponent(name)];
                return value ? decodeURIComponent(value) : undefined;
            },
            fragment: function () {
                return _fragment;
            }
        };
    })();

    this.isWechat = function() {
        var userAgent = window.navigator.userAgent;
        var matches = /MicroMessenger\/([^\s\/.]+)/.exec(userAgent);
        return !!(matches && matches.length > 1);
    };

    this.wxAuth = function (hash) {
        window.location.href = "/login/wechat?request=" + encodeURIComponent(_g.loc.full(hash, true));
    };

    this.authenticateIfRequired = function(hash, callback) {
        if (this.loc.param("from") == "wx_menu"
            || /\/t\/[0-9a-z]+/i.test(hash)
            || /^\/cash$/.test(hash)) {
            auth(hash, callback);
        } else {
            typeof callback == "function" && callback();
        }
    };

    this.initWechat = function() {
        require(['wechat'], function (wx) {
            wxConfig(function(config) {
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
                        'onMenuShareQZone',
                        'getLocation',
                        'openLocation'
                    ]
                });
            });
        });
    };

    this.DOMAIN = window._DOMAIN || this.loc.base();
    this.API_DOMAIN = window._API_DOMAIN || this.DOMAIN;
    this.WEBSOCKET_DOMAIN = window._WEBSOCKET_DOMAIN || "http://websocket.askdog.com";

    return this;
});