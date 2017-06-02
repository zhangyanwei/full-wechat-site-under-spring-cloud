define(['base/BaseService'], function (BaseService) {

    var UserService = BaseService.extend({

        init: function (http, scope) {
            this._super(http, scope);
        },

        status: function () {
            return this.get("/api/users/me/status");
        },

        me: function () {
            return this.get("/api/users/me");
        },

        noticePreview: function () {
            return this.get("/api/notifications/preview");
        },

        notifications: function (page, size) {
            var noticeUrl = "/api/notifications/?page={page}&size={size}".format({
                "page": page,
                "size": size
            });
            return this.get(noticeUrl);
        },

        login: function (userInfo) {
            return this.postForm("http://www.askdog.com/login?ajax=true", userInfo);
        },

        register: function (userInfo, phoneRegister) {
            var registerUri = '/api/users/phone';
            if (!phoneRegister) {
                registerUri = '/api/users';
            }
            return this.post(registerUri, userInfo);
        },

        confirm: function (token) {
            return this.put("/api/users/confirm?token={0}".format(token));
        },

        phoneIdentifyingCode: function (userInfo) {
            return this.post('/api/users/phone/code', userInfo);
        },

        recoverCheckPhone: function (phone) {
            var recoverUri = '/api/password/recover/phone?phone={phone}'.format({
                phone: phone
            });
            return this.post(recoverUri);
        },

        recoverCheckToken: function (phone, code) {
            var recoverUri = '/api/password/code/validation?phone={phone}&code={code}'.format({
                phone: phone,
                code: code
            });
            return this.post(recoverUri);
        },

        recoverIdentifyingCode: function (phone) {
            var recoverUri = '/api/password/recover/code?phone={phone}'.format({
                phone: phone
            });
            return this.post(recoverUri);
        },

        recoverByMail: function (mail) {
            var recoverUri = "/api/password/recover?mail={mail}".format({"mail": mail});
            return this.post(recoverUri);
        },

        recoverByPhone: function (recoverInfo) {
            return this.put("/api/password/phone", recoverInfo);
        },

        logout: function () {
            return this.get("http://www.askdog.com/logout?ajax=true");
        },

        viewHistory: function (page, size) {
            var historyUrl = "/api/users/me/views?page={page}&size={size}".format({
                "page": page,
                "size": size
            });
            return this.get(historyUrl)
        },

        ownedChannels: function (page, size) {
            var ownedChannelsUri = "/api/users/me/channels/owned?page={page}&size={size}".format({
                "page": page,
                "size": size
            });
            return this.get(ownedChannelsUri);
        },

        subscribedChannels: function (page, size) {
            var subscribedChannelsUri = "/api/users/me/channels/subscribed?page={page}&size={size}".format({
                "page": page,
                "size": size
            });
            return this.get(subscribedChannelsUri);
        },

        personalInfo: function () {
            return this.get("/api/users/me/profile/personal_info");
        },

        userInfo: function (userId) {
            return this.get("/api/users/{0}/user_info".format(userId));
        },

        savePersonalInfo: function (personalInfo) {
            return this.put("/api/users/me/profile/personal_info", personalInfo);
        },

        subscribeCategory: function (interests) {
            return this.put("/api/users/me/profile/personal_category", interests);
        },

        saveAvatar: function (linkId) {
            var avatarUri = "/api/users/me/profile/avatar?linkId={linkId}".format({
                "linkId": linkId
            });
            return this.put(avatarUri);
        },

        resetPassword: function (token, mail, password) {
            return this.put("/api/password", {
                'token': token,
                'mail': mail,
                'password': password
            });
        },

        changePassword: function (amendPassword) {
            return this.put("/api/password/change", amendPassword);
        },

        validatePasswordToken: function (token) {
            return this.put("/api/password/token/validation?token={0}".format(token));
        },

        accounts: function () {
            return this.get("/api/users/me/bind_accounts");
        },

        revenue: function () {
            return this.get("/api/users/me/revenue");
        },

        incomeSearch: function (from, to, page, size) {
            var incomeUri = "/api/users/me/income?from={from}&to={to}&page={page}&size={size}".format({
                "from": from,
                "to": to,
                "page": page,
                "size": size
            });
            return this.get(incomeUri);
        },

        withdrawSearch: function (from, to, page, size) {
            var withdrawUri = "/api/users/me/withdraw?from={from}&to={to}&page={page}&size={size}".format({
                "from": from,
                "to": to,
                "page": page,
                "size": size
            });
            return this.get(withdrawUri);
        },

        withdraw: function (withdrawal) {
            return this.post("/api/users/withdraw", withdrawal);
        },

        unbind: function (externalUserId, provider) {
            var unbindUrl = "/api/users/bind?externalUserId={externalUserId}&provider={provider}".format({
                "externalUserId": externalUserId,
                "provider": provider
            });
            return this.delete(unbindUrl);
        },

        bindToken: function (width, height) {
            var bindTokenUri = "/api/users/bind/wx/token?width={width}&height={height}".format({
                "width": width,
                "height": height
            });
            return this.get(bindTokenUri);
        },

        activityStatus: function () {
            return this.get("/api/activities/goldbucketstate");
        },

        activityCreate: function (pureExperience) {
            return this.post("/api/activities/goldbucket", pureExperience);
        }
    });

    angular.module('service.UserService', [])
        .provider('UserService', function () {
            this.$get = ['$http', '$rootScope', function (http, scope) {
                return new UserService(http, scope);
            }];
        });

});