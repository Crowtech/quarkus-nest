package au.com.crowtech.aible.endpoints.imports;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SheetState {
    private static final Log log = LogFactory.getLog(SheetState.class);

    private SheetState() {
    }

    private static Map<String, XlsxImport> state = new HashMap<>();
    private static Set<String> updateState = new HashSet<>();
 
 
    public static Map<String, XlsxImport> getState() {
        return state;
    }

    public static Set<String> getUpdateState() {
        return updateState;
    }

    public static void setUpdateState(Set<String> updateState) {
        SheetState.updateState = updateState;
    }

    public static void removeUpdateState(String key) {
        SheetState.updateState.remove(key);
    }

 
 
    public static Map<String, Map<String, String>> findDeletedRows(
            Map<String, Map<String, String>> newRows,
            Map<String, Map<String, String>> oldRows) {
        Optional<Map<String, Map<String, String>>> reduce = oldRows.entrySet().stream()
                .filter(o -> !newRows.containsKey(o.getKey())
                )
                .map(data -> {
                    Map<String, Map<String, String>> map = new HashMap<>();
                    map.put(data.getKey(), data.getValue());
                    return map;
                })
                .reduce((acc, n) -> {
                    acc.putAll(n);
                    return acc;
                });
        return reduce.orElseGet(HashMap::new);
    }

    public static Map<String, Map<String, String>> findUpdatedRows(
            Map<String, Map<String, String>> newRows,
            Map<String, Map<String, String>> oldRows) {
        Optional<Map<String, Map<String, String>>> reduce = newRows.entrySet().stream()
                .filter(o -> !oldRows.containsKey(o.getKey())
                        ||
                        !oldRows.containsValue(o.getValue())
                )
                .map(data -> {
                    Map<String, Map<String, String>> map = new HashMap<>();
                    map.put(data.getKey(), data.getValue());
                    return map;
                })
                .reduce((acc, n) -> {
                    acc.putAll(n);
                    return acc;
                });
        return reduce.orElseGet(HashMap::new);
    }
}
