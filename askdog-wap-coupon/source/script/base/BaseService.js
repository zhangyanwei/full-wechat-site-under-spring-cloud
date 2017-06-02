define(['class', '_global', 'angular'], function (Class, _g) {

    var checkArgument = AskDog.Precondition.checkArgument;

    function api_endpoint(endpoint) {
        return endpoint.startWith('http') ? endpoint : _g.API_DOMAIN + endpoint;
    }

    var BaseService = Class.extend({

        _http: null,
        _scope: null,

        init: function (http, scope) {
            this._http = http;
            this._scope = scope;
        },

        get: function (endpoint, config) {
            checkArgument(this._http != null, "_http not initialized.");
            endpoint = api_endpoint(endpoint);
            return this.enableLoading(
                this._http.get(
                    endpoint,
                    angular.extend({
                        headers: {'Content-Type': 'application/json;charset=utf-8'},
                        withCredentials: true
                    }, config)
                ),
                {notice: "Retrieving", message: endpoint}
            );
        },

        post: function (endpoint, data, config) {
            checkArgument(this._http != null, "_http not initialized.");
            endpoint = api_endpoint(endpoint);
            return this.enableLoading(
                this._http.post(
                    endpoint,
                    data,
                    angular.extend({
                        headers: {'Content-Type': 'application/json;charset=utf-8'},
                        withCredentials: true
                    }, config)
                ),
                {notice: "Creating", message: endpoint}
            );
        },

        put: function (endpoint, data, config) {
            checkArgument(this._http != null, "_http not initialized.");
            endpoint = api_endpoint(endpoint);
            return this.enableLoading(
                this._http.put(
                    endpoint,
                    data,
                    angular.extend({
                        headers: {'Content-Type': 'application/json;charset=utf-8'},
                        withCredentials: true
                    }, config)
                ),
                {notice: "Updating", message: endpoint}
            );
        },

        delete: function (endpoint, config) {
            checkArgument(this._http != null, "_http not initialized.");
            endpoint = api_endpoint(endpoint);
            return this.enableLoading(
                this._http.delete(
                    endpoint,
                    angular.extend({
                        headers: {'Content-Type': 'application/json;charset=utf-8'},
                        withCredentials: true
                    }, config)
                ),
                {notice: "Deleting", message: endpoint}
            );
        },

        postForm: function (endpoint, data, config) {
            checkArgument(this._http != null, "_http not initialized.");
            endpoint = api_endpoint(endpoint);
            return this.enableLoading(
                this._http.post(
                    endpoint,
                    $.param(data),
                    angular.extend({
                        headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},
                        withCredentials: true
                    }, config)
                ),
                {notice: "Creating", message: endpoint}
            );
        },

        postMultipartForm: function (endpoint, formData, config) {
            checkArgument(this._http != null, "_http not initialized.");
            endpoint = api_endpoint(endpoint);
            return this.enableLoading(
                this._http.post(
                    endpoint,
                    formData,
                    angular.extend({
                        headers: {'Cache-Control': 'no-cache', 'Content-Type': undefined}
                    }, config)
                ),
                {notice: "Creating", message: endpoint}
            );
        },

        jsonp: function(url, callbackParam) {
            checkArgument(this._http != null, "_http not initialized.");
            return this.enableLoading(
                this._http.jsonp(url, {jsonpCallbackParam: callbackParam || 'callback'}),
                {notice: "JSONP", message: url}
            );
        },

        // TODO move this in a angular service/factory, so we could using it not only in service.
        enableLoading: function (promise, message) {
            var owner = this;
            promise
                .error(function (resp) {
                    if (!promise.disableAutoAuthenticate
                        && promise.$$state.value
                        && promise.$$state.value.status == 403
                        && promise.$$state.value.data
                        && promise.$$state.value.data.code == 'WEB_ACCESS_AUTH_REQUIRED') {
                        if (_g.isWechat()) {
                            _g.wxAuth();
                        } else {
                            var signInInterval = setInterval(function () {
                                if (owner.signInMonitor()) {
                                    clearInterval(signInInterval);
                                }
                            }, 1);
                        }

                    } else if (promise.$$state.value && promise.$$state.value.status > 500) {
                        window.location.hash = "/maintenance";
                    }
                });

            return promise;
        },

        signInMonitor: function () {
            if (this._scope.$root.signIn) {
                if (!this._scope.$root.signInOpened) {
                    this._scope.$root.userSelf = undefined;
                    this._scope.$root.signIn();
                }
                return true
            }
            return false;
        }
    });

    return BaseService;

});