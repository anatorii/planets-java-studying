import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Planets {
    static double arcLength;
    static HashMap<String, String[]> planets;
    static int width = 1000;
    static int height = 1000;
    public static void main(String[] args) throws IOException {
        arcLength = 50 * 2 * Math.PI / 24;

        planets = new HashMap<String, String[]>();
        planets.put("merc", new String[]{"140"});
        planets.put("venus", new String[]{"190"});
        planets.put("earth", new String[]{"240"});
        planets.put("mars", new String[]{"290"});
        planets.put("jupiter", new String[]{"340"});
        planets.put("saturn", new String[]{"390"});
        planets.put("uranus", new String[]{"440"});
        planets.put("neptune", new String[]{"490"});

        new MainWindow();
    }
}

class MainWindow extends JFrame {
    public MainWindow () throws IOException {
        super("planets");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setPreferredSize(new Dimension(Planets.width, Planets.height));
        this.setLocation((d.width - Planets.width) / 2, (d.height - Planets.height) / 2);
        this.getContentPane().setLayout(null);
        this.getContentPane().setBackground(Color.black);

        this.pack();

        this.setVisible(true);

        // adding the sun label
        PlanetLabel planet = new PlanetLabel(this, ImageIO.read(new File("./planets/sun.png")), 0);
        planet.setPosition(getWidth() / 2, getHeight() / 2);

        for (String key : Planets.planets.keySet()) {
            (new OrbitPlanet(
                    new PlanetLabel(this,
                            ImageIO.read(new File("./planets/" + key + ".png")),
                            Integer.parseInt(Planets.planets.get(key)[0])
                    )
            )).start();
        }
    }
}

class OrbitPlanet extends Thread {
    double angle = 0;
    PlanetLabel planet;
    public OrbitPlanet(PlanetLabel planet) {
        this.planet = planet;
    }

    @Override
    public void run() {
        super.run();

        for (;;) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            angle += Planets.arcLength * 360.0 / (planet.radius * 2.0 * Math.PI);
            int xc = planet.frame.getWidth() / 2;
            int yc = planet.frame.getHeight() / 2;
            int x = (int) (xc + planet.radius * Math.cos(angle * Math.PI / 180));
            int y = (int) (yc + planet.radius * Math.sin(angle * Math.PI / 180));
            planet.setPosition(x, y);
        }
    }
}

class PlanetLabel extends JLabel {
    JFrame frame;
    BufferedImage image;
    int radius;

    public PlanetLabel(JFrame frame, BufferedImage image, int radius) {
        super();
        this.radius = radius;
        this.image = image;
        this.frame = frame;
        this.setIcon(new ImageIcon(this.image));
        frame.add(this);
    }

    public void setPosition(int x, int y) {
        setBounds(x - image.getWidth() / 2, y - image.getHeight() / 2, image.getWidth(), image.getHeight());
    }
}
