define([
    '../../base/BaseController',
    'socket',
    'stomp',
    'service/UserService',
    'service/SearchService',
    'service/CategoryService'
], function (BaseController, SockJS) {
    var HeaderController = BaseController.extend({

        _contextChangeListener: null,
        _contextReadyListener: null,
        _noNotice:null,

        $stateParams: null,
        $userService: null,
        $searchService: null,
        $rootScope: null,
        $state: null,

        init: function ($scope, $stateParams, $userService, $searchService, $rootScope, $state,$categoryService) {
            this.$stateParams = $stateParams;
            this.$userService = $userService;
            this.$searchService = $searchService;
            this.$rootScope = $rootScope;
            this.$state = $state;
            this.$categoryService = $categoryService;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function () {
            this._returnMain();
            this._searchQuestions();
            this._showSearch();
            this._showLogin();
            this._showSearchResult();
            var owner = this;
            $(function () {
                owner._searchUserInfo();
            })
            this._showNotice();
            this._refreshSystemCategories();
        },

        defineListeners: function () {
            var owner = this;
            this._contextReadyListener = this.$rootScope.$on('contextReady', function (event, userSelf) {
                if (userSelf) {
                    owner._defineNoticeComponents();
                    // notice
                }
            });
            this._noNotice = this.$rootScope.$on('noNotice', function () {
                owner.$rootScope.user.notice_count = 0;
            });
        },

        destroy: function () {
            this._contextReadyListener();
            this._contextReadyListener = null;
            this._noNotice();
            this._noNotice = null;
        },

        _defineNoticeComponents: function () {
            var owner = this;
            if (!owner.$rootScope._noticeClient) {
                var ws = new SockJS("http://api.askdog.com/notification");
                var client = Stomp.over(ws);
                client.heartbeat.outgoing = 20000; // client will send heartbeats every 20000ms
                client.heartbeat.incoming = 0;     // client does not want to receive heartbeats from the server
                var connectCallback = function (iframe) {
                    client.subscribe("/user/topic/notification", function (message) {
                        owner.$rootScope.user.notice_count++;
                        owner.$rootScope.$digest();
                    });
                };
                var errorCallback = function (error) {
                    // TODO
                };
                client.connect({}, connectCallback, errorCallback);
                owner.$rootScope._noticeClient = client;
            }
        },

        _searchUserInfo: function () {
            var owner = this;
            owner.$userService.getStates()
                .success(function (data) {
                    if (data == 'AUTHENTICATED') {
                        owner.$userService.getUserInfo()
                            .success(function (data) {
                                owner.$rootScope.user = data;
                                owner.$rootScope.$broadcast('contextReady', data);
                            });
                    } else if (data == 'ANONYMOUS') {
                        owner.$rootScope.user = undefined;
                        owner.$rootScope.$broadcast('contextReady', undefined);
                    }
                });
            if (this.$stateParams.channel == 'channel') {
                owner.$scope.subTitle = '我的频道';
            }
            if (this.$stateParams.channel == 'subscribe') {
                owner.$scope.subTitle = '订阅频道';
            }
        },

        _returnMain: function () {
            var owner = this;
            owner.$scope.returnMain = function () {
                owner.$rootScope.search = false;
                owner.$rootScope.questionKey = '';
                owner.$rootScope.questions = '';
            }
        },

        _searchQuestions: function () {
            var owner = this;
            this.$scope.searchQuestions = function () {
                owner.$searchService.searchSimilar(owner.$scope.questionKey).success(
                    function (data) {
                        var height =screen.height + 'px';
                        $(".search-box").css('height', height);
                        $(".search-box").css('background', '#fff');
                        owner.$rootScope.questionKey = owner.$scope.questionKey;
                        owner.$rootScope.questions = data.result;

                    }
                );
            }
        },

        _showSearch: function () {
            var owner = this;
            owner.$scope.showSearch = function () {
                if (window.location.href.indexOf("search-questions") < 0) {
                    owner.$state.go('index.search-questions');
                }
            }
        },

        _showLogin: function () {
            var owner = this;
            owner.$scope.showLogin = function () {
                owner.$state.go('index.login');
                owner.$rootScope.forgetPassword = false;
                owner.$rootScope.resetPwdSuccess = false;
                owner.$rootScope.questions = '';
            }

        },

        _showSearchResult: function () {
            var owner = this;
            owner.$scope.showSearchResult = function () {
                owner.$state.go('index.search', {key: owner.$scope.questionKey})
                owner.$rootScope.search = false;
                owner.$rootScope.questionKey = '';
                owner.$rootScope.questions = '';
            }
        },

        _showNotice: function () {
            var owner = this;
            owner.$scope.showNotice = function () {
                owner.$rootScope.$broadcast('noNotice', undefined);
            }

        },

        _refreshSystemCategories: function () {
            var owner = this;
            this.$categoryService.categoriesNested().then(
                function (resp) {
                    owner.$rootScope.sysCategories = resp.data;
                }
            );
        }
    });

    HeaderController.$inject = ['$scope', '$stateParams', 'userService', 'searchService', '$rootScope', '$state','categoryService'];

    angular.module('module.my.HeaderController', ['service.UserService', 'service.SearchService','service.CategoryService']).controller('HeaderController', HeaderController);

});