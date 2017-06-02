define(['base/BaseController', 'app/directive/adAnalytics', 'service/SearchService'], function (BaseController) {

    var historyViewListCache = {};
    var channelRecommendCache = {};
    var userRecommendCache = {};

    var CategoryController = BaseController.extend({

        _scrollBottomListener: null,
        _VIEW_SIZE: 13,
        _VIEW_RECOMMEND_SIZE: 8,
        _CURRENT_CODE: undefined,
        _TOP_CODE: undefined,

        init: function (_rootScope, _scope, _stateParams, _searchService) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$searchService = _searchService;
            this._super(_scope);
        },

        defineScope: function () {
            this._loadCategoryCode();
            this._defineCategoryNavBar();
            this._defineViewHandler();
        },

        defineListeners: function () {
            var owner = this;
            this._scrollBottomListener = this.$rootScope.$on('scrollBottom', function () {
                if (owner.$scope.loadingCompleted && !owner.$scope.viewList.last && (owner.$scope.viewList.from / owner._VIEW_SIZE % 3 != 2)) {
                    owner.$scope.paging();
                }
            });
        },

        destroy: function () {
            this._scrollBottomListener();
            this._scrollBottomListener = null;
        },

        _loadCategoryCode: function () {
            this._TOP_CODE = this.$stateParams.category;
            this._CURRENT_CODE = this.$stateParams.categoryCode;
            if (!this._CURRENT_CODE) {
                this._CURRENT_CODE = this._TOP_CODE;
            }
        },

        _defineCategoryNavBar: function () {
            var owner = this;

            this.$scope.categoryNavBarLink = function (category, categoryCode) {
                owner._TOP_CODE = category;
                owner._CURRENT_CODE = categoryCode;
                owner._refreshListView();
            };

            owner.$rootScope.$watch('sysCategories', function (sysCategories) {
                if (sysCategories) {
                    for (var index = 0; index < sysCategories.length; index++) {
                        if (sysCategories[index].code == owner._TOP_CODE) {
                            owner.$scope._TOP_CATEGORY_NAME = sysCategories[index].name;
                        }
                    }
                    for (var index = 0; index < sysCategories.length; index++) {
                        if (sysCategories[index].code == owner._CURRENT_CODE) {
                            owner.$scope.categoryNavItem = sysCategories[index];
                            return;
                        }
                        for (var jindex = 0; jindex < sysCategories[index].children.length; jindex++) {
                            if (sysCategories[index].children[jindex].code == owner._CURRENT_CODE) {
                                owner.$scope.categoryNavItem = sysCategories[index];
                                return;
                            }
                        }
                    }
                }
            });
        },

        _defineViewHandler: function () {
            var owner = this;
            this._refreshListView();
            this.$scope.paging = function () {
                owner._categorySearch(owner._CURRENT_CODE, (owner.$scope.viewList.from + owner._VIEW_SIZE))
            };
            this.$scope.refreshData = function () {
                owner._categorySearch(owner._CURRENT_CODE, 0);
            };
            this.$scope.channelRecommendPaging = function () {
                owner._channelRecommend((owner.$scope.channelRecommendList.from + owner._VIEW_RECOMMEND_SIZE));
            };
            this.$scope.userRecommendPaging = function () {
                owner._userRecommend((owner.$scope.userRecommendList.from + owner._VIEW_RECOMMEND_SIZE));
            };
        },

        _refreshListView: function () {
            if (historyViewListCache[this._CURRENT_CODE]) {
                this.$scope.viewList = historyViewListCache[this._CURRENT_CODE];
                this.$scope.loadingCompleted = true;
            } else {
                this._categorySearch(this._CURRENT_CODE, 0);
            }

            if (channelRecommendCache[this._TOP_CODE]) {
                this.$scope.channelRecommendList = channelRecommendCache[this._TOP_CODE];
            } else {
                this._channelRecommend(0);
            }
            if (userRecommendCache[this._TOP_CODE]) {
                this.$scope.userRecommendList = userRecommendCache[this._TOP_CODE];
            } else {
                this._userRecommend(0);
            }
        },

        _categorySearch: function (categoryCode, from) {
            var owner = this;
            this.$scope.loadingCompleted = false;
            this.$scope.loadingFailed = false;
            this.$searchService.categorySearch(categoryCode, from, this._VIEW_SIZE).then(
                function (resp) {
                    owner._categorySearchSuccessHandler(resp.data, from);
                    owner.$scope.loadingCompleted = true;
                }, function () {
                    owner.$scope.loadingFailed = true;
                }
            );
        },

        _categorySearchSuccessHandler: function (data, from) {
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
            historyViewListCache[this._CURRENT_CODE] = jQuery.extend(true, {}, lastList);
        },

        _channelRecommend: function (from) {
            var owner = this;
            this.$searchService.channelRecommendWithCategory(from, this._VIEW_RECOMMEND_SIZE, this._TOP_CODE).then(
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
            channelRecommendCache[this._TOP_CODE] = jQuery.extend(true, {}, lastList);
        },

        _userRecommend: function (from) {
            var owner = this;
            this.$searchService.userRecommendWithCategory(from, this._VIEW_RECOMMEND_SIZE, this._TOP_CODE).then(
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
            userRecommendCache[this._TOP_CODE] = jQuery.extend(true, {}, lastList);
        }

    });

    CategoryController.$inject = ['$rootScope', '$scope', '$stateParams', 'SearchService'];

    angular.module('module.CategoryController', ['service.SearchService']).controller('CategoryController', CategoryController);

});