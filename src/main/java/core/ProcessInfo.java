package core;

public class ProcessInfo {
	private final long pid;
	private final String command;
	private final String user;
	private final String startTime;

	public ProcessInfo(long pid, String command, String user, String startTime) {
		this.pid = pid;
		this.command = command;
		this.user = user;
		this.startTime = startTime;
	}

	public long getPid() {
		return pid;
	}

	public String getCommand() {
		return command;
	}

	public String getUser() {
		return user;
	}

	public String getStartTime() {
		return startTime;
	}

	@Override
	public String toString() {
		return String.format("PID: %d | Command: %s | User: %s | Start Time: %s\n", pid, command, user, startTime);
	}
}
