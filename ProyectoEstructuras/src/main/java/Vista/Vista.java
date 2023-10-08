/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * @author Dell
 */
public class Vista {

    private JFrame vent;
    private PanelBotones panelSimon;
    private JLabel labelEstado;
    private JLabel labelNivelActual;
    private JLabel labelTiempoRestante;
    private JLabel labelPuntuacion;
    private JPanel upPanel;
    private ArrayList<Arco> arcos;
    private static Vista.Arco arcoPosado = null;
    private static Vista.Arco arcoPresionado = null;
    private static String mensaje = null;

    public static Arco getArcoPosado() {
        return arcoPosado;
    }

    public static void setArcoPosado(Arco arcoPosado) {
        Vista.arcoPosado = arcoPosado;
    }

    public static Arco getArcoPresionado() {
        return arcoPresionado;
    }

    public static void setArcoPresionado(Arco arcoPresionado) {
        Vista.arcoPresionado = arcoPresionado;
    }

    //Constructor de la clase vista con todos los elementos necesarios
    public Vista() {
        vent = new JFrame("Simon");
        vent.setSize(500, 600);
        vent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vent.setLayout(new BorderLayout());
        vent.setLocationRelativeTo(null);

        panelSimon = new PanelBotones();
        vent.add(panelSimon, BorderLayout.CENTER);

        labelEstado = new JLabel("Mira la secuencia", SwingConstants.CENTER);
        vent.add(labelEstado, BorderLayout.SOUTH);

        upPanel = new JPanel(new GridLayout(3, 1));
        labelNivelActual = new JLabel("Nivel 1", SwingConstants.CENTER);
        labelNivelActual.setFont(new Font("Arial", Font.BOLD, 24));
        upPanel.add(labelNivelActual);

        labelTiempoRestante = new JLabel("TIEMPO: 0", SwingConstants.CENTER);
        labelTiempoRestante.setFont(new Font("Arial", Font.PLAIN, 20));
        upPanel.add(labelTiempoRestante);

        labelPuntuacion = new JLabel("Puntuación: 0", SwingConstants.CENTER);
        labelPuntuacion.setFont(new Font("Arial", Font.PLAIN, 20));
        upPanel.add(labelPuntuacion);

        vent.add(upPanel, BorderLayout.NORTH);

        arcos = new ArrayList<>();

        arcos.add(new Arco(Color.RED, 0));
        arcos.add(new Arco(Color.BLUE, 90));
        arcos.add(new Arco(Color.GREEN, 180));
        arcos.add(new Arco(Color.YELLOW, 270));
        vent.setVisible(true);
    }

    public class PanelBotones extends JPanel {

        public PanelBotones() {
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            int centroX = getWidth() / 2;
            int centroY = getHeight() / 2;
            int radio = 200;

            // Dibuja el círculo grande blanco
            g2d.setColor(Color.WHITE);
            g2d.fillOval(centroX - radio, centroY - radio, 2 * radio, 2 * radio);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(centroX - radio, centroY - radio, 2 * radio, 2 * radio);

            // Dibuja los arcos de colores
            for (Arco arco : arcos) {
                if (arco == arcoPresionado) {
                    g2d.setColor(Color.BLACK);
                } else if (arco == arcoPosado) {
                    g2d.setColor(arco.getColor().darker());
                } else {
                    g2d.setColor(arco.getColorActual());
                }
                g2d.fill(arco.getForma(centroX, centroY, radio));
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(10));
                g2d.draw(arco.getForma(centroX, centroY, radio));
            }

            // Dibuja el círculo gris en el centro
            int radioCentral = 50; // Puedes ajustar este valor según prefieras
            g2d.setColor(Color.GRAY);
            g2d.fillOval(centroX - radioCentral, centroY - radioCentral, 2 * radioCentral, 2 * radioCentral);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(centroX - radioCentral, centroY - radioCentral, 2 * radioCentral, 2 * radioCentral);

            // Dibuja el mensaje en el centro si está presente
            if (mensaje != null) {
                g2d.setColor(Color.RED);
                g2d.setFont(new Font("Arial", Font.BOLD, 30));
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(mensaje)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(mensaje, x, y);
            }
        }
    }

    public class Arco {

        private Color color;
        private int anguloInicio;
        private boolean estaIluminado;

        public Arco(Color color, int anguloInicio) {
            this.color = color;
            this.anguloInicio = anguloInicio;
            this.estaIluminado = false;
        }

        public Color getColor() {
            return color;
        }

        public Color getColorActual() {
            if (estaIluminado) {
                return Color.BLACK;
            }
            return color;
        }

        public Shape getForma(int x, int y, int r) {
            return new Arc2D.Double(x - r, y - r, 2 * r, 2 * r, anguloInicio, 90, Arc2D.PIE);
        }

        public void iluminar() {
            estaIluminado = true;
        }

        public void detenerIluminacion() {
            estaIluminado = false;
        }

        public boolean contiene(int x, int y) {
            return getForma(vent.getWidth() / 2, vent.getHeight() / 2, 200).contains(x, y);
        }
    }

    public JFrame getVent() {
        return vent;
    }

    public void setVent(JFrame vent) {
        this.vent = vent;
    }

    public PanelBotones getPanelSimon() {
        return panelSimon;
    }

    public void setPanelSimon(PanelBotones panelSimon) {
        this.panelSimon = panelSimon;
    }

    public JLabel getLabelEstado() {
        return labelEstado;
    }

    public void setLabelEstado(JLabel labelEstado) {
        this.labelEstado = labelEstado;
    }

    public JLabel getLabelNivelActual() {
        return labelNivelActual;
    }

    public void setLabelNivelActual(JLabel labelNivelActual) {
        this.labelNivelActual = labelNivelActual;
    }

    public JLabel getLabelTiempoRestante() {
        return labelTiempoRestante;
    }

    public void setLabelTiempoRestante(JLabel labelTiempoRestante) {
        this.labelTiempoRestante = labelTiempoRestante;
    }

    public JLabel getLabelPuntuacion() {
        return labelPuntuacion;
    }

    public void setLabelPuntuacion(JLabel labelPuntuacion) {
        this.labelPuntuacion = labelPuntuacion;
    }

    public JPanel getUpPanel() {
        return upPanel;
    }

    public void setUpPanel(JPanel upPanel) {
        this.upPanel = upPanel;
    }

    public ArrayList<Arco> getArcos() {
        return arcos;
    }

    public void setArcos(ArrayList<Arco> arcos) {
        this.arcos = arcos;
    }

    public static String getMensaje() {
        return mensaje;
    }

    public static void setMensaje(String mensaje) {
        Vista.mensaje = mensaje;
    }

}
