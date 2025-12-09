package pasteleria_creativa;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author María Rosete
 * 
 * Botón personalizado a partir de componentes GENÉRICOS que heredan de JComponent
 * 
 * Botón personalizado con forma de corazón.
 * 
 * - Hereda de JComponent.
 * - Soporta ActionListeners.
 * - Implementa un gradiente dinámico, texto con sombra, y efectos visuales.
 */
public class BotonPersonalizado extends JComponent {
    
    private Dimension dimensiones = new Dimension(120, 120); // Ajusta altura para incluir texto
    private String texto;
    private boolean ratonDentro;
    private List<ActionListener> actionListeners = new ArrayList<>();

    public BotonPersonalizado(String texto) {
        this.texto = texto;

        // Agregar MouseListener para la interacción
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fireActionPerformed(); // Disparar evento ActionListener
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                ratonDentro = true;
                repaint(); // Redibujar cuando el ratón entra
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ratonDentro = false;
                repaint(); // Redibujar cuando el ratón sale
            }
        });
    }

    // Métodos de tamaño preferido, máximo y mínimo
    @Override
    public Dimension getPreferredSize() {
        return dimensiones;
    }

    @Override
    public Dimension getMaximumSize() {
        return dimensiones;
    }

    @Override
    public Dimension getMinimumSize() {
        return dimensiones;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Crear forma de corazón
        Path2D heart = new Path2D.Double();
        heart.moveTo(w / 2.0, h * 0.65); 
        heart.curveTo(w * 0.1, h * 0.35, w * 0.1, h * 0.05, w / 2.0, h * 0.25);
        heart.curveTo(w * 0.9, h * 0.05, w * 0.9, h * 0.35, w / 2.0, h * 0.65);
        heart.closePath();

        // Fondo con gradiente dinámico
        if (ratonDentro) {
            g2d.setPaint(new GradientPaint(0, 0, new Color(255, 102, 178), w, h, new Color(255, 51, 102), true));
        } else {
            g2d.setPaint(new GradientPaint(0, 0, new Color(255, 182, 193), w, h, new Color(255, 105, 180), true));
        }
        g2d.fill(heart);

        // Borde del corazón
        g2d.setColor(new Color(139, 0, 139)); // Púrpura oscuro
        g2d.setStroke(new BasicStroke(3));
        g2d.draw(heart);

        // Efecto de brillo
        g2d.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 150), 0, h / 2, new Color(0, 0, 0, 0), true));
        g2d.fill(heart);

        // Dibujar texto con sombra
        Font font = new Font("Comic Sans MS", Font.BOLD, 16);
        g2d.setFont(font);
        
        FontMetrics metrics = g2d.getFontMetrics();
        int x = (w - metrics.stringWidth(texto)) / 2;
        int y = (int) (h * 0.95); // Posiciona el texto justo debajo del corazón

        // Sombra del texto
        g2d.setColor(Color.WHITE);
        g2d.drawString(texto, x + 1, y + 1);

        // Texto principal
        g2d.setColor(new Color(50, 50, 50)); // Gris oscuro
        g2d.drawString(texto, x, y);
    }

    // Agregar ActionListener
    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    // Disparar evento ActionListener
    private void fireActionPerformed() {
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, texto);
        for (ActionListener listener : actionListeners) {
            listener.actionPerformed(event);
        }
    }
}