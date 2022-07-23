package home.gui.exception;

import java.io.Serializable;

public final class SaveAsCancelException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 8576050749715932334L;

    public SaveAsCancelException(String message) {
        super(message);
    }
}
