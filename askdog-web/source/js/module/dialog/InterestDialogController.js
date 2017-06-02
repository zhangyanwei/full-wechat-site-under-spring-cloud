define(['base/BaseController', 'app/directive/adAnalytics', 'service/CategoryService', 'service/UserService'], function (BaseController) {

    var InterestDialogController = BaseController.extend({

        init: function (_rootScope, _scope, _stateParams, _categoryService, _userService) {
            this.$rootScope = _rootScope;
            this.$stateParams = _stateParams;
            this.$categoryService = _categoryService;
            this.$userService = _userService;

            this.$uibModalInstance = _scope.$parent.$uibModalInstance;
            this._super(_scope);
        },

        defineScope: function () {
            this._loadData();
            this._defineSelectItem();
            this._defineSubscribe();
            this._defineCancel();
        },

        _loadData: function () {
            var thumbnail = {
                LAW: 'images/category/category-1.png',
                ENTERTAINMENT: 'images/category/category-2.png',
                ECONOMIC: 'images/category/category-3.png',
                HEALTH: 'images/category/category-4.png',
                SCIENCE: 'images/category/category-5.png',
                EDUCATION: 'images/category/category-6.png',
                CULTURE: 'images/category/category-7.png',
                LIFE: 'images/category/category-8.png',
                ART: 'images/category/category-9.png',
                IT: 'images/category/category-10.png',
                CATE: 'images/category/category-11.png',
                GUITAR: 'images/category/category-12.png'
            };
            var owner = this;
            this.$categoryService.categories().then(
                function (resp) {
                    for (var index = 1; index <= resp.data.length; index++) {
                        //TODO
                        var code = resp.data[index - 1].code;
                        resp.data[index - 1].thumbnail = thumbnail[code];
                    }
                    owner.$scope.categories = resp.data;
                }
            );
        },

        _defineSelectItem: function () {
            var owner = this;
            this.$scope.selectItem = function (code) {
                for (var index = 0; index < owner.$scope.categories.length; index++) {
                    if (owner.$scope.categories[index].code == code) {
                        owner.$scope.categories[index].selected = !owner.$scope.categories[index].selected;
                        break;
                    }
                }
                var hasSelected = false;
                for (var index = 0; index < owner.$scope.categories.length; index++) {
                    if (owner.$scope.categories[index].selected) {
                        hasSelected = true;
                        break;
                    }
                }
                owner.$scope.selected = hasSelected;
            };
        },

        _defineSubscribe: function () {
            var owner = this;
            this.$scope.subscribe = function () {
                var interests = [];
                for (var index = 0; index < owner.$scope.categories.length; index++) {
                    if (owner.$scope.categories[index].selected) {
                        interests.push(owner.$scope.categories[index].code);
                    }
                }
                owner.$userService.subscribeCategory({"category_codes": interests}).then(
                    function () {
                        owner.$uibModalInstance.close();
                    }
                );
            };
        },

        _defineCancel: function () {
            var owner = this;
            this.$scope.cancel = function () {
                owner.$uibModalInstance.dismiss("cancel");
            };
        }
    });

    InterestDialogController.$inject = ['$rootScope', '$scope', '$stateParams', 'CategoryService', 'UserService'];

    angular.module('module.dialog.InterestDialogController', ['service.CategoryService', 'service.UserService']).controller('InterestDialogController', InterestDialogController);

});