define(['base/BaseService', '_global'], function (BaseService, _g) {

    var UserService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        status: function () {
            return this.get("/api/users/me/status");
        },

        me: function () {
            return this.get("/api/users/me");
        },

        login: function (userInfo) {
            return this.postForm(_g.DOMAIN + "/login?ajax=true", userInfo);
        },

        logout: function () {
            return this.get(_g.DOMAIN + "/logout?ajax=true");
        },

        register: function (userInfo, phoneRegister) {
            var registerUri = '/api/users/phone';
            if (!phoneRegister) {
                registerUri = '/api/users';
            }
            return this.post(registerUri, userInfo);
        }

    });

    angular.module('service.UserService', [])
        .provider('UserService', function () {
            this.$get = ['$http', '$rootScope', function (http, scope) {
                return new UserService(http, scope);
            }];
        });

});