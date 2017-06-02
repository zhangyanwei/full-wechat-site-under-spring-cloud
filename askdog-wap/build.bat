rmdir /S /Q build
mkdir build

node copy.js
node r.js -o build.js
start cmd /c gulp

CMD /K