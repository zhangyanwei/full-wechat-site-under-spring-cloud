define([
    'base/BaseController',
    '_global',
    'angular-cookies'
], function (BaseController, _g) {

    var HeaderController = BaseController.extend({

        init: function (_rootScope, _scope, _state, _cookies) {
            this.$rootScope = _rootScope;
            this.$state = _state;
            this.$cookies = _cookies;
            this._super(_scope);
        },

        defineScope: function () {
            this._defineViewHandler();
            this._defineToggle();
            this._refreshListView();
        },

        defineListeners: function () {
            var owner = this;
            this._contextChangeListener = this.$rootScope.$on('contextChange', function () {
            });
            this._contextReadyListener = this.$rootScope.$on('contextReady', function (event, userSelf) {
            });
            this._areaChangeListener = this.$rootScope.$on('areaChange', function (event, address) {
                owner.$scope.address = address;
            });
        },

        destroy: function () {
            this._contextChangeListener();
            this._contextReadyListener();
            this._contextChangeListener = null;
            this._contextReadyListener = null;
            this._areaChangeListener();
            this._areaChangeListener = null;
        },

        _defineViewHandler: function () {
            var owner = this;
            var adcode = this.$cookies.get("ad_code");
            _g.cities(function(cities) {
                owner.$scope.address = cities.find(function(city) {
                    return city.code == adcode;
                });
                owner.$scope.$digest();
            });
        },

        _defineToggle: function () {
            this.$scope.toggle = function(toggle) {
                if (toggle) {
                    this.$state.go(this.$state.previous.state.name || 'layout.view.index', this.$state.previous.params);
                } else {
                    this.$state.go('layout.view.city-list');
                }

            }.bind(this);
        },

        _refreshListView: function () {
            this.$scope.display = !_g.isWechat();
        }

    });

    HeaderController.$inject = ['$rootScope', '$scope', '$state', '$cookies'];

    angular.module('module.HeaderController', ['ngCookies']).controller('HeaderController', HeaderController);

});