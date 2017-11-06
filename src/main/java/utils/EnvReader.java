package utils;

/**
 * Created by simon on 08/07/17.
 */
public final class EnvReader {
    public static String readEnvVariable(String name, String fallback) {
        String envVar = readEnvVariable(name);
        if (envVar == null || envVar.equals("")) {
            envVar = fallback;
        }
        return envVar;
    }

    public static String readEnvVariable(String name) {
        return System.getenv().get(name);
    }
}
