define(['angular'], function () {

    var config = {
        level: 'INFO',
        delay: 5000
    };

    var LogLevels = ['DEBUG', 'TRACE', 'INFO', 'WARN', 'ERROR', 'FATAL'];

    function levelPriority(level) {
        return LogLevels.indexOf(level.toUpperCase());
    }

    function isLevelEnabled(level) {
        return levelPriority(level) >= levelPriority(config.level);
    }

    var Logger = function (scope) {
        this.show = function (message) {
            scope.$emit('LOAD', message);
        };

        this.hide = function () {
            scope.$emit('UNLOAD');
        };
    };

    Logger.prototype = {

        log: function (level, message) {
            if (isLevelEnabled(level)) {
                this.show(message);
                this.setTimeout(function () {
                    this.hide();
                }, config.delay)
            }
        },

        trace: function (message) {
            this.log('TRACE', message);
        },

        info: function (message) {
            this.log('INFO', message);
        },

        warn: function (message) {
            this.log('WARN', message);
        },

        error: function (message) {
            this.log('ERROR', message);
        },

        fatal: function (message) {
            this.log('FATAL', message);
        }
    };

    Logger.prototype.constructor = Logger;

    return {
        setLevel: function (level) {
            config.level = level;
        },

        logger: function (scope) {
            return new Logger(scope);
        }
    };

});