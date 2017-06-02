define([
    'base/BaseController',
    '_global',
    'socket',
    'stomp',
    'service/CouponService',
    'service/StoreService',
    'app/directive/qrCode'
], function (BaseController, _g, SockJS) {

    var EmployeeManagementController = BaseController.extend({

        init: function (_rootScope, _state, _scope, _stateParams, _couponService, _storeService, $uibModal, $window, $interval) {
            this.$rootScope = _rootScope;
            this.$state = _state;
            this.$stateParams = _stateParams;
            this.$couponService = _couponService;
            this.$storeService = _storeService;
            this.$uibModal = $uibModal;
            this.$interval = $interval;
            this.$window = $window;
            this._super(_scope);
        },

        defineScope: function () {
            this._defineViewHandler();
            this._defineFunctions();
            this._refreshListView();
            this._verificationEventListener();
        },

        _defineViewHandler: function () {
            var owner = this;
            this.$scope.addEmployee = function () {
                owner.$storeService.getEmployeeBindToken(owner.$stateParams.storeId).then(
                    function (rep) {
                        var token = rep.data.key;
                        var storeInfoModal = owner.$uibModal.open({
                            windowTemplateUrl: 'views/dialog/modal-window.html',
                            windowTopClass: 'pg-show-modal',
                            templateUrl: 'views/dialog/qrcode_add_employee.html',
                            controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                                $scope.$uibModalInstance = $uibModalInstance;
                                owner.$uibModalInstance = $uibModalInstance;
                                rep.data.timeout = rep.data.timeout / 1000;
                                $scope.expire = rep.data.timeout + '秒';
                                $scope.qrcode_url = '{0}/#/t/{1}'.format(_g.WAP_DOMAIN, token);

                                // TODO the timer not readable
                                var timer = owner.$interval(function () {
                                    $scope.expire = --rep.data.timeout + '秒';
                                    if (rep.data.timeout < 1) {
                                        $scope.expire = "已过期，请重新生成";
                                        owner.$interval.cancel(timer);
                                    }
                                }, 1000);

                                $scope.cancel = function () {
                                    $uibModalInstance.dismiss("cancel");
                                }

                            }]
                        });
                        storeInfoModal.result.then(function () {
                            owner._refreshListView();
                        });

                    }, function () {
                    }
                );

            };

            this.$scope.deleteEmployee = function (employeeId) {
                var messageModel = owner.$uibModal.open({
                    windowTemplateUrl: 'views/dialog/modal-window.html',
                    windowTopClass: 'modal-default',
                    templateUrl: 'views/dialog/message.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                        $scope.message = '确定要解绑吗？';
                    }]
                });
                messageModel.result.then(
                    function () {
                        owner.$storeService.deleteEmployee(owner.$stateParams.storeId,employeeId).then(
                            function () {
                                owner._refreshListView(owner.$scope.page - 1);
                            }
                        );
                    }
                );
            };

        },

        _defineFunctions: function() {
            this.$scope.goPage = function(page) {
                this.$storeService.getEmployees(this.$stateParams.storeId, page || 0, 10).then(
                    function (resp) {
                        this.$scope.data = resp.data;
                    }.bind(this)
                );
            }.bind(this);
        },

        _refreshListView: function (page) {
            this.$scope.goPage(page);
        },

        _verificationEventListener: function () {
            var owner = this;
            var ws = new SockJS("http://websocket.askdog.com/websocket");

            var client = Stomp.over(ws);

            client.heartbeat.outgoing = 20000; // client will send heartbeats every 20000ms
            client.heartbeat.incoming = 0;     // client does not want to receive heartbeats
                                               // from the server

            var connectCallback = function (frame) {
                console.log('[Web Socket] Online !');
                console.log('[Web Socket] transport: ' + ws.transport);

                client.subscribe("/topic/event/store/bind_employee", function (message) {
                    var messageElement = document.createElement("div");
                    messageElement.innerHTML = message.body;
                    console.log('[Web Socket] message: ' + JSON.stringify(message, null, 4));

                    var verifyEvent = JSON.parse(message.body);

                    if (verifyEvent.target.id == owner.$stateParams.storeId) {
                        owner.$uibModalInstance.dismiss("cancel");
                        owner.$uibModalInstance = null;
                        owner._refreshListView();
                    }

                });
            };

            var errorCallback = function (error) {
                console.log('[Web Socket] error: ' + JSON.stringify(error, null, 4));
            };

            client.connect({}, connectCallback, errorCallback);
        }

    });

    EmployeeManagementController.$inject = ['$rootScope', '$state', '$scope', '$stateParams', 'CouponService', 'StoreService', '$uibModal', '$window', '$interval'];

    angular.module('module.EmployeeManagementController', []).controller('EmployeeManagementController', EmployeeManagementController);

});

