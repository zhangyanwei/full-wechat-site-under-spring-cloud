define([
    '../../base/BaseController',
    'app/directive/adAnalytics',
    'service/ChannelService',
    'service/UserService',
    'app/directive/weShare',
    'app/directive/vipApprove'
], function (BaseController) {
    var MySubscribeController = BaseController.extend({

        $stateParams: null,
        $channelService: null,
        $userService:null,

        _VIEW_SIZE:30,

        /**
         * @Override
         * @param $scope
         * @param $stateParams
         * @param $channelService
         * @param $userService
         */
        init: function ($scope, $stateParams, $channelService,$userService,$filter) {
            this.$stateParams = $stateParams;
            this.$channelService = $channelService;
            this.$userService = $userService;
            this._super($scope);
            this.$filter = $filter;
            this.$scope.userName = this.$stateParams.name;

        },

        /**
         * @Override
         */
        defineScope:function(){

            this._subscribeChannel();
            this._showChannel();
            this._cancelSubscribe();
            this._showMoreSubscribe();
        },

        _showChannel:function(){
            var owner = this;
            owner.$userService.otherInfo(owner.$stateParams.id)
                .success(function(data){
                    owner.$scope.subscribeUser = data;
                    owner.$scope.mySubscribeDetail = {
                        subject: data.name + '的个人空间',
                        desc: data.signature || "让知识触手可及",
                        thumbnail:owner.$filter('picTrans')(data.avatar, '58w_58h_1e_1c')|| AskDogExp.URL.base() + '/images/user/user68.png'
                    };
                });
            if(owner.$scope.user && owner.$stateParams.id == owner.$scope.user.id){
                owner.$scope.myChannel = true;
            }
            else{
                owner.$scope.myChannel = false;
            }
            owner._getOtherChannels(0,this._VIEW_SIZE,this.$stateParams.id);
            owner._getOtherSubscribes(0,this._VIEW_SIZE,this.$stateParams.id);
            owner._showMoreChannel();
        },

        _getOtherSubscribes:function(page,size,id){
            var owner = this;
            owner.$scope.subscribeLoadingCompleted = false;
            owner.$channelService.getOtherSubscribe(page,size,id)
                .then(function(resp){
                    owner.$scope.subscribeLoadingCompleted = true;
                    owner._searchSuccess(resp.data,page);
                },function(resp){
                    if(resp.status == 404){
                        owner.$scope.total = 0;
                    }
                });
        },

        _getOtherChannels:function(page,size,id){
            var owner = this;
            owner.$scope.loadingCompleted = false;
            owner.$channelService.getOtherChannels(page,size,id)
                .success(function(data){
                    owner.$scope.loadingCompleted = true;
                    owner._searchSuccessHandler(data,page);
                });
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

        _searchSuccess:function(data,page){
            var lastList = this.$scope.view || {};
            lastList.page = page;
            lastList.total = data.total;
            lastList.last = data.last;
            if(this.$scope.view && this.$scope.view.result){
                lastList.result = this.$scope.view.result;
            }else{
                lastList.result = [];
            }
            for(var index = 0; index < data.result.length;index++){
                lastList.result.push(data.result[index]);
            }
            if(data.result.length == 0){
                lastList.last = true;
            }
            this.$scope.view = lastList;

        },

        _subscribeChannel:function(){
            var owner = this;
            owner.$scope.subscribeChannel = function(id){
                owner.$channelService.createSubscribe(id)
                    .success(function(){
                        for(var i =0;i<owner.$scope.viewList.result.length;i++){
                            if(owner.$scope.viewList.result[i].id == id){
                                owner.$scope.viewList.result[i].subscribed = true;
                                owner.$scope.viewList.result[i].subscriber_count ++;
                            }
                        }
                        for(var i =0;i<owner.$scope.view.result.length;i++){
                            if(owner.$scope.view.result[i].id == id){
                                owner.$scope.view.result[i].subscribed = true;
                                owner.$scope.view.result[i].subscriber_count ++;
                            }
                        }
                    });
            }
        },

        _cancelSubscribe:function(){
            var owner = this;
            owner.$scope.cancelSubscribe = function(id){
                owner.$channelService.cancelSubscribe(id)
                    .success(function(){
                        for(var i =0;i<owner.$scope.viewList.result.length;i++){
                            if(owner.$scope.viewList.result[i].id == id){
                                owner.$scope.viewList.result[i].subscribed = false;
                                owner.$scope.viewList.result[i].subscriber_count --;
                            }
                        }
                        for(var i =0;i<owner.$scope.view.result.length;i++){
                            if(owner.$scope.view.result[i].id == id){
                                owner.$scope.view.result[i].subscribed = false;
                                owner.$scope.view.result[i].subscriber_count --;
                            }
                        }
                    });
            }
        },

        _showMoreChannel:function(){
            var owner = this;
            window.addEventListener("scroll", function () {
                if($("#myChannel").parent().hasClass("active")){
                    if (!owner.$scope.loadingCompleted) {
                        return;
                    }
                    if ($(document).scrollTop() >= $(document).height() - $(window).height() && !owner.$scope.viewList.last) {
                        owner._getOtherChannels((owner.$scope.viewList.page + 1), owner._VIEW_SIZE, owner.$stateParams.id);
                    }
                }
                if($("#mySubscribe").parent().hasClass("active")){
                    if (!owner.$scope.subscribeLoadingCompleted) {
                        return;
                    }
                    if ($(document).scrollTop() >= $(document).height() - $(window).height() && !owner.$scope.view.last) {
                        owner._getOtherSubscribes((owner.$scope.view.page + 1),owner._VIEW_SIZE,owner.$stateParams.id);
                    }

                }

            });
        },

        _showMoreSubscribe:function(){
            var owner = this;
            owner.$scope.showMoreSubscribe = function(){
                owner._showMoreChannel();
            }
        }

    });

    MySubscribeController.$inject = ['$scope', '$stateParams', 'channelService','userService','$filter'];

    angular.module('module.my.MySubscribeController', ['service.ChannelService','service.UserService','app.directive.weShare']).controller('MySubscribeController', MySubscribeController);

});