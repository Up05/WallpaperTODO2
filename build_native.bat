@echo off
g++ -c -Isrc\native -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" src\native\setWallpaper.cpp -o bin\setWallpaper.o -ggdb
g++ -shared -ggdb -o bin\setWallpaper.dll bin\setWallpaper.o -Wl,--add-stdcall-alias

@echo on