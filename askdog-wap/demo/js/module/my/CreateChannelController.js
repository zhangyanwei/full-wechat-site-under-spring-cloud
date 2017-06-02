define([
    '../../base/BaseController',
    'service/ChannelService'
], function (BaseController) {
    var CreateChannelController = BaseController.extend({

        $stateParams: null,
        $channelService: null,
        $rootScope:null,

        _VIEW_SIZE:5,

        /**
         * @Override
         * @param $scope
         * @param $stateParams
         * @param $channelService
         */
        init: function ($scope, $stateParams, $channelService,$rootScope) {
            this.$stateParams = $stateParams;
            this.$channelService = $channelService;
            this.$rootScope = $rootScope;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope:function(){
            this._createChannel();
        },

        _createChannel:function(){
            var owner = this;
            this.$scope.createChannel = function(){
                var pureChannel = {
                    "name": owner.$scope.pureChannel.name,
                    "description": owner.$scope.pureChannel.description
                };
                owner.$channelService.createChannel(pureChannel)
                    .success(function(){
                       window.location = '/#/my/channel';
                    })

            }
        }

    });

    CreateChannelController.$inject = ['$scope', '$stateParams', 'channelService','$rootScope'];

    angular.module('module.my.CreateChannelController', ['service.ChannelService']).controller('CreateChannelController', CreateChannelController);

});