define([
    'base/BaseController'
], function (BaseController) {

    var AutumnController = BaseController.extend({
        init: function (_rootScope, _scope, _stateParams) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this._super(_scope);
        },

        /**
         * @Override
         */
        defineScope: function () {

        }
    });

    AutumnController.$inject = ['$rootScope', '$scope', '$stateParams'];

    angular.module('module.activity.AutumnController', []).controller('AutumnController', AutumnController);

});