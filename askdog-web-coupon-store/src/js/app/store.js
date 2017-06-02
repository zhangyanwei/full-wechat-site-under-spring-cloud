define([
    'app/main',
    'app/routes',
    'angular.ui.bootstrap'
], function () {

    angular.module('store', [
        'app',
        'app.routes',
        'ui.bootstrap'
    ]);

});