define([
    'base/BaseService',
    'angular',
    'class'
], function (BaseService) {

    var SearchService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        homeSearch:function(from,size){
            return this.get("/api/search", {
                params: {
                    from: from,
                    size: size,
                    type: 'experience_hot'
                }

            });
        },

        searchAll: function(from,size,key){
            return this.get("/api/search",{
                params:{
                    type:'experience_search',
                    key:key,
                    from:from,
                    size:size
                }
            })
        },
        
        searchRelated:function(from,size,id){
            return this.get("/api/search",{
                params:{
                    from:from,
                    size:size,
                    type:'experience_related',
                    experienceId:id
                }
            })
        },

        searchSimilar:function(key){
            return this.get("/api/search",{
               params:{
                   type:'query_similar',
                   key:key
               }
            });
        },

        categorySearch: function (categoryCode, from, size) {
            var searchUrl = "/api/search?type=experience_category&categoryCode={categoryCode}&from={from}&size={size}".format({
                "categoryCode": categoryCode,
                "from": from,
                "size": size
            });
            return this.get(searchUrl)
        },

        subscribeUnread:function(from,size){
            return this.get("/api/channels/subscribed/unread?from={from}&size={size}".format({"from":from,"size":size}));
        }

    });

    angular.module('service.SearchService', [])
        .provider('searchService', function () {
            this.$get = ['$http', '$rootScope', function(http, scope) {
                return new SearchService(http, scope);
            }];
        });

});