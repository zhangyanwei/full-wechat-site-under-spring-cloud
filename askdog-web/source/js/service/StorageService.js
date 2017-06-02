define(['base/BaseService'], function (BaseService) {

    var StorageService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        getToken: function (type, extension) {
            return this.get(AskDog.TokenUtil.getUri(type, extension));
        },

        upload: function (token, file) {
            var data = new FormData();
            data.append("policy", token.policy);
            data.append("signature", token.signature);
            data.append("callback", token.callback);
            data.append("OSSAccessKeyId", token.OSSAccessKeyId);
            data.append("key", token.key);
            data.append("success_action_status", "200");
            data.append("file", file);
            return this.postMultipartForm(token.host, data);
        }


    });

    angular.module('service.StorageService', [])
        .provider('StorageService', function () {
            this.$get = ['$http', '$rootScope', function (http, scope) {
                return new StorageService(http, scope);
            }];
        });

});