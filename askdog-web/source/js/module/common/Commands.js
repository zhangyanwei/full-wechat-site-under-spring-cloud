define([], function () {

    var checkArgument = AskDog.Precondition.checkArgument;

    function Command(thisObject) {

        this.$this = thisObject || this;

        var _attrs = {
            icon: null,
            tip: null,
            label: null,
            enabled: true
        };

        var _callbacks = {};

        function property(_attrs, attr) {
            this[attr] = function (value) {
                if (value === undefined) {
                    return _attrs[attr];
                }

                _attrs[attr] = value;
                return this;
            }
        }

        for (var attr in _attrs) {
            if (_attrs.hasOwnProperty(attr)) {
                property.call(this, _attrs, attr);
            }
        }

        this.callback = function (action, func) {
            _callbacks[action] = func;
            return this;
        };

        this.visible = function () {
            var callback = _callbacks['visible'];
            return (callback === undefined) || callback.apply(this.$this, arguments);
        };

        this.execute = function () {
            var callback = _callbacks['execute'];
            callback && callback.apply(this.$this, arguments);
        };
    }

    function Commands($scope, group) {

        group = group || 'default';
        $scope.commands = $scope.commands || {};
        $scope.commands[group] = $scope.commands[group] || {
                $all: []
            };

        return {
            register: function (name, command) {
                var commands = $scope.commands[group];

                checkArgument(!commands[name], "command [{0}] already defined.".format(name));

                command.$this && (command.$this = $scope);
                commands[name] = command;
                commands.$all.push(command);
                return this;
            }
        };
    }

    Commands.new = function ($scope, group) {
        return new Commands($scope, group);
    };

    Commands.command = function () {
        return new Command();
    };

    return Commands;

});