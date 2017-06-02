define([
    'base/BaseController',
    'service/ExperienceService',
    'validate',
    'app/directive/autoHeight'
], function (BaseController) {

    var ReportController = BaseController.extend({

        init: function ($scope, $stateParams,  $experienceService,$uibModal) {
            this.$stateParams = $stateParams;
            this.$experienceService = $experienceService;
            this.$uibModal = $uibModal;
            this._super($scope);
        },

        defineScope: function () {
            this._defineReportController();
            this._defineToggleRadio();
            this._defineValidator();
        },

        _defineReportController:function(){
            var owner = this;
            owner.$scope.reportExperience = function(){
                var message = "";
                if(owner.$scope.reportMessage){
                    message = owner.$scope.reportMessage.replace(/[\r\n]/g,"");
                }
                var type = $('.list-group-item .active').prev().val();
                owner.$experienceService.report(owner.$stateParams.reportId,type,message).then(
                    function(){
                        window.history.go(-1);
                        var successModal = owner.$uibModal.open({
                            windowTemplateUrl: 'views/base/modal-window.html',
                            windowTopClass: 'status-modal',
                            templateUrl: 'views/dialog/success.html',
                            controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                                $scope.$uibModalInstance = $uibModalInstance;
                                $scope.message = "举报提交成功，我们会审核您的举报";
                            }]
                        });
                });
            }
        },

        _defineToggleRadio:function(){
            var owner = this;
            owner.$scope.toggleRadio = function(e){
                $(".report-real-radio ").removeClass("active");
                $(e.target).addClass("active");
                var type = $('.list-group-item .active').prev().val();
            }
        },

        _defineValidator:function(){
            var owner = this;
            owner.$scope.bindValidator = function (element) {
                $(element).validate({
                    rules: {},
                    messages: {},
                    submitHandler: function () {
                        owner.$scope.reportExperience();
                    }
                });
            };
        }
    });

    ReportController.$inject = ['$scope', '$stateParams', 'experienceService','$uibModal'];

    angular.module('module.experience.ReportController', ['service.ExperienceService']).controller('ReportController', ReportController);

})
;