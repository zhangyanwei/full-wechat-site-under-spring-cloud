var gulp = require('gulp');
var templateCache = require('gulp-angular-templatecache');
gulp.task('compress', function () {
    return gulp.src('source/views/**/*.html')
        .pipe(templateCache('template.js', {
            module: 'app.template',
            root: 'views/',
            standalone: true,
            templateHeader: 'define([\'angular\'], function() { \'use strict\'; angular.module("<%= module %>"<%= standalone %>).run(["$templateCache", function($templateCache) {',
            templateBody: '$templateCache.put("<%= url %>",\'<%= contents %>\');',
            templateFooter: '}]);});'
        }))
        .pipe(gulp.dest('build/script/app'));
});

var replace = require('gulp-replace');
gulp.task('default', ['compress'], function(){
    gulp.src(['build/index.html'])
        .pipe(replace(/([\?'"])v=[^'"]+/g, '$1v=' + Math.ceil(new Date().getTime() / 1000)))
        .pipe(gulp.dest('build'));
});