/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.*;
import Vista.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

/**
 * @author Dell
 */
public class Controlador {

    Modelo mod;
    Vista vis;
    private Timer temporizadorTurno;
    private Timer temporizadorMensaje;

    public Controlador(Modelo m, Vista v) {
        mod = m;
        vis = v;

        //Muestra en pantalla el tiempo restante que le queda al jugador para reproducir la secuencia
        temporizadorTurno = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mod.setTiempoRestante(mod.getTiempoRestante() - 1);
                vis.getEtiquetaTiempo().setText("TIEMPO: " + (mod.getTiempoRestante() < 10 ? "0" : "") + mod.getTiempoRestante());
                if (mod.getTiempoRestante() <= 0) {
                    mostrarPantallaFinal();
                }
            }
        });
        temporizadorMensaje = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vis.setMensaje(null);
                vis.getPanelSimon().repaint();
                temporizadorMensaje.stop();
            }
        });

        //Implementación de los listeners
        vis.getPanelSimon().addMouseListener(new MouseAdapter() {
            Vista arcoHovered = null;

            //Presionar los botones de colores y verificar su secuencia
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!mod.isJugandoSecuencia()) {
                    for (Vista.ArcoColor arco : vis.getArcos()) {
                        if (arco.contiene(e.getX(), e.getY())) {
                            verificarSecuencia(arco);
                            vis.getPanelSimon().repaint();
                            break;
                        }
                    }
                }
            }

            //Reproduce el sonido correspondiente a cada botón de la aplicación
            @Override
            public void mousePressed(MouseEvent e) {
                if (!mod.isJugandoSecuencia()) {
                    for (Vista.ArcoColor arco : vis.getArcos()) {
                        if (arco.contiene(e.getX(), e.getY())) {

                            vis.setArcoPressed(arco);
                            if (arco.getColor().equals(Color.RED)) {
                                mod.reproducirSonido("1.wav");
                            } else if (arco.getColor().equals(Color.BLUE)) {
                                mod.reproducirSonido("2.wav");
                            } else if (arco.getColor().equals(Color.GREEN)) {
                                mod.reproducirSonido("3.wav");
                            } else if (arco.getColor().equals(Color.YELLOW)) {
                                mod.reproducirSonido("4.wav");
                            }
                            vis.getPanelSimon().repaint();
                            break;
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (vis.getArcoPressed() != null) {
                    vis.setArcoPressed(null);
                    vis.setArcoHovered(null);
                    vis.getPanelSimon().repaint();
                }
            }
        });

        vis.getPanelSimon().addMouseMotionListener(new MouseMotionAdapter() {
            Vista.ArcoColor arcoPressed = null;
            Vista.ArcoColor arcoHovered = null;

            //Oscurece el color donde el mouse esta posicionado
            @Override
            public void mouseMoved(MouseEvent e) {
                if (!mod.isJugandoSecuencia()) {
                    vis.setArcoHovered(null);
                    for (Vista.ArcoColor arco : vis.getArcos()) {
                        if (arco.contiene(e.getX(), e.getY())) {
                            vis.setArcoHovered(arco);
                            break;
                        }
                    }
                    vis.getPanelSimon().repaint();
                }
            }
        });
        iniciarJuego();
    }

    public void mostrarMensaje(String msg) {
        vis.setMensaje(msg);
        temporizadorMensaje.start();
        vis.getPanelSimon().repaint();
    }

    //Método para verificar que la secuencia que reproduce el jugador coincida con la secuencia generada previamente
    public void verificarSecuencia(Vista.ArcoColor arco) {
        if (!mod.isJugandoSecuencia()) {
            if (arco.getColor() == vis.getArcos().get(mod.getSecuencia().get(mod.getPasoActual())).getColor()) {
                mod.setPasoActual(mod.getPasoActual() + 1);
                if (mod.getPasoActual() == mod.getSecuencia().size()) {
                    temporizadorTurno.stop();
                    vis.getEtiquetaTiempo().setText("TIEMPO: 0");
                    agregarPasoASecuencia();
                    mod.reproducirSonido("yes.wav"); // Llamar cuando la secuencia es correcta
                }
            } else {
                mostrarPantallaFinal();
                mod.reproducirSonido("no.wav"); // Llamar cuando la secuencia es incorrecta
            }
        }
    }

    //Reinicia la secuencia y va agregando colores con el paso de los niveles
    public void iniciarJuego() {
        mod.getSecuencia().clear();
        mod.reproducirSonido("start.wav"); // Llamar cuando la secuencia es correcta
        agregarPasoASecuencia();
    }

    //Muestra la pantalla cuando el jugador pierde e implementa listeners para los botones de volver a jugar o salir
    public void mostrarPantallaFinal() {
        temporizadorTurno.stop();
        mod.reproducirSonido("gameOver.wav"); // Terminó el juego
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

        //Listener para botón de reintentar
        btnReintentar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vis.getVentana().remove(panelFinal);

                vis.getVentana().add(vis.getPanelSimon(), BorderLayout.CENTER);
                vis.getVentana().add(vis.getEtiquetaEstado(), BorderLayout.SOUTH);
                vis.getVentana().add(vis.getPanelSuperior(), BorderLayout.NORTH);
                mod.setNivel(0);
                mod.setPuntuacion(0);
                mod.setMultiplicadorPuntos(1);
                iniciarJuego();
                vis.getVentana().revalidate();
                vis.getVentana().repaint();
            }
        });

        //Listener para botón de Salir
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

    //Metodo para agregar un color a la secuencia si se avanza de nivel
    public void agregarPasoASecuencia() {
        Random rand = new Random();
        mod.getSecuencia().add(rand.nextInt(4));

        mod.setPasoActual(0);
        mod.setNivel(mod.getNivel() + 1);
        vis.getEtiquetaNivel().setText("Nivel " + mod.getNivel());
        mod.setPuntuacion(mod.getPuntuacion() + (10 * mod.getMultiplicadorPuntos()));
        vis.getEtiquetaPuntuacion().setText("Puntuación: " + mod.getPuntuacion());
        if (mod.getNivel() % 4 == 0) {
            if (mod.getTiempoRestante() > 3) {
                int aux = mod.getTiempoRestante();
                aux = aux - 2;
                mod.setTiempoRestante(aux);

                //Se reduce el tiempo que tiene el jugador para reproducir la secuencia
                mostrarMensaje("TIEMPO REDUCIDO!!");
                vis.getPanelSimon().repaint();


            }
            if (mod.getVelocidadSecuencia() > 500) {
                mod.setVelocidadSecuencia(mod.getVelocidadSecuencia() - 100);

                //Se aumenta la velocidad al mostrar la secuencia al jugador
                mostrarMensaje("MÁS RÁPIDO!!!");
                vis.getPanelSimon().repaint();
                //reproducirSonido("moreSpeed");
                mod.setMultiplicadorPuntos(mod.getMultiplicadorPuntos() * 10);
            }
        }
        jugarSecuencia();
    }

    public void reiniciarCronometro() {
        mod.setTiempoRestante(15 - (mod.getNivel() / (4 * 2)));
        if (mod.getTiempoRestante() < 3) {
            mod.setTiempoRestante(3);
        }
        temporizadorTurno.stop();
        vis.getEtiquetaTiempo().setText("TIEMPO: " + (mod.getTiempoRestante() < 10 ? "0" : "") + mod.getTiempoRestante());
        temporizadorTurno.start();
    }

    //Método para mostrar la secuencia al jugador
    public void jugarSecuencia() {
        mod.setJugandoSecuencia(true);
        vis.getEtiquetaEstado().setText("Mira la secuencia");
        for (int i = 0; i < mod.getSecuencia().size(); i++) {
            int indiceFinal = i;
            Timer temporizador = new Timer(mod.getVelocidadSecuencia() * (i + 1), new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    vis.getArcos().get(mod.getSecuencia().get(indiceFinal)).iluminar();
                    if (vis.getArcos().get(mod.getSecuencia().get(indiceFinal)).getColor().equals(Color.RED)) {
                        mod.reproducirSonido("1.wav");
                    } else if (vis.getArcos().get(mod.getSecuencia().get(indiceFinal)).getColor().equals(Color.BLUE)) {
                        mod.reproducirSonido("2.wav");
                    } else if (vis.getArcos().get(mod.getSecuencia().get(indiceFinal)).getColor().equals(Color.GREEN)) {
                        mod.reproducirSonido("3.wav");
                    } else if (vis.getArcos().get(mod.getSecuencia().get(indiceFinal)).getColor().equals(Color.YELLOW)) {
                        mod.reproducirSonido("4.wav");
                    }
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
}
