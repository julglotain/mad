var gulp = require('gulp'),
    del = require('del');


gulp.task('clean:frontend-resources', function (cb) {
    del([
        'src/main'
    ], cb);
});

gulp.task('hello',function(){
   console.log('hello world');
});