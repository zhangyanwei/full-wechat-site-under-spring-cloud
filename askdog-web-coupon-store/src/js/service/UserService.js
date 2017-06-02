define(['base/BaseService'], function (BaseService) {

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
            return this.postForm("/login?ajax=true", userInfo);
        },

        logout: function () {
            return this.get("/logout?ajax=true");
        },

        create: function (user) {
            return this.post("/api/users", user);
        },

        modifyPassword: function (change_password) {
            return this.put("/api/user/password", change_password)
        }

    });

    angular.module('service.UserService', [])
        .provider('UserService', function () {
            this.$get = ['$http', '$rootScope', function (http, scope) {
                return new UserService(http, scope);
            }];
        });

});