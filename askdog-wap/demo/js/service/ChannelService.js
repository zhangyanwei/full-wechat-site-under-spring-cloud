define([
    'base/BaseService',
    'angular',
    'class'
], function (BaseService) {

    var ChannelService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        getChannels: function(page,size){
            return this.get("/api/users/me/channels/owned",{
                params:{
                    size:size,
                    page:page
                }
            });
        },

        getSubscribes: function(page,size){
            return this.get("/api/users/me/channels/subscribed",{
                params:{
                    size:size,
                    page:page
                }
            });
        },

        getChannelDetail:function(_id){
            return this.get("/api/channels/"+_id);
        },

        getChannelExperience:function(page,size,id){
            return this.get("/api/experiences",{
                params:{
                    channelId:id,
                    page:page,
                    size:size
                }
            })
        },

        getOtherSubscribe:function(page,size,id){
            return this.get("/api/channels/subscribed",{
                params:{
                    userId:id,
                    page:page,
                    size:size
                }
            })
        },

        getOtherChannels:function(page,size,id){
            return this.get("/api/channels/owned",{
                params:{
                    userId:id,
                    page:page,
                    size:size
                }
            })
        },

        createSubscribe:function(id){
            return this.post("/api/channels/{0}/subscription".format(id));
        },

        cancelSubscribe:function(id){
            return this.delete("/api/channels/{0}/subscription".format(id));
        },

        createChannel:function(pureChannel){
            return this.post("/api/channels", pureChannel);
        },

        updateChannel:function(name,description,id){
            var data = {
                name:name,
                description:description
            }
            return this.put("/api/channels/"+id,data);
        },

        deleteChannel: function (channelId) {
            var deleteUri = "/api/channels/{channelId}".format({"channelId": channelId});
            return this.delete(deleteUri);
        },

        getCategories:function(){
            return this.get("/api/categories");
        },

        recommendChannel:function(from,size){
            return this.get('/api/search?type=channel_recommend&from={from}&size={size}'.format({"from":from,'size':size}));
        }

    });

    angular.module('service.ChannelService', [])
        .provider('channelService', function () {
            this.$get = ['$http', '$rootScope', function(http, scope) {
                return new ChannelService(http, scope);
            }];
        });

});