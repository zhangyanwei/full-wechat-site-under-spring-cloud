define(['class'], function (Class) {

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

        init: function (http, scope) {
            this._http = http;
            this._scope = scope;
        },

        get: function (endpoint) {
            checkArgument(this._http != null, "_http not initialized.");
            return this.enableLoading(this._http.get(endpoint), {notice: "Retrieving", message: endpoint});
        },

        post: function (endpoint, data, config) {
            checkArgument(this._http != null, "_http not initialized.");
            return this.enableLoading(this._http.post(endpoint, data, angular.extend({
                headers: {'Content-Type': 'application/json;charset=utf-8'}
            }, config)), {notice: "Creating", message: endpoint});
        },

        postForm: function (endpoint, data, config) {
            checkArgument(this._http != null, "_http not initialized.");
            return this.enableLoading(this._http.post(endpoint, $.param(data), angular.extend({
                headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'}
            }, config)), {notice: "Creating", message: endpoint});
        },

        postMultipartForm: function (endpoint, formData, config) {
            checkArgument(this._http != null, "_http not initialized.");
            return this.enableLoading(this._http.post(endpoint, formData, angular.extend({
                headers: {'Cache-Control': 'no-cache', 'Content-Type': undefined}
            }, config)), {notice: "Creating", message: endpoint});
        },

        put: function (endpoint, data, config) {
            checkArgument(this._http != null, "_http not initialized.");

            return this.enableLoading(this._http.put(endpoint, data, angular.extend({
                headers: {'Content-Type': 'application/json;charset=utf-8'}
            }, config)), {notice: "Updating", message: endpoint});
        },

        delete: function (endpoint, config) {
            checkArgument(this._http != null, "_http not initialized.");
            return this.enableLoading(this._http.delete(endpoint, config), {notice: "Deleting", message: endpoint});
        },

        // TODO move t  his in a angular service/factory, so we could using it not only in service.
        enableLoading: function (promise, message) {
            return promise;
        }
    });

    return BaseService;

});