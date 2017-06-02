define([
    'base/BaseController',
    'jquery.video',
    'app/directive/adAnalytics',
    'app/directive/mdView',
    'service/ExperienceService',
    'service/SearchService',
    'service/ChannelService',
    'service/OrderService',
    'app/directive/weShare',
    'validate',
    'app/directive/showComment',
    'app/directive/originalTag',
    'app/directive/vipApprove'
], function (BaseController, videojs) {
    var DetailController = BaseController.extend({

        _VIEW_SIZE: 5,
        _EDITOR_ID: 'word-view',
        _EDITOR_VIEW: null,

        _comment_size: 20,

        _VIDEO: null,

        init: function ($rootScope, $scope, $stateParams, $window, $http, $location, _uibModal, $experienceService, $searchService, $channelService, $orderService,$state) {
            this.$rootScope = $rootScope;
            this.$stateParams = $stateParams;
            this.$window = $window;
            this.$http = $http;
            this.$location = $location;
            this.$uibModal = _uibModal;
            this.$experienceService = $experienceService;
            this.$searchService = $searchService;
            this.$channelService = $channelService;
            this.$orderService = $orderService;
            this.$state = $state;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function () {
            var owner = this;
            this._searchRelated(0, owner._VIEW_SIZE, owner.$stateParams.id);
            this.$scope.more = function () {
                owner._searchRelated((owner.$scope.viewList.from + owner._VIEW_SIZE), owner._VIEW_SIZE, owner.$stateParams.id);
            };
            this._getDetail();
            this._subscribeChannel();
            this._cancelSubscribe();
            this._close();
            this._defineVote();
            this._showWeChat();
            this._defineVideoControls();
            this._weChatPay();
            this._showShareButton();
            this._shareDialog();
            this._getComment(0, this._comment_size);
            this._scrollShowMore();
            this._bindValidator();
            this._defineComment();
            this._defineReply();
            this._showDeleteDialog();
            this._showMoreComment();
            this._showReportDialog();
        },

        _getDetail: function () {
            var owner = this;
            owner.$experienceService.getDetail(owner.$stateParams.id)
                .then(function (resp) {
                    owner.$scope.detail = resp.data;
                    owner.$scope.detail.content.price = owner.$scope.detail.price;
                    owner.$scope.detail.content.thumbnail = owner.$scope.detail.thumbnail;
                    if (owner.$scope.user) {
                        owner.$scope.login = true;
                    }
                    owner._showButton();
                    owner.$scope.playNow = true;
                }, function (resp) {
                    if (resp && resp.status == 404) {
                        owner.$scope.noDetail = true;
                    }
                });
        },

        _searchRelated: function (from, size, id) {
            var owner = this;
            owner.$searchService.searchRelated(from, size, id)
                .success(function (data) {
                    owner._searchSuccessHandler(data, from);
                });
        },

        _subscribeChannel: function () {
            var owner = this;
            owner.$scope.subscribeChannel = function () {
                owner.$channelService.createSubscribe(owner.$scope.detail.channel.id)
                    .success(function () {
                        owner.$scope.detail.channel.subscriber_count++;
                        owner.$scope.detail.channel.subscribed = true;
                    });
            }
        },

        _cancelSubscribe: function () {
            var owner = this;
            owner.$scope.cancelSubscribe = function () {
                owner.$channelService.cancelSubscribe(owner.$scope.detail.channel.id)
                    .success(function () {
                        owner.$scope.detail.channel.subscriber_count--;
                        owner.$scope.detail.channel.subscribed = false;
                    });
            }
        },

        _showButton: function () {
            $("input[name=remind-text]").focus(function () {
                $(this).parent('.input-group').hide().next('.form-group').show();
                $("#commentTextarea").focus();
                $(".detail-contain .remind-box .first-media").css('border-bottom', '1px solid #e2e7f2');
            });
        },

        _showShareButton: function () {
            var owner = this;
            owner.$scope.isOpen = false;
            owner.$scope.showRemind = true;
            owner.$scope.showShareButton = function () {
                if (!$("#collapse-share").hasClass("in")) {
                    owner.$scope.isOpen = false;
                } else {
                    owner.$scope.isOpen = true;
                }

            };
            owner.$scope.showRemindButton = function () {
                if (!$("#collapse-remind").hasClass('in')) {
                    $("#collapse-remind").addClass('in');
                    owner.$scope.showRemind = false;
                    if ($("#collapse-remind").height() < $(window).height()) {
                        var height = $("#collapse-remind").height();
                        $(document).scrollTop($(document).scrollTop() + height);
                    } else {
                        $(document).scrollTop($(document).scrollTop() + $(window).height() - 100);
                    }
                    owner.$scope.$broadcast('remindShow');
                } else {
                    $("#collapse-remind").removeClass('in');
                    owner.$scope.showRemind = true;
                }
            }
        },
        _searchSuccessHandler: function (data, from) {
            var lastList = this.$scope.viewList || {};
            lastList.from = from;
            lastList.total = data.total;
            lastList.last = data.last;
            if (this.$scope.viewList && this.$scope.viewList.result) {
                lastList.result = this.$scope.viewList.result;
            } else {
                lastList.result = [];
            }
            for (var index = 0; index < data.result.length; index++) {
                lastList.result.push(data.result[index]);
            }
            if (data.result.length == 0) {
                lastList.last = true;
            }
            this.$scope.viewList = lastList;
        },

        _close: function () {
            var owner = this;
            owner.$scope.close = function () {
                owner._closeInput();
            }
        },

        _closeInput: function () {
            $("input[name=remind-text]").parent('.input-group').show().next('.form-group').hide();
            $(".detail-contain .remind-box .first-media").css('border-bottom', 'none');
        },

        _defineComment: function () {
            var owner = this;
            owner.$scope.pureComment = {};
            this.$scope.addComment = function (experienceId, replyCommentId) {
                var pureComment = {};
                if (replyCommentId) {
                    pureComment.content = owner.$scope.pureComment.reply_content;
                    pureComment.reply_comment_id = replyCommentId;
                } else {
                    pureComment.content = owner.$scope.pureComment.content;
                }
                owner.$experienceService.createComment(experienceId, pureComment).then(
                    function (resp) {
                        var commentList = owner.$scope.view.result;
                        owner.$scope.pureComment.content = "";
                        owner.$scope.pureComment.reply_content = "";
                        owner.$scope.view.total++;
                        owner._closeInput();

                        resp.data.comments = {
                            result: [],
                            last: true,
                            total: 0
                        };

                        if (!replyCommentId) {
                            commentList.unshift(resp.data);
                        } else if (replyCommentId) {
                            $("#reply-modal").modal("hide");
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
                    }
                );
            }
        },

        _defineReply: function () {
            var owner = this;
            this.$scope.reply = function (comment) {
                var shareModel = owner.$uibModal.open({
                    windowTemplateUrl: 'views/base/modal-window.html',
                    windowTopClass: 'reply-modal',
                    templateUrl: 'views/dialog/replyDialog.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.comment = comment;
                        $scope.detail = owner.$scope.detail;
                        $scope.$uibModalInstance = $uibModalInstance;
                        $scope.viewList = owner.$scope.view;
                    }]
                });
            };
        },

        _bindValidator: function () {
            var owner = this;
            this.$scope.bindValidator = function (element) {
                $(element).validate({
                    rules: {
                        commentText: {
                            required: true,
                            maxlength: 200
                        }
                    },
                    messages: {
                        commentText: {
                            required: '',
                            maxlength: '评论内容不能超过200字'
                        }
                    },
                    submitHandler: function () {
                    }
                });
            }
        },

        _defineVote: function () {
            var owner = this;
            this.$scope.upVote = function () {
                if (!owner.$scope.detail.up_voted) {
                    owner.$experienceService.upVote(owner.$stateParams.id).then(
                        function (resp) {
                            owner.$scope.detail.up_voted = true;
                            owner.$scope.detail.down_voted = false;
                            owner.$scope.detail.up_vote_count = resp.data.up_vote_count;
                            owner.$scope.detail.down_vote_count = resp.data.down_vote_count;
                        }
                    );
                } else {
                    owner.$scope.cancelVote();
                }
            };
            this.$scope.downVote = function () {
                if (!owner.$scope.detail.down_voted) {
                    owner.$experienceService.downVote(owner.$stateParams.id).then(
                        function (resp) {

                            owner.$scope.detail.up_voted = false;
                            owner.$scope.detail.down_voted = true;
                            owner.$scope.detail.up_vote_count = resp.data.up_vote_count;
                            owner.$scope.detail.down_vote_count = resp.data.down_vote_count;
                        }
                    );
                } else {
                    owner.$scope.cancelVote();
                }
            };
            this.$scope.cancelVote = function () {
                owner.$experienceService.cancelVote(owner.$stateParams.id).then(
                    function (resp) {
                        owner.$scope.detail.up_voted = false;
                        owner.$scope.detail.down_voted = false;
                        owner.$scope.detail.up_vote_count = resp.data.up_vote_count;
                        owner.$scope.detail.down_vote_count = resp.data.down_vote_count;
                    }
                );
            };
        },

        _showWeChat: function () {
            var owner = this;
            owner.$scope.showWeChat = function () {
                $("#shareToWeChat").modal("show");
                $("#shareTo").modal("hide");
            }
        },

        //#video play controller
        _defineVideoControls: function () {
            var owner = this;
            this.$scope.videoInitialized = false;
            this.$scope.play = function () {
                if (owner.$scope.detail.mine || owner.$scope.detail.price == 0) {
                    owner._videoPlay();
                } else {
                    owner._videoPayStatus();
                }
            }
        },

        _videoPlay: function () {
            if (!this._VIDEO) {
                this._initVideo();
                this.$scope.videoInitialized = true;
            }

        },

        _initVideo: function () {
            var imagePoster = $('#experience-view #video-poster');
            var videoUrl = '';
            var experience = this.$scope.detail.content;
            if (experience.transcode_videos && experience.transcode_videos.length > 0) {
                videoUrl = experience.transcode_videos[0].url;
            } else if (experience.source && experience.source.url) {
                videoUrl = experience.source.url;
            }
            var videoPlayerHtml = '<video id="video-player" src="' + videoUrl + '" class="video-js vjs-default-skin vjs-big-play-centered"  poster="' + this.$scope.detail.thumbnail + '" width="100%" height="207px"></video>';
            $(imagePoster).replaceWith(videoPlayerHtml);
            var videoPlayer = $('#experience-view #video-player')[0];
            this._VIDEO = videojs(
                videoPlayer,
                {
                    autoplay: false,
                    loop: false,
                    techOrder: ["html5", "flash"],
                    controls: true
                });
            this._VIDEO.play();
        },

        _videoPayStatus: function () {
            var owner = this;
            this.$orderService.getPayStatus(this.$scope.detail.id).then(function (resp) {
                owner.$scope.status = resp.data;
                if (owner.$scope.status.pay_status == 'PAID' || owner.$scope.status.pay_status == 'FREE') {
                    owner._videoPlay();
                } else {
                    owner.$scope.notPay = true;
                    $("#experience-view img").remove();
                }
            });
        },

        _weChatPay: function () {
            var owner = this;
            owner.$scope.weChatPay = function (id) {
                var userAgent = owner.$window.navigator.userAgent;
                var matches = /MicroMessenger\/([^\s\/.]+)/.exec(userAgent);
                if (matches && matches.length > 1) {
                    var version = matches[1];
                    if (version >= 5) {
                        window.location = '/pay/pay.html?id=' + id;
                    }
                } else {
                    var messageModal = owner.$uibModal.open({
                        windowTemplateUrl: 'views/base/modal-window.html',
                        windowTopClass: 'out-Modal',
                        templateUrl: 'views/dialog/messageDialog.html',
                        controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                            $scope.$uibModalInstance = $uibModalInstance;
                            $scope.message = "请到web端支付";
                        }]
                    });
                }

            }
        },

        _shareDialog: function () {
            var owner = this;
            owner.$scope.shareDialog = function () {
                var shareModel = owner.$uibModal.open({
                    windowTemplateUrl: 'views/base/modal-window.html',
                    windowTopClass: 'to-share-modal',
                    templateUrl: 'views/dialog/share.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.detail = owner.$scope.detail;
                        $scope.$uibModalInstance = $uibModalInstance;
                    }]
                });
            }
        },

        _getComment: function (page, size) {
            var owner = this;
            owner.$scope.loadingCompleted = false;
            owner.$experienceService.getComment(owner.$stateParams.id, page, size)
                .success(function (data) {
                    owner.$scope.loadingCompleted = true;
                    owner._commentHandler(data, page);
                });
            owner.$scope.showReply = function (id) {
                var commentList = owner.$scope.view.result;
                owner.$experienceService.getReply(id)
                    .success(function (data) {
                        for (var i = 0; i < commentList.length; i++) {
                            var currentItem = commentList[i];
                            if (currentItem.id == id) {
                                currentItem.comments.result = data;
                                currentItem.comments.last = true;
                                currentItem.comments.total = data.length;
                                return;
                            }
                        }
                    });

            };
            owner.$scope.closeReplyList = function (id) {
                var commentList = owner.$scope.view.result;
                for (var i = 0; i < commentList.length; i++) {
                    var currentItem = commentList[i];
                    if (currentItem.id == id) {
                        var comments = currentItem.comments.result;
                        currentItem.comments.result = [];
                        currentItem.comments.result.push(comments[0]);
                        currentItem.comments.result.push(comments[1]);
                        currentItem.comments.last = false;
                        return;
                    }
                }
            }
        },

        _scrollShowMore: function () {
            var owner = this;
            owner.$scope.$on('remindShow', function () {
                window.addEventListener("scroll", function () {
                    if (!owner.$scope.loadingCompleted) {
                        return;
                    }
                    if ($(document).scrollTop() >= $(document).height() - $(window).height() && !owner.$scope.view.last) {
                        owner._getComment((owner.$scope.view.page + 1), owner._comment_size);
                    }
                });
            });
        },

        _commentHandler: function (data) {
            var lastList = {result: [], total: 0, page: 0, size: this._comment_size, last: true};
            if (data) {
                if (data.page != 0) {
                    lastList = this.$scope.view;
                }
                for (var index = 0; index < data.result.length; index++) {
                    lastList.result.push(data.result[index]);
                }
                lastList.total = data.total;
                lastList.last = data.last;
                lastList.size = data.size;
                lastList.page = data.page;
            }
            this.$scope.view = lastList;
        },

        _showDeleteDialog: function () {
            var owner = this;
            owner.$scope.showDeleteDialog = function (comment) {
                if(comment.reply_comment_id){
                    owner.$scope.deleteReply = true;
                }else {
                    owner.$scope.deleteReply = false;
                }
                var confirmModel = owner.$uibModal.open({
                    windowTemplateUrl: 'views/base/modal-window.html',
                    windowTopClass: 'delete-channel-modal',
                    templateUrl: 'views/dialog/confirm.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                        $scope.confirmInfo = owner.$scope.deleteReply;
                    }]
                });
                confirmModel.result.then(
                    function () {
                        owner.$experienceService.deleteComment(comment.id).then(function () {
                            if(!comment.reply_comment_id){
                                owner.$scope.view.total -- ;
                            }
                            var successModal = owner.$uibModal.open({
                                windowTemplateUrl: 'views/base/modal-window.html',
                                windowTopClass: 'status-modal',
                                templateUrl: 'views/dialog/success.html',
                                controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                                    $scope.$uibModalInstance = $uibModalInstance;
                                    $scope.message = '删除成功';
                                }]
                            });
                            var commentList = owner.$scope.view.result;
                            for (var i = 0; i < commentList.length; i++) {
                                var currentItem = commentList[i];
                                var replyList = currentItem.comments.result;
                                if (currentItem.id == comment.id) {
                                    currentItem.deleted = true;
                                    return;
                                }
                                for (var j = 0; j < replyList.length; j++) {
                                    var currentReplyItem = replyList[j];
                                    if (currentReplyItem.id == comment.id) {
                                        owner.$scope.showReply(currentItem.id);
                                        return;
                                    }
                                }
                            }
                        });
                    },
                    function () {
                    }
                );
            }
        },

        _showMoreComment:function(){
            var owner = this;
            owner.$scope.showMoreComment = function(e){
                $(e.target).prev().css('display','block');
                $(e.target).addClass("hidden");
                $(e.target).next().removeClass("hidden");
            };
            owner.$scope.hideComment = function(e){
                $(e.target).prev().prev().css('display','-webkit-box');
                $(e.target).addClass("hidden");
                $(e.target).prev().removeClass("hidden");
            }
        },

        _showReportDialog:function(){
            var owner = this;
            owner.$scope.showReportDialog = function(experienceId){
                var bottomModal = owner.$uibModal.open({
                    windowTemplateUrl: 'views/base/modal-window.html',
                    windowTopClass: 'delete-channel-modal',
                    templateUrl: 'views/dialog/bottomDialog.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                        $scope.message = '举报';
                    }]
                });
                bottomModal.result.then(function(){
                    owner.$state.go("view.report",{"reportId":experienceId});
                });
            }
        }
    });


    DetailController.$inject = ['$rootScope', '$scope', '$stateParams', '$window', '$http', '$location', '$uibModal', 'experienceService', 'searchService', 'channelService', 'orderService','$state'];
    angular.module('module.detail.DetailController', ['app.directive.weShare', 'service.ExperienceService', 'service.SearchService', 'service.ChannelService', 'service.OrderService']).controller('DetailController', DetailController);

});