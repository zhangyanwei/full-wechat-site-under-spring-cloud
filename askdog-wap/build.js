({
	appDir: "./demo/",
	baseUrl: "js",
	dir: "build/",
	modules: [
		{
			name: "main"
		},
		{
			name:                             "app/exp",
			excludeShallow:                   ["app/template"]
		},
		{
			name: "pay"
		},
		{
			name:                             "app/pay",
			excludeShallow:                   ["app/template"]
		}
	],

	keepBuildDir: true,

	fileExclusionRegExp: /^lib/,

	optimizeCss: "standard",

	paths:   {
		'lib':                                 '../lib',

		// requirejs plugins
		'domReady':                            '../lib/domReady/domReady',

		// jquery and plugins
		'jquery':                              '../lib/jquery/jquery.min',
		'validate':                            '../lib/jquery-plugin/validate',
		'validator':                           '../lib/jquery-plugin/validator/jquery.validate',
		// angular and plugins
		'angular':                             '../lib/angular/angular.min',
		'angular.ui.router':                   '../lib/angular/angular-ui-router.min',
		'angular.ui.bootstrap':                '../lib/angular/ui-bootstrap-tpls-2.0.0.min',
		'angular.translate':                   '../lib/angular/angular-translate.min',
		'angular.translate.loaderStaticFiles': '../lib/angular/angular-translate-loader-static-files.min',
		'angular.oc.lazyLoad':                 '../lib/angular/oclazyload/ocLazyLoad.min',
		'angular.sanitize':                    '../lib/angular/angular-sanitize.min',
		'angular-messages':                    '../lib/angular/angular-messages.min',
		'angular-resource':                    '../lib/angular/angular-resource.min',
		'angular-touch':                       '../lib/angular/angular-touch.min',

		// Add for editor md
		'katex':                               '../lib/editormd/lib/katex.min',
		'marked':                              '../lib/editormd/lib/marked.min',
		'prettify':                            '../lib/editormd/lib/prettify.min',
		'raphael':                             '../lib/editormd/lib/raphael.min',
		'underscore':                          '../lib/editormd/lib/underscore.min',
		'sequence-diagram':                    '../lib/editormd/lib/sequence-diagram.min',
		'flowchart':                           '../lib/editormd/lib/flowchart.min',
		'jqueryflowchart':                     '../lib/editormd/lib/jquery.flowchart.min',
		'editor-md':                           '../lib/editormd/editormd',
		// Add for editor md />

		// bootstrap and plugins
		'bootstrap':                           '../lib/bootstrap/bootstrap',
		'class':                               '../lib/class',
		'namespace':                           '../lib/namespace',

		//jquery plugins
		'jquery-plugin':                       '../lib/jquery-plugin',
		"jquery-toggle-password":              '../lib/jquery-plugin/jquery.toggle-password',
		'jquery.velocity':                     '../lib/jquery-plugin/velocity',

		// weixin
		'weixin':                              'http://res.wx.qq.com/open/js/jweixin-1.0.0'

	},

	//Remember: only use shim config for non-AMD scripts,
	//scripts that do not already call define(). The shim
	//config will not work correctly if used on AMD scripts,
	//in particular, the exports and init config will not
	//be triggered, and the deps config will be confusing
	//for those cases.
	shim: {
		'class':   {
			exports: 'Class'
		},
		// exports
		'angular': {
			exports: 'angular'
		},

		// angular
		'angular.ui.router':                   ['angular'],
		'angular.ui.bootstrap':                ['angular', 'bootstrap'],
		'angular.translate':                   ['angular'],
		'angular.translate.loaderStaticFiles': ['angular.translate'],
		'angular.oc.lazyLoad':                 ['angular'],

		// twitter bootstrap
		'bootstrap': ['jquery'],

		// Add for editor md
		'sequence-diagram': ['flowchart'],
		'jqueryflowchart': ['flowchart'],
		// 'katex'
		'editor-md': ['marked', 'prettify', 'raphael', 'underscore', 'sequence-diagram', 'flowchart', 'jqueryflowchart'],
		// Add for editor md />
		'validate':['validator']
	}
})