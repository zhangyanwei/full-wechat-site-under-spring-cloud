define(['angular'], function() {

    /**
     * loader - Directive for loader
     */
    function loader() {
        return {
            restrict: 'E',
            controller: ['$rootScope', '$scope', function($rootScope, $scope) {
                $rootScope.loadingCount = 0;
                $rootScope.$on('LOAD', function(event, message) {
                    $rootScope.loadingCount++;
                    $scope.notice = message.notice;
                    $scope.message = message.message;
                });
                $rootScope.$on('UNLOAD', function() {
                    $rootScope.loadingCount = Math.max(0, $rootScope.loadingCount - 1);
                });
            }]
        };
    }

    /**
     * Set Angular directive base on the class above,
     * this gives us a lot more flexibility and permits
     * inheritance over Directives this this is not recommanded,
     * with directive composition is better.
     */
    angular.module('app.directive.loader',[])
        .directive('loader', loader);
});