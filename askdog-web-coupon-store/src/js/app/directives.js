/**
 * Defines un-managed directives in the application.
 * NOTE: only use this out side the customer controllers.
 */
define([
    'angular',
    'app/directive/pageTitle',
    'app/directive/afterRender',
    'app/directive/fileChange',
    'app/directive/qrCode'
], function () {
    'use strict';

    angular.module('app.directives', [
        'app.directive.pageTitle',
        'app.directive.afterRender',
        'app.directive.fileChange',
        'app.directive.qrCode'
    ]);
});