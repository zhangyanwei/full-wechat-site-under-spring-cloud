define([
    'base/BaseService',
    'angular',
    'class'
], function (BaseService) {

    var UserService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        getStates: function () {
            return this.get("/api/users/me/status");
        },

        getUserInfo: function () {
            return this.get("/api/users/me");
        },

        getHistory: function (page, size) {
            return this.get("/api/users/me/views", {
                params: {
                    page: page,
                    size: size
                }
            });
        },

        updatePassword: function (oldPassword, newPassword) {
            var data = {
                origin_password: oldPassword,
                new_password: newPassword
            };
            return this.put("/api/password/change", data);
        },

        login: function (username, password) {
            return this.postForm("http://m.askdog.com/login?ajax=true", {
                username: username,
                password: password
            });
        },

        recover: function (mail) {
            var recoverUri = "/api/password/recover?mail={mail}".format({"mail": mail});
            return this.post(recoverUri);
        },

        phoneRecover: function (phone) {
            return this.post("/api/password/recover/phone?phone=" + phone);
        },

        phoneRetSend: function (phone) {
            return this.post("/api/password/recover/code?phone=" + phone);
        },

        phoneOrCodeValidator: function (phoneInfo) {
            return this.post("/api/password/code/validation?phone={phone}&code={code}".format({
                "phone": phoneInfo.phone,
                "code": phoneInfo.code
            }));
        },

        updatePhonePassword: function (data) {
            return this.put("/api/password/phone", data);
        },

        logout: function () {
            return this.get("http://m.askdog.com/logout?ajax=true");
        },

        register: function (userInfo) {
            return this.post("/api/users", userInfo);
        },

        phoneRegisterCode: function (phoneInfo) {
            return this.post("/api/users/phone/code", phoneInfo);
        },

        phoneRegister: function (phoneInfo) {
            return this.post("/api/users/phone", phoneInfo);
        },

        getRevenue: function () {
            return this.get("/api/users/me/revenue");
        },

        getIncome: function (startTime, endTime, page, size) {
            return this.get("/api/users/me/income", {
                params: {
                    from: startTime,
                    to: endTime,
                    page: page,
                    size: size
                }
            })
        },

        getWithdraw: function (form, to, size, page) {
            return this.get("/api/users/me/withdraw", {
                params: {
                    from: form,
                    to: to,
                    size: size,
                    page: page
                }
            })
        },

        updateInfo: function (personalInfo) {
            return this.put("/api/users/me/profile/personal_info", personalInfo);
        },

        bindAccounts: function () {
            return this.get("/api/users/me/bind_accounts")
        },

        getInfo: function () {
            return this.get("/api/users/me/profile/personal_info");
        },

        cashToWeChat: function (amount) {
            var data = {
                withdrawal_way: "WXPAY",
                withdrawal_amount: amount
            };
            return this.post("/api/users/withdraw", data);
        },

        subscribeCategory: function (interests) {
            return this.put("/api/users/me/profile/personal_category", interests);
        },

        tokenValidate: function (token) {
            return this.get("/api/users/bind/" + token)
        },

        bindWechat: function (token) {
            return this.put('/api/users/bind/' + token);
        },

        confirm: function (token) {
            return this.put("/api/users/confirm?token={0}".format(token));
        },

        resetPwd: function (token, userInfo) {
            var data = {
                mail: userInfo.mail,
                token: token,
                password: userInfo.password
            };
            return this.put("/api/password", data);
        },

        validatePasswordToken: function (token) {
            return this.put("/api/password/token/validation?token={0}".format(token));
        },

        otherInfo: function (userId) {
            return this.get("/api/users/{0}/user_info".format(userId));
        },

        getNotice: function (page, size) {
            var noticeUrl = "/api/notifications/?page={page}&size={size}".format({
                "page": page,
                "size": size
            });
            return this.get(noticeUrl);
        },

        subscribedChannels: function (page, size) {
            var subscribedChannelsUri = "/api/users/me/channels/subscribed?page={page}&size={size}".format({
                "page": page,
                "size": size
            });
            return this.get(subscribedChannelsUri);
        }
    });

    angular.module('service.UserService', [])
        .provider('userService', function () {
            this.$get = ['$http', '$rootScope', function (http, scope) {
                return new UserService(http, scope);
            }];
        })
        .provider('auth', function () {
            this.$get = ['$window', '$stateParams', 'userService', '$uibModal', '$location', function ($window, $stateParams, userService, $uibModal, $location) {
                return function (src) {
                    userService.getStates().success(function (data) {
                        if (data == 'ANONYMOUS') {
                            var userAgent = $window.navigator.userAgent;
                            var matches = /MicroMessenger\/([^\s\/.]+)/.exec(userAgent);
                            if (matches && matches.length > 1) {
                                var version = matches[1];
                                if (version >= 5) {
                                    if ($location.$$path.startWith('/exp') || $location.$$path.startWith('/my-channel')) {
                                        if (src) {
                                            $(src.target).blur();
                                        }
                                        var shareModel = $uibModal.open({
                                            windowTemplateUrl: 'views/base/modal-window.html',
                                            windowTopClass: 'weLogin-Modal',
                                            templateUrl: 'views/dialog/wechat-login.html',
                                            controller: ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {
                                                $scope.request = $stateParams.request;
                                                $scope.location = window.location.href;
                                                $scope.$uibModalInstance = $uibModalInstance;
                                            }]
                                        });
                                    }
                                    else {
                                        window.location.href = "/login/wechat?request=" + encodeURIComponent(src || $stateParams.request || window.location.href);
                                    }
                                    return;
                                }
                            }
                            if ($location.$$path.startWith('/exp') || $location.$$path.startWith('/my-channel')) {
                                window.location.href = "/#/login?request=http:" + encodeURIComponent(window.location.href.split(":")[1]);
                            } else {
                               if(src){
                                   console.log(src);
                                   window.location.href = "/#/login?request=http:" + encodeURIComponent(src.split("http:")[1] || $stateParams.request.split("http:")[1] || window.location.href.split("http:")[1]);
                               }else{
                                   window.location.href = "/#/login?request=http:" + encodeURIComponent(window.location.href.split("http:")[1]);
                               }

                            }

                        }
                    });
                }
            }];
        });

});