rmdir /S /Q build
mkdir build\css\lib

node copy.js
node r.js -o build.js
start cmd /c gulp