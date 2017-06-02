define([
    '../../base/BaseController',
    "views/share.html"
], function (BaseController) {
    var ShareController = BaseController.extend({


        /**
         * @Override
         * @param $scope
         */
        init: function ($scope) {
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function () {

        }

    });

    ShareController.$inject = ['$scope'];

    angular.module('module.modal.ShareController', []).controller('ShareController', ShareController);

});