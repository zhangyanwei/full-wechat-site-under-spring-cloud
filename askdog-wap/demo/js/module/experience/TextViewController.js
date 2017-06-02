define([
    'base/BaseController',
    'app/directive/mdView',
    'service/ExperienceService'
], function (BaseController) {

    var TextViewController = BaseController.extend({

        init: function ($scope, $stateParams,  $experienceService) {
            this.$stateParams = $stateParams;
            this.$experienceService = $experienceService;
            this._super($scope);
        },

        defineScope: function () {
            this._loadDetailData();
        },

        _loadDetailData: function () {
            var owner = this;
            this.$experienceService.getDetail(this.$stateParams.experienceId).then(
                function (resp) {
                    owner.$scope.experienceDetail = resp.data;
                }
            );
        }

    });

    TextViewController.$inject = ['$scope', '$stateParams', 'experienceService'];

    angular.module('module.experience.TextViewController', ['service.ExperienceService']).controller('TextViewController', TextViewController);

})
;