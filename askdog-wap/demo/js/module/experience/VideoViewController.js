define([
    'base/BaseController',
    'service/ExperienceService',
    'app/directive/mdView'
], function (BaseController) {

    var VideoViewController = BaseController.extend({

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
                    owner.$scope.detail = resp.data;
                    owner.$scope.$applyAsync(function() {
                        owner._videoPlay();
                    });
                }
            );
        },

        _initVideo: function () {
            var imagePoster = $('#experience-view #video-poster');
            var videoUrl = '';
            var experience = this.$scope.detail.content;
            if (experience.transcode_videos && experience.transcode_videos.length > 0) {
                videoUrl = experience.transcode_videos[0].url;
            } else if (experience.source && experience.source.url) {
                videoUrl = experience.source.url;
            }

            $(imagePoster).replaceWith('<video id="video-player" src="' + videoUrl + '" width="100%" controls=""></video>');
        },

        _videoPlay: function () {
            this._initVideo();
            this.$scope.videoInitialized = true;
        }

    });

    VideoViewController.$inject = ['$scope', '$stateParams', 'experienceService'];

    angular.module('module.experience.VideoViewController', ['service.ExperienceService', 'app.directive.mdView']).controller('VideoViewController', VideoViewController);

})
;