define(['base/BaseController', 'app/directive/adAnalytics', 'service/ChannelService', 'service/ExperienceService', 'service/SearchService'], function (BaseController) {

    var _channelRelatedCache = undefined;

    var ChannelController = BaseController.extend({

        _contextReadyListener: null,
        _VIEW_SIZE: 13,
        _RELATED_SIZE: 8,

        init: function (_rootScope, _scope, _stateParams, _uibModal, _channelService, _experienceService, _searchService) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$uibModal = _uibModal;
            this.$channelService = _channelService;
            this.$experienceService = _experienceService;
            this.$searchService = _searchService;
            this._super(_scope);
        },

        defineScope: function () {
            this._loadData();
            this._defineRefreshData();
            this._defineSubscription();
            this._defineUpdateChannel();
            this._defineDeleteExperience();
        },

        defineListeners: function () {
            var owner = this;
            this._contextReadyListener = this.$rootScope.$on('contextReady', function (event, userSelf) {
                if (userSelf) {
                    owner._loadData();
                }
            });
        },

        destroy: function () {
            this._contextReadyListener();
            this._contextReadyListener = null;
        },

        _defineDeleteExperience: function () {
            var owner = this;
            this.$scope.deleteExperience = function (experienceId) {
                owner.$experienceService.deleteExperience(experienceId).then(
                    function () {
                        owner._loadData();
                    }
                );
            };
        },

        _defineSubscription: function () {
            var owner = this;
            this.$scope.subscribe = function () {
                if (!owner.$scope.channelDetial.subscribed) {
                    owner.$channelService.subscribe(owner.$scope.channelDetial.id).then(
                        function () {
                            owner.$scope.channelDetial.subscribed = true;
                            owner.$scope.channelDetial.subscriber_count++;
                            owner.$rootScope.$broadcast('sidebarRefresh');
                        }
                    )
                } else {
                    owner.$channelService.unsubscribe(owner.$scope.channelDetial.id).then(
                        function () {
                            owner.$scope.channelDetial.subscribed = false;
                            owner.$scope.channelDetial.subscriber_count--;
                            owner.$rootScope.$broadcast('sidebarRefresh');
                        }
                    )
                }
            };
        },

        _defineUpdateChannel: function () {
            var owner = this;
            this.$scope.showUpdateChannel = function () {
                var updateChannelModel = owner.$uibModal.open({
                    windowTemplateUrl: 'views/base/modal-window.html',
                    windowTopClass: 'new-channel-modal',
                    templateUrl: 'views/dialog/channel-update.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.amendChannel = owner.$scope.channelDetial;
                        $scope.thumbnail = owner.$scope.channelDetial.thumbnail;
                        $scope.$uibModalInstance = $uibModalInstance;
                    }]
                });
                updateChannelModel.result.then(
                    function () {
                        //ok callback;
                        owner._loadData();
                    },
                    function () {
                        //cancel callback;
                    }
                );
            };
        },

        _defineRefreshData: function () {
            var owner = this;
            this.$scope.refreshData = function () {
                owner._getExperiences(0, owner._VIEW_SIZE);
            };
        },

        _loadData: function () {
            var owner = this;
            //Channel detail info data.
            this.$channelService.detail(this.$stateParams.channelId).then(
                function (resp) {
                    owner.$scope.channelDetial = resp.data;
                },
                function (resp) {
                    if (resp && resp.status == 404) {
                        owner.$scope.notFound = true;
                    }
                }
            );
            //Channel's experiences list data.
            this._getExperiences(0, this._VIEW_SIZE);
            this.$scope.paging = function () {
                owner._getExperiences((owner.$scope.viewList.from + 1), owner._VIEW_SIZE)
            };

            if (_channelRelatedCache) {
                this.$scope.channelRelatedList = _channelRelatedCache;
            } else {
                this._channelRelated(0, this._RELATED_SIZE);
            }
            this.$scope.channelRelatedPaging = function () {
                owner._channelRelated((owner.$scope.channelRelatedList.from + owner._RELATED_SIZE), owner._RELATED_SIZE);
            };
        },

        _getExperiences: function (from, size) {
            var owner = this;
            this.$scope.loadingCompleted = false;
            this.$scope.loadingFailed = false;
            this.$channelService.experienceList(this.$stateParams.channelId, from, size).then(
                function (resp) {
                    owner._getExperiencesSuccessHandler(resp.data, from);
                    owner.$scope.loadingCompleted = true;
                }, function () {
                    owner.$scope.loadingFailed = true;
                }
            );
        },

        _getExperiencesSuccessHandler: function (data, from) {
            var lastList = this.$scope.viewList || {};
            lastList.from = from;
            lastList.total = data.total;
            lastList.last = data.last;
            if (from == 0) {
                lastList.result = [];
            } else {
                lastList.result = this.$scope.viewList.result;
            }
            for (var index = 0; index < data.result.length; index++) {
                lastList.result.push(data.result[index]);
            }
            if (data.result.length == 0) {
                lastList.last = true;
            }
            this.$scope.viewList = lastList;
        },

        _channelRelated: function (from, size) {
            var owner = this;
            this.$searchService.channelRelated(this.$stateParams.channelId, from, size).then(
                function (resp) {
                    owner._channelRelatedSuccessHandler(resp.data, from);
                }
            );
        },

        _channelRelatedSuccessHandler: function (data, from) {
            var lastList = data;
            lastList.from = from;
            if (data.result.length == 0) {
                lastList.last = true;
            }
            if (lastList.last) {
                lastList.last = false;
                lastList.from = -this._RELATED_SIZE;
            }
            this.$scope.channelRelatedList = lastList;
            _channelRelatedCache = lastList;
        }
    });

    ChannelController.$inject = ['$rootScope', '$scope', '$stateParams', '$uibModal', 'ChannelService', 'ExperienceService', 'SearchService'];

    angular.module('module.ChannelController', ['service.ChannelService', 'service.ExperienceService', 'service.SearchService']).controller('ChannelController', ChannelController);

});