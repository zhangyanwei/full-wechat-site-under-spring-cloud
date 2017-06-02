define(['base/BaseController', 'app/directive/adAnalytics', 'service/UserService'], function (BaseController) {

    var NoticeController = BaseController.extend({

        $rootScope: null,
        $stateParams: null,
        $userService: null,

        _VIEW_SIZE: 13,
        _contextReadyListener: null,

        /**
         * @Override
         * @param _rootScope
         * @param _scope
         * @param _stateParams
         * @param _userService
         */
        init: function (_rootScope, _scope, _stateParams, _userService) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$userService = _userService;
            this._super(_scope);
        },

        /**
         * @Override
         */
        defineScope: function () {
            var owner = this;
            this._defineRefreshData();
            this._loadNotifications(0);
            this.$scope.paging = function () {
                owner._loadNotifications((owner.$scope.viewList.page + 1))
            }
        },

        defineListeners: function () {
            var owner = this;
            this._contextReadyListener = this.$rootScope.$on('contextReady', function (event, userSelf) {
                if (userSelf && !owner.$scope.viewList) {
                    owner._loadNotifications(0);
                }
            });
        },

        destroy: function () {
            this._contextReadyListener();
            this._contextReadyListener = null;
        },

        _defineRefreshData: function () {
            var owner = this;
            this.$scope.refreshData = function () {
                owner._loadNotifications(0);
            };
        },

        _loadNotifications: function (page) {
            var owner = this;
            this.$scope.loadingCompleted = false;
            this.$scope.loadingFailed = false;
            this.$userService.notifications(page, this._VIEW_SIZE).then(
                function (resp) {
                    owner._loadNotificationsSuccessHandler(resp.data);
                    owner.$scope.loadingCompleted = true;
                }, function () {
                    owner.$scope.loadingFailed = true;
                }
            );
        },

        _loadNotificationsSuccessHandler: function (data) {
            var lastList = {result: [], total: 0, page: 0, size: this._VIEW_SIZE, last: true};
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
        }
    });

    NoticeController.$inject = ['$rootScope', '$scope', '$stateParams', 'UserService'];

    angular.module('module.NoticeController', ['service.UserService']).controller('NoticeController', NoticeController);

});