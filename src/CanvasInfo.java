
import java.io.Serializable;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;


/**
 * The CanvasInfo class represents the actual drawing that is displayed on the canvas.
 * It contains the layers of the canvas, the current layer, and the current stroke.
 */
public class CanvasInfo implements Serializable {
    // The current stroke which is a list of points
    private List<Point> currentStroke;
    // The current layer which is a list of strokes
    private List<List<Point>> currentLayer;
    // The layers of the canvas which is a list of a list layers
    private List<List<List<Point>>> layers;

    /**
     * Constructor for the CanvasInfo class.
     */
    public CanvasInfo() {
        layers = new ArrayList<>();
        createNewLayer();
    }

    /**
     * Create a new layer in the canvas.
     */
    public void createNewLayer() {
        currentLayer = new ArrayList<>();
        layers.add(currentLayer);
        currentStroke = new ArrayList<Point>();
        currentLayer.add(currentStroke);
    }

    /**
     * Add a point to the current stroke.
     * 
     * @param p the point to be added
     */
    public void addToStroke(Point p) {
        if (currentStroke.isEmpty()) {
            createEmptyStroke();
        }
        currentStroke.add(p);
    }

    /**
     * Create an empty stroke and add it to the current layer.
     */
    public void createEmptyStroke() {
        if (currentStroke.isEmpty()) {
            return;
        }
        currentStroke = new ArrayList<Point>();
        currentLayer.add(currentStroke);
    }

    /**
     * Add a point to the current stroke.
     * 
     * @param p the point to be added
     */
    public void addPointToStroke(Point p) {
        currentStroke.add(p);
    }

    /**
     * Get the layers of the canvas.
     * 
     * @return the layers of the canvas
     */
    public List<List<List<Point>>> getLayers() {
        return layers;
    }

    /**
     * This allows you to undo the last stroke drawn on the canvas.
     * It will not undo the last stroke if the current layer is empty.
     */
    public void undoLastStroke() {
        if (currentLayer.isEmpty()) {
            return;
        }
        currentLayer.removeLast();
        if (currentLayer.isEmpty()) {
            return;
        }
        currentLayer.removeLast();
        currentStroke = new ArrayList<Point>();
        currentLayer.add(currentStroke);
    }

    /**
     * Get the string representation of the canvas for debugging purposes.
     * 
     * @return the string representation of the canvas
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        currentStroke.stream().forEach(sb::append);
        return sb.toString();
    }

    /**
     * Print the canvas to the console for debugging purposes.
     */
    public void printCanvas() {
        System.out.println(this.toString());
    }

}
