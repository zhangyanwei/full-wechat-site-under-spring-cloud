define(['base/BaseService'], function (BaseService) {

    var ExperienceService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        detail: function (experienceId) {
            var detailUri = "/api/experiences/{experienceId}".format({
                "experienceId": experienceId
            });
            return this.get(detailUri);
        },

        topComments: function (experienceId, page, size) {
            var commentsUri = "/api/comments/firstlevel?experienceId={experienceId}&page={page}&size={size}".format({
                "experienceId": experienceId,
                "page": page,
                "size": size
            });
            return this.get(commentsUri);
        },

        replies: function (commentId) {
            var commentsUri = "/api/comments/secondlevel?commentId={commentId}".format({
                "commentId": commentId
            });
            return this.get(commentsUri);
        },

        create: function (pureExperience) {
            return this.post("/api/experiences", pureExperience);
        },

        update: function (experienceId, amendExperience) {
            var updateUrl = "/api/experiences/{experienceId}".format({
                experienceId: experienceId
            });
            return this.put(updateUrl, amendExperience);
        },

        deleteExperience: function (experienceId) {
            var deleteUri = "/api/experiences/{experienceId}".format({
                "experienceId": experienceId
            });
            return this.delete(deleteUri);
        },

        upVote: function (experienceId) {
            var voteUri = "/api/experiences/{experienceId}/vote?direction=UP".format({
                "experienceId": experienceId
            });
            return this.post(voteUri);
        },

        downVote: function (experienceId) {
            var voteUri = "/api/experiences/{experienceId}/vote?direction=DOWN".format({
                "experienceId": experienceId
            });
            return this.post(voteUri);
        },

        cancelVote: function (experienceId) {
            var voteUri = "/api/experiences/{experienceId}/vote".format({
                "experienceId": experienceId
            });
            return this.delete(voteUri);
        },

        comment: function (experienceId, pureComment) {
            var commentUri = "/api/comments?experienceId={experienceId}".format({
                "experienceId": experienceId
            });
            return this.post(commentUri, pureComment);
        },

        deleteComment: function (commentId) {
            var commentUri = "/api/comments/{commentId}".format({
                "commentId": commentId
            });
            return this.delete(commentUri);
        },

        report: function (experienceId, reportInfo) {
            var reportUri = "/api/experiences/{experienceId}/report".format({
                "experienceId": experienceId
            });
            return this.post(reportUri, reportInfo);
        }


    });

    angular.module('service.ExperienceService', [])
        .provider('ExperienceService', function () {
            this.$get = ['$http', '$rootScope', function (http, scope) {
                return new ExperienceService(http, scope);
            }];
        });

});