import core.CoreProcessManager;
import core.ProcessInfo;

import java.util.List;

public class Main {
	public static void main(String[] args) {
		CoreProcessManager coreProcessManager = new CoreProcessManager();
		List<ProcessInfo> processesYandex = coreProcessManager.getProcesses("yandex");
		for (ProcessInfo pi: processesYandex)
		{
			long pid = pi.getPid();
			coreProcessManager.closeProcess(pid);
		}
	}
}

