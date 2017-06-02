define([
    'angular.ui.router',
    'service/UserService'
], function () {
    'use strict';

    angular.module('app.routes', ['ui.router', 'service.UserService'])
        .config(['$stateProvider', '$urlRouterProvider',
            function ($stateProvider, $urlRouterProvider) {
                $urlRouterProvider.otherwise('/');
                $stateProvider
                    .state('text-view', stateOptions({
                        url: '/view/:experienceId',
                        templateUrl: 'views/text-view.html'
                    }))
                    .state('video-view', stateOptions({
                        url: '/video/:experienceId',
                        templateUrl: 'views/video-view.html'
                    }))
                    /****************** view *****************/
                    .state('view-default', stateOptions({
                        abstract: true,
                        templateUrl: 'views/base/view-default.html'
                    }))
                    .state('view-profile', stateOptions({
                        abstract: true,
                        templateUrl: 'views/base/view-profile.html'
                    }))
                    .state('view-sign', stateOptions({
                        abstract: true,
                        templateUrl: 'views/base/view-sign.html'
                    }))
                    .state('view', stateOptions({
                        abstract: true,
                        templateUrl: 'views/base/view.html'
                    }))
                    /****************** index *****************/
                    .state('view-default.index', {
                        url: '',
                        templateUrl: 'views/home.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    })
                    .state('view-default.index.main', {
                        url: '/',
                        parent: 'view-default.index'
                    })
                    .state('view-default.category', {
                        url: '/category/:categoryCode',
                        templateUrl: 'views/category.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    })
                    /****************** view no-header ***************/
                    .state('view-profile.subscribe', {
                        url: '/subscribe',
                        templateUrl: 'views/subscribe.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    })
                    .state('view-profile.zone', {
                        url: '/my/zone',
                        templateUrl: 'views/my-zone.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    })
                    .state('view-profile.find', {
                        url: '/find',
                        templateUrl: 'views/find.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    })
                    /*************** view no footer no header *****************/
                    .state('view.login', {
                        url: '/login?request',
                        templateUrl: 'views/login.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    })
                    .state('view.register', {
                        url: '/register',
                        templateUrl: 'views/register.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    })
                    .state('view.psdReset', {
                        url: '/pwd/:token',
                        templateUrl: 'views/psd-reset.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    })
                    .state('view.default-subscribe', {
                        url: '/default-subscribe',
                        templateUrl: 'views/default-subscribe.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    })
                    .state('view.bind-weChat', {
                        url: '/wx/bind/:token',
                        templateUrl: 'views/bind-weChat.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    })
                    .state('view.pay', stateOptions({
                        url: '../pay/pay.html/{id:[0-9]+}',
                        templateUrl: 'views/pay-test.html',
                        data: {
                            title: '经验分享社区'
                        }
                    }))
                    .state('view.detail', {
                        url: '/exp/{id:[0-9]+}',
                        templateUrl: 'views/detail.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    })
                    .state('view.report', {
                        url: '/report?reportId',
                        templateUrl: 'views/report.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    })
                    .state('view.subscribe', stateOptions({
                        url: '/my-subscribe?id',
                        templateUrl: 'views/my-subscribe.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    }))
                    .state('view.profile', stateOptions({
                        authRequired: true,
                        url: '/my/profile',
                        templateUrl: 'views/profile.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    }))
                    .state('view.profileEdit', stateOptions({
                        authRequired: true,
                        url: '/profile-edit?type',
                        templateUrl: 'views/profile-edit.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    }))
                    .state('view.income', stateOptions({
                        authRequired: true,
                        url: '/my/income',
                        templateUrl: 'views/my-income.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    }))
                    .state('view.history', stateOptions({
                        authRequired: true,
                        url: '/my/history',
                        templateUrl: 'views/history.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    }))
                    .state('view.securityCenter', stateOptions({
                        authRequired: true,
                        url: '/security-center',
                        templateUrl: 'views/security-center.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    }))
                    .state('view.national', {
                        url: '/national',
                        templateUrl: 'views/activity/national.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    })
                    .state('view.channel', stateOptions({
                        authRequired: true,
                        url: '/my/{channel:(?:channel|subscribe)}',
                        templateUrl: 'views/channel.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    }))
                    .state('view.myChannel', stateOptions({
                        url: '/channel/:id',
                        templateUrl: 'views/my-channel.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    }))
                    .state('view.registeValidation', stateOptions({
                        url: '/reg/:token',
                        templateUrl: 'views/registe-validation.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    }))
                    .state('view.cash', stateOptions({
                        authRequired: true,
                        url: '/my/cash',
                        templateUrl: 'views/cash.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    }))
                    .state('view.notice', {
                        url: '/notice',
                        templateUrl: 'views/notice.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    })
                    .state('view.ranking', {
                        url: '/ranking-list',
                        templateUrl: 'views/activity/ranking-list.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    })
                    .state('view.activity', {
                        url: '/activity',
                        templateUrl: '/gold-bucket.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    })
                    .state('view.search', {
                        url: '/search',
                        templateUrl: 'views/search.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    })
                    .state('view.search-result', {
                        url: '/search-result/:searchKey',
                        templateUrl: 'views/search-result.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    });

                /******************* view no footer *************************/
                // // TODO delete it and related files
            }
        ])
        .run(['$rootScope', '$state', '$stateParams',
            function ($rootScope, $state, $stateParams) {
                $rootScope.$state = $state;
                $rootScope.$stateParams = $stateParams;
            }
        ]);

    function stateOptions(options) {
        var defaultOptions = {
            controller: ['$rootScope', '$scope', '$state', '$window', 'userService', 'auth', function ($rootScope, $scope, $state, $window, $userService, auth) {

                var userAgent = $window.navigator.userAgent;
                var matches = /MicroMessenger\/([^\s\/.]+)/.exec(userAgent);
                if (matches && matches.length > 1) {
                    var version = matches[1];
                    $scope.wechat = version >= 5;
                }
                $userService.getStates()
                    .success(function (data) {
                        if (data != "ANONYMOUS") {
                            $userService.getUserInfo()
                                .success(function (data) {
                                    if (data.type == "EXTERNAL") {
                                        $scope.thirdLogin = true;
                                    }
                                });
                        }
                    });
                $scope.auth = auth;
                $scope.authed = false;
                if (options.authRequired) {
                    $userService.getStates()
                        .success(function (data) {
                            if (data == "ANONYMOUS") {
                                $scope.auth(window.location.href);
                            } else {
                                $scope.authed = true;
                            }
                        });
                }

                // title should be defined in resolve section, but not work !
                if ($state.current.data) {
                    var title = $state.current.data.title;
                    title && ($rootScope.title = title);
                    var subTitle = $state.current.data.subTitle;
                    subTitle && ($rootScope.subTitle = subTitle);
                }
            }]
        };

        return angular.extend(defaultOptions, options);
    }
});