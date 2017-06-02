define(['base/BaseController', 'service/ExperienceService'], function (BaseController) {

    var ReportController = BaseController.extend({

        init: function (_rootScope, _scope, _stateParams, _experienceService) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;

            this.$experienceService = _experienceService;
            this.$uibModalInstance = _scope.$parent.$uibModalInstance;
            this.$experienceId = _scope.$parent.experienceId;
            this._super(_scope);
        },

        defineScope: function () {
            this._defineValidator();
            this._defineCancel();
        },

        _defineCancel: function () {
            var owner = this;
            this.$scope.cancel = function () {
                owner.$uibModalInstance.dismiss("cancel");
            };
        },


        _defineValidator: function () {
            var owner = this;
            this.$scope.reportInfo = {
                type: 'OTHER',
                message: null
            };

            this.$scope.bindValidator = function (element) {
                $(element).validate({
                    rules: {},
                    messages: {},
                    submitHandler: function () {
                        owner.$scope.submitReport();
                    }
                });
            };

            this.$scope.submitReport = function () {
                owner.$experienceService.report(owner.$experienceId, owner.$scope.reportInfo).then(
                    function () {
                        owner.$scope.reported = true;
                    },
                    function () {
                        owner.$scope.reported = true;
                    }
                );
            };
        }
    });

    ReportController.$inject = ['$rootScope', '$scope', '$stateParams', 'ExperienceService'];

    angular.module('module.dialog.ReportController', ['service.ExperienceService']).controller('ReportController', ReportController);

});