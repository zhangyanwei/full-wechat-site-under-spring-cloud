define(['class'], function(Class) {

	var checkArgument = AskDogExp.Precondition.checkArgument;

	/**
	 * BaseController class as super class for all controllers in this application.
	 *
	 * Use of Class.js
	 */
	var BaseController = Class.extend({

		$scope:null,

		init:function(scope){

			checkArgument(scope, "scope is null, you should call this._super() to initialize the class in sub-class");

			this.$scope = scope;
			this.defineScope();
			this.defineCommands();
			this._defineDefaultListeners();
		},

		/**
		 * Initialize listeners needs to be overrided by the subclass.
		 */
		defineListeners:function(){
			//OVERRIDE
		},

		/**
		 * Use this function to define all scope objects.
		 * Give a way to instantaly view whats available
		 * publicly on the scope.
		 */
		defineScope:function(){
			//OVERRIDE
		},

		/**
		 * Define commands using Commands.js
		 * example:
		 * <code>
		 * Commands.new($scope, "checks")
		 * 	.register('checkAll', command()
		 * 		.label('全部')
		 *		.callback('execute', this._checkMessageCallback("checked", function() { return true; }))
		 *	)
		 *</code>
		 */
		defineCommands: function() {
			// OVERRIDE
		},

		/**
		 * Triggered when controller is about
		 * to be destroyed, clear all remaining values.
		 */
		destroy:function(event){
			//OVERRIDE
		},

		_defineDefaultListeners: function() {
			this.$scope.$on('$destroy',this.destroy.bind(this));
			this.defineListeners();
		}
	});

	BaseController.$inject = ['$scope'];

	return BaseController;

});