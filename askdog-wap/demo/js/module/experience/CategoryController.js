define([
    'base/BaseController',
    'app/directive/adAnalytics',
    'service/SearchService'
], function (BaseController) {
    var CategoryController = BaseController.extend({

        _VIEW_SIZE: 13,
        _CURRENT_CODE: undefined,

        init: function ($rootScope, $scope, $stateParams, $searchService) {
            this.$rootScope = $rootScope;
            this.$stateParams = $stateParams;
            this.$searchService = $searchService;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function () {
            this._CURRENT_CODE = this.$stateParams.categoryCode;
            this._defineCategoryNavBar(this._CURRENT_CODE);
            this._defineRefreshData();
            this._scrollShowMore();
        },

        _defineCategoryNavBar: function (categoryCode) {
            var owner = this;
            owner.$rootScope.$watch('sysCategories', function (sysCategories) {
                if (sysCategories) {
                    for (var index = 0; index < sysCategories.length; index++) {
                        if (sysCategories[index].code == categoryCode) {
                            owner.$scope.categoryNavTabItems = sysCategories[index].children;
                            return;
                        }
                        for (var jindex = 0; jindex < sysCategories[index].children.length; jindex++) {
                            if (sysCategories[index].children[jindex].code == categoryCode) {
                                owner.$scope.categoryNavTabItems = sysCategories[index].children;
                                return;
                            }
                        }
                    }
                }
            });
        },

        _defineRefreshData: function () {
            var owner = this;
            this._categorySearch(this._CURRENT_CODE, 0, this._VIEW_SIZE);
            this.$scope.paging = function () {
                owner._categorySearch(owner._CURRENT_CODE, (owner.$scope.viewList.from + owner._VIEW_SIZE), owner._VIEW_SIZE)
            };
            this.$scope.refreshData = function () {
                owner._categorySearch(owner._CURRENT_CODE, 0, owner._VIEW_SIZE);
            };
        },

        _categorySearch: function (categoryCode, from, size) {
            var owner = this;
            this.$scope.loadingCompleted = false;
            this.$scope.loadingFailed = false;
            this.$searchService.categorySearch(categoryCode, from, size).then(
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
        },

        _scrollShowMore: function () {
            var owner = this;
            window.addEventListener("scroll", function () {
                if (!owner.$scope.loadingCompleted) {
                    return;
                }
                if (Math.ceil($(document).scrollTop()) >= $(document).height() - $(window).height() && !owner.$scope.viewList.last) {
                    owner._categorySearch(owner.$stateParams.categoryCode,(owner.$scope.viewList.from + owner._VIEW_SIZE),owner._VIEW_SIZE);
                }
            });
        },
    });

    CategoryController.$inject = ['$rootScope', '$scope', '$stateParams', 'searchService'];

    angular.module('module.experience.CategoryController', ['service.SearchService']).controller('CategoryController', CategoryController);

});