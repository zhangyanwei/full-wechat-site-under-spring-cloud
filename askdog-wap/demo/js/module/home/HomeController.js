define([
    'base/BaseController',
    'app/directive/adAnalytics'
], function (BaseController) {
    var HomeController = BaseController.extend({

        _rootScope: null,
        _stateParams: null,

        /**
         * @Override
         * @param $rootScope
         * @param $scope
         * @param $stateParams
         */
        init: function ($rootScope, $scope, $stateParams) {
            this._rootScope = $rootScope;
            this._stateParams = $stateParams;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope:function(){
            // TODO
        }

    });

    HomeController.$inject = ['$rootScope', '$scope', '$stateParams'];

    angular.module('module.home.HomeController', []).controller('HomeController', HomeController);

});