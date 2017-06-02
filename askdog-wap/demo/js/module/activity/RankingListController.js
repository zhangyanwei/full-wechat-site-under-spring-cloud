define([
    'base/BaseController',
    'service/ActivityService'
], function (BaseController) {
    var RankingListController = BaseController.extend({

        $stateParams: null,

        _view_size:13,

        /**
         * @Override
         * @param $scope
         * @param $stateParams
         */
        init: function ($scope, $stateParams,$ActivityService) {
            this.$stateParams = $stateParams;
            this.$ActivityService = $ActivityService;
            this._super($scope);
            this._scrollShowMore();
        },

        /**
         * @Override
         */
        defineScope:function() {
            this._defineGetActivityList(0,this._view_size);
        },

        _defineGetActivityList:function(page,size){
            var owner = this;
            owner.$scope.loadingCompleted = false;
            owner.$ActivityService.getActivity(page,size).then(function(resp){
                owner.$scope.loadingCompleted = true;
                owner._searchSuccessHandler(resp.data, page);
            });
        },

        _searchSuccessHandler: function (data, page) {
            var lastList = this.$scope.viewList || {};
            lastList.page = page;
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

        _scrollShowMore: function () {
            var owner = this;
            window.addEventListener("scroll", function () {
                if (!owner.$scope.loadingCompleted) {
                    return;
                }
                if (Math.ceil($(document).scrollTop()) >= $(document).height() - $(window).height() && !owner.$scope.viewList.last) {
                    owner._defineGetActivityList((owner.$scope.viewList.page + 1), owner._view_size);
                }
            });
        },


    });

    RankingListController.$inject = ['$scope', '$stateParams','activityService'];

    angular.module('module.activity.RankingListController',['service.ActivityService']).controller('RankingListController', RankingListController);

});