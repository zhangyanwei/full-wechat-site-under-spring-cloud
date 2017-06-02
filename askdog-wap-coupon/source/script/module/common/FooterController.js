define([
    'base/BaseController'
], function (BaseController) {

    var FooterController = BaseController.extend({

        init: function (_rootScope, _scope, _stateParams, _uibModal) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$uibModal = _uibModal;

            this._super(_scope);
        },

        defineScope: function () {
            this._defineViewHandler();
        },

        defineListeners: function () {
            this._contextChangeListener = this.$rootScope.$on('contextChange', function () {
            });
            this._contextReadyListener = this.$rootScope.$on('contextReady', function (event, userSelf) {
            });
        },

        destroy: function () {
            this._contextChangeListener();
            this._contextReadyListener();
            this._contextChangeListener = null;
            this._contextReadyListener = null;
        },

        _defineViewHandler: function () {
            var owner = this;
            // TODO
        },

        _refreshListView: function () {
            // TODO
        }

    });

    FooterController.$inject = ['$rootScope', '$scope', '$stateParams', '$uibModal'];

    angular.module('module.FooterController', []).controller('FooterController', FooterController);

});