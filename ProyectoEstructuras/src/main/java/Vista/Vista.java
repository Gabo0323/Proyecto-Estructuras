/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
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
 *
 * @author Dell
 */
public class Vista {

    private JFrame ventana;
    private PanelSimon panelSimon;
    private JLabel etiquetaEstado;
    private JLabel etiquetaNivel;
    private JLabel etiquetaTiempo;
    private JLabel etiquetaPuntuacion;
    private JPanel panelSuperior;
    private ArrayList<ArcoColor> arcos;
    private static Vista.ArcoColor arcoHovered = null;
    private static Vista.ArcoColor arcoPressed = null;

    public static ArcoColor getArcoHovered() {
        return arcoHovered;
    }

    public static void setArcoHovered(ArcoColor arcoHovered) {
        Vista.arcoHovered = arcoHovered;
    }

    public static ArcoColor getArcoPressed() {
        return arcoPressed;
    }

    public static void setArcoPressed(ArcoColor arcoPressed) {
        Vista.arcoPressed = arcoPressed;
    }

    public Vista() {
        System.out.println("Constructor");
        ventana = new JFrame("Juego Simon");
        ventana.setSize(500, 600);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(new BorderLayout());
        ventana.setLocationRelativeTo(null);

        panelSimon = new PanelSimon();
        ventana.add(panelSimon, BorderLayout.CENTER);

        etiquetaEstado = new JLabel("Mira la secuencia", SwingConstants.CENTER);
        ventana.add(etiquetaEstado, BorderLayout.SOUTH);

        panelSuperior = new JPanel(new GridLayout(3, 1));
        etiquetaNivel = new JLabel("Nivel 1", SwingConstants.CENTER);
        etiquetaNivel.setFont(new Font("Arial", Font.BOLD, 24));
        panelSuperior.add(etiquetaNivel);

        etiquetaTiempo = new JLabel("Tiempo restante: 00:00", SwingConstants.CENTER);
        etiquetaTiempo.setFont(new Font("Arial", Font.PLAIN, 20));
        panelSuperior.add(etiquetaTiempo);

        etiquetaPuntuacion = new JLabel("Puntuación: 0", SwingConstants.CENTER);
        etiquetaPuntuacion.setFont(new Font("Arial", Font.PLAIN, 20));
        panelSuperior.add(etiquetaPuntuacion);

        ventana.add(panelSuperior, BorderLayout.NORTH);

        arcos = new ArrayList<>();

        arcos.add(new ArcoColor(Color.RED, 0));
        arcos.add(new ArcoColor(Color.BLUE, 90));
        arcos.add(new ArcoColor(Color.GREEN, 180));
        arcos.add(new ArcoColor(Color.YELLOW, 270));
        ventana.setVisible(true);
    }

    public class PanelSimon extends JPanel {

        
//        private String mensaje = null;
//        private Timer temporizadorMensaje;

