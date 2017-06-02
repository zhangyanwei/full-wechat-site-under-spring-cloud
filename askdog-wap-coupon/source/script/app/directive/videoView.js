define(['angular'], function (angular) {

    function videoView() {

        function videoUrl(video) {
            var url;
            if (video.transcode_videos && video.transcode_videos.length > 0) {
                url = video.transcode_videos[0].url;
            } else if (video.source && video.source.url) {
                url = video.source.url;
            }
            return url;
        }

        function videoCover(scope, video) {
            var cover = scope.content[scope.cover || 'cover_url'];
            if (!cover && video.snapshots && video.snapshots.length > 0) {
                cover = video.snapshots[0].url;
            }
            return cover;
        }

        function videoSize(video) {
            var videoWidth;
            if (window.orientation && (window.orientation === 90 || window.orientation === -90)) {
                //noinspection JSSuspiciousNameCombination
                videoWidth = window.screen.height;
            } else {
                videoWidth = window.screen.width;
            }

            var videoHeight = video.source.height / video.source.width * videoWidth;
            return { videoWidth: videoWidth, videoHeight: videoHeight };
        }

        function placeVideo(video, url, scope, element) {
            var size = videoSize(video);
            var videoPlayer = '<div style="width:{w}px; height:{h}px;"><video id="video-player" controls="" src="{url}" width="{w}px" height="{h}px" poster="data:image/gif,AAAA" style="{style}"></video></div>'.format({
                url: url,
                w: size.videoWidth,
                h: size.videoHeight,
                style: "background-color: #eee; background-image: url(" + videoCover(scope, video) + "@420w_1e_1c.png); background-size: cover; background-position: center; background-clip: content-box;"
            });

            $(element).replaceWith(videoPlayer);
        }

        function traceVideo(scope) {
            var played = false, ended = false;
            $("video")
                .on("play", function () {
                    if (!played) {
                        _hmt.push(['_trackEvent', 'video', 'play', scope.content.name]);
                        played = true;
                    }
                })
                .on("ended", function () {
                    if (!ended) {
                        _hmt.push(['_trackEvent', 'video', 'ended', scope.content.name]);
                        ended = true;
                    }
                });
        }

        function render(scope, element) {
            var video = scope.content.video;
            var url = videoUrl(video);
            if (url) {
                placeVideo(video, url, scope, element);
                traceVideo(scope);
            }
        }

        return {
            restrict: 'AE',
            scope: {
                content: '=',
                cover: '@'
            },
            link: function (scope, element, attributes) {
                scope.$watch("content", function(content) {
                    if (scope.content && scope.content.video) {
                        render(scope, element);
                    }
                });
            }
        }
    }

    videoView.$inject = [];
    angular.module('app.directive.videoView', []).directive('videoView', videoView);
});