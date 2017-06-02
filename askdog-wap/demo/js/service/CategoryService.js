define([
    'base/BaseService',
    'angular',
    'class'
], function (BaseService) {

    var CategoryService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        categoriesNested: function () {
            return this.get("/api/categories/nested");
        }
    });

    angular.module('service.CategoryService', [])
        .provider('categoryService', function () {
            this.$get = ['$http', '$rootScope', function(http, scope) {
                return new CategoryService(http, scope);
            }];
        });

});