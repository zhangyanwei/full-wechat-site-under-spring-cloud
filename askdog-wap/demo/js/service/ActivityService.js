define([
    'base/BaseService',
    'angular',
    'class'
], function (BaseService) {

    var ActivityService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        getActivity:function(page,size){
            return this.get("/api/activities/moon_festival/rank?page={page}&size={size}".format({"page":page,"size":size}));
        },
    });

    angular.module('service.ActivityService', [])
        .provider('activityService', function () {
            this.$get = ['$http', '$rootScope', function(http, scope) {
                return new ActivityService(http, scope);
            }];
        });

});