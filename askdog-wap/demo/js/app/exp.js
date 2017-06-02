define([
    'app/main',
    'app/routes',
    'app/directive/loader',
    'angular.ui.bootstrap'
], function() {
    angular.module('exp', [
        'app',
        'app.routes',
        'app.directive.loader',
        'ui.bootstrap'
    ]).provider('$auth', function () {
        this.$get = ['$window', '$stateParams', '$userService', function($window, $stateParams, $userService) {
            var userAgent = $window.navigator.userAgent;
            var matches = /MicroMessenger\/([^\s\/.]+)/.exec(userAgent);
            if (matches && matches.length > 1) {
                var version = matches[1];
                if (version >= 5) {
                    $userService.getStates()
                        .success(function (data) {
                            if (data == "ANONYMOUS") {
                                alert($stateParams.request || window.location.origin);
                                window.location.href = "/login/wechat?request=" + encodeURIComponent($stateParams.request || window.location.origin);
                            }
                        });
                }
            }
        }];
    })
    .run(['$rootScope',function($rootScope){
        $rootScope.CATEGORY_THUMBNAIL = AskDogExp.Constants.CATEGORY_THUMBNAIL;
    }]);

});