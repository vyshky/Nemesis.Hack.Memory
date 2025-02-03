package winapi;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public interface Psapi extends StdCallLibrary {
    Psapi INSTANCE = Native.load("psapi", Psapi.class, W32APIOptions.DEFAULT_OPTIONS);


    boolean GetModuleInformation(WinNT.HANDLE hProcess, WinDef.HMODULE hModule, com.sun.jna.platform.win32.Psapi.MODULEINFO modinfo, int cb);

    int GetModuleFileNameExA(WinNT.HANDLE hProcess, WinDef.HMODULE hModule, byte[] lpImageFileName, int nSize);

    int GetModuleBaseNameA(WinNT.HANDLE hProcess, WinDef.HMODULE hModule, byte[] lpImageFileName, int nSize);

    int GetProcessImageFileName(WinNT.HANDLE var1, char[] var2, int var3);

    boolean EnumProcessModules(WinNT.HANDLE var1, WinDef.HMODULE[] var2, int var3, IntByReference var4);

}
