define(['base/BaseService'], function (BaseService) {

    var StoreService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        list: function (page, size) {
            return this.get("/api/stores?page={page}&size={size}".format({"page": page, "size": size}));
        },

        getEmployeeBindToken: function (storeId) {
            return this.get("/api/stores/{0}/employee-bind-token".format(storeId));
        },

        getEmployees: function (storeId, page, size) {
            return this.get("/api/stores/{storeId}/employees?page={page}&size={size}".format({
                "storeId": storeId,
                "page": page,
                "size": size
            }));
        },

        deleteEmployee: function (storeId, employeeId) {
            return this.delete("/api/stores/{storeId}/employees/{employeeId}".format({
                "storeId": storeId,
                "employeeId": employeeId
            }));
        },

        storeList: function (page, size) {
            return this.get('/api/stores/list?page={page}&size={size}'.format({"page": page, "size": size}));
        },

        deleteStoreInfo: function (id) {
            return this.delete('/api/stores/' + id);
        },

        getStoreDetail: function (storeId) {
            return this.get('/api/stores/' + storeId);
        },

        updateStoreInfo: function (storeId, storeInfo) {

            var data = {
                name: storeInfo.name,
                address: storeInfo.address,
                phone: storeInfo.phone,
                description: storeInfo.description,
                user_id: storeInfo.owner.id,
                cover_image_link_id: storeInfo.linkId,
                pure_contacts_user: {
                    name: storeInfo.contacts_user_detail.name,
                    phone: storeInfo.contacts_user_detail.phone
                },
                location: {
                    lat: storeInfo.location.lat,
                    lng: storeInfo.location.lng
                },
                type: storeInfo.type,
                cpc: storeInfo.cpc,
                business_hours: storeInfo.business_hours
            };
            return this.put('/api/stores/' + storeId, data);
        },

        getStoreSetting: function(storeId) {
            return this.get("/api/stores/{0}/setting".format(storeId));
        },

        updateStoreSetting: function(storeId, setting) {
            return this.put("/api/stores/{0}/setting".format(storeId), setting);
        },

        storeSearch: function (key) {
            return this.get('/api/stores/search/' + key);
        },

        addStore: function (storeInfo) {
            var data = {
                name: storeInfo.name,
                address: storeInfo.address,
                phone: storeInfo.phone,
                description: storeInfo.description,
                user_id: storeInfo.userId,
                cover_image_link_id: storeInfo.linkId,
                pure_contacts_user: {
                    name: storeInfo.contacts_user_detail.name,
                    phone: storeInfo.contacts_user_detail.phone
                },
                location: {
                    lat: storeInfo.location.lat,
                    lng: storeInfo.location.lng
                },
                type: storeInfo.type,
                cpc: storeInfo.cpc,
                business_hours: storeInfo.business_hours

            };
            return this.post('/api/stores', data)
        },

        getStoreData: function () {
            return this.get('/api/stores/storedata');
        },

        getCouponData: function () {
            return this.get('api/stores/coupondata');
        }
    });

    angular.module('service.StoreService', [])
        .provider('StoreService', function () {
            this.$get = ['$http', '$rootScope', function (http, scope) {
                return new StoreService(http, scope);
            }];
        });

});