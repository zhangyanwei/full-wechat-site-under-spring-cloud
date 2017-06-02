define([
    'base/BaseController',
    'service/SearchService'
], function (BaseController) {
    var SharePicController = BaseController.extend({

        $rootScope: null,
        $stateParams: null,
        $searchService: null,

        /**
         * @Override
         * @param $rootScope
         * @param $scope
         * @param $stateParams
         * @param $searchService
         */
        init: function ($rootScope, $scope, $stateParams, $searchService) {
            this.$rootScope = $rootScope;
            this.$stateParams = $stateParams;
            this.$searchService = $searchService;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function() {

        },
        
    });

    SharePicController.$inject = ['$rootScope', '$scope', '$stateParams', 'searchService'];

    angular.module('module.experience.SharePicController', ['service.SearchService']).controller('SharePicController', SharePicController);

});