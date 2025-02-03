package memory;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Psapi;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import core.ProcessInfo;
import memory.exception.ModulesNotFoundException;

public class ProcessMemoryReader {
    private static final Kernel32 KERNEL32 = Kernel32.INSTANCE;
    private static final Psapi PSAPI = Psapi.INSTANCE;
    private final long pid;

    /**
     * Создание ридера для взаимодействия с процессом
     *
     * @param pid - процесса
     */
    public ProcessMemoryReader(long pid) {
        this.pid = pid;
    }

    /**
     * Чтение байта памяти по указанному отступу от начала памяти процесса
     *
     * @param offset отступ от начала памяти процесса
     * @return данные хранящиеся по указанному адресу
     */
    public byte readMemory(int offset) {
        WinNT.HANDLE process = KERNEL32.OpenProcess(Kernel32.PROCESS_QUERY_INFORMATION | Kernel32.PROCESS_VM_READ, false, (int) pid);

        Memory memory = new Memory(1024);
        IntByReference bytesReceived = new IntByReference();
        Pointer baseAddress = getBaseAddress(process);

        KERNEL32.ReadProcessMemory(process, baseAddress, memory, (int) memory.size(), bytesReceived);


        return memory.getByte(offset);
    }

    /**
     * Получение адреса начала памяти процесса
     *
     * @param process процесс, чей адрес начала памяти необходимо получить
     * @return указатель на начало памяти процесса
     */
    public Pointer getBaseAddress(WinNT.HANDLE process) {

        char[] chars = new char[128];
        PSAPI.GetProcessImageFileName(process, chars, 128);

        String processImageFileName = Native.toString(chars);

        WinDef.HMODULE[] hmodules = new WinDef.HMODULE[1024];
        IntByReference countReceived = new IntByReference();
        PSAPI.EnumProcessModules(process, hmodules, hmodules.length, countReceived);

        if (countReceived.getValue() == 0)
            throw new ModulesNotFoundException();

        Pointer baseAddress = null;
        for (WinDef.HMODULE hmodule : hmodules) {
            if (hmodule == null)
                continue;

            Module module = new Module(process, hmodule);

            String fileName = module.getAbsoluteImagePath();


            if (fileName.compareToIgnoreCase(processImageFileName) == 0) {

                baseAddress = hmodule.getPointer();
                break;
            }

        }


        return baseAddress;
    }
}
