package core;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.*;

import java.util.*;

public final class CoreProcessManager {
	/**
	 * Получение списка всех запущенных процессов.
	 */
	public List<ProcessInfo> findProcesses() {
		List<ProcessInfo> processes = new ArrayList<>();

		Kernel32.HANDLE snapshot = Kernel32.INSTANCE.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPALL, new Kernel32.DWORD(0L));
		if (snapshot == Kernel32.INVALID_HANDLE_VALUE) {
			return processes;
		}
		Tlhelp32.PROCESSENTRY32 processEntry = new Tlhelp32.PROCESSENTRY32();
		processEntry.dwSize = new WinDef.DWORD(processEntry.size());
		if (Kernel32.INSTANCE.Process32First(snapshot, processEntry)) {
			do {
				processes.add(mapToProcessInfo(processEntry));
			} while (Kernel32.INSTANCE.Process32Next(snapshot, processEntry));
		}
		Kernel32.INSTANCE.CloseHandle(snapshot);
		processes.sort(Comparator.comparingLong(ProcessInfo::getPid));
		return processes;
	}

	/**
	 * Поиск списка всех запущенных процессов по имени.
	 * @param processName имя процесса, который нужно искать
	 */
	public List<ProcessInfo> findProcesses(String processName) {
		List<ProcessInfo> processes = new ArrayList<>();

		Kernel32.HANDLE snapshot = Kernel32.INSTANCE.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPALL, new Kernel32.DWORD(0L));
		if (snapshot == Kernel32.INVALID_HANDLE_VALUE) {
			return processes;
		}
		Tlhelp32.PROCESSENTRY32 processEntry = new Tlhelp32.PROCESSENTRY32();
		processEntry.dwSize = new WinDef.DWORD(processEntry.size());
		if (Kernel32.INSTANCE.Process32First(snapshot, processEntry)) {
			do {
				String currentProcess = Native.toString(processEntry.szExeFile).toLowerCase();
				if (currentProcess.contains(processName.toLowerCase())) {
					processes.add(mapToProcessInfo(processEntry));
				}
			} while (Kernel32.INSTANCE.Process32Next(snapshot, processEntry));
		}
		Kernel32.INSTANCE.CloseHandle(snapshot);
		processes.sort(Comparator.comparingLong(ProcessInfo::getPid));
		return processes;
	}
	/**
	 * Маппинг для класса ProcessInfo.
	 * @param processEntry из этого аргумента вытаскиваются нужные параметры и на основе этих параметров, создается объект ProcessInfo
	 */
	public ProcessInfo mapToProcessInfo(Tlhelp32.PROCESSENTRY32 processEntry) {
		return new ProcessInfo(processEntry.th32ProcessID.longValue(), Native.toString(processEntry.szExeFile), "Unknown", "Unknown");
	}
}