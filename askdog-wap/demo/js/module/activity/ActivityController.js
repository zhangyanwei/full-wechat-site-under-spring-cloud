define([
    '../../base/BaseController',
    'app/directive/weShare'
], function (BaseController) {
    var ActivityController = BaseController.extend({

        $stateParams: null,

        /**
         * @Override
         * @param $scope
         * @param $stateParams
         */
        init: function ($scope, $stateParams) {
            this.$stateParams = $stateParams;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope:function() {
            var owner = this;
            owner.$scope.activityDetail = {
                subject: '一桶金计划---ASKDOG经验分享社区',
                desc: '快来赚取您的人生第一桶金！',
                thumbnail: AskDogExp.URL.base() + '/images/activity/activity.png'
            };
        },


    });

    ActivityController.$inject = ['$scope', '$stateParams'];

    angular.module('module.activity.ActivityController',['app.directive.weShare']).controller('ActivityController', ActivityController);

});