define([
    'app/main',
    'app/routes',
    'angular.ui.bootstrap'
], function () {

    angular.module('exp', [
        'app',
        'app.routes',
        'ui.bootstrap'
    ]);

});