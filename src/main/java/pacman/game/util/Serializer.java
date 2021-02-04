package pacman.game.util;

import com.google.gson.*;
import pacman.controllers.examples.po.NN.*;

import java.util.ArrayList;

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

        // Handle NeuralNetwork classes carefully, since GSON doesn't know what Layer subclass to use
        if (className.getName() == "pacman.controllers.examples.po.NN.NeuralNetwork") {
            // Copy the layers in the new object
            ArrayList<Layer> layers = (ArrayList<Layer>) ((NeuralNetwork) object).getLayers();
            ArrayList<Layer> newLayers = new ArrayList<>();

            // Iterate through the layers
            for (int i = 0; i < layers.size(); i++) {
                // Get the i'th layer
                Layer layer = layers.get(i);

                // If the layer is the first layer...
                if (i == 0) {
                    // Then this layer should be an InputLayer
                    InputLayer inputLayer = new InputLayer(layer.getNeurons(), layer.getInputs());
                    newLayers.add(inputLayer);
                }
                // If the layer is the last layer (i.e. an OutputLayer)...
                else if (i == layers.size() - 1) {
                    OutputLayer outputLayer = new OutputLayer(layer.getNeurons(), layer.getInputs());
                    newLayers.add(outputLayer);
                }
                // Otherwise this layer is a HiddenLayer
                else {
                    HiddenLayer hiddenLayer = new HiddenLayer(layer.getNeurons(), layer.getInputs());
                    newLayers.add(hiddenLayer);
                }
            }

            // Create a new NeuralNetwork using the new layers
            object = new NeuralNetwork(newLayers);
        }

        return object;
    }

    /**
     * Save a JSON string to a txt file
     * @param string
     */
    public void saveJSON(String string) {

    }
}
