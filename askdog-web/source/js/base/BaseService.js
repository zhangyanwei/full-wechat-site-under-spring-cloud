define(['class', 'common/loadingMessage'], function (Class, loadingMessage) {

    var checkArgument = AskDog.Precondition.checkArgument;

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
            endpoint = AskDog.ApiUtil.apiUrl(endpoint);
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
            endpoint = AskDog.ApiUtil.apiUrl(endpoint);
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
            endpoint = AskDog.ApiUtil.apiUrl(endpoint);
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
            endpoint = AskDog.ApiUtil.apiUrl(endpoint);
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
            endpoint = AskDog.ApiUtil.apiUrl(endpoint);
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
            endpoint = AskDog.ApiUtil.apiUrl(endpoint);
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


        // TODO move this in a angular service/factory, so we could using it not only in service.
        enableLoading: function (promise, message) {
            var owner = this;
            this._logger.show(message);
            var unload = function () {
                this._logger.hide();
            }.bind(this);

            promise
                .success(function (resp) {
                    unload();
                })
                .error(function (resp) {
                    unload();
                    if (promise.$$state.value
                        && promise.$$state.value.status == 403
                        && promise.$$state.value.data
                        && promise.$$state.value.data.code == 'WEB_ACCESS_AUTH_REQUIRED') {
                        var signInInterval = setInterval(function () {
                            if (owner.signInMonitor()) {
                                clearInterval(signInInterval);
                            }
                        }, 1);
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