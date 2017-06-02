/**
 * Defines un-managed directives in the application.
 * NOTE: only use this out side the customer controllers.
 */
define([
    'angular',
    'app/filter/ellipsis',
    'app/filter/trust',
    'app/filter/trustSrc',
    'app/filter/durationFix',
    'app/filter/picTrans',
    'app/filter/percent'
], function () {
    'use strict';

    angular.module('app.filters', [
        'app.filter.ellipsis',
        'app.filter.trust',
        'app.filter.trustSrc',
        'app.filter.durationFix',
        'app.filter.picTrans',
        'app.filter.percent'
    ]);
});