        public PanelSimon() {//Falta poner esto en el controlador, pero por hoy descanzo


//                            public void mousePressed(MouseEvent e) {
//
//                                String archivoAudio = "yes.wav";
//                                String archivoAudioNo = "no.wav";
//
//                                if (enCircunferencia(e) && score <= listSecuencia.size() && !isIlluminationTimerRunning && recordInputs) {
//                                    int sector = getSector(anguloRad);
//                                    input = sector;
//
//                                    if (!mainControl.compararInput(input)) {
//
//                                        try {
//                                            File sonido = new File("no.wav");
//                                            AudioInputStream audioInputStream
//                                                    = AudioSystem.getAudioInputStream(sonido);
//                                            try (Clip clip = AudioSystem.getClip()) {
//                                                clip.open(audioInputStream);
//
//                                                clip.start();
//                                                Thread.sleep(clip.getMicrosecondLength() / 1_000);
//
//                                                JOptionPane.showMessageDialog(null, "¡Perdiste!", "Mensaje",
//                                                        JOptionPane.ERROR_MESSAGE);
//                                            }
//                                        } catch (IOException | InterruptedException | LineUnavailableException | UnsupportedAudioFileException ex) {
//                                            System.err.printf("Excepción al reproducir audio: '%s'%n", ex.getMessage());
//                                            ex.printStackTrace(); // Imprime la traza de excepción para obtener más detalles.
//
//                                        }
//
//                                        for (int i = 0; i < COLORS.length; i++) {
//                                            iluminarSector(i);
//                                        }
//
//                                        illuminationTimer.stop();
//                                        recordInputs = false;
//                                        System.out.println("======");
//
//                                    } else {
//                                        try {
//                                            File sonido = new File("yes.wav");
//                                            AudioInputStream audioInputStream
//                                                    = AudioSystem.getAudioInputStream(sonido);
//                                            try (Clip clip = AudioSystem.getClip()) {
//                                                clip.open(audioInputStream);
//
//                                                clip.start();
//                                                Thread.sleep(clip.getMicrosecondLength() / 1_000);
//                                            }
//                                        } catch (IOException | InterruptedException | LineUnavailableException | UnsupportedAudioFileException ex) {
//                                            System.err.printf("Excepción al reproducir audio: '%s'%n", ex.getMessage());
//                                            ex.printStackTrace(); // Imprime la traza de excepción para obtener más detalles.
//
//                                        }
//
//                                        System.out.println("OK");
//                                    }
//                                }
//
//                            }
//                        }
//
//                    );

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
            for (ArcoColor arco : arcos) {
                if (arco == arcoPressed) {
                    g2d.setColor(Color.BLACK);
                } else if (arco == arcoHovered) {
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
            int radioCentro = 50; // Puedes ajustar este valor según prefieras
            g2d.setColor(Color.GRAY);
            g2d.fillOval(centroX - radioCentro, centroY - radioCentro, 2 * radioCentro, 2 * radioCentro);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(centroX - radioCentro, centroY - radioCentro, 2 * radioCentro, 2 * radioCentro);

            // Dibuja el mensaje en el centro si está presente
//            if (mensaje != null) {
//                g2d.setColor(Color.RED);
//                g2d.setFont(new Font("Arial", Font.BOLD, 30));
//                FontMetrics fm = g2d.getFontMetrics();
//                int x = (getWidth() - fm.stringWidth(mensaje)) / 2;
//                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
//                g2d.drawString(mensaje, x, y);
//
//            }//agregar a controlador
        }
    }

    public class ArcoColor {

        private Color color;
        private int anguloInicio;
        private boolean estaIluminado;

        public ArcoColor(Color color, int anguloInicio) {
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
            return getForma(ventana.getWidth() / 2, ventana.getHeight() / 2, 200).contains(x, y);
        }
    }

    public JFrame getVentana() {
        return ventana;
    }

    public void setVentana(JFrame ventana) {
        this.ventana = ventana;
    }

    public PanelSimon getPanelSimon() {
        return panelSimon;
    }

    public void setPanelSimon(PanelSimon panelSimon) {
        this.panelSimon = panelSimon;
    }

    public JLabel getEtiquetaEstado() {
        return etiquetaEstado;
    }

    public void setEtiquetaEstado(JLabel etiquetaEstado) {
        this.etiquetaEstado = etiquetaEstado;
    }

    public JLabel getEtiquetaNivel() {
        return etiquetaNivel;
    }

    public void setEtiquetaNivel(JLabel etiquetaNivel) {
        this.etiquetaNivel = etiquetaNivel;
    }

    public JLabel getEtiquetaTiempo() {
        return etiquetaTiempo;
    }

    public void setEtiquetaTiempo(JLabel etiquetaTiempo) {
        this.etiquetaTiempo = etiquetaTiempo;
    }

    public JLabel getEtiquetaPuntuacion() {
        return etiquetaPuntuacion;
    }

    public void setEtiquetaPuntuacion(JLabel etiquetaPuntuacion) {
        this.etiquetaPuntuacion = etiquetaPuntuacion;
    }

    public JPanel getPanelSuperior() {
        return panelSuperior;
    }

    public void setPanelSuperior(JPanel panelSuperior) {
        this.panelSuperior = panelSuperior;
    }

    public ArrayList<ArcoColor> getArcos() {
        return arcos;
    }

    public void setArcos(ArrayList<ArcoColor> arcos) {
        this.arcos = arcos;
    }

}
