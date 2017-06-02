define([
    'base/BaseController',
    'app/directive/adAnalytics',
    'service/SearchService'
], function (BaseController) {
    var SearchController = BaseController.extend({

        $rootScope: null,
        $stateParams: null,
        $searchService: null,

        _VIEW_SIZE:5,
        /**
         * @Override
         * @param $rootScope
         * @param $scope
         * @param $stateParams
         * @param $searchService
         */
        init: function ($rootScope, $scope, $stateParams, $searchService,$state) {
            this.$rootScope = $rootScope;
            this.$stateParams = $stateParams;
            this.$searchService = $searchService;
            this.$state = $state;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function() {
            this._defineRelateSearch();
            this._defineShowSearchResult();
            this._defineReturnSearch();
        },

        _defineRelateSearch:function(){
            var owner = this;
            owner.$scope.relateSearch = function(){
                owner.$rootScope.haveSearch = true;
                owner.$rootScope.searchKey = owner.$scope.searchKey;
                owner.$searchService.searchSimilar(owner.$scope.searchKey)
                    .then(function(resp){
                        owner.$rootScope.searchResult = resp.data.result;
                    });
                if(owner.$scope.searchKey == ''){
                    owner.$rootScope.haveSearch = false;
                }
            }
        },

        _defineShowSearchResult:function(){
            var owner = this;
            owner.$scope.showSearchResult = function(){
                owner.$state.go("view.search-result",{"searchKey":owner.$scope.searchKey});
            }
        },

        _defineReturnSearch:function(){
            var owner = this;
            owner.$scope.returnSearch = function(){
                owner.$state.go("view.search");
            }
        }
    });

    SearchController.$inject = ['$rootScope', '$scope', '$stateParams', 'searchService',"$state"];

    angular.module('module.experience.SearchController', ['service.SearchService']).controller('SearchController', SearchController);

});