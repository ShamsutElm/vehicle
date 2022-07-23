package home.gui.exception;

public class SaveAsToSameFileException extends RuntimeException {

    private static final long serialVersionUID = 2094813878334014114L;

    public SaveAsToSameFileException(String message) {
        super(message);
    }
}
