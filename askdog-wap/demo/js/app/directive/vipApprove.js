define(['angular'], function () {

    /**
     * pageTitle - Directive for set Page title - mata title
     */
    function vipApprove($window, $http) {
        return {
            restrict: 'AE',
            scope: {
                vip: '='
            },
            link: function (scope, element, attributes) {
                scope.$watch(
                    function () {
                        return scope.vip;
                    },
                    function (vip) {
                       if(!vip){
                           return;
                       }
                        if(scope.vip.tags){
                            for(var i=0; i<scope.vip.tags.length; i++){
                                if(scope.vip.tags[i] == 'VIP'){
                                    $(element).parent().find(".icon-vv").removeClass("hidden");
                                    $(element).parent().find(".icon-v").removeClass("hidden");
                                }
                                if(scope.vip.tags[i] == 'OFFICIAL_VIP'){
                                    $(element).parent().find(".icon-vv").removeClass("hidden");
                                    $(element).parent().find(".icon-vv").css("color","#3688E5");
                                    $(element).parent().find(".icon-v").removeClass("hidden");
                                    $(element).parent().find(".icon-v").css("color","#3688E5");
                                }
                            }
                        }
                    });

            }
        }
    }

    vipApprove.$inject = ['$window', '$http'];
    angular.module('app.directive.vipApprove', []).directive('vipApprove', vipApprove);
});