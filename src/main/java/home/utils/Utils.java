package home.utils;

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

    private Utils() {
    }
}
