/**
 * Defines un-managed directives in the application.
 * NOTE: only use this out side the customer controllers.
 */
define([
    'angular',
    'app/filter/count',
    'app/filter/created',
    'app/filter/ellipsis',
    'app/filter/trust',
    'app/filter/pages',
    'app/filter/picTrans',
    'app/filter/distance',
    'app/filter/durationFix'
], function () {
    'use strict';

    angular.module('app.filters', [
        'app.filter.count',
        'app.filter.created',
        'app.filter.ellipsis',
        'app.filter.trust',
        'app.filter.pages',
        'app.filter.picTrans',
        'app.filter.distance',
        'app.filter.durationFix'
    ]);
});