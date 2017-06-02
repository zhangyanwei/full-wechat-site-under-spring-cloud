define(['base/BaseController', 'app/directive/adAnalytics', 'service/SearchService', 'service/UserService'], function (BaseController) {

    var historyViewListCache = undefined;
    var channelRecommendListCache = undefined;
    var userRecommendListCache = undefined;

    var HomeController = BaseController.extend({

        _contextReadyListener: null,
        _contextChangeListener: null,
        _scrollBottomListener: null,

        _VIEW_SIZE: 13,
        _VIEW_RECOMMEND_SIZE: 8,

        init: function (_rootScope, _scope, _stateParams, _uibModal, _searchService, _userService) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$uibModal = _uibModal;

            this.$searchService = _searchService;
            this.$userService = _userService;
            this._super(_scope);
        },

        defineScope: function () {
            this._defineViewHandler();
        },

        defineListeners: function () {
            var owner = this;
            this._contextChangeListener = this.$rootScope.$on('contextChange', function (event, userSelf) {
            });

            this._contextReadyListener = this.$rootScope.$on('contextReady', function (event, userSelf) {
                owner._homeSearch(0);
            });

            this._scrollBottomListener = this.$rootScope.$on('scrollBottom', function () {
                if (owner.$scope.loadingCompleted && !owner.$scope.viewList.last && (owner.$scope.viewList.from / owner._VIEW_SIZE % 3 != 2)) {
                    //if (owner.$scope.loadingCompleted && !owner.$scope.viewList.last && (owner.$scope.viewList.from / owner._VIEW_SIZE < 3)) {
                    owner.$scope.paging();
                }
            });
        },

        destroy: function () {
            this._contextChangeListener();
            this._contextReadyListener();
            this._scrollBottomListener();
            this._contextChangeListener = null;
            this._contextReadyListener = null;
            this._scrollBottomListener = null;
        },

        _defineViewHandler: function () {
            var owner = this;
            this._refreshListView();
            this.$scope.paging = function () {
                owner._homeSearch((owner.$scope.viewList.from + owner._VIEW_SIZE));
            };
            this.$scope.refreshData = function () {
                owner._homeSearch(0);
            };
            this.$scope.channelRecommendPaging = function () {
                owner._channelRecommend((owner.$scope.channelRecommendList.from + owner._VIEW_RECOMMEND_SIZE));
            };
            this.$scope.userRecommendPaging = function () {
                owner._userRecommend((owner.$scope.userRecommendList.from + owner._VIEW_RECOMMEND_SIZE));
            };
        },

        _refreshListView: function () {
            if (historyViewListCache) {
                this.$scope.viewList = historyViewListCache;
                this.$scope.loadingCompleted = true;
            } else {
                this._homeSearch(0);
            }
            if (channelRecommendListCache) {
                this.$scope.channelRecommendList = channelRecommendListCache;
            } else {
                this._channelRecommend(0);
            }

            if (userRecommendListCache) {
                this.$scope.userRecommendList = userRecommendListCache;
            } else {
                this._userRecommend(0);
            }
        },

        _homeSearch: function (from) {
            var owner = this;
            this.$scope.loadingCompleted = false;
            this.$scope.loadingFailed = false;
            this.$searchService.homeSearch(from, this._VIEW_SIZE).then(
                function (resp) {
                    owner._homeSearchSuccessHandler(resp.data, from);
                    owner.$scope.loadingCompleted = true;
                }, function () {
                    owner.$scope.loadingFailed = true;
                }
            );
        },

        _homeSearchSuccessHandler: function (data, from) {
            var lastList = this.$scope.viewList || {};
            lastList.from = from;
            lastList.total = data.total;
            lastList.last = data.last;
            if (from == 0) {
                lastList.result = [];
            } else {
                lastList.result = this.$scope.viewList.result;
            }
            for (var index = 0; index < data.result.length; index++) {
                lastList.result.push(data.result[index]);
            }
            if (data.result.length == 0) {
                lastList.last = true;
            }
            this.$scope.viewList = lastList;
            historyViewListCache = lastList;
        },

        _channelRecommend: function (from) {
            var owner = this;
            this.$searchService.channelRecommend(from, this._VIEW_RECOMMEND_SIZE).then(
                function (resp) {
                    owner._channelRecommendSuccessHandler(resp.data, from);
                }
            );
        },

        _channelRecommendSuccessHandler: function (data, from) {
            var lastList = data;
            lastList.from = from;
            if (data.result.length == 0) {
                lastList.last = true;
            }
            if (lastList.last) {
                lastList.last = false;
                lastList.from = -this._VIEW_RECOMMEND_SIZE;
            }
            this.$scope.channelRecommendList = lastList;
            channelRecommendListCache = lastList;
        },

        _userRecommend: function (from) {
            var owner = this;
            this.$searchService.userRecommend(from, this._VIEW_RECOMMEND_SIZE).then(
                function (resp) {
                    owner._userRecommendSuccessHandler(resp.data, from);
                }
            );
        },

        _userRecommendSuccessHandler: function (data, from) {
            var lastList = data;
            lastList.from = from;
            if (data.result.length == 0) {
                lastList.last = true;
            }
            if (lastList.last) {
                lastList.last = false;
                lastList.from = -this._VIEW_RECOMMEND_SIZE;
            }
            this.$scope.userRecommendList = lastList;
            userRecommendListCache = lastList;
        }
    });

    HomeController.$inject = ['$rootScope', '$scope', '$stateParams', '$uibModal', 'SearchService', 'UserService'];

    angular.module('module.HomeController', ['service.SearchService', 'service.UserService']).controller('HomeController', HomeController);

});