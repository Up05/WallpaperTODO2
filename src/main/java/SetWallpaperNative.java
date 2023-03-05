public class SetWallpaperNative {

    static {
        System.loadLibrary("setWallpaper");
    }

    public native void setWallpaper(String filepath);
}
