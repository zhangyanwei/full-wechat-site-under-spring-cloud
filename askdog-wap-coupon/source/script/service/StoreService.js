define(['base/BaseService'], function (BaseService) {

    var StoreService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        stores: function (page, size, location, adCode) {
            var uri = "/api/stores?page={page}&size={size}".format({
                "page": page,
                "size": size
            });

            uri = "/api/stores?page={page}&size={size}&lat={lat}&lng={lng}&ad_code={adCode}".format({
                "page": page,
                "size": size,
                "lat": location ? location.latitude : "",
                "lng": location ? location.longitude : "",
                "adCode": adCode || ""
            });
            return this.get(uri);
        },

        storeDetail: function (id) {
            return this.get("/api/stores/" + id);
        },

        storeCities: function () {
            return this.get("/api/stores/cities");
        },

        addEmployee: function (storeId, token) {
            return this.post("/api/stores/{storeId}/employees?token={token}".format({
                "storeId": storeId,
                "token": token
            }));
        },

        eventDetail: function (id) {
            return this.get("/api/events/" + id);
        }
    });

    angular.module('service.StoreService', [])
        .provider('StoreService', function () {
            this.$get = ['$http', '$rootScope', function (http, scope) {
                return new StoreService(http, scope);
            }];
        });

});