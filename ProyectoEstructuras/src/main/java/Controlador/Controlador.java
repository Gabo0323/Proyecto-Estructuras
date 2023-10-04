/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.*;
import Vista.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.*;

/**
 * @author Dell
 */
public class Controlador {

    Modelo mod = new Modelo();
    Vista vis = new Vista();
    private Timer temporizadorTurno;

    public Controlador(Modelo m, Vista v) {
        mod = m;
        vis = v;
        temporizadorTurno = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mod.setTiempoRestante(mod.getTiempoRestante() - 1);
                vis.getEtiquetaTiempo().setText("Tiempo restante: 00:" + (mod.getTiempoRestante() < 10 ? "0" : "") + mod.getTiempoRestante());

                if (mod.getTiempoRestante() <= 0) {
                    mostrarPantallaFinal();

                }
            }
        });
        iniciarJuego();
    }

    public void iniciarJuego() {
        mod.getSecuencia().clear();
        //reproducirSonido("start");
        agregarPasoASecuencia();
    }

    public void mostrarPantallaFinal() {
        temporizadorTurno.stop();
        //reproducirSonido("gameOver");
        vis.getVentana().remove(vis.getPanelSimon());
        vis.getVentana().remove(vis.getEtiquetaEstado());
        vis.getVentana().remove(vis.getPanelSuperior());

        JPanel panelFinal = new JPanel(new BorderLayout());
        JLabel etiquetaFinal = new JLabel("Quedaste en el nivel: " + mod.getNivel(), SwingConstants.CENTER);
        panelFinal.add(etiquetaFinal, BorderLayout.CENTER);

        JLabel etiquetaPuntosFinal = new JLabel("Puntuación final: " + mod.getPuntuacion(), SwingConstants.CENTER);
        panelFinal.add(etiquetaPuntosFinal, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        JButton btnReintentar = new JButton("Volver a intentar");
        JButton btnSalir = new JButton("Salir");

        btnReintentar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vis.getVentana().remove(panelFinal);

                vis.getVentana().add(vis.getPanelSimon(), BorderLayout.CENTER);
                vis.getVentana().add(vis.getEtiquetaEstado(), BorderLayout.SOUTH);
                vis.getVentana().add(vis.getPanelSuperior(), BorderLayout.NORTH);
                mod.setNivel(0);
                mod.setPuntuacion(0);
                mod.setMultiplicadorPuntos(0);
                iniciarJuego();
                vis.getVentana().revalidate();
                vis.getVentana().repaint();
            }
        });

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        panelBotones.add(btnReintentar);
        panelBotones.add(btnSalir);
        panelFinal.add(panelBotones, BorderLayout.SOUTH);
        vis.getVentana().add(panelFinal);
        vis.getVentana().revalidate();
        vis.getVentana().repaint();
    }

    public void agregarPasoASecuencia() {
        Random rand = new Random();
        mod.getSecuencia().add(rand.nextInt(4));
        mod.setPasoActual(0);
        mod.setNivel(mod.getNivel() + 1);
        vis.getEtiquetaNivel().setText("Nivel " + mod.getNivel());
        mod.setPuntuacion(mod.getPuntuacion() + (10 * mod.getMultiplicadorPuntos()));//cuidado con esta linea, puede fallar
        vis.getEtiquetaPuntuacion().setText("Puntuación: " + mod.getPuntuacion());
        if (mod.getNivel() % 4 == 0) {
            if (mod.getTiempoRestante() > 3) {
                mod.setTiempoRestante(mod.getTiempoRestante() - 2);//ciudado, puede fallar

                vis.getPanelSimon().mostrarMensaje("TIEMPO REDUCIDO!!");

            }
            if (mod.getVelocidadSecuencia() > 500) {
                mod.setVelocidadSecuencia(mod.getVelocidadSecuencia() - 100);//ciudado, puede fallar

                vis.getPanelSimon().mostrarMensaje("MÁS RÁPIDO!!!");
                //reproducirSonido("moreSpeed");
                mod.setMultiplicadorPuntos(mod.getMultiplicadorPuntos() * 10);//ciudado, puede fallar
            }
        }
        jugarSecuencia();

    }

    public void reiniciarCronometro() {
        mod.setTiempoRestante(15 - (mod.getNivel() / (4 * 2)));//cuidado, puede fallar
        if (mod.getTiempoRestante() < 3) {
            mod.setTiempoRestante(3);
        }
        temporizadorTurno.stop();
        vis.getEtiquetaTiempo().setText("Tiempo restante: 00:" + (mod.getTiempoRestante() < 10 ? "0" : "") + mod.getTiempoRestante());
        temporizadorTurno.start();
    }

    public void jugarSecuencia() {
        mod.setJugandoSecuencia(true);
        vis.getEtiquetaEstado().setText("Mira la secuencia");
        for (int i = 0; i < mod.getSecuencia().size(); i++) {
            int indiceFinal = i;
            Timer temporizador = new Timer(mod.getVelocidadSecuencia() * (i + 1), new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    vis.getArcos().get(mod.getSecuencia().get(indiceFinal)).iluminar();
                    //reproducirSonido(secuencia.get(indiceFinal) + 1);
                    vis.getPanelSimon().repaint();
                    Timer temporizadorRetornoColor = new Timer(mod.getVelocidadSecuencia() / 2, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            vis.getArcos().get(mod.getSecuencia().get(indiceFinal)).detenerIluminacion();
                            vis.getPanelSimon().repaint();
                            if (indiceFinal == mod.getSecuencia().size() - 1) {
                                vis.getEtiquetaEstado().setText("Es tu turno");
                                mod.setJugandoSecuencia(false);
                                reiniciarCronometro();
                            }
                        }
                    });
                    temporizadorRetornoColor.setRepeats(false);
                    temporizadorRetornoColor.start();
                }
            });
            temporizador.setRepeats(false);
            temporizador.start();
        }
    }

    public class PanelSimon extends JPanel {

        private Vista.ArcoColor arcoHovered = null;
        private Vista.ArcoColor arcoPressed = null;
        private String mensaje = null;
        private Timer temporizadorMensaje;

        public PanelSimon() {//Falta poner esto en el controlador, pero por hoy descanzo
            temporizadorMensaje = new Timer(2000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mensaje = null;
                    repaint();
                    temporizadorMensaje.stop();
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!jugandoSecuencia) {
                        for (Vista.Vista.ArcoColor arco : arcos) {
                            if (arco.contiene(e.getX(), e.getY())) {
                                verificarSecuencia(arco);
                                repaint();
                                break;
                            }
                        }
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (!jugandoSecuencia) {
                        for (ArcoColor arco : arcos) {
                            if (arco.contiene(e.getX(), e.getY())) {
                                arcoPressed = arco;
                                repaint();
                                break;
                            }
                        }
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (arcoPressed != null) {
                        arcoPressed = null;
                        arcoHovered = null;
                        repaint();
                    }
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    if (!jugandoSecuencia) {
                        arcoHovered = null;
                        for (ArcoColor arco : arcos) {
                            if (arco.contiene(e.getX(), e.getY())) {
                                arcoHovered = arco;
                                break;
                            }
                        }
                        repaint();
                    }
                }
            });


            public void mousePressed (MouseEvent e){

                String archivoAudio = "yes.wav";
                String archivoAudioNo = "no.wav";

                if (enCircunferencia(e) && score <= listSecuencia.size() && !isIlluminationTimerRunning && recordInputs) {
                    int sector = getSector(anguloRad);
                    input = sector;

                    if (!mainControl.compararInput(input)) {

                        try {
                            File sonido = new File("no.wav");
                            AudioInputStream audioInputStream
                                    = AudioSystem.getAudioInputStream(sonido);
                            try (Clip clip = AudioSystem.getClip()) {
                                clip.open(audioInputStream);

                                clip.start();
                                Thread.sleep(clip.getMicrosecondLength() / 1_000);

                                JOptionPane.showMessageDialog(null, "¡Perdiste!", "Mensaje",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (IOException | InterruptedException | LineUnavailableException |
                                 UnsupportedAudioFileException ex) {
                            System.err.printf("Excepción al reproducir audio: '%s'%n", ex.getMessage());
                            ex.printStackTrace(); // Imprime la traza de excepción para obtener más detalles.

                        }

                        for (int i = 0; i < COLORS.length; i++) {
                            iluminarSector(i);
                        }

                        illuminationTimer.stop();
                        recordInputs = false;
                        System.out.println("======");

                    } else {
                        try {
                            File sonido = new File("yes.wav");
                            AudioInputStream audioInputStream
                                    = AudioSystem.getAudioInputStream(sonido);
                            try (Clip clip = AudioSystem.getClip()) {
                                clip.open(audioInputStream);

                                clip.start();
                                Thread.sleep(clip.getMicrosecondLength() / 1_000);
                            }
                        } catch (IOException | InterruptedException | LineUnavailableException |
                                 UnsupportedAudioFileException ex) {
                            System.err.printf("Excepción al reproducir audio: '%s'%n", ex.getMessage());
                            ex.printStackTrace(); // Imprime la traza de excepción para obtener más detalles.

                        }

                        System.out.println("OK");
                    }
                }
            }
        }

        public void mostrarMensaje(String msg) {
            this.mensaje = msg;
            temporizadorMensaje.start();
            repaint();
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

}