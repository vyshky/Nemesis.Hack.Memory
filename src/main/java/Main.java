import core.CoreProcessManager;
import core.ProcessInfo;

import java.util.List;
import java.util.Optional;

public class Main {
	public static void main(String[] args) {
		System.out.println("Nemesis");
		CoreProcessManager coreProcessManager = new CoreProcessManager();
		List<ProcessInfo> processes = coreProcessManager.getAllProcesses();
		for (int i = 0; i < processes.size(); ++i) {
			if (processes.get(i).getCommand().toLowerCase().contains("yandex".toLowerCase())) {
				Optional<ProcessHandle> process = coreProcessManager.selectProcess(processes.get(i).getPid());
				coreProcessManager.closeProcess(process.get().pid());
			}
		}
	}
}
