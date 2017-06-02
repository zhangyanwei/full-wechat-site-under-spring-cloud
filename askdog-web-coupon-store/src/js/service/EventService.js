define(['base/BaseService'], function (BaseService) {

    var EventService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        createEvent: function(event) {
            return this.post("/api/events", event);
        },

        modifyEvent: function(event) {
            return this.put("/api/events", event);
        },

        deleteEvent: function(eventId) {
            return this.delete("/api/events/{0}".format(eventId));
        },

        search: function(storeId, page, size) {
            return this.get("/api/events/search?storeId={storeId}&page={page}&size={size}".format({
                storeId: storeId,
                page: page,
                size: size
            }));
        }
    });

    angular.module('service.EventService', [])
        .provider('EventService', function () {
            this.$get = ['$http', '$rootScope', function (http, scope) {
                return new EventService(http, scope);
            }];
        });

});