define([
    'app/main',
    'app/routes',
    'app/directive/loader',
    'angular.ui.bootstrap'
], function () {

    angular.module('exp', [
        'app',
        'app.routes',
        'app.directive.loader',
        'ui.bootstrap'
    ]).run(['$rootScope', function($rootScope){
        $rootScope.CATEGORY_THUMBNAIL = AskDog.Constants.CATEGORY_THUMBNAIL;
    }]);

});