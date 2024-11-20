package client.ui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import client.ChatClient;
import shared.CanvasInfo;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DrawingApp extends JFrame {
    private DrawPanel drawPanel;
    private Color drawColor = Color.BLACK;
    private CanvasInfo canvas;
    private int canvasWidth = 600;
    private int canvasHeight = 400;


    public DrawingApp() {
        canvas = new CanvasInfo();
        drawPanel = new DrawPanel();

        JButton newLayerButton = new JButton("Submit");
        JButton undoButton = new JButton("< Undo");

        undoButton.addActionListener(e -> drawPanel.undoLastStroke());
        newLayerButton.addActionListener(e -> {
            synchronized (ChatClient.class) {
                ChatClient.submit();
            }
        });

        JPanel controls = new JPanel();
        controls.add(newLayerButton);
        controls.add(undoButton);

        add(controls, BorderLayout.NORTH);
        add(drawPanel, BorderLayout.CENTER);

        setResizable(false);
        setSize(canvasWidth, canvasHeight + 50);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void setCanvas(CanvasInfo canvas) {
        this.canvas = canvas;
        this.canvas.createNewLayer();
        drawPanel.repaint();
    }

    public CanvasInfo getCanvas() {
        return canvas;
    }

    private class DrawPanel extends JPanel {

        private static final int CANVAS_PADDING = 20;

        public DrawPanel() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(400, 400));
            
            // Goofy drawpanel border
            setBorder(new CompoundBorder(
                    new EmptyBorder(CANVAS_PADDING, CANVAS_PADDING, CANVAS_PADDING, CANVAS_PADDING),
                    BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED)));
            canvas.createEmptyStroke();

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

        private boolean isInCanvasBoundary(Point p) {
            Rectangle r = getVisibleRect();
            r.grow(-CANVAS_PADDING, -CANVAS_PADDING);
            return r.contains(p);
        }

        public void undoLastStroke() {
            canvas.undoLastStroke();
            repaint();
        }

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
