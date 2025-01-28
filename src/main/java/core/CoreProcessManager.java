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
	 * Выбор процесса для работы по его PID.
	 *
	 * @param pid идентификатор процесса
	 * @return ProcessHandle процесса (если найден), иначе Optional.empty()
	 */
	public Optional<ProcessHandle> selectProcess(long pid) {
		return ProcessHandle.of(pid).filter(ProcessHandle::isAlive);
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
	 * Закрытие хендла процесса. Этот метод завершает процесс.
	 *
	 * @param pid идентификатор процесса
	 * @return true, если процесс успешно завершен, иначе false
	 */
	public boolean closeProcess(long pid) {
		Optional<ProcessHandle> processHandle = ProcessHandle.of(pid).filter(ProcessHandle::isAlive);

		if (processHandle.isPresent()) {
			boolean result = processHandle.get().destroy(); // Попытка завершить процесс
			if (result) {
				System.out.println("Процесс с PID " + pid + " успешно завершен.");
			} else {
				System.out.println("Не удалось завершить процесс с PID " + pid);
			}
			return result;
		}
		System.out.println("Процесс с PID " + pid + " не найден.");
		return false;
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