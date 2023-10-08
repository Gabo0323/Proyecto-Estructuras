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
    private Timer temporizadorDeTurno;
    private Timer temporizadorMensaje;

    public Controlador(Modelo m, Vista v) {
        mod = m;
        vis = v;

        //Muestra en pantalla el tiempo restante que le queda al jugador para reproducir la secuencia
        temporizadorDeTurno = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mod.setTiempoRestante(mod.getTiempoRestante() - 1);
                vis.getLabelTiempoRestante().setText("TIEMPO: " + (mod.getTiempoRestante() < 10 ? "0" : "") + mod.getTiempoRestante());
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

            //Presionar los botones de colores y verificar su secuencia
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!mod.isJugando()) {
                    for (Vista.Arco arco : vis.getArcos()) {
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
                if (!mod.isJugando()) {
                    for (Vista.Arco arco : vis.getArcos()) {
                        if (arco.contiene(e.getX(), e.getY())) {

                            vis.setArcoPresionado(arco);
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
                if (vis.getArcoPresionado() != null) {
                    vis.setArcoPresionado(null);
                    vis.setArcoPosado(null);
                    vis.getPanelSimon().repaint();
                }
            }
        });

        vis.getPanelSimon().addMouseMotionListener(new MouseMotionAdapter() {

            //Oscurece el color donde el mouse esta posicionado
            @Override
            public void mouseMoved(MouseEvent e) {
                if (!mod.isJugando()) {
                    vis.setArcoPosado(null);
                    for (Vista.Arco arco : vis.getArcos()) {
                        if (arco.contiene(e.getX(), e.getY())) {
                            vis.setArcoPosado(arco);
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
    public void verificarSecuencia(Vista.Arco arco) {
        if (!mod.isJugando()) {
            if (arco.getColor() == vis.getArcos().get(mod.getSecuencia().get(mod.getPasoActual())).getColor()) {
                mod.setPasoActual(mod.getPasoActual() + 1);
                if (mod.getPasoActual() == mod.getSecuencia().size()) {
                    temporizadorDeTurno.stop();
                    vis.getLabelTiempoRestante().setText("TIEMPO: 0");
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
        temporizadorDeTurno.stop();
        mod.reproducirSonido("gameOver.wav"); // Terminó el juego
        vis.getVent().remove(vis.getPanelSimon());
        vis.getVent().remove(vis.getLabelEstado());
        vis.getVent().remove(vis.getUpPanel());

        JPanel panelFinal = new JPanel(new BorderLayout());
        JLabel labelFinal = new JLabel("Quedaste en el nivel: " + mod.getNivel(), SwingConstants.CENTER);
        panelFinal.add(labelFinal, BorderLayout.CENTER);

        JLabel etiquetaPuntosFinal = new JLabel("Puntuación final: " + mod.getPuntos(), SwingConstants.CENTER);
        panelFinal.add(etiquetaPuntosFinal, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        JButton btnReintentar = new JButton("Volver a intentar");
        JButton btnSalir = new JButton("Salir");

        //Listener para botón de reintentar
        btnReintentar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vis.getVent().remove(panelFinal);

                vis.getVent().add(vis.getPanelSimon(), BorderLayout.CENTER);
                vis.getVent().add(vis.getLabelEstado(), BorderLayout.SOUTH);
                vis.getVent().add(vis.getUpPanel(), BorderLayout.NORTH);
                mod.setNivel(0);
                mod.setPuntos(0);
                mod.setMultiplicadorDePuntos(1);
                iniciarJuego();
                vis.getVent().revalidate();
                vis.getVent().repaint();
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
        vis.getVent().add(panelFinal);
        vis.getVent().revalidate();
        vis.getVent().repaint();
    }

    //Metodo para agregar un color a la secuencia si se avanza de nivel
    public void agregarPasoASecuencia() {
        Random rand = new Random();
        mod.getSecuencia().add(rand.nextInt(4));

        mod.setPasoActual(0);
        mod.setNivel(mod.getNivel() + 1);
        vis.getLabelNivelActual().setText("Nivel " + mod.getNivel());
        mod.setPuntos(mod.getPuntos() + (10 * mod.getMultiplicadorDePuntos()));
        vis.getLabelPuntuacion().setText("Puntuación: " + mod.getPuntos());
        if (mod.getNivel() % 4 == 0) {
            if (mod.getTiempoRestante() > 3) {
                int aux = mod.getTiempoRestante();
                aux = aux - 2;
                mod.setTiempoRestante(aux);

                //Se reduce el tiempo que tiene el jugador para reproducir la secuencia
                mostrarMensaje("TIEMPO REDUCIDO!!");
                vis.getPanelSimon().repaint();

            }
            if (mod.getVelocidadDeSecuencia() > 500) {
                mod.setVelocidadDeSecuencia(mod.getVelocidadDeSecuencia() - 100);

                //Se aumenta la velocidad al mostrar la secuencia al jugador
                mostrarMensaje("MÁS RÁPIDO!!!");
                vis.getPanelSimon().repaint();
                //reproducirSonido("moreSpeed");
                mod.setMultiplicadorDePuntos(mod.getMultiplicadorDePuntos() * 10);
            }
        }
        reproducirSecuencia();
    }

    public void reiniciarCronometro() {

        mod.setTiempoRestante(mod.getTiempoRestante() - (mod.getNivel() / (4 * 2)));//cuidado, puede fallar

        if (mod.getTiempoRestante() < 3) {
            mod.setTiempoRestante(3);
        }
        temporizadorDeTurno.stop();
        vis.getLabelTiempoRestante().setText("TIEMPO: " + (mod.getTiempoRestante() < 10 ? "0" : "") + mod.getTiempoRestante());
        temporizadorDeTurno.start();
    }

    //Método para mostrar la secuencia al jugador
    public void reproducirSecuencia() {
        mod.setJugando(true);
        vis.getLabelEstado().setText("Mira la secuencia");
        for (int i = 0; i < mod.getSecuencia().size(); i++) {
            int indiceFinal = i;
            Timer temporizador = new Timer(mod.getVelocidadDeSecuencia() * (i + 1), new ActionListener() {
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
                    Timer temporizadorRetornoColor = new Timer(mod.getVelocidadDeSecuencia() / 2, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            vis.getArcos().get(mod.getSecuencia().get(indiceFinal)).detenerIluminacion();
                            vis.getPanelSimon().repaint();
                            if (indiceFinal == mod.getSecuencia().size() - 1) {
                                vis.getLabelEstado().setText("Es tu turno");
                                mod.setJugando(false);
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
