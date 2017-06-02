define([
    'base/BaseController',
    'service/ExperienceService'
], function (BaseController) {
    var ReplyDialogController = BaseController.extend({

        init: function ($scope, $stateParams, $uibModal,$experienceService) {
            this.$stateParams = $stateParams;
            this.$uibModal = $uibModal;
            this.$comment = $scope.$parent.comment;
            this.$detail = $scope.$parent.detail;
            this.$viewList = $scope.$parent.viewList;
            this.$uibModalInstance = $scope.$parent.$uibModalInstance;
            this.$experienceService = $experienceService;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function () {
            this._defineClose();
            this._defineCancel();
            this._defineValidator();
            this._defineAddReply();
        },

        _defineClose:function(){
            var owner = this;
            this.$scope.close = function () {
                owner.$uibModalInstance.close();
            };
        },

        _defineCancel:function(){
            var owner = this;
            this.$scope.cancel = function () {
                owner.$uibModalInstance.dismiss("cancel");
            };
        },

        _defineValidator:function(){
            var owner = this;
            this.$scope.bindValidatorReply = function (element) {
                $(element).validate({
                    rules: {
                        ReplyText: {
                            required: true,
                            maxlength: 200
                        }
                    },
                    messages: {
                        ReplyText: {
                            required: '',
                            maxlength: '回复内容不能超过200字'
                        }
                    },
                    submitHandler: function () {
                        owner.$scope.addReply();

                    }
                });
            }
        },

        _defineAddReply: function () {
            var owner = this;
            var replyCommentId = this.$comment.id;
            var experienceId = this.$detail.id;
            owner.$scope.addReply = function () {
                owner.$uibModalInstance.dismiss("cancel");
                var pureComment = {};
                pureComment.content = owner.$scope.pureComment.reply_content;
                pureComment.reply_comment_id = replyCommentId;
                owner.$experienceService.createComment(experienceId, pureComment).then(
                    function (resp) {
                        var commentList = owner.$viewList.result;
                        owner.$scope.pureComment.reply_content = "";
                        resp.data.comments = {
                            result: [],
                            last: true,
                            total: 0
                        };
                        for (var i = 0; i < commentList.length; i++) {
                            var currentItem = commentList[i];
                            var replyList = currentItem.comments.result;
                            if (currentItem.id == replyCommentId) {
                                replyList.push(resp.data);
                                currentItem.comments.total = currentItem.comments.total + 1;
                                return;
                            }
                            for (var j = 0; j < replyList.length; j++) {
                                if (replyList[j].id == replyCommentId) {
                                    replyList.push(resp.data);
                                    currentItem.comments.total = currentItem.comments.total + 1;
                                    return;
                                }
                            }
                        }
                    }
                );
            }
        },

    });

    ReplyDialogController.$inject = ['$scope', '$stateParams', '$uibModal','experienceService'];

    angular.module('module.dialog.ReplyDialogController', ['service.ExperienceService']).controller('ReplyDialogController', ReplyDialogController);

});