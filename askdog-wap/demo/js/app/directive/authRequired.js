define(['angular', 'service/UserService'], function () {

    function authRequired(auth, $userService) {
        return {
            priority: 100,
            restrict: 'A',
            compile: function ($element, attr) {

                function authenticated(authenticated, anonymous) {
                    $userService.getStates().then(
                        function (resp) {
                            if (resp.data == "AUTHENTICATED") {
                                authenticated && authenticated.call(this, resp);
                            } else if (resp.data == "ANONYMOUS") {
                                anonymous && anonymous.call(this, resp);
                            }
                        }
                    );
                }

                function doContinue(scope, element) {
                    var elementType = $(element).attr("type");
                    var handler = $(element).attr("ng-click");
                    var animateEvent = $(element).attr("animate-event");
                    if (elementType == "submit") {
                        scope.continue = false;
                        var form = $(element).closest("form");
                        var invalid = $(form).hasClass("ng-invalid");
                        handler = $(form).attr("ng-submit");
                        if (handler && !invalid) {
                            scope.$eval(handler);
                        }
                    } else if (handler) {
                        scope.continue = false;
                        if (animateEvent) {
                            $(element).trigger(animateEvent);
                        }
                        scope.$eval(handler);
                    } else if (element.attr('ui-sref')) {
                        scope.continue = false;
                        window.location.href = "/" + element.attr("href");
                    } else {
                        scope.continue = true;
                        $(element).click();
                    }
                }

                return {
                    pre: function link(scope, element) {
                        element.on('click', function (event) {
                            if (!scope.continue) {
                                // prevents ng-click to be executed
                                event.stopImmediatePropagation();
                                // prevents href
                                event.preventDefault();

                                authenticated(
                                    function () {
                                        doContinue(scope, element);
                                    },
                                    function () {
                                        scope.$evalAsync(function () {
                                            var href = element.attr("auth-required") || element.attr("href") ? (element.attr("ui-sref") ? "/" : "") + element.attr("href") : undefined;
                                            auth(href ? window.location.origin + href.replace(/\/?(.+)/, '/$1') : undefined);
                                        });
                                    });

                                return false;
                            }

                            scope.continue = false;
                        });
                    },
                    post: function () {
                    }
                }
            }
        };
    }

    authRequired.$inject = ['auth', 'userService'];
    angular.module('app.directive.authRequired', ['service.UserService']).directive('authRequired', authRequired);
});