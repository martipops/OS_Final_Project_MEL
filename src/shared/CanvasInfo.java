package shared;
import java.io.Serializable;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class CanvasInfo implements Serializable {
    private List<List<List<Point>>> layers;
    private List<List<Point>> currentLayer;
    private List<Point> currentStroke;

    public CanvasInfo() {
        layers = new ArrayList<>();
        createNewLayer();
    }

    public void createNewLayer() {
        currentLayer = new ArrayList<>();
        layers.add(currentLayer);
        currentStroke = new ArrayList<Point>();
        currentLayer.add(currentStroke);
    }

    public void addToStroke(Point p) {
        if (currentStroke.isEmpty()) {
            createEmptyStroke();
        }
        currentStroke.add(p);
    }

    public void createEmptyStroke() {
        if (currentStroke.isEmpty()) {
            return;
        }
        currentStroke = new ArrayList<Point>();
        currentLayer.add(currentStroke);
    }

    public void addPointToStroke(Point p) {
        currentStroke.add(p);
    }

    public List<List<List<Point>>> getLayers() {
        return layers;
    }

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        currentStroke.stream().forEach(sb::append);
        return sb.toString();
    }

    public void printCanvas() {
        System.out.println(this.toString());
    }

}
