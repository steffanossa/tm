package tm.model.classes;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class DynamicClass {
    private Map<String, String> attributes = new HashMap<>();
    private Map<String, Callable<Object>> callables = new HashMap<>();
    

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public void setAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public Object call(String key) {
        Callable<Object> callable = callables.get(key);
        if (callable != null) {
            try { return callable.call(); }
            catch (Exception e) {}
        }
        return null;
    }

    public void define(String key, Callable<Object> callable) {
        callables.put(key, callable);
    }
}