import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Planets {
    public static void main(String[] args) throws IOException {
        new MainWindow();
    }
}

class MainWindow extends JFrame {

    MainPanel panel;

    public MainWindow () throws IOException {
        super("planets");

        panel = new MainPanel();
        panel.addComponentListener(new MainPanelListener());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setPreferredSize(new Dimension(800, 600));

        this.setLocation(100, 60);

        this.pack();

        this.setVisible(true);

        this.add(panel);

        Timer timer = new Timer(100, new PaintPanelListener(panel));
        timer.start();
    }
}

class MainPanel extends JPanel {
    BufferedImage sunImg;
    boolean init;
    double arcLength;
    int xc, yc;
    HashMap<String, double[]> planets;
    HashMap<String, BufferedImage> images;

    public MainPanel() {
        super();

        init = false;

        try {
            images = new HashMap<String, BufferedImage>();
            images.put("merc", ImageIO.read(new File("merc.png")));
            images.put("venus", ImageIO.read(new File("venus.png")));
            images.put("earth", ImageIO.read(new File("earth.png")));
            images.put("mars", ImageIO.read(new File("mars.png")));
            images.put("jupiter", ImageIO.read(new File("jupiter.png")));
            images.put("saturn", ImageIO.read(new File("saturn.png")));
            images.put("uranus", ImageIO.read(new File("uranus.png")));
            images.put("neptune", ImageIO.read(new File("neptune.png")));
            sunImg = ImageIO.read(new File("sun.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        planets = new HashMap<String, double[]>();
        planets.put("merc", new double[]{100, 0});
        planets.put("venus", new double[]{150, 0});
        planets.put("earth", new double[]{200, 0});
        planets.put("mars", new double[]{250, 0});
        planets.put("jupiter", new double[]{300, 0});
        planets.put("saturn", new double[]{350, 0});
        planets.put("uranus", new double[]{400.0, 0.0});
        planets.put("neptune", new double[]{450.0, 0.0});
    }

    public void initDimensions() {
        arcLength = 100 * 2 * Math.PI / 24;
        xc = getWidth() / 2;
        yc = getHeight() / 2;
        init = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!init) {
            initDimensions();
        }

        buildPicture(g);
    }

    public void repaintImage() {
        repaint();
    }

    protected void drawPlanet(Graphics g, int x, int y, BufferedImage img) {
        g.drawImage(img, x - img.getWidth() / 2, y - img.getHeight() / 2, this);
    }

    protected void buildPicture(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.drawImage(sunImg, xc - sunImg.getWidth() / 2, yc - sunImg.getHeight() / 2, this);

        int x, y;
        double[] params;

        for (String key : planets.keySet()) {
            params = planets.get(key);
            params[1] += arcLength * 360.0 / (params[0] * 2.0 * Math.PI);
            x = (int) (xc + params[0] * Math.cos(params[1] * Math.PI / 180));
            y = (int) (yc + params[0] * Math.sin(params[1] * Math.PI / 180));
            drawPlanet(g, x, y, images.get(key));
        }
    }
}

class PaintPanelListener implements ActionListener {
    JPanel panel;

    public PaintPanelListener(JPanel panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ((MainPanel) panel).repaintImage();
    }
}

class MainPanelListener extends ComponentAdapter {
    @Override
    public void componentResized(ComponentEvent e) {
        super.componentResized(e);
        ((MainPanel) e.getComponent()).init = false;
    }
}
