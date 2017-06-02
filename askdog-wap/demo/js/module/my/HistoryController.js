define([
    '../../base/BaseController',
    'app/directive/adAnalytics',
    'service/UserService',
    'app/directive/originalTag',
    'app/directive/vipApprove'
], function (BaseController) {
    var HistoryController = BaseController.extend({

        $stateParams: null,
        $userService: null,

        _VIEW_SIZE:13,

        /**
         * @Override
         * @param $scope
         * @param $stateParams
         * @param $channelService
         */
        init: function ($scope, $stateParams, $userService) {
            this.$stateParams = $stateParams;
            this.$userService = $userService;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope:function(){
            var owner = this;
            owner._getHistory(0,this._VIEW_SIZE);
            owner._scrollShowMore();

        },

        _getHistory:function(page,size){
            var owner = this;
            owner.$scope.loadingCompleted = false;
            this.$userService.getHistory(page,size)
                .success(function(data){
                    console.log(data);
                    owner.$scope.loadingCompleted = true;
                    owner._searchSuccessHandler(data,page);
                })
        },

        _searchSuccessHandler:function(data,page){
            var lastList = this.$scope.viewList || {};
            lastList.page = page;
            lastList.total = data.total;
            lastList.last = data.last;
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
                if ($(document).scrollTop() >= $(document).height() - $(window).height()) {
                    owner._getHistory((owner.$scope.viewList.page +1 ),owner._VIEW_SIZE);
                }
            });
        }

    });

    HistoryController.$inject = ['$scope', '$stateParams', 'userService'];

    angular.module('module.my.HistoryController', ['service.UserService']).controller('HistoryController', HistoryController);

});