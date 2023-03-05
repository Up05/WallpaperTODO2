#include "jni.h"
#include "setWallpaperNative.h"
#include <windows.h>

JNIEXPORT void JNICALL Java_SetWallpaperNative_setWallpaper(JNIEnv * env, jobject jobj, jstring jstrpath){
    LPWSTR path = (LPWSTR) env->GetStringUTFChars(jstrpath, NULL);

    SystemParametersInfo(SPI_SETDESKWALLPAPER, 0, path, SPIF_UPDATEINIFILE);
}