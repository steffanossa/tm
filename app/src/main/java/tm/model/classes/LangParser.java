package tm.model.classes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LangParser {
    private JsonObject jsonObject;

    public LangParser() {
        jsonObject = getConfigObject();
    }

    private JsonObject getConfigObject() {
        try {
            System.out.println(System.getProperty("user.dir"));
            Gson gson = new Gson();
            InputStream inputStream = new FileInputStream("app/src/main/resources/de.json");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            // JsonObject langObject = gson.fromJson(new FileReader("src/main/resources/de.json"), JsonObject.class);
            JsonObject langObject = gson.fromJson(inputStreamReader, JsonObject.class);
            return langObject;
        } catch (Exception e) { return null; }
    }


    public String get(String category, String key) {
        return jsonObject.get(category).getAsJsonObject().get(key).getAsString();
    }
}
