define(['_global', 'angular.ui.router'], function (_g) {
    'use strict';

    angular.module('app.routes', ['ui.router']).config(['$stateProvider', '$urlRouterProvider',
        function ($stateProvider, $urlRouterProvider) {
            $urlRouterProvider.otherwise('/');
            $stateProvider
            /************************************************************layout**/
                .state('layout', {
                    abstract: true,
                    templateUrl: 'views/base/layout.html'
                })
                .state('layout.view', {
                    abstract: true,
                    templateUrl: 'views/base/view.html'
                })
                .state('layout.view-index', {
                    abstract: true,
                    templateUrl: 'views/base/view-index.html'
                })
                /************************************************************index**/
                .state('layout.view.index', options({
                    url: '/',
                    templateUrl: 'views/home.html'
                }))
                /************************************************************pages**/
                .state('layout.view.store', options({
                    url: '/store/:id',
                    templateUrl: 'views/store.html'
                }))
                .state('layout.view.event', options({
                    url: '/event/:id',
                    templateUrl: 'views/event.html'
                }))
                .state('layout.view.detail', options({
                    url: '/detail/:id',
                    templateUrl: 'views/detail.html'
                }))
                .state('layout.view.cash', options({
                    url: '/cash',
                    templateUrl: 'views/cash-list.html'
                }))
                .state('layout.view.cash-detail', options({
                    url: '/cash-detail/:id',
                    templateUrl: 'views/cash-detail.html'
                }))
                .state('layout.view.qr-code', options({
                    url: '/qr-code',
                    templateUrl: 'views/qr-code.html'
                }))
                .state('layout.view.contact-us', options({
                    url: '/contact-us',
                    templateUrl: 'views/contact-us.html'
                }))
                .state('layout.view.city-list', options({
                    url: '/city-list',
                    templateUrl: 'views/city-list.html'
                }))
                .state('layout.view.maintenance', options({
                    url: '/maintenance',
                    templateUrl: 'views/maintenance.html'
                }))
                .state('employee_confirm', options({
                    url: '/t/:token',
                    templateUrl: 'views/store/employee-confirm.html'
                }))
                .state('coupon_confirm', options({
                    url: '/stores/:storeId/coupons/:id/consume',
                    templateUrl: 'views/store/consume-confirm.html'
                }));
        }
    ]).run(['$rootScope', '$state', '$stateParams',
        function ($rootScope, $state, $stateParams) {
            var skip = false;
            $rootScope.$state = $state;
            $rootScope.$stateParams = $stateParams;
            $rootScope.$on('$stateChangeStart', function(event, toState, toParams) {
                if (!skip) {
                    event.preventDefault();
                    _g.authenticateIfRequired(path(toState, toParams), function () {
                        skip = true;
                        // $urlRouter.sync();
                        $state.go(toState, toParams);
                    });
                }
                skip = false;
            });
            $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams){
                $rootScope.$state.previous = {
                    state: fromState,
                    params: fromParams
                };
            });
            $rootScope.$on('$viewContentLoaded', function(){
                document.getElementsByTagName("body")[0].scrollTop = 0;
            });
        }
    ]);

    function path(state, params) {
        var path = (state.url || '/').replace(/:(\w+)/g, function(m, k) {
            return params[k];
        });
        var query = _g.loc.query();
        query && (path += "?" + query);
        return path;
    }

    function options(options) {
        var defaultOptions = {
            controller: ['$rootScope', '$state', function ($rootScope, $state) {
                _hmt.push(['_trackPageview', path($state.current, $state.params)]);
            }]
        };
        return angular.extend(defaultOptions, options);
    }
});