define(['angular', 'swiper'], function () {

    function swiper() {
        var _initializeSwiper = function (attributes) {
            if ($(".swiper-slide").length > 0) {
                var mySwiper = new Swiper(attributes.swiperContainer, {
                    speed: 1000,
                    autoplay: 5000,
                    loop: true,
                    pagination : '.swiper-pagination',
                    autoplayDisableOnInteraction:false

                });
                //mySwiper.enableMousewheelControl();
                //mySwiper.enableKeyboardControl();
                return true;
            } else {
                return false
            }
        };

        return {
            restrict: 'AE',
            link: function (scope, element, attributes) {
                if (element && attributes) {
                    var interval = setInterval(function () {
                        if (_initializeSwiper(attributes)) {
                            clearInterval(interval);
                        }
                    }, 50);
                }
            }
        }
    }

    swiper.$inject = [];

    angular.module('app.directive.swiper', []).directive('swiper', swiper);
});