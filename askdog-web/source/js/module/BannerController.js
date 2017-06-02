define(['base/BaseController', 'app/directive/carousel'], function (BaseController) {

    var BannerController = BaseController.extend({

        _contextChangeListener: null,

        init: function (_rootScope, _scope, _stateParams) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this._super(_scope);
        },

        defineScope: function () {
            this._defineScrollWatch();
        },

        defineListeners: function () {
            var owner = this;
            this._contextChangeListener = this.$rootScope.$on('contextChange', function (event, userSelf) {
                if (!userSelf) {
                    owner.$scope.open();
                }
            });
        },

        destroy: function () {
            this._contextChangeListener();
            this._contextChangeListener = null;
        },

        _defineScrollWatch: function () {
            var owner = this;
            this.$scope.open = function () {
                owner.$rootScope.bannerClosed = false;
                var scrollTop = $("#main").scrollTop();
                if (scrollTop < 240) {
                    $('.sidebar-container .nav-bar').css('top', (266 - scrollTop) + 'px');
                } else {
                    $('.sidebar-container .nav-bar').css('top', '56px');
                }
            };
            this.$scope.close = function () {
                owner.$rootScope.bannerClosed = true;
                $('.sidebar-container .nav-bar').css('top', '56px');
            };
        }
    });

    BannerController.$inject = ['$rootScope', '$scope', '$stateParams'];

    angular.module('module.BannerController', []).controller('BannerController', BannerController);

});