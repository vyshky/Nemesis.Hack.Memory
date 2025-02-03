import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import core.CoreProcessManager;
import core.ProcessInfo;
import memory.ProcessMemoryReader;

import java.util.List;

public class Main {

	public static void main(String[] args) {
		CoreProcessManager coreProcessManager = new CoreProcessManager();
		List<ProcessInfo> list = coreProcessManager.findProcesses("skyrimse.exe");
		System.out.println(list);
		//..................................................................//

		int pid = (int) list.get(0).getPid();
		WinNT.HANDLE currentProcess = Kernel32.INSTANCE.OpenProcess(Kernel32.PROCESS_ALL_ACCESS | Kernel32.PROCESS_VM_READ | Kernel32.PROCESS_QUERY_INFORMATION,
				false,
				pid);

		ProcessMemoryReader processMemoryReader = new ProcessMemoryReader(pid);
		Pointer baseAddress = processMemoryReader.getBaseAddress(currentProcess);
		System.out.println(baseAddress);
	}
}

