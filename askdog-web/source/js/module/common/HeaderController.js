define([
    'base/BaseController',
    'socket',
    'stomp',
    'service/UserService',
    'service/SearchService'
], function (BaseController, SockJS) {

    var HeaderController = BaseController.extend({

        _contextChangeListener: null,
        _contextReadyListener: null,

        init: function ($rootScope, $scope, $stateParams, $uibModal, $state, $userService, $searchService) {
            this.$rootScope = $rootScope;
            this.$stateParams = $stateParams;
            this.$uibModal = $uibModal;
            this.$state = $state;
            this.$userService = $userService;
            this.$searchService = $searchService;
            this._super($scope);
        },

        /**
         * @Override
         */
        defineScope: function () {
            this._defineSignIn();
            this._defineSignUp();
            this._defineSignOut();
            this._defineKeySearch();
            this._defineKeyAutocomplete();
            this._defineHideDrop();

            var owner = this;
            $(function () {
                owner._updateUserStatus();
            })
        },

        defineListeners: function () {
            var owner = this;
            this._contextChangeListener = this.$rootScope.$on('contextChange', function () {
                owner._updateUserStatus();
            });
            this._contextReadyListener = this.$rootScope.$on('contextReady', function (event, userSelf) {
                if (userSelf) {
                    owner._defineNoticeComponents();
                    // notice
                    if (userSelf.notice_count > 0) {
                        owner._updateNotifications();
                    }
                }
            });
        },

        destroy: function () {
            this._contextChangeListener();
            this._contextReadyListener();
            this._contextChangeListener = null;
            this._contextReadyListener = null;
        },

        _updateUserStatus: function () {
            //User status & me info
            var owner = this;
            this.$userService.status().then(
                function (resp) {
                    if (resp.data == "AUTHENTICATED") {
                        owner.$userService.me().then(
                            function (resp) {
                                owner.$rootScope.userSelf = resp.data;
                                owner.$rootScope.$broadcast('contextReady', resp.data);
                            }
                        );
                    } else if (resp.data == "ANONYMOUS") {
                        owner.$rootScope.userSelf = undefined;
                        owner.$rootScope.$broadcast('contextReady', undefined);
                    }
                }
            );
        },

        _defineSignIn: function () {
            var owner = this;
            this.$rootScope.signIn = function (authenticated, cancelled) {
                owner.$rootScope.signInOpened = true;
                var signInModel = owner.$uibModal.open({
                    windowTemplateUrl: 'views/base/modal-window.html',
                    windowTopClass: 'lr-Modal',
                    templateUrl: 'views/dialog/sign-in.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                    }]
                });

                signInModel.result.then(function () {
                    owner.$rootScope.signInOpened = false;
                    authenticated && authenticated.call(this);
                }, function () {
                    owner.$rootScope.signInOpened = false;
                    cancelled && cancelled.call(this);
                });
            };
        },

        _defineSignUp: function () {
            var owner = this;
            this.$rootScope.signUp = function () {
                owner.$uibModal.open({
                    windowTemplateUrl: 'views/base/modal-window.html',
                    windowTopClass: 'lr-Modal',
                    templateUrl: 'views/dialog/sign-up.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                    }]
                });
            };
        },

        _defineSignOut: function () {
            var owner = this;
            this.$scope.signOut = function () {
                owner.$userService.logout().then(
                    function () {
                        owner.$rootScope.userSelf = undefined;
                        owner.$rootScope.$broadcast('contextChange');
                        window.location.href = '/'
                    }
                );
            }
        },

        _defineKeySearch: function () {
            var owner = this;
            this.$scope.goSearch = function () {
                if (owner.$scope.key) {
                    owner.$state.go('layout.view-default.search', {key: encodeURIComponent(owner.$scope.key)});
                    owner.$scope.drop = false;
                }
            };
        },

        _defineHideDrop: function () {
            var owner = this;
            this.$scope.hideDrop = function () {
                owner.$scope.drop = false;
            }
        },

        _defineKeyAutocomplete: function () {
            var owner = this;
            var lastTime = new Date();

            /* this.$scope.listenDropdown = function (element) {
             $(element).on("show.bs.dropdown", function () {
             console.log("show.bs.dropdown")
             return !!(owner.$scope.key && owner.$scope.keySimilar && owner.$scope.keySimilar.total > 0);
             });
             };*/

            this.$scope.keyAutocomplete = function (event) {
                var nowTime = new Date();
                var querySimilar = undefined;
                if (event.keyCode == 27) {
                    owner.$scope.hideDrop();
                    owner.$scope.key = undefined;
                } else if (event.keyCode == 40 || event.keyCode == 39) {
                    var menuItems = $('.header-container .search-box .dropdown-menu li a');
                    if (menuItems.length > 0) {
                        menuItems[0].focus();
                    }
                } else if (event.keyCode == 38 || event.keyCode == 37) {
                    var menuItems = $('.header-container .search-box .dropdown-menu li a');
                    if (menuItems.length > 0) {
                        menuItems[menuItems.length - 1].focus();
                    }
                } else {
                    if (event.keyCode != 13 && nowTime - lastTime > 500) {
                        lastTime = nowTime;
                        querySimilar = setTimeout(owner.$scope.querySimilar, 500);
                    } else {
                        if (querySimilar) {
                            clearTimeout(querySimilar);
                        }
                    }
                }
            };

            this.$scope.querySimilar = function () {
                if (!owner.$scope.key == "") {
                    owner.$searchService.querySimilar(encodeURIComponent(owner.$scope.key)).then(
                        function (resp) {
                            owner.$scope.keySimilar = resp.data;
                            owner.$scope.drop = true;
                        }
                    );
                } else {
                    owner.$scope.keySimilar = {
                        last: true,
                        result: [],
                        total: 0
                    };
                    owner.$scope.drop = false;
                }
            };
        },

        _defineNoticeComponents: function () {
            var owner = this;
            var ws = new SockJS("http://api.askdog.com/notification");
            var client = Stomp.over(ws);
            client.heartbeat.outgoing = 20000; // client will send heartbeats every 20000ms
            client.heartbeat.incoming = 0;     // client does not want to receive heartbeats from the server
            var connectCallback = function (iframe) {
                client.subscribe("/user/topic/notification", function (message) {
                    owner.$rootScope.userSelf.notice_count++;
                    owner._updateNotifications();
                });
            };
            var errorCallback = function (error) {
                // TODO
            };
            client.connect({}, connectCallback, errorCallback);
        },

        _updateNotifications: function () {
            var owner = this;
            this.$userService.noticePreview().then(
                function (resp) {
                    owner.$scope.notifications = resp.data;
                    for (var index = 0; index < owner.$scope.notifications.length; index++) {
                        var item = owner.$scope.notifications[index];
                        switch (item.content.type) {
                            case 'CREATE_EXPERIENCE_COMMENT':
                                item.content.typeName = '评论';
                                break;
                            case 'CREATE_EXPERIENCE_COMMENT_COMMENT':
                                item.content.typeName = '回复';
                                break;
                            case 'ACCEPT_GOLD_BUCKET':
                                item.content.user.name = '内容管理员';
                                item.content.typeName = '一桶金审核通过';
                                break;
                            case 'REJECT_GOLD_BUCKET':
                                item.content.user.name = '内容管理员';
                                item.content.typeName = '一桶金审核拒绝';
                                break;
                            default:
                                // TODO
                                item.content.typeName = '未知';
                                break;
                        }
                    }
                }
            );
        }

    });

    HeaderController.$inject = ['$rootScope', '$scope', '$stateParams', '$uibModal', '$state', 'UserService', 'SearchService'];

    angular.module('module.common.HeaderController', ['service.UserService', 'service.SearchService']).controller('HeaderController', HeaderController);

});