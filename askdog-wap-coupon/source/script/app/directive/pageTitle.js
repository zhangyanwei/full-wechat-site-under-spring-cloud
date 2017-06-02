define(['angular'], function () {

    /**
     * pageTitle - Directive for set Page title - mata title
     */
    function pageTitle($rootScope, $timeout) {
        return {
            link: function (scope, element, attributes) {

                function updateTitle(title) {
                    element.text('惠券' + (title ? ' - ' + title : ' - 让美味更优惠'));
                }

                $rootScope.$watch('title', function (title) {
                    if (typeof title == 'string') {
                        updateTitle(title);
                    } else if (typeof title == 'function') {
                        updateTitle(title.call(this));
                    } else {
                        // Default title
                        $timeout(function () {
                            updateTitle();
                        });
                    }
                });
            }
        }
    }

    pageTitle.$inject = ['$rootScope', '$timeout'];

    /**
     * Set Angular directive base on the class above,
     * this gives us a lot more flexibility and permits
     * inheritance over Directives this this is not recommanded,
     * with directive composition is better.
     */
    angular.module('app.directive.pageTitle', []).directive('pageTitle', pageTitle);
});