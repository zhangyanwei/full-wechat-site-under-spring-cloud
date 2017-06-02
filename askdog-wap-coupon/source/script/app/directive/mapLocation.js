define(['_global', 'angular'], function (_g) {

    function mapLocation() {

        return {
            restrict: 'AE',
            scope: {
                location: '<',
                name: '<',
                address: '<'
            },
            link: function (scope, element, attributes) {
                $(element).on('click', function (event) {
                    event.stopPropagation();
                    require(['wechat'], function (wx) {
                        wx.openLocation({
                            latitude: scope.location.lat, // 纬度，浮点数，范围为90 ~ -90
                            longitude: scope.location.lng, // 经度，浮点数，范围为180 ~ -180。
                            name: scope.name, // 位置名
                            address: scope.address, // 地址详情说明
                            scale: 20, // 地图缩放级别,整形值,范围从1~28。默认为最大
                            infoUrl: '' // 在查看位置界面底部显示的超链接,可点击跳转
                        });
                    });
                    return false;
                });
            }
        }
    }

    mapLocation.$inject = [];
    angular.module('app.directive.mapLocation', []).directive('mapLocation', mapLocation);
});