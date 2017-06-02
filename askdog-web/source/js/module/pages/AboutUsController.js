define(['base/BaseController', 'idangerous', 'jquery.velocity'], function (BaseController) {

    var AboutUsController = BaseController.extend({

        init: function (_scope) {
            this._super(_scope);

            var owner = this;

            var interval = setInterval(function () {

                if (owner._initializeSwiper()) {
                    clearInterval(interval);
                }

            }, 50);
        },

        _initializeSwiper: function () {
            var owner = this;
            if ($(".swiper-slide").length == 4) {

                owner._scrollView(0);

                var _swiper = new Swiper('.swiper-container', {
                    pagination: '.pagination',
                    mode: 'vertical',
                    speed: '1500',
                    paginationClickable: true,
                    mousewheelControl: true,
                    calculateHeight: false,
                    onSlideChangeEnd: function (swiper) {
                        owner._scrollView(swiper.activeIndex);
                    },
                    onLastContinue: function (swiper) {
                        $(".footer").fadeIn(500);
                    }
                });
                _swiper.enableMousewheelControl();
                _swiper.enableKeyboardControl();

                return true;
            }
            return false
        },


        _scrollView: function (index) {
            $(".footer").hide();
            $(".swiper-slide .img-box img").removeClass("img-start").removeClass("img-bigger");
            $(".swiper-slide .box-title").removeClass("font-fall-start");
            $(".swiper-slide .box-content").removeClass("font-rise-start");
            $(".about-logo").removeClass("about-logo-fall-start");
            $(".about-font").removeClass("last-rise-start");

            var pageSelector = ".swiper-slide.page-" + (index + 1);
            var animateImageSelector = pageSelector + " .img-box img";
            $(animateImageSelector).addClass("img-start").addClass("img-bigger");

            if (index == 3) {
                $(".about-logo").addClass("about-logo-fall-start");
                $(".about-font").addClass("last-rise-start");
            }
            else {
                var animateTitleSelector = pageSelector + " .box-title";
                var animateContentSelector = pageSelector + " .box-content";
                $(animateTitleSelector).addClass("font-fall-start");
                $(animateContentSelector).addClass("font-rise-start");
            }

        }
    });

    AboutUsController.$inject = ['$scope'];

    angular.module('module.pages.AboutUsController', []).controller('AboutUsController', AboutUsController);

});