rm -R ./build
mkdir ./build

nodejs copy.js
nodejs r.js -o build.js
gulp
