define(['base/BaseController'], function (BaseController) {

    var FooterController = BaseController.extend({

        $rootScope: null,
        $stateParams: null,

        /**
         * @Override
         * @param _rootScope
         * @param _scope
         * @param _stateParams
         */
        init: function (_rootScope, _scope, _stateParams) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this._super(_scope);
        },

        /**
         * @Override
         */
        defineScope: function () {
            //TODO
        }

    });

    FooterController.$inject = ['$rootScope', '$scope', '$stateParams'];

    angular.module('module.common.FooterController', []).controller('FooterController', FooterController);

});