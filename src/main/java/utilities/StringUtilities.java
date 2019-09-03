package utilities;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class StringUtilities {



    public static String join(String separator, String... parts) {
        if(parts.length == 0) {
            return "";
        }
        StringBuilder list = new StringBuilder();
        for(int i = 0; i < parts.length - 1; i++){
            list.append(parts[i]);
            list.append(separator);
        }
        list.append(parts[parts.length - 1]);
        return list.toString();
    }

    public static void forEachPair(String[] options, BiConsumer<String, String> function) {
        if(options.length % 2 != 0) {
            throw new IllegalArgumentException("options is not correct length, must be key-value pairs");
        }
        for(int i = 0; i < options.length; i += 2) {
            String key = options[i];
            String value = options[i + 1];
            function.accept(key, value);
        }
    }

    public static String join(String separator, double... values) {
        String[] strings = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            strings[i] = String.valueOf(values[i]);
        }
        return join(separator, strings);
    }

    public static HashMap<String, String> pairValuesToMap(String... pairValues) {
        HashMap<String, String> map = new HashMap<>();
        forEachPair(pairValues, map::put);
        return map;
    }

    public static boolean equalPairs(String[] a, String[] b) {
        HashMap<String, String> mapA = pairValuesToMap(a);
        HashMap<String, String> mapB = pairValuesToMap(b);
        for(Map.Entry<String, String> entry : mapA.entrySet()) {
            String valueA = entry.getValue();
            String keyA = entry.getKey();
            String valueB = mapB.get(keyA);
            if(valueB == null || !valueA.equals(valueB)) {
                return false;
            }
        }
        return true;
    }
}