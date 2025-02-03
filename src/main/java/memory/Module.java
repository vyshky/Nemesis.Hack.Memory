package memory;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Psapi;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;

public class Module {
    private final WinNT.HANDLE process;
    private final WinDef.HMODULE hmodule;

    private Pointer lpBaseOfDll = null;
    private int sizeOfImage = 0;
    private Pointer entryPoint = null;

    private final winapi.Psapi psapi = winapi.Psapi.INSTANCE;


    public Module(WinNT.HANDLE process, WinDef.HMODULE hmodule) {
        this.process = process;
        this.hmodule = hmodule;
    }

    public String getFileName() {
        byte[] imageFileName = new byte[256];
        psapi.GetModuleFileNameExA(process, hmodule, imageFileName, 256);
        return Native.toString(imageFileName);
    }

    public String getBaseName() {
        byte[] imageBaseName = new byte[256];
        psapi.GetModuleBaseNameA(process, hmodule, imageBaseName, 256);
        return Native.toString(imageBaseName);
    }

    private void getModuleInfo() {
        if (lpBaseOfDll == null) {
            Psapi.MODULEINFO moduleinfo = new Psapi.MODULEINFO();
            psapi.GetModuleInformation(process, hmodule, moduleinfo, moduleinfo.size());

            lpBaseOfDll = moduleinfo.lpBaseOfDll;
            sizeOfImage = moduleinfo.SizeOfImage;
            entryPoint = moduleinfo.EntryPoint;

        }
    }

    public Pointer getLpBaseOfDll() {
        getModuleInfo();
        return lpBaseOfDll;
    }

    public int getSizeOfImage() {
        getModuleInfo();
        return sizeOfImage;
    }

    public Pointer getEntryPoint() {
        getModuleInfo();
        return entryPoint;
    }
}
