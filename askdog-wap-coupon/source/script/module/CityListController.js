define([
    'base/BaseController',
    'service/StoreService',
    'angular-cookies'
], function (BaseController) {

    var CityListController = BaseController.extend({

        init: function (_rootScope, _scope, _stateParams, _uibModal,_storeService,_cookies,_state) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$uibModal = _uibModal;
            this.$storeService = _storeService;
            this.$cookies = _cookies;
            this.$state = _state;
            this._super(_scope);
        },

        defineScope: function () {
            this._defineViewHandler();
            this._refreshListView();
        },

        defineListeners: function () {
            var owner = this;
            this._areaChangeListener = this.$rootScope.$on('areaChange', function (event, address) {
                owner.$scope.address = address;
            });
        },

        destroy: function () {
            this._areaChangeListener();
            this._areaChangeListener = null;
        },

        _defineViewHandler: function () {
            var owner = this;
            var adcode = this.$cookies.get("ad_code");
            require(['_global'], function(_g) {
                _g.cities(function(cities) {
                    owner.$scope.areas = cities;
                    owner.$scope.address = cities.find(function(city) {
                        return city.code == adcode;
                    });
                    owner.$scope.$digest();
                });
            });

            this.$scope.selectAddress = function (address) {
                owner.$cookies.put("ad_code", address ? address.code : '00');
                owner.$rootScope.$broadcast('areaChange', address);
                owner.$state.go('layout.view.index');
            };
        },
        _refreshListView: function () {
        }

    });

    CityListController.$inject = ['$rootScope', '$scope', '$stateParams', '$uibModal','StoreService','$cookies','$state'];

    angular.module('module.CityListController', ['service.StoreService','ngCookies']).controller('CityListController', CityListController);

});