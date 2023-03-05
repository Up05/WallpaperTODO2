// #include <stdlib.h>
#include <libloaderapi.h>
#include <string>

int system (const char* command);
typedef char* PSTR, *LPSTR;

int main(){
    LPSTR buf = new char[256];
    GetModuleFileName(NULL, buf, 256);

    std::string a("start /b javaw -D\"java.library.path=bin\" -jar \"");
    std::string c("out\\artifacts\\WallpaperTODO2_jar\\WallpaperTODO2.jar\"");

    std::string out = a + buf + c;
    out.replace(out.find("run.exe"), 7, "");

    system((LPSTR) out.c_str());
    return 0;
}
