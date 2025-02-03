package memory;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Psapi;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;

public class Module {
    private static final Psapi PSAPI = Psapi.INSTANCE;

    private final WinNT.HANDLE process;
    private final WinDef.HMODULE hmodule;
    private Pointer lpBaseOfDll = null;
    private int sizeOfImage = 0;
    private Pointer entryPoint = null;


    public Module(WinNT.HANDLE process, WinDef.HMODULE hmodule) {
        this.process = process;
        this.hmodule = hmodule;
    }

    /**
     * Получение полного пути до файла.
     *
     * @return полный путь до запускаемого файла
     */

    public String getAbsoluteImagePath() {
        byte[] imagePath = new byte[256];
        PSAPI.GetModuleFileNameExA(process, hmodule, imagePath, 256);
        return Native.toString(imagePath);
    }


    /**
     * Адрес загрузки модуля
     *
     * @return Адрес загрузки модуля
     */
    public Pointer getLpBaseOfDll() {
        getModuleInfo();
        return lpBaseOfDll;
    }

    /**
     * Размер линейного пространства, занимаемого модулем, в байтах
     *
     * @return размер пространства занимаемого модулем
     */
    public int getSizeOfImage() {
        getModuleInfo();
        return sizeOfImage;
    }

    /**
     * Точка входа модуля
     *
     * @return адрес точки входа модуля
     */
    public Pointer getEntryPoint() {
        getModuleInfo();
        return entryPoint;
    }


    /**
     * Получение информации о модуле
     */
    private void getModuleInfo() {
        if (lpBaseOfDll == null) {
            Psapi.MODULEINFO moduleinfo = new Psapi.MODULEINFO();
            PSAPI.GetModuleInformation(process, hmodule, moduleinfo, moduleinfo.size());

            lpBaseOfDll = moduleinfo.lpBaseOfDll;
            sizeOfImage = moduleinfo.SizeOfImage;
            entryPoint = moduleinfo.EntryPoint;

        }
    }
}
