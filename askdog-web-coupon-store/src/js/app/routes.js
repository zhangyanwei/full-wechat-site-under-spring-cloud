define([
    'angular.ui.router'
], function () {
    'use strict';

    angular.module('app.routes', ['ui.router']).config(['$stateProvider', '$urlRouterProvider',
        function ($stateProvider, $urlRouterProvider) {
            $urlRouterProvider.otherwise('/');
            $stateProvider
                .state('layout-main', {
                    abstract: true,
                    templateUrl: 'views/layout/main.html'
                })
                .state('welcome', {
                    url: '/welcome',
                    parent: 'layout-main',
                    templateUrl: 'views/welcome.html'
                })
                // error states
                .state('error', {
                    abstract: true,
                    templateUrl: 'views/layout/main.html'
                })
                .state('error.403', {
                    url: '/403',
                    parent: 'layout-main',
                    templateUrl: 'views/403.html'
                })
                // store states
                .state('store', {
                    abstract: true,
                    templateUrl: 'views/layout/main.html'
                })
                .state('store.verify', {
                    url: '/s/:storeId/verify',
                    templateUrl: 'views/store/coupon-verify.html'
                })
                .state('store.dashboard', {
                    url: '/s/:storeId/dashboard',
                    templateUrl: 'views/store/dashboard.html'
                })
                .state('store.history', {
                    url: '/s/:storeId/history',
                    templateUrl: 'views/store/coupon-history.html'
                })
                .state('store.product', {
                    url: '/s/:storeId/product',
                    templateUrl: 'views/store/product-management.html'
                })
                .state('store.coupon', {
                    url: '/s/:storeId/coupon',
                    templateUrl: 'views/store/coupon-management.html'
                })
                .state('store.event', {
                    url: '/s/:storeId/event',
                    templateUrl: 'views/store/event-management.html'
                })
                .state('store.employee', {
                    url: '/s/:storeId/employee',
                    templateUrl: 'views/store/employee-management.html'
                })
                // admin states
                .state('admin', {
                    abstract: true,
                    templateUrl: 'views/layout/main.html'
                })
                .state('admin.store', {
                    url: '/m/s',
                    templateUrl: 'views/admin/store-management.html'
                })
                .state('admin.dashboard', {
                    url: '/m/dashboard',
                    templateUrl: 'views/admin/store-dashboard.html'
                });
        }
    ]).run(['$rootScope', '$state', '$stateParams',
        function ($rootScope, $state, $stateParams) {
            window.session && ($rootScope.userinfo = window.session.user);
            $rootScope.$state = $state;
            $rootScope.$stateParams = $stateParams;
            $rootScope.hasRole = function (authority) {
                return $rootScope.userinfo
                    && $rootScope.userinfo.authorities
                    && $rootScope.userinfo.authorities.indexOf('ROLE_' + authority) > -1;
            };
            $rootScope.hasAnyRole = function (authorities) {
                return $rootScope.userinfo
                    && $rootScope.userinfo.authorities
                    && authorities.find(
                        function (authority, index) {
                            return $rootScope.userinfo.authorities.indexOf('ROLE_' + authority) > -1;
                        }
                    );
            };

        }
    ]);
});