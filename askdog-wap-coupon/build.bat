rmdir /S /Q build

xcopy /sy source\lib build\lib\

node r.js -o build.js
start cmd /c gulp

CMD /K