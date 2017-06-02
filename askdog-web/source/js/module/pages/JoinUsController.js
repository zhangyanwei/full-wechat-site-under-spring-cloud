define(['base/BaseController'], function (BaseController) {

    var JoinUsController = BaseController.extend({


        init: function (_scope) {
            this._super(_scope);
        },

        defineScope: function () {
            var owner = this;
            var time = setInterval(function () {
                if (owner._joinUs()) {
                    clearInterval(time);
                }
            }, 500);
            this._defineScrollWatch();
        },

        _joinUs: function () {
            if ($(".col-920 .t-body li")) {
                $(".col-920 .t-body li").click(function () {
                    $(this).addClass("current").siblings("li").removeClass("current");
                    var index = $(this).index();
                    $(".jobs .jobs-details").eq(index).show().siblings(".jobs-details").hide();
                });
                $(".col-920 .t-body li").click(function () {
                    $(this).addClass("current").siblings("li").removeClass("current");
                    var index = $(this).index();
                    $(".jobs .jobs-details").eq(index).show().siblings(".jobs-details").hide();
                });
                $(".btn-close").click(function () {
                    $(this).parents(".jobs-details").hide();
                    $(".col-920 .t-body li").removeClass("current");
                });
                return true;
            }
            return false;
        },

        _defineScrollWatch: function () {
            var owner = this;
            $('#main').scroll(
                function () {
                    var scrollTop = $(this).scrollTop();
                    var height = $(".job-list").height();
                    if (scrollTop > 800) {
                        var minHeight = height - (scrollTop - 736);
                        console.log(minHeight);
                        $('.jobs-details').css('top',scrollTop - 736);
                        $('.jobs-details').css('height',minHeight);
                        $('.jobs-details').css('overflow-y',"auto");
                    }else{
                        $('.jobs-details').css('top','0px');
                    }
                }
            );
        }
    });

    JoinUsController.$inject = ['$scope'];

    angular.module('module.pages.JoinUsController', []).controller('JoinUsController', JoinUsController);

});