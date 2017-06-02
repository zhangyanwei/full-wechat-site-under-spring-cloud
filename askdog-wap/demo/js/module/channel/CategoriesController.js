define([
    'base/BaseController',
    'app/directive/adAnalytics',
    'service/ChannelService',
    'service/UserService'
], function (BaseController) {
    var CategoriesController = BaseController.extend({

        $stateParams: null,
        $channelService: null,
        $state:null,

        init: function ($scope, $stateParams, $channelService,$state,$userService) {
            this.$stateParams = $stateParams;
            this.$channelService = $channelService;
            this.$state = $state;
            this.$userService = $userService;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function () {
            this._getCategories();
            this._returnToMain();
            this._selectItem();
            this._subscribe();
        },

        _getCategories:function(){
            var thumbnail = {
                LIFE:'images/channel/channel-pic-12.png',
                HEALTH:'images/channel/channel-pic-2.png',
                LAW:'images/channel/channel-pic-3.png',
                ECONOMIC:'images/channel/channel-pic-4.png',
                CULTURE:'images/channel/channel-pic-5.png',
                SCIENCE:'images/channel/channel-pic-6.png',
                ART:'images/channel/channel-pic-7.png',
                ENTERTAINMENT:'images/channel/channel-pic-11.png',
                EDUCATION:'images/channel/channel-pic-9.png',
                IT:'images/channel/channel-pic-10.png',
                CATE:'images/channel/channel-pic-1.png',
                GUITAR:'images/channel/channel-pic-8.png',
            }
            var owner = this;
            owner.$channelService.getCategories()
                .then(function(resp){
                    console.log(resp.data);
                    for(var index = 1;index <=resp.data.length;index++){
                        var code = resp.data[index - 1].code;
                        resp.data[index - 1].thumbnail = thumbnail[code];
                    }
                    owner.$scope.categories = resp.data;
                });

        },

        _returnToMain:function(){
            var owner = this;
            var interests = [];
            owner.$scope.returnToMain = function(){
                owner.$userService.subscribeCategory({"category_codes": interests}).then(
                    function () {
                        owner.$state.go('default');
                    }
                );
            }
        },

        _selectItem:function(){
            var owner = this;
            owner.$scope.selectItem = function(code){
                for (var index = 0; index < owner.$scope.categories.length; index++) {
                    if (owner.$scope.categories[index].code == code) {
                        owner.$scope.categories[index].selected = !owner.$scope.categories[index].selected;
                        break;
                    }
                }
            }
        },

        _subscribe:function(){
            var owner = this;
            owner.$scope.subscribe = function(){
                var interests = [];
                for (var index = 0; index < owner.$scope.categories.length; index++) {
                    if (owner.$scope.categories[index].selected) {
                        interests.push(owner.$scope.categories[index].code);
                    }
                }
                if(interests.length == 0){
                    owner.$scope.error = '还没有选择';
                }else{
                    owner.$userService.subscribeCategory({"category_codes": interests}).then(
                        function () {
                            owner.$state.go('default');
                        }
                    );
                }
            }
        }
    });


    CategoriesController.$inject = ['$scope', '$stateParams', 'channelService','$state','userService'];

    angular.module('module.channel.CategoriesController', ['service.ChannelService','service.UserService']).controller('CategoriesController', CategoriesController);

});