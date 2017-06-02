define(['base/BaseService'], function (BaseService) {

    var SearchService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        keySearch: function (key, from, size) {
            var searchUrl = "/api/search?type=experience_search&key={key}&from={from}&size={size}".format({
                "key": key,
                "from": from,
                "size": size
            });
            return this.get(searchUrl)
        },

        querySimilar: function (key) {
            var searchUrl = "/api/search?type=query_similar&key={key}".format({
                "key": key
            });
            return this.get(searchUrl)
        },

        homeSearch: function (from, size) {
            var searchUrl = "/api/search?type=homeSearch&from={from}&size={size}".format({
                "from": from,
                "size": size
            });
            return this.get(searchUrl)
        },

        hotSearch: function (from, size) {
            var searchUrl = "/api/search?type=experience_hot&from={from}&size={size}".format({
                "from": from,
                "size": size
            });
            return this.get(searchUrl)
        },

        categorySearch: function (categoryCode, from, size) {
            var searchUrl = "/api/search?type=experience_category&categoryCode={categoryCode}&from={from}&size={size}".format({
                "categoryCode": categoryCode,
                "from": from,
                "size": size
            });
            return this.get(searchUrl)
        },

        channelRecommend: function (from, size) {
            var searchUrl = "/api/search?type=channel_recommend&from={from}&size={size}".format({
                "from": from,
                "size": size
            });
            return this.get(searchUrl)
        },

        userRecommend: function (from, size) {
            var searchUrl = "/api/search?type=user_recommend&from={from}&size={size}".format({
                "from": from,
                "size": size
            });
            return this.get(searchUrl)
        },


        channelRecommendWithCategory: function (from, size, code) {
            var searchUrl = "/api/search?type=channel_recommend&from={from}&size={size}&code={code}".format({
                "from": from,
                "size": size,
                "code": code
            });
            return this.get(searchUrl)
        },

        userRecommendWithCategory: function (from, size, code) {
            var searchUrl = "/api/search?type=user_recommend&from={from}&size={size}&code={code}".format({
                "from": from,
                "size": size,
                "code": code
            });
            return this.get(searchUrl)
        },


        experienceRecommend: function (from, size) {
            var searchUrl = "/api/search?type=experience_recommend&from={from}&size={size}".format({
                "from": from,
                "size": size
            });
            return this.get(searchUrl)
        },

        experienceRelated: function (experienceId, from, size) {
            var searchUrl = "/api/search?type=experience_related&experienceId={experienceId}&from={from}&size={size}".format({
                "experienceId": experienceId,
                "from": from,
                "size": size
            });
            return this.get(searchUrl)
        },

        channelRelated: function (channelId, from, size) {
            var searchUrl = "/api/search?type=channel_related&channelId={channelId}&from={from}&size={size}".format({
                "channelId": channelId,
                "from": from,
                "size": size
            });
            return this.get(searchUrl)
        }

    });

    angular.module('service.SearchService', [])
        .provider('SearchService', function () {
            this.$get = ['$http', '$rootScope', function (http, scope) {
                return new SearchService(http, scope);
            }];
        });

});