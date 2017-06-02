require(['_global', 'config'], function (_g) {
    "use strict";

    function render() {

        _hmt.push(['_setCustomVar', 2, 'from', _g.loc.param('from') || 'direct']);

        require(['app/exp'], function (app) {
            'use strict';

            require(['domReady!'], function (document) {
                angular.bootstrap(document, ['exp'], {
                    //strictDi: true
                });
            });
        });
    }

    _g.authenticateIfRequired(_g.loc.fragment(), render);

    _g.initWechat();
});
