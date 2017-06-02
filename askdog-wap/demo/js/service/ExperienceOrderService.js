define([
    'base/BaseService',
    'angular',
    'class'
], function (BaseService) {

    var ExperienceOrderService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        /**
         * Create the preorder for experience.
         * @param experienceId
         * @param {Object}  payRequest - example: {pay_way: 'WXPAY', pay_way_detail: 'JSAPI', title: 'ASKDOG 经验分享平台', product_description: 'Title of Experience'}
         */
        pay: function(experienceId, payRequest) {
            return this.post("/api/orders/experience/{0}/pay".format(experienceId), payRequest);
        }

    });

    angular.module('service.ExperienceOrderService', [])
        .provider('experienceOrderService', function () {
            this.$get = ['$http', '$rootScope', function(http, scope) {
                return new ExperienceOrderService(http, scope);
            }];
        });

});