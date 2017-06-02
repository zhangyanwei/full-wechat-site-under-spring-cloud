define([
    'base/BaseController',
    'service/ChannelService'
], function (BaseController) {
    var FindController = BaseController.extend({

        $stateParams: null,
        $channelService: null,
        $state:null,

        _VIEW_SIZE:13,
        init: function ($scope, $stateParams, $channelService,$state) {
            this.$stateParams = $stateParams;
            this.$channelService = $channelService;
            this.$state = $state;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function () {
            this._defineShowSearch();
            this._defineGetChannel(0,this._VIEW_SIZE);
            this._scrollShowMore();
            this._defineSubscribeChannel();
            this._defineCancelSubscribe();
        },

        _defineShowSearch:function(){
            var owner = this;
            owner.$scope.showSearch = function(){
                owner.$state.go("view.search");
            }
        },

        _defineGetChannel:function(from,size){
            var owner = this;
            owner.$scope.loadingCompleted = false;
            owner.$channelService.recommendChannel(from,size)
                .then(function(resp){
                    console.log(resp.data);
                    owner.$scope.loadingCompleted = true;
                    owner._searchSuccessHandler(resp.data, from);
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

                if ($(document).scrollTop() >= $(document).height() - $(window).height() && !owner.$scope.viewList.last) {
                    owner._defineGetChannel((owner.$scope.viewList.from + owner._VIEW_SIZE), owner._VIEW_SIZE);
                }
            });
        },

        _defineSubscribeChannel:function(){
            var owner = this;
            owner.$scope.findSubscribe = function(channelId){
                owner.$channelService.createSubscribe(channelId)
                    .success(function () {
                        for (var index = 0; index < owner.$scope.viewList.result.length; index++) {
                            if (owner.$scope.viewList.result[index].id == channelId) {
                                owner.$scope.viewList.result[index].subscribed = true;
                                return;
                            }
                        }
                    });
            }
        },

        _defineCancelSubscribe:function(){
            var owner = this;
            owner.$scope.cancelSubscribe = function(channelId){
                owner.$channelService.cancelSubscribe(channelId)
                    .success(function () {
                        for (var index = 0; index < owner.$scope.viewList.result.length; index++) {
                            if (owner.$scope.viewList.result[index].id == channelId) {
                                owner.$scope.viewList.result[index].subscribed = false;
                                return;
                            }
                        }
                    });
            }
        }
    });


    FindController.$inject = ['$scope', '$stateParams', 'channelService','$state'];

    angular.module('module.channel.FindController', ['service.ChannelService']).controller('FindController', FindController);

});