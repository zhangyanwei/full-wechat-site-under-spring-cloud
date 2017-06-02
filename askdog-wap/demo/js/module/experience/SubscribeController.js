define([
    'base/BaseController',
    'app/directive/adAnalytics',
    'service/UserService',
    'service/SearchService',
    'app/directive/originalTag'
], function (BaseController) {
    var SubscribeController = BaseController.extend({
        _VIEW_SIZE:13,

        init: function ($rootScope, $scope, $stateParams, $userService,$searchService) {
            this.$rootScope = $rootScope;
            this.$stateParams = $stateParams;
            this.$userService = $userService;
            this.$searchService = $searchService;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function () {
            this._refreshSubscribedChannels();
            this._defineSubscribeUnread(0,this._VIEW_SIZE);
            this._scrollShowMore();
        },

        _refreshSubscribedChannels: function () {
            var owner = this;
            this.$userService.subscribedChannels(0, 6).then(
                function (resp) {
                    if(resp.data.result.length == 0){
                        owner.$scope.haveNoChannel = true;
                    }
                    owner.$scope.subscribedChannels = resp.data.result;
                }
            );
        },

        _defineSubscribeUnread:function(from,size){
            var owner = this;
            owner.$scope.loadingCompleted = false;
            owner.$searchService.subscribeUnread(from,size)
                .then(function(resp){
                    owner.$scope.loadingCompleted = true;
                    owner._searchSuccessHandler(resp.data, from);
                    console.log(resp.data);
                });
        },

        _searchSuccessHandler: function (data, from) {
            var lastList = this.$scope.viewList || {};
            lastList.from = from;
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
                    owner._refresh((owner.$scope.viewList.from + owner._VIEW_SIZE), owner._VIEW_SIZE, owner.$stateParams.type);
                }
            });
        }
    });

    SubscribeController.$inject = ['$rootScope', '$scope', '$stateParams', 'userService','searchService'];

    angular.module('module.experience.SubscribeController', ['service.UserService','service.SearchService']).controller('SubscribeController', SubscribeController);

});