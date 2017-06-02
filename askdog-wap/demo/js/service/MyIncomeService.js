define([
	'base/BaseService',
	'angular',
	'class'
], function (BaseService) {

	var MyIncomeService = BaseService.extend({

		init: function (http, scope) {
			this._super(http, scope);
		},
		getTotal: function () {
			// 
            return this.get("/api/myincome");
        },
        getList: function () {
        	// 
        	return this.get("/api/myincome");
        }

	});

	angular.module('service.MyIncomeService', [])
		.provider('myIncomeService', function () {
			this.$get = ['$http', '$rootScope', function(http, scope) {
				return new MyIncomeService(http, scope);
			}];
		});

});