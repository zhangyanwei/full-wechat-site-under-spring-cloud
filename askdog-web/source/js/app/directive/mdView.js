define(['angular', 'editor-md'], function () {

    /**
     * pageTitle - Directive for set Page title - mata title
     */
    function mdView() {
        return {
            restrict: 'AE',
            scope: {
                type: '=',
                content: '='
            },
            link: function (scope, element, attributes) {
                scope.$watch(
                    function () {
                        return scope.content;
                    },
                    function (experience) {
                        $(element).empty();
                        if (experience && experience.type == "TEXT") {
                            editormd.markdownToHTML(
                                element.attr('id'),
                                {
                                    atLink: false,
                                    markdown: picTrans(experience.content)
                                    //htmlDecode: "style,script,iframe|on*"  // you can filter tags decode
                                }
                            );
                            $(element).removeClass('editormd-html-preview');
                        } else if (experience && experience.type == "VIDEO") {
                            var videoUrl = undefined;
                            if (experience.transcode_videos && experience.transcode_videos.length > 0) {
                                videoUrl = experience.transcode_videos[0].url;
                            } else if (experience.source && experience.source.url) {
                                videoUrl = experience.source.url;
                            } else {
                                $(element).append("<div class='tran-code-fail'>" +
                                    "<i class='iconfont icon-logo'></i>" +
                                    "<img src='images/loading-fail.gif'>" +
                                    "<div>视频正在转码中...</div>" +
                                    "</div>"
                                );
                                $(element).closest(".vedio-box").find(".btn-play").hide();
                            }
                            if (videoUrl) {
                                var thumbnail = experience.thumbnail || "";
                                var videoPlayer = '<video id="video-player" src="" class="video-js vjs-default-skin vjs-big-play-centered"  poster="' + thumbnail + '" width="650px" height="384px"></video>';
                                $(element).append(videoPlayer);
                            }
                        }
                    }
                );
            }
        }
    }

    function picTrans(content) {

        if (!content) {
            return content;
        }

        for (var i = 0; i < AskDog.Constants.PICTRUE_URL_MAPPINS.length; i++) {
            var pictureUrlMapping = AskDog.Constants.PICTRUE_URL_MAPPINS[i];
            var to = pictureUrlMapping.to;
            var mdRegex = pictureUrlMapping.mdRegex;
            content = content.replace(mdRegex, "$1" + to + "$3@650w_1l.png$4");
        }

        return content;
    }

    mdView.$inject = [];
    angular.module('app.directive.mdView', []).directive('mdView', mdView);
});