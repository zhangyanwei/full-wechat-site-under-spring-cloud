define([
    'angular.ui.router'
], function () {
    'use strict';

    angular.module('app.routes', ['ui.router']).config(['$stateProvider', '$urlRouterProvider',
        function ($stateProvider, $urlRouterProvider) {
            $urlRouterProvider.otherwise('/');
            $stateProvider
                .state('text-view', stateOptions({
                    url: '/view/:experienceId',
                    templateUrl: 'views/experience/text-view.html'
                }))
                /************************************************************layout**/
                .state('layout', stateOptions({
                    abstract: true,
                    templateUrl: 'views/base/layout.html'
                }))
                .state('layout.view', stateOptions({
                    abstract: true,
                    templateUrl: 'views/base/view.html'
                }))
                .state('layout.view-index', stateOptions({
                    abstract: true,
                    templateUrl: 'views/base/view-index.html'
                }))
                .state('layout.view-default', stateOptions({
                    abstract: true,
                    templateUrl: 'views/base/view-default.html'
                }))
                .state('layout.view-profile', stateOptions({
                    abstract: true,
                    templateUrl: 'views/base/view-profile.html'
                }))
                .state('layout.view-footer', stateOptions({
                    abstract: true,
                    templateUrl: 'views/base/view-footer.html'
                }))
                /************************************************************register ... **/
                .state('layout.view.register', {
                    url: '/reg/:token',
                    templateUrl: 'views/register-confirm.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                .state('layout.view.resetPassword', {
                    url: '/pwd/:token',
                    templateUrl: 'views/reset-password.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                /************************************************************index**/
                .state('layout.view-index.index', {
                    url: '',
                    templateUrl: 'views/home.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                .state('layout.view-index.index.main', {
                    url: '/',
                    parent: 'layout.view-index.index'
                })
                /************************************************************index page**/
                .state('layout.view-default.search', {
                    url: '/search?key',
                    templateUrl: 'views/search.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                .state('layout.view-index.hot', {
                    url: '/hot',
                    templateUrl: 'views/hot.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                .state('layout.view-index.category', {
                    url: '/category/:category',
                    templateUrl: 'views/category.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                .state('layout.view-index.category.list', {
                    url: '/:categoryCode'
                })

                /************************************************************home page**/
                .state('layout.view-default.zone', {
                    url: '/zone/:userId',
                    templateUrl: 'views/zone.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                .state('layout.view-default.recommend', {
                    url: '/recommend',
                    templateUrl: 'views/recommend.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                .state('layout.view-default.history', {
                    url: '/history',
                    templateUrl: 'views/history.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                /************************************************************detail page**/
                .state('layout.view-default.channel', {
                    url: '/channel/:channelId',
                    templateUrl: 'views/channel/detail.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                .state('layout.view-default.experience', {
                    url: '/experience/:experienceId',
                    templateUrl: 'views/experience/detail.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                // sync wap for experience detail
                .state('layout.view-default.exp', {
                    url: '/exp/:experienceId',
                    templateUrl: 'views/experience/detail.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                .state('layout.view.share-video', {
                    url: '/experience/share/video?expId&activity',
                    templateUrl: 'views/experience/share/video.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                .state('layout.view.share-word', {
                    url: '/experience/share/word?expId',
                    templateUrl: 'views/experience/share/word.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                /************************************************************profile page**/
                .state('layout.view-profile.profile', {
                    url: '/profile',
                    templateUrl: 'views/profile.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                .state('layout.view-profile.account', {
                    url: '/account',
                    templateUrl: 'views/profile/account.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                .state('layout.view-profile.security', {
                    url: '/security',
                    templateUrl: 'views/profile/security.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                /************************************************************notice page**/
                .state('layout.view-default.notice', {
                    url: '/notice',
                    templateUrl: 'views/notice.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                /************************************************************activity page**/
                .state('layout.view.activity-video', {
                    url: '/activity/share/video',
                    templateUrl: 'views/activity/share/video.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                /****************************************************************footer page**/
                .state('layout.view-footer.activity', {
                    url: '/activity',
                    templateUrl: 'views/pages/activity.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                .state('layout.view-footer.coming-soon', {
                    url: '/coming-soon',
                    templateUrl: 'views/pages/coming-soon.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                .state('layout.view-footer.about-us', {
                    url: '/about-us',
                    templateUrl: 'views/pages/about-us.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                .state('layout.view-footer.join-us', {
                    url: '/join-us',
                    templateUrl: 'views/pages/join-us.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                .state('layout.view-footer.contact', {
                    url: '/contact',
                    templateUrl: 'views/pages/contact.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                .state('layout.view-footer.autumn', {
                    url: '/autumn-activity',
                    templateUrl: 'views/activity/autumn.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })
                .state('layout.view-footer.download', {
                    url: '/app-download',
                    templateUrl: 'views/pages/app-download.html',
                    data: {
                        title: 'MAIN.TITLE'
                    }
                })


        }
    ]).run(['$rootScope', '$state', '$stateParams',
        function ($rootScope, $state, $stateParams) {
            $rootScope.$state = $state;
            $rootScope.$stateParams = $stateParams;
        }
    ]);

    function stateOptions(options) {
        var defaultOptions = {
            controller: ['$rootScope', '$state', function ($rootScope, $state) {
                // title should be defined in resolve section, but not work !
                if ($state.current.data) {
                    var title = $state.current.data.title;
                    title && ($rootScope.title = title);
                }
            }]
        };
        return angular.extend(defaultOptions, options);
    }
});