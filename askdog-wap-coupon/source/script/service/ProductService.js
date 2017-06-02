define(['base/BaseService'], function (BaseService) {

    var ProductService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        productDetail:function(id){
            return this.get('/api/products/' + id);
        },
        
        createVote:function(id,type){
            return this.put('/api/products/{productId}/vote?direction={type}'.format({"productId":id,"type":type}));
        }

    });

    angular.module('service.ProductService', [])
        .provider('ProductService', function () {
            this.$get = ['$http', '$rootScope', function (http, scope) {
                return new ProductService(http, scope);
            }];
        });

});