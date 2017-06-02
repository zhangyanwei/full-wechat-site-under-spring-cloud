rm -R ./build
mkdir -p ./build/css/lib

nodejs copy.js
nodejs r.js -o build.js
gulp
cp ./source/lib/jquery/jquery.min.js ./build/lib/jquery/
