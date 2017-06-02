define(['angular'], function () {

    /**
     * pageTitle - Directive for set Page title - mata title
     */
    function showComment($window, $http) {
        return {
            restrict: 'AE',
            scope: {
                comment: '='
            },
            link: function (scope, element, attributes) {
                var time = setInterval(function(){
                    if(element[0].scrollHeight && element[0].scrollHeight != 0){
                        clearInterval(time);
                        if(element[0].clientHeight < element[0].scrollHeight){
                            $(element).next().removeClass("hidden");
                        }
                    }
                },5);
            }
        }
    }

    showComment.$inject = ['$window', '$http'];
    angular.module('app.directive.showComment', []).directive('showComment', showComment);
});