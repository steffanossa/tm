package tm.model.dtos;

import java.util.Map;
import java.util.HashMap;

/**
 * übertreib nciht
 */
public class GenericDTO {
    private Map<String, Object> properties;

    public GenericDTO() {
        this.properties = new HashMap<>();
    }

    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

}
