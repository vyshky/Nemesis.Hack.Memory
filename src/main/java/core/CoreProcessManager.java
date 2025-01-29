package core;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class CoreProcessManager {

	/**
	 * Получение списка всех запущенных процессов.
	 */
	public List<ProcessInfo> getAllProcesses() {
		return ProcessHandle.allProcesses()
				.filter(ProcessHandle::isAlive)
				.sorted(Comparator.comparingLong(ProcessHandle::pid))
				.map(this::mapToProcessInfo)
				.toList();
	}

	/**
	 * Поиск списка всех запущенных процессов по имени.
	 */
	public List<ProcessInfo> getProcesses(String processName) {
		return ProcessHandle.allProcesses()
				.filter(ProcessHandle::isAlive)
				.sorted(Comparator.comparingLong(ProcessHandle::pid))
				.map(this::mapToProcessInfo)
				.filter(x->{
					String command = x.getCommand();
					return command.toLowerCase().contains(processName.toLowerCase());
				})
				.toList();
	}

	/**
	 * Закрыть процесс.
	 * @param pid идентификатор процесса
	 */
	public void closeProcess(long pid)
	{
		ProcessHandle.of(pid)
			.filter(ProcessHandle::isAlive)
					.map(ProcessHandle::destroy)
					.orElse(false);
	}

	/**
	 * Открытие хендла процесса.
	 * Для работы с процессом нужно получить его ProcessHandle.
	 *
	 * @param pid идентификатор процесса
	 * @return ProcessHandle для дальнейшего взаимодействия
	 */
	public Optional<ProcessHandle> openProcess(long pid) {
		Optional<ProcessHandle> processHandle = ProcessHandle.of(pid).filter(ProcessHandle::isAlive);

		if (processHandle.isPresent()) {
			System.out.println("Процесс с PID " + pid + " успешно открыт.");
		} else {
			System.out.println("Не удалось открыть процесс с PID " + pid);
		}
		return processHandle;
	}

	/**
	 * Преобразование ProcessHandle в ProcessInfo для представления данных.
	 */
	private ProcessInfo mapToProcessInfo(ProcessHandle process) {
		ProcessHandle.Info info = process.info();
		return new ProcessInfo(
				process.pid(),
				info.command().orElse("Unknown"),
				info.user().orElse("Unknown"),
				info.startInstant().map(Object::toString).orElse("Unknown")
		);
	}
}