define(['class', 'common/loadingMessage'], function (Class, loadingMessage) {

    var checkArgument = AskDogExp.Precondition.checkArgument;

    /**
     * Base service for all services.
     * Use this as a template for all future services
     *
     * Use of Class.js
     */
    var BaseService = Class.extend({

        _http: null,
        _scope: null,
        _logger: null,

        init: function (http, scope) {
            this._http = http;
            this._scope = scope;
            this._logger = loadingMessage.logger(this._scope);
        },

        get: function (endpoint, config) {
            checkArgument(this._http != null, "_http not initialized.");
            endpoint = AskDogExp.ApiUtil.apiUrl(endpoint);
            return this.enableLoading(
                this._http.get(
                    endpoint,
                    angular.extend({
                        headers: {'Content-Type': 'application/json;charset=utf-8'},
                        withCredentials: true
                    }, config)),
                {notice: "Retrieving", message: endpoint});
        },

        post: function (endpoint, data, config) {
            checkArgument(this._http != null, "_http not initialized.");
            endpoint = AskDogExp.ApiUtil.apiUrl(endpoint);
            return this.enableLoading(
                this._http.post(
                    endpoint,
                    data,
                    angular.extend({
                        headers: {'Content-Type': 'application/json;charset=utf-8'},
                        withCredentials: true
                    }, config)),
                {notice: "Creating", message: endpoint});
        },

        postForm: function (endpoint, data, config) {
            checkArgument(this._http != null, "_http not initialized.");
            endpoint = AskDogExp.ApiUtil.apiUrl(endpoint);
            return this.enableLoading(
                this._http.post(
                    endpoint,
                    $.param(data),
                    angular.extend({
                        headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},
                        withCredentials: true
                    }, config)),
                {notice: "Creating", message: endpoint});
        },

        put: function (endpoint, data, config) {
            checkArgument(this._http != null, "_http not initialized.");
            endpoint = AskDogExp.ApiUtil.apiUrl(endpoint);
            return this.enableLoading(
                this._http.put(
                    endpoint,
                    data,
                    angular.extend({
                        headers: {'Content-Type': 'application/json;charset=utf-8'},
                        withCredentials: true
                    }, config)),
                {notice: "Updating", message: endpoint});
        },

        delete: function (endpoint, config) {
            checkArgument(this._http != null, "_http not initialized.");
            endpoint = AskDogExp.ApiUtil.apiUrl(endpoint);
            return this.enableLoading(
                this._http.delete(
                    endpoint,
                    angular.extend({
                        headers: {'Content-Type': 'application/json;charset=utf-8'},
                        withCredentials: true
                    }, config)),
                {notice: "Deleting", message: endpoint});
        },

        // TODO move this in a angular service/factory, so we could using it not only in service.
        enableLoading: function (promise, message) {
            this._logger.show(message);
            promise
                .success(function () {
                    promise.then(function (response) {

                    });
                    return promise;
                })
                .error(function () {
                    promise.then(null, function (response) {
                        if (response.status == 403) {
                            var userAgent = window.navigator.userAgent;
                            var matches = /MicroMessenger\/([^\s\/.]+)/.exec(userAgent);
                            if (matches && matches.length > 1) {
                                var version = matches[1];
                                if (version) {
                                    window.location.href = "/login/wechat?request=" + encodeURIComponent(window.location.href);
                                    return;
                                }
                            }
                            window.location.href = "/#/login?request=" + encodeURIComponent(window.location.href);
                        }
                    });
                    return promise;
                });
            return promise;
        }
    });

    return BaseService;

});