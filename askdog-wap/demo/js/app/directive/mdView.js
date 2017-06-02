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
                                $(element).append("<div id='tranCodeFail' class='tranCodeFail'><img src='images/loading-fail.gif'><br>视频正在转码中...</div>");
                                $(element).closest(".vedio-box").find(".btn-play").hide();
                            }
                            if (videoUrl) {
                                var thumbnail = experience.thumbnail || "";
                                var imagePlayer = '<img src="' + thumbnail + '" height="207px" id="video-poster"/>';
                                $(element).append(imagePlayer);
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

        for (var i = 0; i < AskDogExp.Constants.PICTRUE_URL_MAPPINS.length; i++) {
            var pictureUrlMapping = AskDogExp.Constants.PICTRUE_URL_MAPPINS[i];
            var to = pictureUrlMapping.to;
            var mdRegex = pictureUrlMapping.mdRegex;
            content = content.replace(mdRegex, "$1" + to + "$3@650w_1l.png$4");
        }

        return content;
    }

    mdView.$inject = [];
    angular.module('app.directive.mdView', []).directive('mdView', mdView);
});