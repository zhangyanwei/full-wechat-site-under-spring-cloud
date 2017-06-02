/**
 * Defines the main translates in the application.
 */
define([
	'angular.translate.loaderStaticFiles'
], function(app) {
	'use strict';

	angular.module('app.translate', [
		'pascalprecht.translate'
	])
	.config(['$translateProvider', function($translateProvider) {
		$translateProvider.useStaticFilesLoader({
			prefix: 'assets/i18n/locale-',
			suffix: '.json'
		});

		$translateProvider.preferredLanguage('zh-CN');
		$translateProvider.fallbackLanguage('zh-CN');
		// $translateProvider.useMissingTranslationHandlerLog();
		// if we want using local storage, should plugin following plugins.
		// https://github.com/angular-translate/bower-angular-translate-storage-local
		// $translateProvider.useLocalStorage();
	}])
	.run(['$rootScope', '$translate', function($rootScope, $translate) {
		$rootScope.lang = $translate.use();
	}]);
});