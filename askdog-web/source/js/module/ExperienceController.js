define([
    'base/BaseController',
    'jquery.video',
    'app/directive/adAnalytics',
    'app/directive/mdView',
    'jquery.validator',
    'service/ExperienceService',
    'service/ChannelService',
    'service/SearchService',
    'service/OrderService'
], function (BaseController, videojs) {

    var ExperienceController = BaseController.extend({

        _WIDTH: 200,
        _HEIGHT: 200,
        _ORDER_TOKEN_LINK: AskDog.ApiUtil.apiUrl("/api/orders/{orderId}/wxqrcode?width={width}&height={height}&stamp={stamp}"),

        _VIDEO: null,
        _COMMENT_SIZE: 20,
        _RELATED_SIZE: 4,

        init: function (_rootScope, _scope, _stateParams, _uibModal, _state, _experienceService, _channelService, _searchService, _orderService) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$uibModal = _uibModal;
            this.$state = _state;

            this.$experienceService = _experienceService;
            this.$channelService = _channelService;
            this.$searchService = _searchService;
            this.$orderService = _orderService;
            this._super(_scope);
        },

        defineScope: function () {
            this._loadData();
            this._defineCommentPaging();
            this._defineVideoControls();
            this._defineGenerateQRCode();
            this._defineSubscription();
            this._defineVote();
            this._defineShare();
            this._defineComment();
            this._defineEdit();
            this._defineReport();
        },

        defineListeners: function () {
            var owner = this;
            this._contextReadyListener = this.$rootScope.$on('contextReady', function (event, userSelf) {
                if (userSelf) {
                    owner._loadData();
                }
            });
        },

        destroy: function () {
            this._contextReadyListener();
            this._contextReadyListener = null;
        },

        _loadData: function () {
            var owner = this;
            //Experience detail info data.
            this._loadDetailData();

            //Experience related
            this._experienceRelated(0, this._RELATED_SIZE);
            this.$scope.experienceRelatedPaging = function () {
                owner._experienceRelated((owner.$scope.experienceRelatedList.from + owner._RELATED_SIZE), owner._RELATED_SIZE);
            };
        },

        _loadDetailData: function () {
            var owner = this;
            this.$experienceService.detail(this.$stateParams.experienceId).then(
                function (resp) {
                    owner.$scope.experienceDetail = resp.data;
                    owner.$scope.experienceDetail.content.price = owner.$scope.experienceDetail.price;
                    owner.$scope.experienceDetail.content.thumbnail = owner.$scope.experienceDetail.thumbnail;

                    owner._loadCommentsData(0);
                },
                function (resp) {
                    if (resp && resp.status == 404) {
                        owner.$scope.notFound = true;
                    }
                }
            );
        },

        _defineCommentPaging: function () {
            var owner = this;
            this.$scope.commentPaging = function () {
                owner._loadCommentsData(owner.$scope.commentList.page + 1);
            };
        },

        _loadCommentsData: function (page) {
            var owner = this;
            owner.$scope.pagingCompleted = false;
            this.$experienceService.topComments(this.$scope.experienceDetail.id, page, this._COMMENT_SIZE).then(
                function (resp) {
                    owner._loadCommentsSuccessHandler(resp.data);
                    owner.$scope.pagingCompleted = true;
                }, function () {
                }
            );
        },

        _loadCommentsSuccessHandler: function (data) {
            var lastList = {result: [], total: 0, page: 0, size: this._COMMENT_SIZE, last: true};
            if (data) {
                if (data.page != 0) {
                    lastList = this.$scope.commentList;
                }
                for (var index = 0; index < data.result.length; index++) {
                    lastList.result.push(data.result[index]);
                }
                lastList.total = data.total;
                lastList.last = data.last;
                lastList.size = data.size;
                lastList.page = data.page;
            }
            this.$scope.commentList = lastList;
        },

        _experienceRelated: function (from, size) {
            var owner = this;
            this.$searchService.experienceRelated(this.$stateParams.experienceId, from, size).then(
                function (resp) {
                    owner._experienceRelatedSuccessHandler(resp.data, from);
                }
            );
        },

        _experienceRelatedSuccessHandler: function (data, from) {
            var lastList = data;
            lastList.from = from;
            if (data.result.length == 0) {
                lastList.last = true;
            }
            if (lastList.last) {
                lastList.last = false;
                lastList.from = -this._RELATED_SIZE;
            }
            this.$scope.experienceRelatedList = lastList;
        },

        //#video play controller
        _defineVideoControls: function () {
            var owner = this;
            this.$scope.videoInitialized = false;
            this.$scope.play = function () {
                owner.$scope.order_token_link = undefined;
                if (owner.$scope.experienceDetail.mine || owner.$scope.experienceDetail.price == 0) {
                    owner._videoPlay();
                } else {
                    owner._videoPayStatus();
                }
            }
        },

        _videoPayStatus: function () {
            var owner = this;
            //TODO Multi payment platform
            var orderInfo = {
                "pay_way": "WXPAY",
                "pay_way_detail": "NATIVE",
                "title": "ASKDOG 经验分享平台",
                "product_description": this.$scope.experienceDetail.subject
            };
            this.$orderService.getOrder(this.$scope.experienceDetail.id, orderInfo).then(
                function (resp) {
                    var order = resp.data;
                    owner.$scope.order = order;
                    if (order.pay_status == 'PREPAY') {
                        owner._genderQRCode();
                    } else if (order.pay_status == 'PAID' || order.pay_status == 'FREE') {
                        owner._videoPlay();
                    }
                },
                function (resp) {
                    if (resp && resp.status == 403) {
                        owner.$rootScope.signIn();
                    }
                }
            );
        },

        _defineGenerateQRCode: function () {
            var owner = this;
            this.$scope.generateQRCode = function () {
                owner._genderQRCode();
            };
        },

        _genderQRCode: function () {
            this.$scope.order_token_link = this._ORDER_TOKEN_LINK.format({
                "orderId": this.$scope.order.order_id,
                "width": this._WIDTH,
                "height": this._HEIGHT,
                "stamp": new Date().getTime()
            });
        },

        _videoPlay: function () {
            if (!this._VIDEO) {
                this._initVideo();
                this.$scope.videoInitialized = true;
            }
            this._VIDEO.play();
        },

        _initVideo: function () {
            var owner = this;
            var videoPlayer = $('#experience-view #video-player')[0];
            var videoUrl = '';
            var experience = this.$scope.experienceDetail.content;
            if (experience.transcode_videos && experience.transcode_videos.length > 0) {
                videoUrl = experience.transcode_videos[0].url;
            } else if (experience.source && experience.source.url) {
                videoUrl = experience.source.url;
            }
            $(videoPlayer).attr("src", videoUrl);
            this._VIDEO = videojs(
                videoPlayer,
                {
                    autoplay: false,
                    loop: false,
                    techOrder: ["html5", "flash"],
                    controls: true
                },
                function () {
                    this.on('play', function () {
                        owner.$scope.paused = false;
                        owner.$scope.$digest();
                    });
                    this.on('pause', function () {
                        owner.$scope.paused = true;
                        owner.$scope.$digest();
                    });
                    this.on('ended', function () {
                        owner.$scope.paused = true;
                        owner.$scope.$digest();
                    });
                });

        },

        _defineSubscription: function () {
            var owner = this;
            this.$scope.subscribe = function () {
                if (!owner.$scope.experienceDetail.channel.subscribed) {
                    owner.$channelService.subscribe(owner.$scope.experienceDetail.channel.id).then(
                        function () {
                            owner.$scope.experienceDetail.channel.subscribed = true;
                            owner.$scope.experienceDetail.channel.subscriber_count++;
                            owner.$rootScope.$broadcast('sidebarRefresh');
                        }
                    )
                } else {
                    owner.$channelService.unsubscribe(owner.$scope.experienceDetail.channel.id).then(
                        function () {
                            owner.$scope.experienceDetail.channel.subscribed = false;
                            owner.$scope.experienceDetail.channel.subscriber_count--;
                            owner.$rootScope.$broadcast('sidebarRefresh');
                        }
                    )
                }
            };
        },

        _defineVote: function () {
            var owner = this;
            this.$scope.upVote = function () {
                if (!owner.$scope.experienceDetail.up_voted) {
                    owner.$experienceService.upVote(owner.$stateParams.experienceId).then(
                        function (resp) {
                            owner.$scope.experienceDetail.up_voted = true;
                            owner.$scope.experienceDetail.down_voted = false;
                            owner.$scope.experienceDetail.up_vote_count = resp.data.up_vote_count;
                            owner.$scope.experienceDetail.down_vote_count = resp.data.down_vote_count;
                        }
                    );
                } else {
                    owner.$scope.cancelVote();
                }
            };
            this.$scope.downVote = function () {
                if (!owner.$scope.experienceDetail.down_voted) {
                    owner.$experienceService.downVote(owner.$stateParams.experienceId).then(
                        function (resp) {
                            owner.$scope.experienceDetail.up_voted = false;
                            owner.$scope.experienceDetail.down_voted = true;
                            owner.$scope.experienceDetail.up_vote_count = resp.data.up_vote_count;
                            owner.$scope.experienceDetail.down_vote_count = resp.data.down_vote_count;
                        }
                    );
                } else {
                    owner.$scope.cancelVote();
                }
            };
            this.$scope.cancelVote = function () {
                owner.$experienceService.cancelVote(owner.$stateParams.experienceId).then(
                    function (resp) {
                        owner.$scope.experienceDetail.up_voted = false;
                        owner.$scope.experienceDetail.down_voted = false;
                        owner.$scope.experienceDetail.up_vote_count = resp.data.up_vote_count;
                        owner.$scope.experienceDetail.down_vote_count = resp.data.down_vote_count;
                    }
                );
            };
        },

        _defineShare: function () {
            var owner = this;
            this.$scope.share = function () {
                var shareModel = owner.$uibModal.open({
                    windowTemplateUrl: 'views/base/modal-window.html',
                    windowTopClass: 'to-share-modal',
                    templateUrl: 'views/dialog/share.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.experienceDetail = owner.$scope.experienceDetail;
                        $scope.$uibModalInstance = $uibModalInstance;
                    }]
                });
                shareModel.result.then(
                    function () {
                        //ok callback;
                    },
                    function () {
                        //cancel callback;
                    }
                );
            };
        },

        _defineComment: function () {
            var owner = this;
            owner.$scope.pureComment = {};
            this.$scope.comment = function (experienceId, replyCommentId) {
                var pureComment = {};
                if (replyCommentId) {
                    pureComment.content = owner.$scope.pureComment.reply_content;
                    pureComment.reply_comment_id = replyCommentId;
                } else {
                    pureComment.content = owner.$scope.pureComment.content;
                }
                owner.$experienceService.comment(experienceId, pureComment).then(
                    function (resp) {
                        owner.$scope.commentCancel();
                        if (replyCommentId) {
                            owner.$scope.replyCancel(replyCommentId);
                        }

                        var commentList = owner.$scope.commentList.result;
                        //TODO
                        resp.data.comments = {
                            result: [],
                            last: true,
                            total: 0
                        };

                        if (!replyCommentId) {
                            commentList.unshift(resp.data);
                        } else if (replyCommentId) {
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
            };

            this.$scope.openReplyList = function (commentId) {
                var commentList = owner.$scope.commentList.result;
                owner.$experienceService.replies(commentId).then(
                    function (resp) {
                        for (var i = 0; i < commentList.length; i++) {
                            var currentItem = commentList[i];
                            if (currentItem.id == commentId) {
                                currentItem.comments.result = resp.data;
                                currentItem.comments.last = true;
                                currentItem.comments.total = resp.data.length;
                                return;
                            }
                        }
                    }
                );
            };

            this.$scope.closeReplyList = function (replyCommentId) {
                var commentList = owner.$scope.commentList.result;
                for (var i = 0; i < commentList.length; i++) {
                    var currentItem = commentList[i];
                    if (currentItem.id == replyCommentId) {
                        var comments = currentItem.comments.result;
                        currentItem.comments.result = [];
                        currentItem.comments.result.push(comments[0]);
                        currentItem.comments.result.push(comments[1]);
                        currentItem.comments.last = false;
                        return;
                    }
                }
            };

            this.$scope.reply = function (replyCommentId) {
                $('.reply-container').addClass('hidden');
                var replyContainer = $("div[reply-container-id='" + replyCommentId + "']");
                replyContainer.removeClass('hidden');
                owner.$scope.pureComment.reply_content = "";
            };

            this.$scope.deleteReply = function (commentId) {
                owner.$experienceService.deleteComment(commentId).then(
                    function () {
                        var commentList = owner.$scope.commentList.result;
                        for (var i = 0; i < commentList.length; i++) {
                            var currentItem = commentList[i];
                            var replyList = currentItem.comments.result;
                            if (currentItem.id == commentId) {
                                currentItem.deleted = true;
                                return;
                            }
                            for (var j = 0; j < replyList.length; j++) {
                                var currentReplyItem = replyList[j];
                                if (currentReplyItem.id == commentId) {
                                    owner.$scope.openReplyList(currentItem.id);
                                    return;
                                }
                            }
                        }
                    }
                );
            };

            this.$scope.commentCancel = function () {
                $('form[name="commentForm"] textarea').trigger("resetHeight");
                owner.$scope.pureComment.content = "";
            };

            this.$scope.replyCancel = function (replyCommentId) {
                var replyContainer = $("div[reply-container-id='" + replyCommentId + "']");
                replyContainer.addClass('hidden');
                $("div[reply-container-id='" + replyCommentId + "']  textarea").trigger("resetHeight");
                owner.$scope.pureComment.reply_content = "";
            };

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
                            required: "",
                            maxlength: "评论内容不能超过200字"
                        }
                    },
                    submitHandler: function () {
                    }
                });
            };
        },

        _defineEdit: function () {
            var owner = this;
            this.$scope.edit = function () {
                if (owner.$scope.experienceDetail.content.type == "TEXT") {
                    owner.$state.go('layout.view.share-word', {expId: owner.$scope.experienceDetail.id});
                } else if (owner.$scope.experienceDetail.content.type == "VIDEO") {
                    owner.$state.go('layout.view.share-video', {expId: owner.$scope.experienceDetail.id});
                }
            };
        },

        _defineReport: function () {
            var owner = this;
            this.$scope.report = function () {
                var reportModel = owner.$uibModal.open({
                    windowTemplateUrl: 'views/base/modal-window.html',
                    windowTopClass: 'to-share-modal',
                    templateUrl: 'views/dialog/report.html',
                    size: 'report',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.experienceId = owner.$scope.experienceDetail.id;
                        $scope.$uibModalInstance = $uibModalInstance;
                    }]
                });
                reportModel.result.then(
                    function () {
                        //ok callback;
                    },
                    function () {
                        //cancel callback;
                    }
                );
            };
        }

    });

    ExperienceController.$inject = ['$rootScope', '$scope', '$stateParams', '$uibModal', '$state', 'ExperienceService', 'ChannelService', 'SearchService', 'OrderService'];

    angular.module('module.ExperienceController', ['service.ExperienceService', 'service.ChannelService', 'service.SearchService', 'service.OrderService']).controller('ExperienceController', ExperienceController);

});