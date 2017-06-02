define([
    'base/BaseController',
    'app/directive/adAnalytics',
    'service/SearchService',
    'app/directive/originalTag',
    'app/directive/vipApprove'
], function (BaseController) {
    var ExperienceSearchListController = BaseController.extend({

        $stateParams: null,
        $searchService: null,

        _VIEW_SIZE:13,

        /**
         * @Override
         * @param $rootScope
         * @param $scope
         * @param $stateParams
         * @param $searchService
         */
        init: function ($scope, $stateParams, $searchService) {
            this.$stateParams = $stateParams;
            this.$searchService = $searchService;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function() {
            var owner = this;
            this._searchAll(0,this._VIEW_SIZE,owner.$stateParams.searchKey);
            this._scrollShowMore();
        },

        _searchAll:function(from,size,key){
            var owner = this;
            owner.$scope.loadingCompleted = false;
            this.$searchService.searchAll(from,size,key)
                .success(function(data){
                    owner.$scope.searchKey = owner.$stateParams.searchKey;
                    owner.$scope.loadingCompleted = true;
                    owner._searchSuccessHandler(data,from);
                });
        },

        _searchSuccessHandler:function(data,from){
            var lastList = this.$scope.viewList || {};
            lastList.from = from;
            lastList.total = data.total;
            lastList.last = data.last;
            lastList.key = this.$stateParams.key;
            if(this.$scope.viewList && this.$scope.viewList.result){
                lastList.result = this.$scope.viewList.result;
            }else{
                lastList.result = [];
            }
            for(var index = 0; index < data.result.length;index++){
                lastList.result.push(data.result[index]);
            }
            if(data.result.length == 0){
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
                if ($(document).scrollTop() >= $(document).height() - $(window).height() && !owner.$scope.viewList.last) {
                    owner._searchAll((owner.$scope.viewList.from + owner._VIEW_SIZE),owner._VIEW_SIZE,owner.$stateParams.searchKey);
                }
            });
        }
    });

    ExperienceSearchListController.$inject = [ '$scope', '$stateParams', 'searchService'];

    angular.module('module.experience.ExperienceSearchListController', ['service.SearchService']).controller('ExperienceSearchListController', ExperienceSearchListController);

});