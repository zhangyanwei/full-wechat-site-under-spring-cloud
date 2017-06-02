define(['angular'], function () {

    /**
     * pageTitle - Directive for set Page title - mata title
     */
    function pageTitle($rootScope, $timeout, $translate) {
        return {
            link: function (scope, element, attributes) {

                function updateTitle(title) {
                    element.text('ASKDOG' + (title ? ' - ' + title : ' - 经验分享社区'));
                }

                $rootScope.$watch('title', function (title) {
                    if (typeof title == 'string') {
                        $translate(title).then(function (translated) {
                            updateTitle(translated);
                        });
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

    pageTitle.$inject = ['$rootScope', '$timeout', '$translate'];

    /**
     * Set Angular directive base on the class above,
     * this gives us a lot more flexibility and permits
     * inheritance over Directives this this is not recommanded,
     * with directive composition is better.
     */
    angular.module('app.directive.pageTitle', []).directive('pageTitle', pageTitle);
});