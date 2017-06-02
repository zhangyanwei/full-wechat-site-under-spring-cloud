define([
    'base/BaseController',
    'service/ExperienceService'
], function (BaseController) {
    var UploadVideoController = BaseController.extend({

        $stateParams: null,
        $experienceService: null,

        /**
         * @Override
         * @param $scope
         * @param $stateParams
         * @param $experienceService
         */
        init: function ($scope, $stateParams, $experienceService) {
            this.$stateParams = $stateParams;
            this.$experienceService = $experienceService;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope:function(){
            // TODO
            // console.log("detail controller has been invoked.");
            // var owner = this;
            // this.$experienceService.detail(this.$stateParams.experienceId)
            //     .then(null, function(resp) {
            //         console.trace("response status is: " + resp.status);
            //         console.trace(arguments);
            //     });
        }

    });

    UploadVideoController.$inject = ['$scope', '$stateParams', 'experienceService'];

    angular.module('module.upload-video.UploadVideoController', ['service.ExperienceService']).controller('UploadVideoController', UploadVideoController);

});