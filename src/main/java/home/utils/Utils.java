package home.utils;

import java.awt.Component;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.slf4j.Logger;

public class Utils {

    public static String idsToString(Long[] ids) {
        var sb = new StringBuilder("(");
        for (Long id : ids) {
            sb.append(id).append(' ');
        }
        sb.setLength(sb.length() - 1);
        sb.append(')');
        return sb.toString();
    }

    public static <T> void logAndShowError(Logger log, Component parent, String msg,
            String title, Exception e) {
        log.error("Exception: ", e);
        JOptionPane.showMessageDialog(parent, msg + "\n\nDescription.\n" + e.getMessage(),
                title, JOptionPane.ERROR_MESSAGE);
    }

    public static IOException getNewException(Exception e, String msg) {
        var ex = new IOException(msg);
        ex.addSuppressed(e);
        return ex;
    }

    private Utils() {
    }
}
