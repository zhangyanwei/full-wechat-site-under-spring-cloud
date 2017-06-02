define([
    'base/BaseController',
    'app/directive/adAnalytics',
    'service/UserService',
    'validate'
], function (BaseController) {
    var NoticeController = BaseController.extend({

        _view_size: 20,

        init: function ($scope, $stateParams, $userService, $rootScope, $location) {
            this.$stateParams = $stateParams;
            this.$userService = $userService;
            this.$rootScope = $rootScope;
            this.$location = $location;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function () {
            var owner = this;
            this._scrollShowMore();
            if (this.$location.$$path.startWith('/notice')) {
                owner.$scope.notNotice = true;
            }
            owner._defineGetNotice(0, owner._view_size);
        },

        defineListeners: function () {
            var owner = this;
            this._contextReadyListener = this.$rootScope.$on('contextReady', function (event, userSelf) {
                if (userSelf && !owner.$scope.viewList) {
                    owner._defineGetNotice(0, owner._view_size);
                }
            });
        },

        _defineGetNotice: function (page, size) {
            var owner = this;
            this.$scope.loadingCompleted = false;
            owner.$userService.getNotice(page, size)
                .then(function (resp) {
                    owner._loadNotificationsSuccessHandler(resp.data);
                    owner.$scope.loadingCompleted = true;
                });
        },

        _loadNotificationsSuccessHandler: function (data) {
            var lastList = {result: [], total: 0, page: 0, size: this._view_size, last: true};
            if (data) {
                if (data.page != 0) {
                    lastList = this.$scope.viewList;
                }
                this._updateNotificationsData(data, lastList);
                lastList.total = data.total;
                lastList.last = data.last;
                lastList.size = data.size;
                lastList.page = data.page;
            }
            this.$scope.viewList = lastList;
        },

        _updateNotificationsData: function (data, lastList) {
            var emptyList = lastList.result.length == 0;
            for (var i = 0; i < data.result.length; i++) {
                var resultItem = data.result[i];
                for (var j = 0; j < resultItem.group_data.length; j++) {
                    var groupItem = resultItem.group_data[j];
                    switch (groupItem.content.type) {
                        case 'CREATE_EXPERIENCE_COMMENT':
                            groupItem.content.link = '#/exp/' + groupItem.content.target.owner.id;
                            groupItem.content.typeName = '评论';
                            break;
                        case 'CREATE_EXPERIENCE_COMMENT_COMMENT':
                            groupItem.content.link = '#/exp/' + groupItem.content.target.owner.id;
                            groupItem.content.typeName = '回复';
                            break;
                        case 'ACCEPT_GOLD_BUCKET':
                            groupItem.content.link = '#/exp/' + groupItem.content.target.id;
                            groupItem.content.user.name = '内容管理员';
                            groupItem.content.typeName = '一桶金审核通过';
                            break;
                        case 'REJECT_GOLD_BUCKET':
                            groupItem.content.link = '#/exp/' + groupItem.content.target.id;
                            groupItem.content.user.name = '内容管理员';
                            groupItem.content.typeName = '一桶金审核拒绝';
                            break;
                        default:
                            // TODO
                            groupItem.content.typeName = '未知';
                            break;
                    }

                    if (!emptyList && i == 0) {
                        var lasterItem = lastList.result[lastList.result.length - 1];
                        var lastItemDate = lasterItem.group_date.y + '' + lasterItem.group_date.m + '' + lasterItem.group_date.d;
                        var resultItemDate = resultItem.group_date.y + '' + resultItem.group_date.m + '' + resultItem.group_date.d;
                        if (lastItemDate == resultItemDate) {
                            lasterItem.group_data.push(groupItem);
                        }
                    }
                }
                if (emptyList || i > 0) {
                    lastList.result.push(resultItem);
                }
            }
        },

        _scrollShowMore: function () {
            var owner = this;
            window.addEventListener("scroll", function () {
                if (!owner.$scope.loadingCompleted) {
                    return;
                }
                if (Math.ceil($(document).scrollTop()) >= $(document).height() - $(window).height() && !owner.$scope.viewList.last) {
                    owner._defineGetNotice((owner.$scope.viewList.page + 1), owner._view_size);
                }
            });
        },


    });

    NoticeController.$inject = ['$scope', '$stateParams', 'userService', '$rootScope', '$location'];

    angular.module('module.my.NoticeController', ['service.UserService'])
        .controller('NoticeController', NoticeController);

});