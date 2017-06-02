define(['base/BaseService'], function (BaseService) {

    var CategoryService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        categories: function () {
            return this.get("/api/categories");
        },

        categoriesNested: function () {
            return this.get("/api/categories/nested");
        }


    });

    angular.module('service.CategoryService', [])
        .provider('CategoryService', function () {
            this.$get = ['$http', '$rootScope', function (http, scope) {
                return new CategoryService(http, scope);
            }];
        });

});