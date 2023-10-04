/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.proyectoestructuras;

import Vista.Vista;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
//import javazoom.jl.decoder.JavaLayerException;
//import javazoom.jl.player.Player;

public class ProyectoEstructuras {

    private JFrame ventana;
    private PanelSimon panelSimon;
    private JLabel etiquetaEstado;
    private JLabel etiquetaNivel;
    private JLabel etiquetaTiempo;
    private JLabel etiquetaPuntuacion;
    private JPanel panelSuperior;
    private ArrayList<ArcoColor> arcos;
    private ArrayList<Integer> secuencia;
    private int pasoActual;
    private int nivel;
    private boolean jugandoSecuencia;
    private Timer temporizadorTurno;
    private int tiempoRestante;
    private int velocidadSecuencia;
    private int puntuacion;
    private int multiplicadorPuntos;

    public ProyectoEstructuras() {
        tiempoRestante = 15;
        velocidadSecuencia = 1000;
        puntuacion = 0;
        multiplicadorPuntos = 1;

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
        secuencia = new ArrayList<>();

        arcos.add(new ArcoColor(Color.RED, 0));
        arcos.add(new ArcoColor(Color.BLUE, 90));
        arcos.add(new ArcoColor(Color.GREEN, 180));
        arcos.add(new ArcoColor(Color.YELLOW, 270));

        nivel = 0;
        jugandoSecuencia = false;

        temporizadorTurno = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tiempoRestante--;
                etiquetaTiempo.setText("Tiempo restante: 00:" + (tiempoRestante < 10 ? "0" : "") + tiempoRestante);
                if (tiempoRestante <= 0) {
                    mostrarPantallaFinal();
                }
            }
        });

        ventana.setVisible(true);
        iniciarJuego();
    }

    public void iniciarJuego() {
        secuencia.clear();
        reproducirSonido("start");
        agregarPasoASecuencia();
    }

    public void agregarPasoASecuencia() {
        Random rand = new Random();
        secuencia.add(rand.nextInt(4));
        pasoActual = 0;
        nivel++;
        etiquetaNivel.setText("Nivel " + nivel);
        puntuacion += 10 * multiplicadorPuntos;
        etiquetaPuntuacion.setText("Puntuación: " + puntuacion);
        if (nivel % 4 == 0) {
            if (tiempoRestante > 3) {
                tiempoRestante -= 2;
                panelSimon.mostrarMensaje("TIEMPO REDUCIDO!!");
            }
            if (velocidadSecuencia > 500) {
                velocidadSecuencia -= 100;
                panelSimon.mostrarMensaje("MÁS RÁPIDO!!!");
                reproducirSonido("moreSpeed");
                multiplicadorPuntos *= 10;
            }
        }
        jugarSecuencia();
    }

    public void jugarSecuencia() {
        jugandoSecuencia = true;
        etiquetaEstado.setText("Mira la secuencia");
        for (int i = 0; i < secuencia.size(); i++) {
            int indiceFinal = i;
            Timer temporizador = new Timer(velocidadSecuencia * (i + 1), new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    arcos.get(secuencia.get(indiceFinal)).iluminar();
                    reproducirSonido(secuencia.get(indiceFinal) + 1);
                    panelSimon.repaint();
                    Timer temporizadorRetornoColor = new Timer(velocidadSecuencia / 2, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            arcos.get(secuencia.get(indiceFinal)).detenerIluminacion();
                            panelSimon.repaint();
                            if (indiceFinal == secuencia.size() - 1) {
                                etiquetaEstado.setText("Es tu turno");
                                jugandoSecuencia = false;
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

    public void reiniciarCronometro() {
        tiempoRestante = 15 - (nivel / 4 * 2);
        if (tiempoRestante < 3) {
            tiempoRestante = 3;
        }
        temporizadorTurno.stop();
        etiquetaTiempo.setText("Tiempo restante: 00:" + (tiempoRestante < 10 ? "0" : "") + tiempoRestante);
        temporizadorTurno.start();
    }

    public void verificarSecuencia(ArcoColor arco) {
        if (!jugandoSecuencia) {
            reproducirSonido(arcos.indexOf(arco) + 1);
            if (arco.getColor() == arcos.get(secuencia.get(pasoActual)).getColor()) {
                pasoActual++;
                if (pasoActual == secuencia.size()) {
                    temporizadorTurno.stop();
                    etiquetaTiempo.setText("Tiempo restante: 00:00");
                    agregarPasoASecuencia();
                }
            } else {
                mostrarPantallaFinal();
            }
        }
    }

    public void mostrarPantallaFinal() {
        temporizadorTurno.stop();
        reproducirSonido("gameOver");
        ventana.remove(panelSimon);
        ventana.remove(etiquetaEstado);
        ventana.remove(panelSuperior);

        JPanel panelFinal = new JPanel(new BorderLayout());
        JLabel etiquetaFinal = new JLabel("Quedaste en el nivel: " + nivel, SwingConstants.CENTER);
        panelFinal.add(etiquetaFinal, BorderLayout.CENTER);

        JLabel etiquetaPuntosFinal = new JLabel("Puntuación final: " + puntuacion, SwingConstants.CENTER);
        panelFinal.add(etiquetaPuntosFinal, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        JButton btnReintentar = new JButton("Volver a intentar");
        JButton btnSalir = new JButton("Salir");

        btnReintentar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.remove(panelFinal);
                ventana.add(panelSimon, BorderLayout.CENTER);
                ventana.add(etiquetaEstado, BorderLayout.SOUTH);
                ventana.add(panelSuperior, BorderLayout.NORTH);
                nivel = 0;
                puntuacion = 0;
                multiplicadorPuntos = 1;
                iniciarJuego();
                ventana.revalidate();
                ventana.repaint();
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
        ventana.add(panelFinal);
        ventana.revalidate();
        ventana.repaint();
    }

    public void reproducirSonido(String nombreArchivo) {
//        try {
//            FileInputStream fis = new FileInputStream(nombreArchivo + ".mp3");
//            Player player = new Player(fis);
//            new Thread(() -> {
//                try {
//                    player.play();
//                } catch (JavaLayerException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        } catch (JavaLayerException | FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    public void reproducirSonido(int nombreArchivo) {
//        try {
//            FileInputStream fis = new FileInputStream(nombreArchivo + ".mp3");
//            Player player = new Player(fis);
//            new Thread(() -> {
//                try {
//                    player.play();
//                } catch (JavaLayerException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        } catch (JavaLayerException | FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    public static void main(String[] args) {
        //new ProyectoEstructuras();
        Vista vis = new Vista();
    }

    class PanelSimon extends JPanel {

        private ArcoColor arcoHovered = null;
        private ArcoColor arcoPressed = null;
        private String mensaje = null;
        private Timer temporizadorMensaje;

        public PanelSimon() {
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
                        for (ArcoColor arco : arcos) {
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

    class ArcoColor {

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
}
