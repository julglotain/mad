var gulp = require('gulp'),
    $ = require('gulp-load-plugins')({
        pattern: ['gulp-*', 'main-bower-files']
    });

var config = {
    baseSrc: 'src/main/frontend',
    baseDist: 'src/main/resources/frontend'
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
        .pipe($.uglify({
            mangle: false
        }))
        .pipe(gulp.dest(config.baseDist + '/scripts'))
        .pipe($.size());


    return gulp
        .src($.mainBowerFiles())
        .pipe($.filter('*.js'))
        .pipe($.concat('vendors.js'))
        .pipe($.uglify({
            mangle: false
        }))
        .pipe(gulp.dest(config.baseDist + '/scripts' ));

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

gulp.task('styles', function () {

    return gulp.src(config.baseSrc + '/styles/*.scss')
        .pipe($.sass({style: 'expanded'}))
        .on('error', handleError)
        .pipe($.autoprefixer('last 1 version'))
        .pipe(gulp.dest(config.baseDist + '/styles'))
        .pipe($.size());
});

gulp.task('build', ['html', 'styles', 'scripts', 'fonts']);