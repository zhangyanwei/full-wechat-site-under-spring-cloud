define([
    'base/BaseController',
    'service/ExperienceOrderService',
    'service/ExperienceService'
], function (BaseController) {

    var PayController = BaseController.extend({

        init: function ($scope, $stateParams, $experienceOrderService,$experienceService) {
            this.$stateParams = $stateParams;
            this.$experienceOrderService = $experienceOrderService;
            this.$experienceService = $experienceService;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope:function(){
            var owner = this;
            var experienceId = AskDogExp.URL.param("id");
            owner.$experienceService.getDetail(experienceId)
                .success(function(data){
                    owner.$scope.orderInfo = data;
                });
            this.$scope.pay = function() {
                owner.$experienceOrderService.pay(experienceId, {
                    "pay_way": "WXPAY",
                    "pay_way_detail": "JSAPI",
                    "title": "ASKDOG 经验分享平台",
                    "product_description": owner.$scope.orderInfo.subject
                }).then(function(resp) {
                    if (resp.data.pay_status == 'PREPAY') {
                        function onBridgeReady(){
                            WeixinJSBridge.invoke(
                                'getBrandWCPayRequest', resp.data.pay_request,
                                function(res){
                                    if(res.err_msg == "get_brand_wcpay_request:ok" ) {
                                        window.location='/#/exp/'+experienceId;
                                    }
                                }
                            );
                        }
                        if (typeof WeixinJSBridge == "undefined"){
                            if( document.addEventListener ){
                                document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
                            }else if (document.attachEvent){
                                document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                                document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
                            }
                        }else{
                            onBridgeReady();
                        }
                    }
                }, function(resp) {
                    alert(JSON.stringify(resp.data) || "服务器忙，请稍后再试。");
                });
            };
        }

    });

    PayController.$inject = ['$scope', '$stateParams', 'experienceOrderService','experienceService'];

    angular.module('module.upload-video.UploadVideoController', ['service.ExperienceOrderService','service.ExperienceService']).controller('PayController', PayController);

});