/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.*;
import Vista.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 *
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
                mod.setTiempoRestante(mod.getTiempoRestante()-1);
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
        mod.setNivel(mod.getNivel()+1);
        vis.getEtiquetaNivel().setText("Nivel " + mod.getNivel());
        mod.setPuntuacion(mod.getPuntuacion()+(10 * mod.getMultiplicadorPuntos()));//cuidado con esta linea, puede fallar
        vis.getEtiquetaPuntuacion().setText("Puntuación: " + mod.getPuntuacion());
        if (mod.getNivel() % 4 == 0) {
            if (mod.getTiempoRestante() > 3) {
                mod.setTiempoRestante(mod.getTiempoRestante()-2);//ciudado, puede fallar
                
                vis.getPanelSimon().mostrarMensaje("TIEMPO REDUCIDO!!");
                
            }
            if (mod.getVelocidadSecuencia() > 500) {
                mod.setVelocidadSecuencia(mod.getVelocidadSecuencia()-100);//ciudado, puede fallar
                
                vis.getPanelSimon().mostrarMensaje("MÁS RÁPIDO!!!");
                //reproducirSonido("moreSpeed");
                mod.setMultiplicadorPuntos(mod.getMultiplicadorPuntos()*10);//ciudado, puede fallar
            }
        }
        jugarSecuencia();
        
    }
    
    public void reiniciarCronometro() {
        mod.setTiempoRestante(15-(mod.getNivel()/(4*2)));//cuidado, puede fallar
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
}
