define(['base/BaseController', 'app/directive/adAnalytics', 'service/UserService'], function (BaseController) {

    var AccountController = BaseController.extend({

        _contextReadyListener: null,
        _VIEW_FROM: null,
        _VIEW_TO: null,
        _VIEW_SIZE: 13,

        init: function (_rootScope, _scope, _stateParams, _userService) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$userService = _userService;
            this._super(_scope);
        },

        defineScope: function () {
            this._loadData();
            this._defineIncomeSearch();
            this._defineWithdrawSearch();
            this._defineOpenWithdraw();
        },

        defineListeners: function () {
            var owner = this;
            this._contextReadyListener = this.$rootScope.$on('contextReady', function (event, userSelf) {
                if (userSelf && !owner.$scope.revenueDetail) {
                    owner._loadData();
                }
            });
        },

        destroy: function () {
            this._contextReadyListener();
            this._contextReadyListener = null;
        },

        _loadData: function () {
            var owner = this;

            var now = new Date();
            now.setDate(now.getDate() + 1);
            this._VIEW_TO = now.format("yyyy-MM-dd");
            now.setDate(now.getDate() - 366);
            this._VIEW_FROM = now.format("yyyy-MM-dd");

            this.$userService.revenue().then(
                function (resp) {
                    owner.$scope.revenueDetail = resp.data;
                }
            );

            owner.$userService.accounts().then(
                function (resp) {
                    owner.$scope.accounts = resp.data;
                }
            );

            this._incomeSearch(0);

            this._withdrawSearch(0);
        },

        _defineIncomeSearch: function () {
            var owner = this;
            this.$scope.incomeActive = true;
            this.$scope.withdrawalActive = false;
            this.$scope.incomeSearch = function (page) {
                owner._incomeSearch(page);
            }
        },

        _incomeSearch: function (page) {
            var owner = this;
            this.$userService.incomeSearch(owner._VIEW_FROM, owner._VIEW_TO, page, owner._VIEW_SIZE).then(
                function (resp) {
                    owner.$scope.incomeViewData = resp.data;
                }
            );
        },

        _defineWithdrawSearch: function () {
            var owner = this;
            this.$scope.withdrawSearch = function (page) {
                owner._withdrawSearch(page);
            }
        },

        _withdrawSearch: function (page) {
            var owner = this;
            this.$userService.withdrawSearch(owner._VIEW_FROM, owner._VIEW_TO, page, owner._VIEW_SIZE).then(
                function (resp) {
                    owner.$scope.withdrawViewData = resp.data;
                }
            );
        },


        _defineOpenWithdraw: function () {
            var owner = this;
            this.$scope.openWithdraw = function () {
                owner.$scope.withdrawOpened = true;
                owner.$scope.currentAccount = owner.$scope.accounts[0];
            };

            this.$scope.withdrawalAmountlimit = function () {
                var withdrawal_amount = Number(owner.$scope.withdrawal.withdrawal_amount);
                if (isNaN(withdrawal_amount)) {
                    owner.$scope.withdrawal.withdrawal_amount = undefined;
                } else {
                    if (/^[1-9]?[0-9]*\.[0-9]*$/.test(owner.$scope.withdrawal.withdrawal_amount)) {
                        owner.$scope.withdrawal.withdrawal_amount = withdrawal_amount.toFixed(2);
                    }
                }
            };

            owner.$scope.withdrawal = {};
            this.$scope.withdrawSubmit = function () {
                if (!owner.$scope.withdrawal.withdrawal_amount) {
                    owner.$scope.withdrawalError = "请输入提现金额";
                    return false;
                }
                var withdrawalAmount = Number(owner.$scope.withdrawal.withdrawal_amount);
                if (isNaN(withdrawalAmount)) {
                    owner.$scope.withdrawalError = "请输入正确的金额";
                    return false;
                }
                if (withdrawalAmount > 50000) {
                    owner.$scope.withdrawalError = "当日提现金额不得超过5,0000元";
                    return false;
                }
                if (withdrawalAmount < 1) {
                    owner.$scope.withdrawalError = "提现最低限额1元";
                    return false;
                }
                if ((withdrawalAmount * 100) > owner.$scope.revenueDetail.balance) {
                    owner.$scope.withdrawalError = "余额不足";
                    return false;
                }
                var withdrawal = {
                    "withdrawal_way": "WXPAY",
                    "withdrawal_amount": (withdrawalAmount * 100).toFixed(0)
                };
                owner.$userService.withdraw(withdrawal).then(
                    function () {
                        owner.$scope.withdrawalMessage = "提现成功，请您关注短信到账通知。";
                        owner._loadData();
                    },
                    function (resp) {
                        if (resp && resp.status == 403) {
                            owner.$scope.withdrawalMessage = undefined;
                            if (resp.data.code == 'SRV_FORBIDDEN_WITHDRAWAL_INTERVAL_TOO_SHORT') {
                                owner.$scope.withdrawalError = "15分钟后才可以再次提现";
                            } else if (resp.data.code == 'WEB_VALIDATE_FAILED') {
                                owner.$scope.withdrawalError = "余额不足";
                            } else if (resp.data.code == 'WX_PAY_ERROR') {
                                owner.$scope.withdrawalError = "帐号余额不足，请用户充值或更换支付卡后再支付.";
                            } else if (resp.data.code == 'WEB_WITHDRAW_FAILED') {
                                owner.$scope.withdrawalError = "付款金额不能小于最低限额";
                            }
                        }
                    }
                );
            };
        }

    });

    AccountController.$inject = ['$rootScope', '$scope', '$stateParams', 'UserService'];

    angular.module('module.AccountController', ['service.UserService']).controller('AccountController', AccountController);

});