define(['base/BaseController', 'app/directive/adAnalytics', 'service/UserService', 'service/ChannelService'], function (BaseController) {

    var ZoneController = BaseController.extend({

        _userId: null,
        _contextReadyListener: null,
        _VIEW_SIZE: 20,

        init: function (_rootScope, _scope, _stateParams, _uibModal, _userService, _channelService) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$uibModal = _uibModal;
            this.$userService = _userService;
            this.$channelService = _channelService;
            this._super(_scope);
        },

        defineScope: function () {
            this.$scope.myzone = this.$rootScope.userSelf ? this.$stateParams.userId == this.$rootScope.userSelf.id : false;
            this._loadData();
            this._defineCreateChannel();
            this._defineUpdateChannel();
            this._defineDeleteChannel();
            this._defineSubscribe();
            this._defineUnsubscribe();
        },

        defineListeners: function () {
            var owner = this;
            this._userId = this.$stateParams.userId;
            this._contextReadyListener = this.$rootScope.$on('contextReady', function (event, userSelf) {
                if (userSelf) {
                    owner.$scope.myzone = (userSelf.id == owner._userId);
                    owner._loadData();
                }
            });
        },

        destroy: function () {
            this._contextReadyListener();
            this._contextReadyListener = null;
        },

        _loadData: function () {
            var owner = this;
            this._loadUserInfo();

            this.$scope.ownedChannels = {
                last: true
            };
            this.$scope.subscribedChannels = {
                last: true
            };

            this._loadOwnedChannels(0, this._VIEW_SIZE);
            this.$scope.ownedChannelsPaging = function () {
                var page = owner.$scope.ownedChannels.page + 1;
                if (owner.$scope.ownedChannels.size > owner._VIEW_SIZE) {
                    page = owner.$scope.ownedChannels.size / owner._VIEW_SIZE;
                }
                owner._loadOwnedChannels(page, owner._VIEW_SIZE);
            };

            this._loadSubscribedChannels(0, this._VIEW_SIZE);
            this.$scope.subscribedChannelsPaging = function () {
                var page = owner.$scope.subscribedChannels.page + 1;
                if (owner.$scope.subscribedChannels.size > owner._VIEW_SIZE) {
                    page = owner.$scope.subscribedChannels.size / owner._VIEW_SIZE;
                }
                owner._loadSubscribedChannels(page, owner._VIEW_SIZE);
            }
        },

        _refreshData: function () {
            var size = (this.$scope.ownedChannels.page + 1) * this._VIEW_SIZE;
            if (this.$scope.ownedChannels.size > this._VIEW_SIZE) {
                size = this.$scope.ownedChannels.size;
            }
            this._loadOwnedChannels(0, size);

            size = (this.$scope.subscribedChannels.page + 1) * this._VIEW_SIZE;
            if (this.$scope.subscribedChannels.size > this._VIEW_SIZE) {
                size = this.$scope.subscribedChannels.size;
            }
            this._loadSubscribedChannels(0, size);
        },

        _loadUserInfo: function () {
            var owner = this;
            this._userId = this.$stateParams.userId;
            this.$userService.userInfo(this._userId)
                .then(function (resp) {
                    owner.$scope.user = resp.data;
                });
        },

        _loadOwnedChannels: function (page, size) {
            var owner = this;
            this.$scope.loadingCompleted = false;
            this.$channelService.ownedChannels(this._userId, page, size).then(
                function (resp) {
                    owner._loadOwnedChannelsSuccessHandler(resp);
                    owner.$scope.loadingCompleted = true;
                }
            );
        },

        _loadOwnedChannelsSuccessHandler: function (resp) {
            if (resp.data.page == 0) {
                this.$scope.ownedChannels = {
                    result: resp.data.result,
                    page: resp.data.page,
                    total: resp.data.total,
                    size: resp.data.size,
                    last: resp.data.last
                };
                return;
            }
            var result = this.$scope.ownedChannels ? this.$scope.ownedChannels.result : [];
            for (var index = 0; index < resp.data.result.length; index++) {
                result.push(resp.data.result[index]);
            }
            this.$scope.ownedChannels = {
                result: result,
                page: resp.data.page,
                total: resp.data.total,
                size: resp.data.size,
                last: resp.data.last
            };
        },

        _loadSubscribedChannels: function (page, size) {
            var owner = this;
            this.$scope.loadingCompleted = false;
            this.$channelService.subscribedChannels(this._userId, page, size).then(
                function (resp) {
                    owner._loadSubscribedChannelsSuccessHandler(resp);
                    owner.$scope.loadingCompleted = true;
                }
            );
        },

        _loadSubscribedChannelsSuccessHandler: function (resp) {
            if (resp.data.page == 0) {
                this.$scope.subscribedChannels = {
                    result: resp.data.result,
                    page: resp.data.page,
                    total: resp.data.total,
                    size: resp.data.size,
                    last: resp.data.last
                };
                return;
            }
            var result = this.$scope.subscribedChannels ? this.$scope.subscribedChannels.result : [];
            for (var index = 0; index < resp.data.result.length; index++) {
                result.push(resp.data.result[index]);
            }
            this.$scope.subscribedChannels = {
                result: result,
                page: resp.data.page,
                total: resp.data.total,
                size: resp.data.size,
                last: resp.data.last
            };
        },

        _defineSubscribe: function () {
            var owner = this;
            this.$scope.subscribe = function (channelId) {
                owner.$channelService.subscribe(channelId).then(
                    function () {
                        owner._updateChannelSubscribeState(channelId, true);
                        owner.$rootScope.$broadcast('sidebarRefresh');
                    }
                );
            };
        },

        _defineUnsubscribe: function () {
            var owner = this;
            this.$scope.unsubscribe = function (channelId) {
                owner.$channelService.unsubscribe(channelId).then(
                    function () {
                        owner._updateChannelSubscribeState(channelId, false);
                        owner.$rootScope.$broadcast('sidebarRefresh');
                    }
                );
            };
        },

        _updateChannelSubscribeState: function (channelId, subscribed) {
            for (var index = 0; index < this.$scope.ownedChannels.result.length; index++) {
                if (this.$scope.ownedChannels.result[index].id == channelId) {
                    subscribed ? this.$scope.ownedChannels.result[index].subscriber_count++ : this.$scope.ownedChannels.result[index].subscriber_count--;
                    this.$scope.ownedChannels.result[index].subscribed = subscribed;
                    return;
                }
            }
            for (var index = 0; index < this.$scope.subscribedChannels.result.length; index++) {
                if (this.$scope.subscribedChannels.result[index].id == channelId) {
                    subscribed ? this.$scope.subscribedChannels.result[index].subscriber_count++ : this.$scope.subscribedChannels.result[index].subscriber_count--;
                    this.$scope.subscribedChannels.result[index].subscribed = subscribed;
                    return;
                }
            }
        },

        _defineCreateChannel: function () {
            var owner = this;
            this.$scope.showCreateChannel = function () {
                var createChannelModel = owner.$uibModal.open({
                    windowTemplateUrl: 'views/base/modal-window.html',
                    windowTopClass: 'new-channel-modal',
                    templateUrl: 'views/dialog/channel-create.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                    }]
                });
                createChannelModel.result.then(
                    function () {
                        //ok callback;
                        owner._refreshData();
                    },
                    function () {
                        //cancel callback;
                    }
                );
            };
        },

        _defineUpdateChannel: function () {
            var owner = this;
            this.$scope.showUpdateChannel = function (channelId) {
                owner.$channelService.detail(channelId).then(
                    function (resp) {
                        var updateChannelModel = owner.$uibModal.open({
                            windowTemplateUrl: 'views/base/modal-window.html',
                            windowTopClass: 'new-channel-modal',
                            templateUrl: 'views/dialog/channel-update.html',
                            controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                                $scope.amendChannel = resp.data;
                                $scope.thumbnail = resp.data.thumbnail;
                                $scope.$uibModalInstance = $uibModalInstance;
                            }]
                        });
                        updateChannelModel.result.then(
                            function () {
                                //ok callback;
                                owner._refreshData();
                            },
                            function () {
                                //cancel callback;
                            }
                        );

                    }
                );

            };
        },

        _defineDeleteChannel: function () {
            var owner = this;
            this.$scope.deleteChannel = function (channelId) {
                owner.$channelService.deleteChannel(channelId).then(
                    function () {
                        owner._refreshData();
                    },
                    function (resp) {
                        if (resp.status = 400 && resp.data.code == "SRV_FORBIDDEN_DELETE_CHANNEL") {
                            //TODO error tip
                        }
                    }
                )
            }
        }
    });

    ZoneController.$inject = ['$rootScope', '$scope', '$stateParams', '$uibModal', 'UserService', 'ChannelService'];

    angular.module('module.ZoneController', ['service.UserService', 'service.ChannelService']).controller('ZoneController', ZoneController);

});