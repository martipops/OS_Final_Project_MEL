package client.ui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import client.Client;
import shared.CanvasInfo;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * The DrawingApp class represents the main window of the drawing application.
 * It contains the drawing panel and the controls for the application.
 * 
 * The drawing panel subclass is where the user can draw on the canvas.
 */
public class DrawingApp extends JFrame {
    private static final int CANVAS_WIDTH = 600;
    private static final int CANVAS_HEIGHT = 400;
    private static final int CONTROL_PANEL_HEIGHT = 50;
    private static final Color DEFAULT_DRAW_COLOR = Color.BLACK;
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = Color.RED;
    private static final int BORDER_THICKNESS = 2;
    private static final int CANVAS_PADDING = 20;

    private DrawPanel drawPanel;
    private Color drawColor = DEFAULT_DRAW_COLOR;
    private CanvasInfo canvas;

    /**
     * Constructor for the DrawingApp class.
     * Sets up the drawing panel and the controls for the GUI.
     */
    public DrawingApp() {
        canvas = new CanvasInfo();
        drawPanel = new DrawPanel();

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> Client.submit());;
        JButton undoButton = new JButton("< Undo");
        undoButton.addActionListener(e -> drawPanel.undoLastStroke());

        JPanel controls = new JPanel();
        controls.add(submitButton);
        controls.add(undoButton);

        add(controls, BorderLayout.NORTH);
        add(drawPanel, BorderLayout.CENTER);

        setResizable(false);
        setSize(CANVAS_WIDTH, CANVAS_HEIGHT + CONTROL_PANEL_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(false);
    }

    /**
     * Set the canvas for the drawing application.
     * 
     * @param canvas the canvas to be set
     */
    public void setCanvas(CanvasInfo canvas) {
        this.canvas = canvas;
        this.canvas.createNewLayer();
        drawPanel.repaint();
    }

    /**
     * Get the canvas for the drawing application.
     * 
     * @return the canvas
     */
    public CanvasInfo getCanvas() {
        return canvas;
    }

    /**
     * The DrawPanel subclass represents the drawing panel of the application.
     * It is where the user can draw on the canvas.
     */
    private class DrawPanel extends JPanel {

        /**
         * Constructor for the DrawPanel subclass.
         * Sets some default values about the drawing panel.
         * Also hooks up mouse listeners to handle drawing.
         */
        public DrawPanel() {
            setBackground(BACKGROUND_COLOR);
            setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
            setBorder(createBorder());

            canvas.createEmptyStroke();
            setupMouseListeners();
        }

        /**
         * Create a compound border for the drawing panel.
         * 
         * @return the compound border
         */
        private CompoundBorder createBorder() {
            return new CompoundBorder(
                new EmptyBorder(CANVAS_PADDING, CANVAS_PADDING, CANVAS_PADDING, CANVAS_PADDING),
                BorderFactory.createMatteBorder(BORDER_THICKNESS, BORDER_THICKNESS, BORDER_THICKNESS, BORDER_THICKNESS, BORDER_COLOR)
            );
        }

        /**
         * Set up the mouse listeners for the drawing panel.
         */
        private void setupMouseListeners() {

            // Add a mouse listener to handle mouse click and release events
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (isInCanvasBoundary(e.getPoint())) {
                        canvas.addToStroke(e.getPoint());
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    canvas.createEmptyStroke();
                }
            });

            // Add a mouse motion listener to handle mouse drag events
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    Point currentPoint = e.getPoint();
                    if (!isInCanvasBoundary(currentPoint)) {
                        canvas.createEmptyStroke();
                        return;
                    }
                    canvas.addPointToStroke(currentPoint);
                    repaint();
                }
            });
        }

        /**
         * A helper method to check if the given point is within the canvas boundary.
         * 
         * @param p the point to be checked
         * @return true if the point is within the canvas boundary, false otherwise
         */
        private boolean isInCanvasBoundary(Point p) {
            Rectangle r = getVisibleRect();
            r.grow(-CANVAS_PADDING, -CANVAS_PADDING);
            return r.contains(p);
        }

        /**
         * Undo the last stroke drawn on the canvas.
         */
        public void undoLastStroke() {
            canvas.undoLastStroke();
            repaint();
        }

        /**
         * Paint the component based on the current CanvasInfo object.
         * 
         * @param g the graphics object
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(drawColor);
            g2d.setStroke(new BasicStroke(3));

            for (List<List<Point>> layer : canvas.getLayers()) {
                for (List<Point> points : layer) {
                    for (int i = 1; i < points.size(); i++) {
                        Point startPoint = points.get(i - 1);
                        Point endPoint = points.get(i);
                        g2d.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
                    }
                }
            }
        }
    }
}