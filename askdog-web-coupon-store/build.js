({
	appDir: "./src/",
	baseUrl: "js",
	dir: "build/",
	modules: [
		{
			name: "main"
		},
		{
			name:                             "app/store",
			excludeShallow:                   ["app/template"]
		}
	],

	keepBuildDir: true,

	removeCombined: true,

	fileExclusionRegExp: /^lib/,

	optimizeCss: "standard",

	cssImportIgnore: 'lib/csshake.min.css',

	paths: {
		'lib':                                 '../lib',

		// requirejs plugins
		'domReady':                            '../lib/domReady/domReady',

		// jquery and plugins
		'jquery':                              '../lib/jquery/jquery.min',
		'jquery.validator':                    '../lib/jquery/validate',
		'jquery.velocity':                     '../lib/jquery/velocity',
		'jquery.qrcode':						 '../lib/jquery/qrcode/jquery.qrcode.min',

		// angular and plugins
		'angular':                             '../lib/angular/angular.min',
		'angular.ui.router':                   '../lib/angular/angular-ui-router.min',
		'angular.ui.bootstrap':                '../lib/angular/ui-bootstrap-tpls-2.0.0.min',
		'angular.oc.lazyLoad':                 '../lib/angular/oclazyload/ocLazyLoad.min',
		'angular.sanitize':                    '../lib/angular/angular-sanitize.min',
		'angular-messages':                    '../lib/angular/angular-messages.min',
		'angular-resource':                    '../lib/angular/angular-resource.min',
		'angular-touch':                       '../lib/angular/angular-touch.min',

		// editor md lib
		'katex':                               '../lib/editormd/lib/katex.min',
		'marked':                              '../lib/editormd/lib/marked.min',
		'prettify':                            '../lib/editormd/lib/prettify.min',
		'raphael':                             '../lib/editormd/lib/raphael.min',
		'underscore':                          '../lib/editormd/lib/underscore.min',
		'sequence-diagram':                    '../lib/editormd/lib/sequence-diagram.min',
		'flowchart':                           '../lib/editormd/lib/flowchart.min',
		'jqueryflowchart':                     '../lib/editormd/lib/jquery.flowchart.min',
		'editor-md':                           '../lib/editormd/editormd',

		//aliyun vod lib
		'aliyun-sdk':                          '../lib/aliyun/aliyun-sdk.min',
		'vod-sdk':                             '../lib/aliyun/vod-sdk-upload.min',

		//jquery plugins
		'jquery-plugin':                       '../lib/jquery-plugin',

		// bootstrap and plugins
		'bootstrap':                           '../lib/bootstrap/js/bootstrap.min',
		'class':                               '../lib/class',
		'namespace':                           '../lib/namespace'
	},

	//Remember: only use shim config for non-AMD scripts,
	//scripts that do not already call define(). The shim
	//config will not work correctly if used on AMD scripts,
	//in particular, the exports and init config will not
	//be triggered, and the deps config will be confusing
	//for those cases.
	shim: {
		'class': {
			exports: 'Class'
		},
		// exports
		'angular': {
			exports: 'angular'
		},

		// angular
		'angular.ui.router': ['angular'],
		'angular.ui.bootstrap': ['angular', 'bootstrap'],
		'angular.oc.lazyLoad': ['angular'],

		// dependencies
		// jquery
		'jquery.ui': ['jquery'],
		'bootstrap': ['jquery'],
		'jquery.validator': ['jquery'],
		'jquery.qrcode': ['jquery'],

		// editor md
		'sequence-diagram': ['flowchart'],
		'jqueryflowchart': ['flowchart'],
		'editor-md': ['marked', 'prettify', 'raphael', 'underscore', 'sequence-diagram', 'jqueryflowchart'],

		//OSS storage
		'vod-sdk': ['aliyun-sdk']
	}
})