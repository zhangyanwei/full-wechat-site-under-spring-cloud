define([
    'base/BaseController',
    'service/UserService'
], function (BaseController) {

    var AuthController = BaseController.extend({

        init: function (_rootScope, _scope, _stateParams, _uibModal, _state, _userService) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$uibModal = _uibModal;
            this.$state = _state;

            this.$userService = _userService;
            this._super(_scope);
        },

        defineScope: function () {
            this._defineSignIn();
            this._defineSignOut();
            this._authRequire();
        },

        defineListeners: function () {
            var owner = this;
            this._contextChangeListener = this.$rootScope.$on('contextChange', function () {
                owner._authRequire();
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

        _authRequire: function () {
            //User status & me info
            var owner = this;
            this.$userService.status().then(
                function (resp) {
                    if (resp.data == "AUTHENTICATED") {
                        owner.$userService.me().then(
                            function (resp) {
                                owner.$rootScope.userSelf = resp.data;
                                owner.$rootScope.$broadcast('contextReady', resp.data);
                                _hmt.push(['_setCustomVar', 1, 'visitor', 'AUTHENTICATED']);
                            }
                        );
                    } else if (resp.data == "ANONYMOUS") {
                        owner.$rootScope.userSelf = undefined;
                        owner.$rootScope.$broadcast('contextReady', undefined);
                        _hmt.push(['_setCustomVar', 1, 'visitor', 'ANONYMOUS']);
                    }
                }
            );
        },

        _defineSignIn: function () {
            var owner = this;
            this.$rootScope.signIn = function (authenticated, cancelled) {
                owner.$rootScope.signInOpened = true;
                var signInModal = owner.$uibModal.open({
                    windowTemplateUrl: 'views/dialog/modal-window.html',
                    windowTopClass: 'modal-default',
                    templateUrl: 'views/dialog/sign-in.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                    }]
                });

                signInModal.result.then(function () {
                    owner.$rootScope.signInOpened = false;
                    authenticated && authenticated.call(this);
                }, function () {
                    owner.$rootScope.signInOpened = false;
                    cancelled && cancelled.call(this);
                });
            };
        },

        _defineSignOut: function () {
            var owner = this;
            this.$rootScope.signOut = function () {
                owner.$userService.logout().then(
                    function () {
                        owner.$rootScope.userSelf = undefined;
                        owner.$state.go('layout.view.index');
                        owner.$rootScope.$broadcast('contextChange');
                    }
                );
            }
        }
    });

    AuthController.$inject = ['$rootScope', '$scope', '$stateParams', '$uibModal', '$state', 'UserService'];

    angular.module('module.AuthController', ['service.UserService']).controller('AuthController', AuthController);

});