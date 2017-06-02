rmdir /S /Q build
mkdir build\css\lib

xcopy /sy src\lib build\lib\
xcopy /sy src\css\lib build\css\lib\

node r.js -o build.js
start cmd /c gulp

CMD /K