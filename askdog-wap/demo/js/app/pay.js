define([
    'app/main',
    'app/directive/loader',
    'angular.ui.bootstrap',
    'angular.ui.router',
    'service/UserService'
], function() {

    angular.module('app.routes', ['ui.router', 'service.UserService'])
        .config(['$stateProvider', '$urlRouterProvider',
            function ($stateProvider, $urlRouterProvider) {
                $urlRouterProvider.otherwise('/pay');
                $stateProvider
                    .state('index', stateOptions({
                        abstract: true,
                        templateUrl: '/views/common/pay.html'
                    }))
                    .state('index.pay', stateOptions({
                        authRequired: true,
                        url: '',
                        templateUrl: '/views/pay-test.html',
                        data: {
                            title: 'MAIN.TITLE'
                        }
                    }))
            }
        ])
        .run(['$rootScope', '$state', '$stateParams',
            function ($rootScope, $state, $stateParams) {
                $rootScope.$state = $state;
                $rootScope.$stateParams = $stateParams;
            }
        ]);

    angular.module('pay', [
        'app',
        'app.routes',
        'app.directive.loader',
        'ui.bootstrap'
    ]);

    function stateOptions(options) {
        var defaultOptions = {
            controller: ['$rootScope', '$state', '$window', 'userService', function ($rootScope, $state, $window, $userService) {

                function tryAuth() {
                    var userAgent = $window.navigator.userAgent;
                    var matchs = /MicroMessenger\/([^\s\/.]+)/.exec(userAgent);
                    if (matchs && matchs.length > 1) {
                        var version = matchs[1];
                        if (version >= 5) {
                            window.location.href = "/login/wechat?request=" + encodeURIComponent(window.location.href);
                        }
                    }
                }

                if (options.authRequired) {
                    $userService.getStates()
                        .success(function(data){
                            if(data == "ANONYMOUS") {
                                tryAuth();
                            }
                        });
                }

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