package android.flickrsearch.utils;

import org.json.JSONObject;

public class JsonParser {

    public static String getString(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getString(key);
        } catch (Exception e) {
            return "";
        }
    }
}
