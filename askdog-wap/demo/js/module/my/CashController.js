define([
    '../../base/BaseController',
    'app/directive/adAnalytics',
    'service/UserService'
], function (BaseController) {
    var CashController = BaseController.extend({

        $stateParams: null,
        $userService:null,

        /**
         * @Override
         * @param $scope
         * @param $stateParams
         * @param $channelService
         */
        init: function ($scope, $stateParams,$userService) {
            this.$stateParams = $stateParams;
            this.$userService = $userService;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope:function(){
            this._getRevenue();
            this._cash();
            this._clearNoNum();
        },

        _getRevenue:function(){
            var owner = this;
            owner.$userService.getRevenue()
                .success(function(data){
                    owner.$scope.revenue = data;
                    owner.$scope.revenue.balance = owner.$scope.revenue.balance / 100;
                });
        },

        _cash:function(){
            var owner = this;
            owner.$scope.cash = function(money){
                owner.money = money * 100;
                owner.$userService.cashToWeChat(owner.money)
                    .then(function(){
                        owner.$scope.cashSuccess = true;
                    },function(resp){
                        if(resp.status == 500){
                            owner.$scope.error = '提现时间太短，请过段时间重试';
                        }
                        if(resp.status == 400){
                           if(resp.data.code == 'WX_PAY_ERROR'){
                               owner.$scope.error = '请填写提现金额';
                           }
                        }
                        owner.$scope.error = resp.data.detail;
                        owner.$scope.cashFail = true;
                    });
            }
        },

        _clearNoNum:function(){
            var owner = this;
            owner.$scope.clearNoNum = function(obj,attr){
                obj[attr] = obj[attr].replace(/[^\d.]/g,"");
                obj[attr] = obj[attr].replace(/^\./g,"");
                obj[attr] = obj[attr].replace(/\.{2,}/g,"");
                obj[attr] = obj[attr].replace(".","$#$").replace(/\./g,"").replace("$#$",".");
            }
        }
    });

    CashController.$inject = ['$scope', '$stateParams', 'userService'];

    angular.module('module.my.CashController', ['service.UserService']).controller('CashController', CashController);

});