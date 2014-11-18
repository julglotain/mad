var gulp = require('gulp'),
    ngHtml2Js = require('gulp-ng-html2js'),
    $ = require('gulp-load-plugins')({
        pattern: ['gulp-*', 'main-bower-files']
    });

var config = {
    baseSrc: 'src/main/frontend',
    baseDist: 'target/classes/frontend'
};

function handleError(err) {
    console.error(err.toString());
    this.emit('end');
}

gulp.task('clean', function () {

    return gulp.src([config.baseDist], { read: false }).pipe($.rimraf());

});

gulp.task('scripts', ['clean'], function () {

    gulp
        .src(config.baseSrc + '/**/*.js')
        .pipe($.jshint())
        .pipe($.jshint.reporter('jshint-stylish'))
        .pipe($.concat("app.js"))
        /*
         .pipe($.uglify({
         mangle: false
         }))
         */
        .pipe(gulp.dest(config.baseDist + '/scripts'))
        .pipe($.size());


    return gulp
        .src($.mainBowerFiles())
        .pipe($.filter('*.js'))
        .pipe($.concat('vendors.js'))
        /*
         .pipe($.uglify({
         mangle: false
         }))
         */
        .pipe(gulp.dest(config.baseDist + '/scripts'));

});


gulp.task('html', ['clean'], function () {

    return gulp
        .src(config.baseSrc + '/views/*.html')
        .pipe(gulp.dest(config.baseDist + '/views'))
        .pipe($.size());

});

gulp.task('fonts', ['clean'], function () {
    return gulp.src($.mainBowerFiles())
        .pipe($.filter('**/*.{eot,svg,ttf,woff}'))
        .pipe($.flatten())
        .pipe(gulp.dest(config.baseDist + '/fonts'))
        .pipe($.size());
});

gulp.task('styles', ['clean','styles-vendor','styles-app']);

gulp.task('styles-app', ['clean'], function () {

    return gulp.src(config.baseSrc + '/styles/*.scss')
        .pipe($.sass({style: 'expanded'}))
        .on('error', handleError)
        .pipe($.autoprefixer('last 1 version'))
        .pipe($.csso())
        .pipe(gulp.dest(config.baseDist + '/styles'))
        .pipe($.size());

});

gulp.task('styles-vendor', ['clean'], function () {

    var lessFilter = $.filter('**/*.less');
    var cssFilter = $.filter('**/*.css');

    return gulp.src($.mainBowerFiles())

        // less files
        .pipe(lessFilter)
        .pipe($.less())
        .pipe(lessFilter.restore())

        // then css files
        .pipe(cssFilter)

        // minify and concatenate
        .pipe($.csso())
        .pipe($.concat('vendors.css'))

        .pipe(gulp.dest(config.baseDist + '/styles'));


});



gulp.task('partials', ['clean'], function () {

    return gulp.src(config.baseSrc + '/partials/*.tpl.html')
        .pipe(ngHtml2Js({
            moduleName: "mad.partials",
            prefix: "/partials/"
        }))
        .pipe($.concat('partials-tpls.js'))
        .pipe(gulp.dest(config.baseDist + '/scripts'));

});

gulp.task('build', ['html', 'styles', 'scripts', 'fonts', 'partials']);