# WallpaperTODO2
A somewhat configurable, to do list program for Windows, which puts your notes on the wallpaper. 

*By the way, WallpaperTODO1 exists.*

After installing the program, in your `%LocalAppData%\Ult1\WallpaperTODO2` folder you can find config.cfg & profiles folder,  
`config.cfg` has some general options,  
while `\profiles` let you customise the look of the wallpaper. YAML is used for all configuration files.

The program uses notepad.exe for editing notes with symbols (e.g. '#', '!', "HW") at starts of lines denoting type of information in the line.


# How to use

*Many explanations can be found in `config.cfg` & `default_(light/dark/wallpaper).yml`*

Although, at the start there will be three different profiles, `default_light`, `default_dark` & `default_wallpaper`.

All default profiles have:
  - `!`, or important; 
  - `HW`, or homework; 
  - `ND`, alias for homework („namų darbai" meaning "homework" in my native tongue).

All profiles *(currently unconfigurable, although, if needed it's easy-ish to add)*: 
  - `#` is a comment & the line starting with this will **not** be drawn onto the wallpaper.

# Documentation

## Config.cfg

(string) `current_profile`, let's you select a profile. Its value should be a filename (without extension), which is in `\profiles` directory, although, it can also be a path, just again, **without file extension (`.yml`)!**

(boolean) `pop_up_dialog_boxes`, when a common error (one, that is caught by me) occurs, this makes a Windows dialog box popup, instead of just a message in the console & a crash.

(boolean) `open_notepad`, automatically opens todo.txt as notepad, let's you edit todos easily. You can turn this off to make it easier to designing a profile.

## {profile_name}.yml

*I'll finish this later as a proof of concept*

