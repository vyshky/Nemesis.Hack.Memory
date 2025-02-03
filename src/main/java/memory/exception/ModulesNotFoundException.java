package memory.exception;

public class ModulesNotFoundException extends RuntimeException {

    public ModulesNotFoundException() {
        super("No one module was received!");
    }
}
