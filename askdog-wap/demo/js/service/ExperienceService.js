define([
    'base/BaseService',
    'angular',
    'class'
], function (BaseService) {

    var ExperienceService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        getDetail: function (experienceId) {
            return this.get("/api/experiences/" + experienceId);
        },

        cancelVote: function(experienceId){
            return this.delete("/api/experiences/"+experienceId +'/vote');
        },

        upVote: function(experienceId){
            return this.post("/api/experiences/{0}/vote?direction=UP".format(experienceId));
        },

        downVote: function (experienceId) {
            var voteUri = "/api/experiences/{experienceId}/vote?direction=DOWN".format({
                "experienceId": experienceId
            });
            return this.post(voteUri);
        },

        deleteExperience:function(id){
            return this.delete('/api/experiences/'+id);
        },

        createComment:function(experienceId,pureComment){
            var commentUri = "/api/comments?experienceId={experienceId}".format({
                "experienceId": experienceId
            });
            return this.post(commentUri, pureComment);
        },
        /**
         * Create the preorder for experience.
         * @param experienceId
         * @param {Object}  payRequest - example: {pay_way: 'WXPAY', pay_way_detail: 'JSAPI', title: 'ASKDOG 经验分享平台', product_description: 'Title of Experience'}
         */
        pay: function(experienceId, payRequest) {
            return this.post("/api/experiences/{0}/pay".format(experienceId), payRequest);
        },

        getComment:function(experienceId,page,size){
            return this.get('/api/comments/firstlevel?experienceId={experienceId}&page={page}&size={size}'.format({"experienceId":experienceId,'page':page,'size':size}));
        },

        getReply:function(commentId){
            return this.get('/api/comments/secondlevel?commentId={commentId}'.format({'commentId':commentId}))
        },

        deleteComment: function (commentId) {
            var commentUri = "/api/comments/{commentId}".format({
                "commentId": commentId
            });
            return this.delete(commentUri);
        },

        report:function(experienceId,type,message){
            var data = {
                type:type,
                message: message
            };
            return this.post("/api/experiences/{experienceId}/report".format({'experienceId':experienceId}),data);
        }

    });

    angular.module('service.ExperienceService', [])
        .provider('experienceService', function () {
            this.$get = ['$http', '$rootScope', function(http, scope) {
                return new ExperienceService(http, scope);
            }];
        });

});