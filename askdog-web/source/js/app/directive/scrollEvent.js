define(['angular'], function () {

    function scrollEvent($rootScope) {
        return {
            link: function (scope, element, attributes) {
                $('#main').unbind('scroll');
                if (attributes.active == true || attributes.active == 'true') {
                    $('#main').scroll(
                        function () {
                            var scrollTop = $(this).scrollTop();
                            // sidebar position control
                            if (attributes.hasbanner == true || attributes.hasbanner == 'true') {
                                if ($rootScope.bannerClosed) {
                                    $('.sidebar-container .nav-bar').css('top', '56px');
                                } else {
                                    if (scrollTop < 240) {
                                        $('.sidebar-container .nav-bar').css('top', (266 - scrollTop) + 'px');
                                    } else {
                                        $('.sidebar-container .nav-bar').css('top', '56px');
                                    }
                                }
                            }

                            if ($("#wrapper").height() - scrollTop + 126 == $(window).height()) {
                                $rootScope.$broadcast('scrollBottom');
                            }
                            // fixed IE high degree of problem
                            if ($("#wrapper").height() - scrollTop + 127 == $(window).height()) {
                                $rootScope.$broadcast('scrollBottom');
                            }
                        }
                    );
                }
            }
        }
    }

    scrollEvent.$inject = ['$rootScope'];

    angular.module('app.directive.scrollEvent', []).directive('scrollEvent', scrollEvent);
});