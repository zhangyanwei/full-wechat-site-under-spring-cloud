define(['base/BaseService'], function (BaseService) {

    var ChannelService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        detail: function (channelId) {
            var detailUri = "/api/channels/{channelId}".format({"channelId": channelId});
            return this.get(detailUri);
        },

        experienceList: function (channelId, page, size) {
            var expListUri = "/api/experiences?channelId={channelId}&page={page}&size={size}".format({
                "channelId": channelId,
                "page": page,
                "size": size
            });
            return this.get(expListUri);
        },

        create: function (pureChannel) {
            return this.post("/api/channels", pureChannel);
        },

        update: function (channelId, amendChannel) {
            var updateUri = "/api/channels/{channelId}".format({"channelId": channelId});
            return this.put(updateUri, amendChannel);
        },

        deleteChannel: function (channelId) {
            var deleteUri = "/api/channels/{channelId}".format({"channelId": channelId});
            return this.delete(deleteUri);
        },

        subscribe: function (channelId) {
            var subscriptionUri = "/api/channels/{channelId}/subscription".format({
                "channelId": channelId
            });
            return this.post(subscriptionUri);
        },

        unsubscribe: function (channelId) {
            var subscriptionUri = "/api/channels/{channelId}/subscription".format({
                "channelId": channelId
            });
            return this.delete(subscriptionUri);
        },

        ownedChannels: function (userId, page, size) {
            var ownedChannelsUri = "/api/channels/owned?userId={userId}&page={page}&size={size}".format({
                "userId": userId,
                "page": page,
                "size": size
            });
            return this.get(ownedChannelsUri);
        },

        subscribedChannels: function (userId, page, size) {
            var subscribedChannelsUri = "/api/channels/subscribed?userId={userId}&page={page}&size={size}".format({
                "userId": userId,
                "page": page,
                "size": size
            });
            return this.get(subscribedChannelsUri);
        }

    });

    angular.module('service.ChannelService', [])
        .provider('ChannelService', function () {
            this.$get = ['$http', '$rootScope', function (http, scope) {
                return new ChannelService(http, scope);
            }];
        });

});