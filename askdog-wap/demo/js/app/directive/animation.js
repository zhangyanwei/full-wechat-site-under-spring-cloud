define(['angular', 'jquery.velocity/velocity.min'], function () {

    function animation() {
        return {
            priority: 1000,
            restrict: 'AE',
            scope: {},
            link: function (scope, element, attributes) {
                // TODO animte type : zoom
                var animateType = attributes.animateType || 'zoom';
                var animateEvent = attributes.animateEvent || 'click';
                var animateDuration = !isNaN(Number(attributes.animateDuration)) ? Number(attributes.animateDuration) : 500;
                var animateSelector = attributes.animateSelector ? $(element).find(attributes.animateSelector) : $(element);
                $(element).bind(animateEvent, function () {
                    $(animateSelector).css("color", "#20b5ff");
                    $(animateSelector).velocity(
                        {
                            "font-size": "2em",
                            "opacity": 0,
                            "left": "-8px"
                        },
                        {
                            duration: animateDuration,
                            complete: function () {
                                $(animateSelector).css("font-size", "").css("opacity", "").css("color", "").css("left", "");
                            }
                        }
                    );
                });
            }
        }
    }

    animation.$inject = [];
    angular.module('app.directive.animation', []).directive('animation', animation);
});