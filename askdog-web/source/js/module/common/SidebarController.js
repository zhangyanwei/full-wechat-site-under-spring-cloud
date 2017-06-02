define(['base/BaseController', 'service/UserService', 'service/CategoryService'], function (BaseController) {

    var SidebarController = BaseController.extend({

        _contextReadyListener: null,
        _sidebarRefreshListener: null,

        init: function (_rootScope, _scope, _stateParams, _userService, _categoryService) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$userService = _userService;
            this.$categoryService = _categoryService;
            this._super(_scope);
        },

        defineScope: function () {
            this._loadSystemCategories();
            if (this.$rootScope.userSelf) {
                this._refreshSubscribedChannels();
            }
        },

        defineListeners: function () {
            var owner = this;
            this._contextReadyListener = this.$rootScope.$on('contextReady', function (event, userSelf) {
                if (userSelf && !owner.$scope.subscribedChannels) {
                    owner._refreshSubscribedChannels();
                } else {
                    owner.$scope.subscribedChannels = undefined;
                }
            });

            this._sidebarRefreshListener = this.$rootScope.$on('sidebarRefresh', function () {
                owner._refreshSubscribedChannels();
            });
        },

        destroy: function () {
            this._contextReadyListener();
            this._sidebarRefreshListener();
            this._contextReadyListener = null;
            this._sidebarRefreshListener = null;
        },

        _loadSystemCategories: function () {
            var owner = this;
            if (!owner.$rootScope.sysCategories) {
                this.$categoryService.categoriesNested().then(
                    function (resp) {
                        owner.$rootScope.sysCategories = resp.data;
                    }
                );
            }
        },

        _refreshSubscribedChannels: function () {
            var owner = this;
            this.$userService.subscribedChannels(0, 10).then(
                function (resp) {
                    owner.$scope.subscribedChannels = resp.data.result;
                }
            );
        }

    });

    SidebarController.$inject = ['$rootScope', '$scope', '$stateParams', 'UserService', 'CategoryService'];

    angular.module('module.common.SidebarController', ['service.UserService', 'service.CategoryService']).controller('SidebarController', SidebarController);

});