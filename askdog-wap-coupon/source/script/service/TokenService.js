define(['base/BaseService', '_global'], function (BaseService, _g) {

    var TokenService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        detail: function (name, token) {
            return this.get("/api/token/{0}/{1}".format(name, token));
        }

    });

    angular.module('service.TokenService', [])
        .provider('TokenService', function () {
            this.$get = ['$http', '$rootScope', function (http, scope) {
                return new TokenService(http, scope);
            }];
        });

});