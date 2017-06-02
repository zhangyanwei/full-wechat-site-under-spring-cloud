define([
    '../../base/BaseController',
    'app/directive/adAnalytics',
    'service/UserService'
], function (BaseController) {
    var RegisterConfirmController = BaseController.extend({

        init: function ($rootScope, $scope, $state, $stateParams, $window, $userService) {
            this.$rootScope = $rootScope;
            this.$state = $state;
            this.$stateParams = $stateParams;
            this.$window = $window;
            this.$userService = $userService;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function () {
            var owner = this;
            this.$scope.status = "verifying";
            this.$userService.confirm(this.$stateParams.token)
                .then(function() {
                    owner.$scope.status = "succeed";
                }, function() {
                    owner.$scope.status = "failed";
                });

            this.$scope.findPassword = function() {
                owner.$state.go("view.login");
            }
        }

    });

    RegisterConfirmController.$inject = ['$rootScope', '$scope', '$state', '$stateParams', '$window', 'userService'];

    angular.module('module.login.RegisterConfirmController', ['service.UserService']).controller('RegisterConfirmController', RegisterConfirmController);

});