define(['base/BaseService'], function (BaseService) {

    var ProductService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        createProduct: function (pureProduct) {
            return this.post('/api/products', pureProduct);
        },

        products: function (storeId, page, size) {
            var uri = '/api/products/management?store_id={storeId}&page={page}&size={size}'.format({
                storeId: storeId,
                page: page,
                size: size
            });
            return this.get(uri)
        },

        deleteProduct:function(storeId){
            return this.delete('/api/products/' + storeId);
        },

        getProductDetail:function(storeId){
            return this.get('/api/products/' + storeId);
        },

        updateProduct:function(productId,pureProduct){
            return this.put('/api/products/'+ productId, pureProduct);
        }
    });

    angular.module('service.ProductService', [])
        .provider('ProductService', function () {
            this.$get = ['$http', '$rootScope', function (http, scope) {
                return new ProductService(http, scope);
            }];
        });

});