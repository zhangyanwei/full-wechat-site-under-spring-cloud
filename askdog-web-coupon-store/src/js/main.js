//Load common code that includes config, then load the app
//logic for this page. Do the require calls here instead of
//a separate file so after a build there are only 2 HTTP
//requests instead of three.

define('_global', function () {

    // TODO we should move the related pages into current domain, not put it in end-user front-end.
    // now we using it just because we not need write complex code to handle it.
    this.WAP_DOMAIN = window._WAP_DOMAIN || 'http://coupon.askdog.com';

    return this;
});

(function () {

    function render() {
        require(['config'], function () {

            require([
                'app/store'
            ], function (app) {
                'use strict';

                /*
                 * place operations that need to initialize prior to app start here
                 * using the `run` function on the top-level module
                 */
                require(['domReady!'], function (document) {
                    angular.bootstrap(document, ['store'], {
                        //strictDi: true
                    });
                });
            });
        });
    }

    function navigateIf(hash, authorities, expectAuthorities) {
        return expectAuthorities.find(function (item) {
            if (authorities.indexOf('ROLE_' + item) > -1) {
                window.location.href = hash;
                return true;
            }
        });
    }

    var xhr = new XMLHttpRequest();
    xhr.withCredentials = true;

    xhr.addEventListener("readystatechange", function () {
        if (this.readyState === 4) {
            if (this.status == 200) {
                window.session = {user: JSON.parse(this.responseText)};
                var authorities = window.session.user.authorities;
                // 'ADMIN', 'ROLE_AGENT_ADMIN', 'ROLE_AGENT_EMPLOYEE', 'ROLE_STORE_ADMIN', 'ROLE_STORE_EMPLOYEE'
                navigateIf("/#/welcome", authorities, ['ADMIN', 'AGENT_ADMIN', 'AGENT_EMPLOYEE'])
                || navigateIf("/#/s/" + window.session.user.store_ids[0] + "/verify", authorities, ['STORE_ADMIN', 'STORE_EMPLOYEE'])
                || navigateIf("/#/403", authorities, ['USER']);
            } else {
                window.location.href = "/#/welcome";
            }

            render();
        }
    });

    xhr.open("GET", "/api/users/current");
    xhr.setRequestHeader("cache-control", "no-cache");
    xhr.send();
})();