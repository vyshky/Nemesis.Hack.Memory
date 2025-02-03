package memory;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import core.ProcessInfo;
import winapi.Psapi;

public class ProcessMemoryReader {
    private final Kernel32 kernel32;
    private final Psapi psapi;
    private final ProcessInfo processInfo;

    public ProcessMemoryReader(ProcessInfo processInfo) {
        this.kernel32 = Kernel32.INSTANCE;
        this.psapi = Psapi.INSTANCE;
        this.processInfo = processInfo;
    }

    public byte readMemory(int offset) {
        WinNT.HANDLE process = kernel32.OpenProcess(Kernel32.PROCESS_QUERY_INFORMATION | Kernel32.PROCESS_VM_READ, false, (int) processInfo.getPid());

        Memory memory = new Memory(1024);
        IntByReference bytesRecived = new IntByReference();
        Pointer baseAddress = getBaseAddress(process);

        kernel32.ReadProcessMemory(process, baseAddress, memory, (int) memory.size(), bytesRecived);


        return memory.getByte(offset);
    }

    public Pointer getBaseAddress(WinNT.HANDLE process) {

        if (process == null)
            throw new RuntimeException("Process with pid %s wasn't open!");


        char[] chars = new char[128];
        psapi.GetProcessImageFileName(process, chars, 128);

        WinDef.HMODULE[] hmodules = new WinDef.HMODULE[1024];
        IntByReference countReceived = new IntByReference();
        psapi.EnumProcessModules(process, hmodules, hmodules.length, countReceived);

        if (countReceived.getValue() == 0)
            throw new RuntimeException("No one module was received!");


        for (WinDef.HMODULE hmodule : hmodules) {
            if (hmodule == null)
                continue;

            Module module = new Module(process, hmodule);

            String fileName = module.getFileName();

            if (fileName.compareToIgnoreCase(processInfo.getCommand()) == 0) {

                return hmodule.getPointer();
            }

        }

        return null;//TODO
    }
}
