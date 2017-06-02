rm -R ./build
mkdir -p ./build/lib
mkdir -p ./build/css/lib

cp -r ./src/lib/* ./build/lib/
cp -r ./src/css/lib/* ./build/css/lib/

nodejs r.js -o build.js
gulp
