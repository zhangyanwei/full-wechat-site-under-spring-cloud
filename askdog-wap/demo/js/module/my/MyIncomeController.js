define([
    '../../base/BaseController',
    'app/directive/adAnalytics',
    'service/UserService'
], function (BaseController) {
    var MyIncomeController = BaseController.extend({

        $stateParams: null,
        $userService: null,
        $state:null,

        _VIEW_FROM: null,
        _VIEW_TO: null,
        _VIEW_SIZE:10,

        /**
         * @Override
         * @param $scope
         * @param $stateParams
         * @param $userService
         */
        init: function ($scope, $stateParams, $userService,$state) {
            this.$stateParams = $stateParams;
            this.$userService = $userService;
            this.$state = $state;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope:function(){
            this._getRevenue();
            this._getIncome(0,this._VIEW_SIZE);
            this._getWithdraw(0,this._VIEW_SIZE);
            this._cash();
            var owner = this;
            this.$scope.incomeMore = function(){
                owner._getIncome((owner.$scope.viewList.page +1 ),owner._VIEW_SIZE);
            }
            this.$scope.withdrawMore = function(){
                owner._getWithdraw((owner.$scope.viewList.page +1 ),owner._VIEW_SIZE);
            }
        },

        _getRevenue:function(){
            var owner = this;
            owner.$userService.getRevenue()
                .success(function(data){
                    owner.$scope.revenue = data;
                });
        },

        _getIncome:function(page,size){
            var owner = this;
            owner.$scope.loadingCompleted = false;
            var now = new Date();
            now.setDate(now.getDate());
            this._VIEW_TO = now.format("yyyy-MM-dd");
            now.setDate(now.getDate() - 30);
            this._VIEW_FROM = now.format("yyyy-MM-dd");
           owner.$userService.getIncome(this._VIEW_FROM, this._VIEW_TO,page,size)
               .success(function(data){
                   owner.$scope.loadingCompleted = true;
                   owner._searchHandler(data,0);
               });
        },

        _getWithdraw:function(page,size){
            var owner = this;
            var now = new Date();
            now.setDate(now.getDate());
            this._VIEW_TO = now.format("yyyy-MM-dd");
            now.setDate(now.getDate() - 30);
            this._VIEW_FROM = now.format("yyyy-MM-dd");
            owner.$userService.getWithdraw(this._VIEW_FROM , this._VIEW_TO,size,page)
                .success(function(data){
                    owner._searchSuccessHandler(data,0);
                });

        },

        _cash:function(){
            var owner = this;
            owner.$scope.cash = function(){
                owner.$userService.bindAccounts()
                    .then(function(resp){
                        console.log(resp.data);
                        if(resp.data.length == 0){
                            $("#bind-weChat").modal("show");
                        }else{
                            console.log(owner.$scope.revenue.balance);
                            if(owner.$scope.revenue.balance  == 0){
                                console.log(1);
                                $("#no-income").modal("show");
                            }else{
                                owner.$state.go('view.cash');
                            }

                        }
                    });
            }
        },

        _searchSuccessHandler: function (data, page) {
            var lastList = this.$scope.view || {};
            lastList.page = page;
            lastList.total = data.total;
            lastList.last = data.last;
            if (this.$scope.view && this.$scope.view.result) {
                lastList.result = this.$scope.view.result;
            } else {
                lastList.result = [];
            }
            for (var index = 0; index < data.result.length; index++) {
                lastList.result.push(data.result[index]);
            }
            if (data.result.length == 0) {
                lastList.last = true;
            }
            this.$scope.view = lastList;
        },

        _searchHandler: function (data, page) {
            var lastList = this.$scope.viewList || {};
            lastList.page = page;
            lastList.total = data.total;
            lastList.last = data.last;
            if (this.$scope.viewList && this.$scope.viewList.result) {
                lastList.result = this.$scope.viewList.result;
            } else {
                lastList.result = [];
            }
            for (var index = 0; index < data.result.length; index++) {
                lastList.result.push(data.result[index]);
            }
            if (data.result.length == 0) {
                lastList.last = true;
            }
            this.$scope.viewList = lastList;
        }

    });

    MyIncomeController.$inject = ['$scope', '$stateParams', 'userService','$state'];

    angular.module('module.my.MyIncomeController', ['service.UserService']).controller('MyIncomeController', MyIncomeController);

});