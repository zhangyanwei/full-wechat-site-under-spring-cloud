define([
    'base/BaseController'
], function (BaseController) {

    var FollowController = BaseController.extend({

        init: function (_rootScope, _scope, _stateParams, _uibModal) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$uibModal = _uibModal;

            this._super(_scope);
        },

        defineListeners: function () {
            var owner = this;
            this._contextChangeListener = this.$rootScope.$on('contextChange', function (event,userSelf) {
                owner.$rootScope.subscribed = (!!userSelf && userSelf.subscribed);
                owner._udpateTopStyle();
            });
            this._contextReadyListener = this.$rootScope.$on('contextReady', function (event, userSelf) {
                owner.$rootScope.subscribed = (!!userSelf && userSelf.subscribed);
                owner._udpateTopStyle();
            });
            this._viewContentLoadedListener = this.$rootScope.$on('$viewContentLoaded', function(){
                owner._udpateTopStyle();
            });
            this._closeListener = this.$scope.$watch('close', function() {
                owner._udpateTopStyle();
            });
        },

        destroy: function () {
            this._contextChangeListener();
            this._contextReadyListener();
            this._viewContentLoadedListener();
            this._closeListener();
        },

        _udpateTopStyle: function () {
            var mainContent = $('.main-content');
            this.$scope.show = this.$rootScope.subscribed != undefined && !this.$rootScope.subscribed && !this.$scope.close;
            if (this.$scope.show) {
                mainContent.css('padding-bottom', '106px');
            } else {
                mainContent.css('padding-bottom', '50px');
            }
        }

    });

    FollowController.$inject = ['$rootScope', '$scope', '$stateParams', '$uibModal'];

    angular.module('module.FollowController', []).controller('FollowController', FollowController);

});