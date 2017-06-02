define(['base/BaseController', 'app/directive/adAnalytics', 'service/SearchService'], function (BaseController) {

    var SearchController = BaseController.extend({

        _scrollBottomListener: null,
        _VIEW_SIZE: 13,

        init: function (_rootScope, _scope, _stateParams, _searchService) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$searchService = _searchService;
            this._super(_scope);
        },

        defineScope: function () {
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

        _defineViewHandler: function () {
            var owner = this;
            this._refreshListView();
            this.$scope.paging = function () {
                owner._keySearch(owner.$stateParams.key, (owner.$scope.viewList.from + owner._VIEW_SIZE))
            };
            this.$scope.refreshData = function () {
                owner._keySearch(owner.$stateParams.key, 0);
            };
        },

        _refreshListView: function () {
            var key = this.$stateParams.key;
            this.$scope.key = decodeURI(key);
            this._keySearch(key, 0);
        },

        _keySearch: function (key, from) {
            var owner = this;
            this.$scope.loadingCompleted = false;
            this.$scope.loadingFailed = false;
            this.$searchService.keySearch(key, from, this._VIEW_SIZE).then(
                function (resp) {
                    owner._keySearchSuccessHandler(resp.data, from);
                    owner.$scope.loadingCompleted = true;
                }, function () {
                    owner.$scope.loadingFailed = true;
                }
            );
        },

        _keySearchSuccessHandler: function (data, from) {
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
        }

    });

    SearchController.$inject = ['$rootScope', '$scope', '$stateParams', 'SearchService'];

    angular.module('module.SearchController', ['service.SearchService']).controller('SearchController', SearchController);

});