package pacman.game.util;

import com.google.gson.*;

/**
 * Contains functionality for saving and loaing files.
 * Uses the Gson library.
 *
 * @author Sam Robinson
 */
public class Serializer {

    private Gson gson;

    public Serializer() {
        this.gson = new Gson();
    }

    /**
     * Serialize an object
     * @param object
     * @return
     */
    public String serialize(Object object) {
        String json = gson.toJson(object);
        return json;
    }

    /**
     * Deserialize a JSON string into an object
     * @param string
     * @return
     */
    public Object deserialize(String string, Class className) {
        Object object = gson.fromJson(string, className);
        return object;
    }

    /**
     * Save a JSON string to a txt file
     * @param string
     */
    public void saveJSON(String string) {

    }
}
