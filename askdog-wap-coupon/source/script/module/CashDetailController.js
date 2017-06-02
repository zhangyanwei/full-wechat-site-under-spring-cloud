define([
    'base/BaseController',
    '_global',
    'service/CouponService',
    'app/directive/qrCode',
    'socket',
    'stomp'
], function (BaseController, _g, couponService, qrCode, SockJS) {

    var CashDetailController = BaseController.extend({

        init: function (_scope, _stateParams, _couponService) {
            this.$stateParams = _stateParams;
            this.$couponService = _couponService;
            this._super(_scope);
        },

        defineScope: function () {
            this._refreshListView();
            this._verificationEventListener();
        },

        _refreshListView: function () {
            var owner = this;
            this.$couponService.cashDetail(owner.$stateParams.id).then(
                function (resp) {
                    var cashDetail = resp.data;
                    var coupon_id = cashDetail.id;
                    var store_id = cashDetail.store_basic.id;
                    owner.$scope.qrcode_url = '{0}/#/stores/{1}/coupons/{2}/consume'.format(_g.DOMAIN, store_id, coupon_id);
                    owner.$scope.cashDetail = cashDetail;
                }
            )
        },

        _verificationEventListener: function () {
            var owner = this;
            var ws = new SockJS(_g.WEBSOCKET_DOMAIN + "/websocket");

            var client = Stomp.over(ws);

            client.heartbeat.outgoing = 20000; // client will send heartbeats every 20000ms
            client.heartbeat.incoming = 0;     // client does not want to receive heartbeats
                                               // from the server

            var connectCallback = function (frame) {
                console.log('[Web Socket] Online !');
                console.log('[Web Socket] transport: ' + ws.transport);

                client.subscribe("/topic/event/coupon/verification", function (message) {
                    var messageElement = document.createElement("div");
                    messageElement.innerHTML = message.body;
                    console.log('[Web Socket] message: ' + JSON.stringify(message, null, 4));

                    var verifyEvent = JSON.parse(message.body);
                    if (verifyEvent.target.id == owner.$stateParams.id) {
                        owner.$scope.cashDetail.coupon_state = 'USED';
                        owner.$scope.$digest();
                        $('#description').slideUp();
                    }
                });
            };

            var errorCallback = function (error) {
                console.log('[Web Socket] error: ' + JSON.stringify(error, null, 4));
            };

            client.connect({}, connectCallback, errorCallback);
        }
});

CashDetailController.$inject = ['$scope', '$stateParams', 'CouponService'];

angular.module('module.CashDetailController', ['app.directive.qrCode', 'service.CouponService']).controller('CashDetailController', CashDetailController);

})
;