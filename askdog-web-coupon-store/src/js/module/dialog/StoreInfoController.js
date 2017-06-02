define([
    'base/BaseController',
    'jquery.validator',
    'service/CouponService',
    'service/StoreService',
    'service/StorageService'
], function (BaseController) {

    var StoreInfoController = BaseController.extend({

        init: function ($rootScope, $scope, $state, $stateParams, $couponService, $uibModal, $storeService, $storageService) {
            this.$rootScope = $rootScope;
            this.$state = $state;
            this.$stateParams = $stateParams;
            this.$uibModalInstance = $scope.$parent.$uibModalInstance;
            this.$couponService = $couponService;
            this.$uibModal = $uibModal;
            this.$message = $scope.$parent.message;
            this.$storeService = $storeService;
            this.$storageService = $storageService;
            this._super($scope);
        },

        defineScope: function () {
            this.$scope.storeDetail = this.$scope.$parent.storeDetail || {};
            this._defineCancel();
            this._defineBindValidator();
            this._next();
            this._defineViewHandler();
            this._updateStore();
            this._previous();
            this._defineShowMap();
        },

        _defineCancel: function () {
            var owner = this;
            this.$scope.cancel = function () {
                owner.$uibModalInstance.dismiss("cancel");
            }
        },

        _defineViewHandler: function () {
            var owner = this;
            owner.$scope.storeSearch = function () {
                owner.$uibModalInstance.dismiss("cancel");
                var storeSearchModal = owner.$uibModal.open({
                    windowTemplateUrl: 'views/dialog/modal-window.html',
                    windowTopClass: 'pg-show-modal',
                    templateUrl: 'views/dialog/store-search.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                        $scope.storeInfo = owner.$scope.storeDetail;
                    }]
                });
                storeSearchModal.result.then(function () {

                });
            };

            this.$scope.videoLoad = function (index) {
                var file = $(".imageLoad")[index].files[0];
                if (file) {
                    var extension = file.name.split('.').pop().toLowerCase();
                    owner.$storageService.getToken('STORE_COVER', extension).then(
                        function (resp) {
                            var token = resp.data;
                            //file upload
                            owner.$scope._upload(token, file);
                        }
                    );
                }
            };
            this.$scope._upload = function (token, file) {
                owner.$storageService.upload(token, file).then(
                    function (resp) {
                        if (resp && resp.status == 200) {
                            owner.$scope.avatarCache = {
                                linkId: resp.data.linkId,
                                avatar: resp.data.url
                            };
                            owner.$scope.storeDetail.linkId = resp.data.linkId;
                        }
                    }
                );
            };

            owner.$scope.searchUser = function () {
                owner.$storeService.storeSearch(owner.$scope.userKey)
                    .then(function (resp) {
                        owner.$scope.searchResult = resp.data;
                    });
            };

            owner.$scope.selectUser = function (id, e) {
                $("#btn_add").removeClass("disabled");
                owner.$scope.storeDetail.userId = id;
            };

            owner.$scope.createUser = function (e) {
                var createUserModal = owner.$uibModal.open({
                    windowTemplateUrl: 'views/dialog/modal-window.html',
                    windowTopClass: 'pg-show-modal',
                    templateUrl: 'views/dialog/create-user.html',
                    controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                        $scope.$uibModalInstance = $uibModalInstance;
                        $scope.index = 0.9;
                        $scope.createUserCallback = function (user) {
                            $("#btn_add").removeClass("disabled");
                            owner.$scope.createdUser = user;
                            owner.$scope.storeDetail.userId = user.id;
                        };
                    }]
                });
            };

            this.$scope.createStore = function () {
                if ($("#btn_add").hasClass("disabled")) {
                    return;
                }
                owner.$storeService.addStore(owner.$scope.storeDetail).then(
                    function () {
                        owner.$uibModalInstance.close();
                    }
                );
            }
        },

        _defineBindValidator: function () {
            var owner = this;
            this.$scope.bindValidator = function (element) {
                $(element).validate({
                    rules: {
                        storeName: {
                            required: true,
                            maxlength: 24
                        },
                        storeAddress: {
                            required: true,
                            maxlength: 50
                        },
                        contactName: {
                            required: true
                        },
                        phone: {
                            required: true
                        },
                        description: {
                            required: true,
                            maxlength:150
                        },
                        hours:{
                            required: true
                        },
                        storeType:{
                            required:true,
                            maxlength:5
                        },
                        money:{
                            required:true
                        },
                        contactPhone:{
                            required:false
                        },
                        picFile:{
                            required:true
                        }
                    },
                    messages: {
                        storeName: {
                            required: '请填写商户名',
                            maxlength: "商户名过长"
                        },
                        storeAddress: {
                            required: '请填写商户地址',
                            maxlength: '地址过长'
                        },
                        contactName: {
                            required: ''
                        },
                        phone: {
                            required: ""
                        },
                        description: {
                            required: '',
                            maxlength:'输入内容过长'
                        },
                        hours:{
                            required: "请填写营业时间"
                        },
                        storeType:{
                            required:'',
                            maxlength:""
                        },
                        money:{
                            required:""
                        },
                        contactPhone:{
                            required:""
                        },
                        picFile:{
                            required:""
                        }
                    },
                    submitHandler: function () {
                        if (owner.$message.addStore) {
                            owner.$scope.next();
                        } else {
                            owner.$scope.updateStoreMessage();
                        }
                    }
                });
            }
        },

        _next: function () {
            var owner = this;
            owner.$scope.next = function () {
                if (owner.$scope.storeDetail.linkId == undefined) {
                    owner.$scope.error = '图片上传出现问题，请重试';
                    owner.$scope.$digest();
                    return;
                }
                if( owner.$scope.storeDetail.location == undefined){
                    owner.$scope.error ='请选取位置信息';
                    owner.$scope.$digest();
                    return;
                }
                var current = $(".form-view.active"), next = current.next(".form-view");
                if (next) {
                    current.removeClass("active").removeClass("a-fadeinB");
                    next.addClass("active").addClass("a-fadeinB");
                    $("#btn_previous").removeClass("hidden");
                    if (!next.next(".form-view").length) {
                        $("#btn_next").addClass("hidden");
                        $("#btn_add").removeClass("hidden");
                    }
                }
            }

        },

        _previous: function () {
            var owner = this;
            this.$scope.previous = function () {
                var current = $(".form-view.active"), prev = current.prev(".form-view");
                if (prev) {
                    current.removeClass("active").removeClass("a-fadeinB");
                    prev.addClass("active").addClass("a-fadeinB");
                    $("#btn_next").removeClass("hidden");
                    $("#btn_add").addClass("hidden");
                    if (!prev.prev(".form-view").length) {
                        $("#btn_previous").addClass("hidden");
                    }
                }
                owner.$scope.error = false;
            }
        },

        _updateStore: function () {
            var owner = this;
            this.$scope.updateStoreMessage = function () {
                if (owner.$scope.storeDetail.description == '') {
                    owner.$scope.error = '请填写商户描述';
                    owner.$scope.$digest();
                    return;
                }
                owner.$storeService.updateStoreInfo(owner.$scope.storeDetail.id, owner.$scope.storeDetail)
                    .then(function (resp) {
                        owner.$uibModalInstance.close();
                    });
            }
        },

        _defineShowMap:function(){
            var owner = this;

            function init() {
                window.qq = qq;
            }

            function loadScript() {
                var script = document.createElement("script");
                script.type = "text/javascript";
                script.src = "http://map.qq.com/api/js?v=2.exp&callback=qqmap_init";
                document.body.appendChild(script);
                window.qqmap_loaded = true;
            }

            window.qqmap_init = init;
            window.qqmap_loaded || loadScript();

            this.$scope.popover = function() {
                var initMap = function() {
                    var map = new qq.maps.Map(document.getElementById("map-container"), {
                        zoom: 13
                    });
                    var marker;

                    function placeMarker(location) {
                        marker && marker.setMap(null);
                        marker = new qq.maps.Marker({
                            position: location,
                            map: map
                        });
                        owner.$scope.storeDetail.location = location;
                        owner.$scope.$digest();
                    }

                    //调用地址解析类
                    var geocoder = new qq.maps.Geocoder({
                        complete : function(result){
                            map.setCenter(result.detail.location);
                            placeMarker(result.detail.location);
                            // owner.$scope.storeDetail.address || (owner.$scope.storeDetail.address = result.detail.address);
                        }
                    });

                    //获取城市列表接口设置中心点
                    var citylocation = new qq.maps.CityService({
                        complete : function(result){
                            map.setCenter(result.detail.latLng);
                        }
                    });

                    //添加监听事件, 获取鼠标单击事件
                    qq.maps.event.addListener(map, 'click', function(event) {
                        placeMarker(event.latLng);
                    });

                    // default location
                    var location = owner.$scope.storeDetail.location,
                        address = owner.$scope.storeDetail.address;
                    if (location) {
                        var latlng = new qq.maps.LatLng(location.lat, location.lng);
                        map.setCenter(latlng);
                        placeMarker(latlng);
                    } else if (address) {
                        geocoder.getLocation(address);
                    } else {
                        citylocation.searchLocalCity();
                    }
                };

                $(this).popover({
                    placement: 'bottom',
                    title: '请在地图中标记位置',
                    triggle: 'click manual',
                    template: '<div class="popover" style="max-width: inherit;" role="tooltip"> \
                            <div class="arrow"></div>\
                            <div>\
                                <button type="button" class="map close pull-right" style="margin: 5px;" onclick="javascript:$(\'#map-holder\').click();">×</button>\
                                <h3 class="popover-title"></h3>\
                            </div>\
                            <div id="map-container" style="width: 600px; height: 400px"></div>\
                        </div>'
                }).on('shown.bs.popover', function () {
                    initMap();
                });
            };
        }
    });

    StoreInfoController.$inject = ['$rootScope', '$scope', '$state', '$stateParams', 'CouponService', '$uibModal', 'StoreService', 'StorageService'];

    angular.module('module.dialog.StoreInfoController', ['service.CouponService', 'service.StoreService', 'service.StorageService']).controller('StoreInfoController', StoreInfoController);

});
