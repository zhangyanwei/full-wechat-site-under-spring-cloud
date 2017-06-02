define([
    'base/BaseController',
    'service/CouponService'
], function (BaseController) {

    var CashListController = BaseController.extend({

        init: function (_rootScope, _scope, _stateParams, _uibModal, _couponService) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$uibModal = _uibModal;
            this.$couponService = _couponService;
            this._super(_scope);
        },

        defineScope: function () {
            this.$scope.state = 'NOT_USED';
            this._defineViewHandler();
            this._defineRefresh();
            this._refreshListView();
            this._scrollShowMore();
        },

        _defineViewHandler: function () {
            var owner = this;
            owner.$scope.showCouponList = function (state) {
                owner.$scope.state = state;
                owner.$scope.viewList = {};
                owner._refreshListView();
            }
        },

        _refreshListView: function (page) {
            this.$scope.refresh(page || 0);
        },

        _defineRefresh: function () {
            var owner = this;
            this.$scope.refresh = function(page) {
                $("#retry").hide();
                owner.$scope.loadingCompleted = false;
                owner.$couponService.cashList(page, 10, owner.$scope.state).then(
                    function (resp) {
                        owner._searchSuccessHandler(resp.data);
                        owner.$scope.loadingCompleted = true;
                    },
                    function (resp) {
                        owner.$scope.loadingCompleted = true;
                        $("#retry").show();
                    }
                )
            };
        },

        _searchSuccessHandler: function (data) {
            this.$scope.viewList
                && this.$scope.viewList.result && (data.result = this.$scope.viewList.result.concat(data.result));
            this.$scope.viewList = data;
        },

        _scrollShowMore: function () {
            var owner = this;
            window.addEventListener("scroll", function () {
                if (!owner.$scope.loadingCompleted) {
                    return;
                }
                if (Math.ceil($(document).scrollTop()) >= $(document).height() - $(window).height() && !owner.$scope.viewList.last) {
                    owner._refreshListView((owner.$scope.viewList.page + 1));
                }
            });
        }

    });

    CashListController.$inject = ['$rootScope', '$scope', '$stateParams', '$uibModal', 'CouponService'];

    angular.module('module.CashListController', ['service.CouponService']).controller('CashListController', CashListController);

});