define([
    'base/BaseController',
    'app/directive/adAnalytics',
    'service/SearchService',
    'service/UserService',
    'service/ExperienceService',
    'app/directive/weShare',
    'app/directive/originalTag',
    'app/directive/vipApprove'
], function (BaseController) {
    var ExperienceListController = BaseController.extend({

        _VIEW_SIZE: 13,
        _contextChangeListener: null,

        init: function ($rootScope, $scope, $stateParams, $searchService, $userService, $auth, $experienceService, $state,$location) {
            this.$rootScope = $rootScope;
            this.$stateParams = $stateParams;
            this.$searchService = $searchService;
            this.$userService = $userService;
            this.$auth = $auth;
            this.$experienceService = $experienceService;
            this.$state = $state;
            this.$location = $location;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function () {
            var owner = this;
            owner.$userService.getStates()
                .then(function (resp) {
                    if (resp.data != 'ANONYMOUS') {
                        owner.$userService.getUserInfo()
                            .then(function (resp) {
                                if (resp.data.category_codes == undefined) {
                                    owner.$state.go('index.default-subscribe');
                                }
                            });
                    }
                });

            this._refresh(0, owner._VIEW_SIZE, owner.$stateParams.type);
            this._mySubscribe();
            this._scrollShowMore();
            this._refreshList();
            if(this.$location.$$path == '' || this.$location.$$path.startWith('/main') || this.$location.$$path.startWith('/')){
               owner.$scope.activity = true;
            }
            var link = '';
            if(this.$location.$$path.startWith('/hot')){
               link = window.location.href;
            }else{
                link = 'm.askdog.com';
            }
            owner.$scope.listDetail = {
                subject: 'ASKDOG---经验分享社区',
                desc: 'askdog经验分享社区，是一个专注于经验分享的平台，目前支持图文经验分享，视频经验分享……',
                thumbnail: AskDogExp.URL.base() + '/images/share-logo.png',
                link:link
            }
            this._defineShowSearch();
        },

        defineListeners: function () {
            var owner = this;
            this._contextReadyListener = this.$rootScope.$on('$stateChangeSuccess', function (event, toState, toParams) {
                owner._refresh(0, owner._VIEW_SIZE);
                if(toParams.type == 'main'){
                    owner.$scope.activity = true;
                }else{
                    owner.$scope.activity = false;
                }
                owner.$scope.viewList = '';
            });
        },
        destroy: function () {
            this._contextReadyListener();
            this._contextReadyListener = null;
        },

        _refresh: function (from, size, type) {
            var owner = this;
            owner.$scope.loadingCompleted = false;
            owner.$searchService.homeSearch(from, size, type)
                .then(function (resp) {
                    owner.$scope.loadingCompleted = true;
                    owner._searchSuccessHandler(resp.data, from);
                }, function (resp) {
                    if (resp.status == 500) {
                        owner.$scope.loadFail = true;
                    }
                });
        },

        _searchSuccessHandler: function (data, from) {
            var lastList = this.$scope.viewList || {};
            lastList.from = from;
            lastList.total = data.total;
            lastList.last = data.last;
            if (this.$scope.viewList && this.$scope.viewList.result) {
                lastList.result = this.$scope.viewList.result;
            } else {
                lastList.result = [];
            }
            for (var index = 0; index < data.result.length; index++) {
                lastList.result.push(data.result[index]);
            }
            if (data.result.length == 0) {
                lastList.last = true;
            }
            this.$scope.viewList = lastList;
        },

        _mySubscribe: function () {
            var owner = this;
            owner.$scope.mySubscribe = function (author) {
                owner.$rootScope.otherUser = '';
                owner.$rootScope.otherUser = author;
            }
        },

        _scrollShowMore: function () {
            var owner = this;
            window.addEventListener("scroll", function () {
                if (!owner.$scope.loadingCompleted) {
                    return;
                }
                if (Math.ceil($(document).scrollTop()) >= $(document).height() - $(window).height() && !owner.$scope.viewList.last) {
                    owner._refresh((owner.$scope.viewList.from + owner._VIEW_SIZE), owner._VIEW_SIZE, owner.$stateParams.type);
                }
            });
        },

        _refreshList:function(){
            var owner = this;
            owner.$scope.refreshList = function(){
                owner._refresh(0,owner._VIEW_SIZE,owner.$stateParams.type)
            }
        },

        _defineShowSearch:function(){
            var owner = this;
            owner.$scope.showSearch = function(){
                owner.$state.go("view.search");
            }
        }
    });

    ExperienceListController.$inject = ['$rootScope', '$scope', '$stateParams', 'searchService', 'userService', 'auth', 'experienceService', '$state','$location'];

    angular.module('module.experience.ExperienceListController', ['service.SearchService', 'service.UserService', 'service.ExperienceService','app.directive.weShare']).controller('ExperienceListController', ExperienceListController);

});