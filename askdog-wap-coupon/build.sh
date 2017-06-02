#!/usr/bin/env bash
rm -R ./build
mkdir -p ./build/lib
#cp -r ./source/lib ./build/lib
nodejs copy.js
nodejs r.js -o build.js
gulp

#sed -i "s/v=[^\'\"]*/v=$(date +%s)/g" build/index.html
