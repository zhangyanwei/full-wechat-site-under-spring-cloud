define([
    '../../base/BaseController',
    'app/directive/adAnalytics',
    'service/ChannelService',
    'service/ExperienceService',
    'app/directive/weShare',
    'validate',
    'app/directive/originalTag',
    'app/directive/vipApprove',
    'app/directive/autoHeight'
], function (BaseController) {
    var MyChannelController = BaseController.extend({

        _VIEW_SIZE:13,

        init: function ($scope, $stateParams, $channelService, $experienceService, $rootScope, $filter,$uibModal) {
            this.$stateParams = $stateParams;
            this.$channelService = $channelService;
            this.$experienceService = $experienceService;
            this.$rootScope = $rootScope;
            this.$filter = $filter;
            this.$uibModal = $uibModal;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope:function(){
            var owner = this;
            this._getChannelDetail();
            this._getChannelExperience(0,this._VIEW_SIZE,owner.$stateParams.id);
            this._deleteExperience();
            this._subscribeChannel();
            this._cancelSubscribe();
            this._updateChannel();
            this._showDescription();
            this._hideDescription();
            this._scrollShowMore();
            this._bindValidator();
        },

        _getChannelDetail:function(){
            var owner = this;
            owner.$channelService.getChannelDetail(owner.$stateParams.id)
                .success(function(data){
                    owner.$scope.detail = data;
                    owner.$scope.detail.description =  owner.$scope.detail.description.replace(/[\r\n]/g,"");
                    owner.$scope.shareDetail = {
                        subject: data.name,
                        desc: data.description || "频道来自于askdog经验分享社区",
                        thumbnail: owner.$filter('picTrans')(data.thumbnail, '58w_58h_1e_1c') || AskDogExp.URL.base() + '/images/channel58.png'
                    };
                });
        },

        _getChannelExperience:function(page,size,id){
            var owner = this;
            owner.$scope.loadingCompleted = false;
            owner.$channelService.getChannelExperience(page,size,id)
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

        _deleteExperience:function(){
            var owner = this;
            owner.$scope.deleteExperience = function(id){
                var bottomModal = owner.$uibModal.open({
                    windowTemplateUrl: 'views/base/modal-window.html',
                    windowTopClass: 'delete-channel-modal',
                    templateUrl: 'views/dialog/bottomDialog.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                        $scope.message = '删除经验';
                    }]
                });
                bottomModal.result.then(function(){
                    owner._delete(id)
                });

            }
        },

        _delete:function(id){
            var owner = this;
            owner.$experienceService.deleteExperience(id)
                .success(function(){
                    var successModal = owner.$uibModal.open({
                        windowTemplateUrl: 'views/base/modal-window.html',
                        windowTopClass: 'status-modal',
                        templateUrl: 'views/dialog/success.html',
                        controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                            $scope.$uibModalInstance = $uibModalInstance;
                            $scope.message = '删除成功';
                        }]
                    });
                    owner.$scope.viewList = '';
                    owner._getChannelExperience(0,owner._VIEW_SIZE,owner.$stateParams.id);
                });
        },

        _subscribeChannel:function(){
            var owner = this;
            owner.$scope.subscribeChannel = function(){
                owner.$channelService.createSubscribe(owner.$stateParams.id)
                    .success(function(){
                        owner._getChannelDetail();
                    });
            }
        },

        _cancelSubscribe:function(){
            var owner = this;
            owner.$scope.cancelSubscribe = function(){
                owner.$channelService.cancelSubscribe(owner.$stateParams.id)
                    .success(function(){
                        owner._getChannelDetail();
                    });
            }
        },

        _updateChannel:function(){
            var owner = this;
           owner.$scope.updateChannel = function(detail){
               if(detail.mine){
                  owner.$scope.updateMyChannel = true;
               }
           }
        },

        _updateChannelTitle:function(){
            var owner = this;
            if(owner.$scope.description == undefined){
                owner.$scope.description = '';
            }
            owner.$channelService.updateChannel(owner.$scope.detail.name,owner.$scope.detail.description,owner.$stateParams.id)
                .success(function(data){
                    owner.$scope.updateMyChannel = false;
                    owner.$scope.updateTitle = false;
                    owner.$scope.updateDescription = false;
                });
        },

        _showDescription:function(){
            var owner = this;
            owner.$scope.showDescription = function(){
                $("#collapseExample").show();
                $("#showDetail").hide();
            }
        },

        _hideDescription:function(){
            var owner = this;
            owner.$scope.hideDescription = function(){
                $("#collapseExample").hide();
                $("#showDetail").show();
            }
        },

        _scrollShowMore: function () {
            var owner = this;
            window.addEventListener("scroll", function () {
                if (!owner.$scope.loadingCompleted) {
                    return;
                }
                if ($(document).scrollTop() >= $(document).height() - $(window).height() && !owner.$scope.viewList.last) {
                    owner._getChannelExperience((owner.$scope.viewList.page + 1),owner._VIEW_SIZE,owner.$stateParams.id);
                }
            });
        },

        _bindValidator:function(){
            var owner = this;
            owner.$scope.bindValidator = function(element){
                $(element).validate({
                    rules: {
                        channelName: {
                            required:true
                        }
                    },
                    messages: {
                        channelName: {
                            required:'频道标题不能为空'
                        }
                    },
                    submitHandler: function () {
                        owner._updateChannelTitle();
                    }
                })
            }
        }

    });

    MyChannelController.$inject = ['$scope', '$stateParams', 'channelService', 'experienceService', '$rootScope', '$filter','$uibModal'];

    angular.module('module.my.MyChannelController', ['app.directive.weShare', 'service.ChannelService','service.ExperienceService']).controller('MyChannelController', MyChannelController);

});