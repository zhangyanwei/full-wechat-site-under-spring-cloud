define([
    'base/BaseController',
    '_global',
    'service/StoreService',
    'service/QQMapService',
    'angular-cookies'
], function (BaseController, _g) {

    var HomeController = BaseController.extend({

        _location: undefined,

        _AD_CODE: undefined,

        init: function (_rootScope, _scope, _stateParams, _uibModal, _storeService, _qqmapService, _cookies, _state) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$uibModal = _uibModal;
            this.$storeService = _storeService;
            this.$qqmapService = _qqmapService;
            this.$cookies = _cookies;
            this.$state = _state;
            this._super(_scope);
        },

        defineScope: function () {
            this._defineViewHandler();
            this._refreshListView();
            this._scrollShowMore();
        },

        _defineViewHandler: function () {
            var owner = this;
            // this.$scope.loading = true;
            this.$scope.reload = function () {
                owner._defaultView();
            };
            this.$scope.totalDiscount = function(coupons) {
                var total = 0;
                if (coupons) {
                    for (var i = 0; i < coupons.length; i++) {
                        total += Number(coupons[i].rule) || 0;
                    }
                }
                return total;
            };
        },

        _defaultView: function () {
            var adCode = this.$cookies.get("ad_code");
            var cookieLocation = this.$cookies.get("location");
            var location = cookieLocation && cookieLocation != "none" ? JSON.parse(cookieLocation) : undefined;
            this._listStores(0, location, adCode == 'none' ? '00' : adCode);
        },

        _refreshListView: function () {
            var owner = this;
            var adCode = this.$cookies.get("ad_code");
            var cookieLocation = this.$cookies.get("location");

            if (adCode && cookieLocation) {
                var location = cookieLocation != "none" ? JSON.parse(cookieLocation) : undefined;
                return this._listStores(0, location, adCode == 'none' ? '00' : adCode);
            }

            if (!_g.isWechat()) {
                return this._defaultView();
            }

            function updateLocationAndRefreshView(res) {
                owner.$cookies.put("location", JSON.stringify(res), {"expires" : expires()});
                if (adCode) {
                    owner._listStores(0, res, adCode);
                } else {
                    owner.$qqmapService.address(res).then(
                        function (resp) {
                            updateAdCodeAndRefreshView(resp, res);
                        },
                        function (resp) {
                            owner._defaultView();
                        }
                    );
                }
            }

            function updateAdCodeAndRefreshView(resp, res) {
                var adcode = resp.data.result.ad_info.adcode.substring(0, 4) + '00',
                    city = resp.data.result.ad_info.city;
                _g.cities(function (cities) {
                    var exists = cities.find(function (city) {
                        return city.code == adcode;
                    });
                    owner.$cookies.put("ad_code", exists ? adcode : '00');
                    owner.$rootScope.$broadcast('areaChange', exists ? {
                        name: city,
                        code: adcode
                    } : undefined);
                    owner._listStores(0, res, exists ? adcode : '00');
                });
            }

            require(['wechat'], function (wx) {
                wx.ready(function () {
                    wx.getLocation({
                        type: 'gcj02', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
                        success: function (res) {
                            updateLocationAndRefreshView(res);
                        },
                        cancel: function (resp) {
                            owner.$cookies.put("location", "none", {"expires" : expires()});
                            owner._defaultView();
                        },
                        fail: function (resp) {
                            owner.$cookies.put("location", "none", {"expires" : expires()});
                            owner._defaultView();
                        }
                    });
                });
            });
        },

        _listStores: function (page, location, adCode) {
            var owner = this;
            if (!this.$scope.loading) {
                this.$scope.loading = true;
                this.$storeService.stores(page, 50, location, adCode).then(
                    function (resp) {
                        owner._searchSuccessHandler(resp.data);
                        owner.$scope.loading = false;
                    },
                    function (resp) {
                        owner.$scope.loading = false;
                        owner.$scope.loadFailed = true;
                    }
                );
            }
        },

        _searchSuccessHandler: function (data) {
            this.$scope.viewList
                    && this.$scope.viewList.result && (data.result = this.$scope.viewList.result.concat(data.result));
            this.$scope.viewList = data;
        },

        _scrollShowMore: function () {
            var owner = this;
            window.addEventListener("scroll", function () {
                if (!owner.$scope.loading) {
                    if (Math.ceil($(document).scrollTop()) >= $(document).height() - $(window).height() && !owner.$scope.viewList.last) {
                        owner._listStores((owner.$scope.viewList.page + 1));
                    }
                }
            });
        }
    });

    function expires() {
        var expires = new Date();
        expires.setMinutes(expires.getMinutes() + 10);
        return expires;
    }

    HomeController.$inject = ['$rootScope', '$scope', '$stateParams', '$uibModal', 'StoreService', 'QQMapService', '$cookies', '$state'];

    angular.module('module.HomeController', ['service.StoreService', 'service.QQMapService', 'ngCookies']).controller('HomeController', HomeController);

});