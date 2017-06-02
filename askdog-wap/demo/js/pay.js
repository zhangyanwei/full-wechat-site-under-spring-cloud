//Load common code that includes config, then load the app
//logic for this page. Do the require calls here instead of
//a separate file so after a build there are only 2 HTTP
//requests instead of three.
require(['common'], function () {

    require([
        'app/pay'
    ], function (app) {
        'use strict';

        /*
         * place operations that need to initialize prior to app start here
         * using the `run` function on the top-level module
         */
        require(['domReady!'], function (document) {
            angular.bootstrap(document, ['pay'], {
                //strictDi: true
            });
        });
    });
});