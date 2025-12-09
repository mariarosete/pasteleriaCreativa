package pasteleria_creativa;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JPanel;

/**
 * 
 *  @author María Rosete 
 * 
 * Panel personalizado con bordes redondeados y sombra.
 * 
 * Panel personalizado a partir de componentes CONCRETOS.
 * 
 * Se personalizan las propiedades estándar (como el color de fondo y el borde), 
 * y se utiliza paintComponent() solo para personalizar la parte visual de manera avanzada (dibujando bordes redondeados y sombra). 
 */
public class PanelPersonalizado extends JPanel {

    private Color colorFondo;

    public PanelPersonalizado(Color color) {
        this.colorFondo = color;
        setOpaque(false); // Hacemos el panel transparente para ver el efecto de sombra
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibuja el fondo con borde redondeado
        g.setColor(colorFondo);
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);  // Bordes redondeados

        // Dibuja la sombra
        g.setColor(new Color(0, 0, 0, 50)); // Color de sombra semi-transparente
        g.fillRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 30, 30); // Sombra
    }

    @Override
    public Insets getInsets() {
        return new Insets(10, 10, 10, 10);  // Márgenes internos para el contenido
    }

    // Método para cambiar el color de fondo
    public void setColorFondo(Color colorFondo) {
        this.colorFondo = colorFondo;
        repaint(); // Redibuja el panel con el nuevo color
    }
}
