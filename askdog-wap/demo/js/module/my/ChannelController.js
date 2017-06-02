define([
    '../../base/BaseController',
    'app/directive/adAnalytics',
    'service/ChannelService'
], function (BaseController) {
    var ChannelController = BaseController.extend({

        $stateParams: null,
        $channelService: null,
        $rootScope:null,

        _VIEW_SIZE:20,

        /**
         * @Override
         * @param $scope
         * @param $stateParams
         * @param $channelService
         */
        init: function ($scope, $stateParams, $channelService,$rootScope,$uibModal) {
            this.$stateParams = $stateParams;
            this.$channelService = $channelService;
            this.$rootScope = $rootScope;
            this.$uibModal = $uibModal;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope:function(){
            var owner = this;
            if(this.$stateParams.channel == 'channel')
            {
                var owner = this;
                owner.$scope.channel = true;
                owner.$scope.channelTitle = '我的频道';
                owner._getChannels(0,this._VIEW_SIZE);
                owner.$scope.deleteMyChannel = true;
               
            }
            else
            {
                owner.$scope.subscribe = true;
                owner._getSubscribes(0,this._VIEW_SIZE);
                owner.$scope.channelTitle = '订阅频道';
            }
            this._deleteIcon();
            owner._scrollShowMoreChannel();

        },

        _getChannels:function(page,size){
            var owner = this;
            owner.$scope.loadingCompleted = false;
            owner.$channelService.getChannels(page,size)
                .success(function(data){
                    owner.$scope.loadingCompleted = true;
                    owner._searchSuccessHandler(data,page);
                });
        },

        _getSubscribes:function(page,size){
            var owner = this;
            owner.$scope.loadingCompleted = false;
            owner.$channelService.getSubscribes(page,size)
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

        _deleteIcon:function(){
            var owner = this;
            owner.$scope.deleteIcon = function(id){
                var bottomModal = owner.$uibModal.open({
                    windowTemplateUrl: 'views/base/modal-window.html',
                    windowTopClass: 'delete-channel-modal',
                    templateUrl: 'views/dialog/bottomDialog.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                        $scope.message = '删除频道';
                    }]
                });
                bottomModal.result.then(function(){

                    owner.$channelService.getChannelExperience(0,5,id)
                        .success(function(data){
                            owner._deleteChannel(id,data);
                        });
                });

            }
        },

        _deleteChannel:function(id,data){
            var owner = this;
            if(data.total >0){
                owner.$scope.haveExperience = true;
                var successModal = owner.$uibModal.open({
                    windowTemplateUrl: 'views/base/modal-window.html',
                    windowTopClass: 'status-modal',
                    templateUrl: 'views/dialog/success.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                        $scope.message = '请先删除频道内容';
                    }]
                });
            }else{
                owner.$channelService.deleteChannel(id)
                    .success(function(){
                        owner.$scope.viewList = '';
                        owner.$scope.haveExperience = false;
                        var successModal = owner.$uibModal.open({
                            windowTemplateUrl: 'views/base/modal-window.html',
                            windowTopClass: 'status-modal',
                            templateUrl: 'views/dialog/success.html',
                            controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                                $scope.$uibModalInstance = $uibModalInstance;
                                $scope.message = '删除成功';
                            }]
                        });
                        owner._getChannels(0,owner._VIEW_SIZE);
                    });
            }

        },

        _scrollShowMoreChannel: function () {
            var owner = this;
            window.addEventListener("scroll", function () {
                if(owner.$stateParams.channel == 'channel'){
                    if (!owner.$scope.loadingCompleted) {
                        return;
                    }
                    if ($(document).scrollTop() >= $(document).height() - $(window).height() && !owner.$scope.viewList.last) {
                        owner._getChannels((owner.$scope.viewList.page + 1),owner._VIEW_SIZE);
                    }
                }else{
                    if (!owner.$scope.loadingCompleted) {
                        return;
                    }
                    if ($(document).scrollTop() >= $(document).height() - $(window).height() && !owner.$scope.viewList.last) {
                        owner._getSubscribes((owner.$scope.viewList.page + 1),owner._VIEW_SIZE);
                    }
                }

            });
        }


    });

    ChannelController.$inject = ['$scope', '$stateParams', 'channelService','$rootScope','$uibModal'];

    angular.module('module.my.ChannelController', ['service.ChannelService']).controller('ChannelController', ChannelController);

});