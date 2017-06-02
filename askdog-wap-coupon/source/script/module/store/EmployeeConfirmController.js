define([
    '../../base/BaseController',
    'service/CouponService',
    'service/UserService',
    'service/StoreService',
    'service/TokenService'
], function (BaseController) {

    var EmployeeConfirmController = BaseController.extend({

        init: function (_rootScope, _state, _scope, _stateParams, _userService, _storeService, _tokenService) {
            this.$rootScope = _rootScope;
            this.$state = _state;
            this.$stateParams = _stateParams;
            this.$userService = _userService;
            this.$storeService = _storeService;
            this.$tokenService = _tokenService;
            this._super(_scope);
        },

        defineScope: function () {
            this._defineViewHandler();
            this._storeDetail();
        },

        _defineViewHandler: function () {
            var owner = this;
            this.$scope.confirm = function () {
                owner._addEmployee();
            };

            this.$scope.cancel = function () {
                owner._close();
            }
        },

        _addEmployee: function () {
            var owner = this;
            this.$storeService.addEmployee(this.$scope.storeId, this.$stateParams.token).then(
                function () {
                    owner.$scope.success = true;
                },
                function (resp) {
                    owner.$scope.failed = true;
                    if (resp.data.code == 'SRV_CONFLICT_ADD_EMPLOYEE') {
                        owner.$scope.error = "该员工已绑定！";
                    } else {
                        owner.$scope.error = "绑定失败，请重试！"
                    }
                }
            );
        },

        _storeDetail: function () {
            var owner = this;
            this.$tokenService.detail('employee.bind', owner.$stateParams.token).then(
                function(resp) {
                    owner.$scope.storeId = resp.data.storeId;
                    owner.$storeService.storeDetail(resp.data.storeId).then(
                        function (resp) {
                            owner.$scope.detail = resp.data;
                        }
                    )
                },
                function() {
                    owner.$scope.invalidToken = true;
                }
            );
        },

        _close: function() {
            require(['wechat'], function (wx) {
                wx.closeWindow();
            });
        }
    });

    EmployeeConfirmController.$inject = ['$rootScope', '$state', '$scope', '$stateParams', 'UserService', 'StoreService', 'TokenService'];

    angular.module('module.EmployeeConfirmController', ['service.CouponService', 'service.TokenService']).controller('EmployeeConfirmController', EmployeeConfirmController);

});
