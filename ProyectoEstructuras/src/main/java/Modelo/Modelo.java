/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;

/**
 *
 * @author Dell
 */


public class Modelo {
    private ArrayList<Integer> secuencia;
    private int pasoActual;
    private int nivel;
    private boolean jugandoSecuencia;
    //private Timer temporizadorTurno;//NOTA: todo lo que tenga el Timer debe ser mejor incluido en el control
    private int tiempoRestante;
    private int velocidadSecuencia;
    private int puntuacion;
    private int multiplicadorPuntos;

    public Modelo() {
        tiempoRestante = 15;
        velocidadSecuencia = 1000;
        puntuacion = 0;
        multiplicadorPuntos = 1;
        secuencia = new ArrayList<>();
        nivel = 0;
        jugandoSecuencia = false;
//        temporizadorTurno = new Timer(1000, new ActionListener() {//Esto debe ir en el control
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                tiempoRestante--;
//                //etiquetaTiempo.setText("Tiempo restante: 00:" + (tiempoRestante < 10 ? "0" : "") + tiempoRestante);
//                if (tiempoRestante <= 0) {
//                    //mostrarPantallaFinal();
//                }
//            }
//        });
        iniciarJuego();
    }

     public void iniciarJuego() {
        secuencia.clear();
        reproducirSonido("start");
        agregarPasoASecuencia();
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
     
     public void agregarPasoASecuencia() {
        Random rand = new Random();
        secuencia.add(rand.nextInt(4));
        pasoActual = 0;
        nivel++;
        //etiquetaNivel.setText("Nivel " + nivel);
        puntuacion += 10 * multiplicadorPuntos;
        //etiquetaPuntuacion.setText("Puntuación: " + puntuacion);
        if (nivel % 4 == 0) {
            if (tiempoRestante > 3) {
                tiempoRestante -= 2;
                //panelSimon.mostrarMensaje("TIEMPO REDUCIDO!!");
            }
            if (velocidadSecuencia > 500) {
                velocidadSecuencia -= 100;
                //panelSimon.mostrarMensaje("MÁS RÁPIDO!!!");
                reproducirSonido("moreSpeed");
                multiplicadorPuntos *= 10;
            }
        }
        jugarSecuencia();
    }
     
     public void jugarSecuencia() {
        jugandoSecuencia = true;
        //etiquetaEstado.setText("Mira la secuencia");
        for (int i = 0; i < secuencia.size(); i++) {
            int indiceFinal = i;
//            Timer temporizador = new Timer(velocidadSecuencia * (i + 1), new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    arcos.get(secuencia.get(indiceFinal)).iluminar();
//                    reproducirSonido(secuencia.get(indiceFinal) + 1);
//                    panelSimon.repaint();
//                    Timer temporizadorRetornoColor = new Timer(velocidadSecuencia / 2, new ActionListener() {
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
//                            arcos.get(secuencia.get(indiceFinal)).detenerIluminacion();
//                            panelSimon.repaint();
//                            if (indiceFinal == secuencia.size() - 1) {
//                                etiquetaEstado.setText("Es tu turno");
//                                jugandoSecuencia = false;
//                                reiniciarCronometro();
//                            }
//                        }
//                    });
//                    temporizadorRetornoColor.setRepeats(false);
//                    temporizadorRetornoColor.start();
//                }
//            });
            //temporizador.setRepeats(false);
            //temporizador.start();
        }
    }
     
     public void reiniciarCronometro() {
        tiempoRestante = 15 - (nivel / 4 * 2);
        if (tiempoRestante < 3) {
            tiempoRestante = 3;
        }
//        temporizadorTurno.stop();
//        //etiquetaTiempo.setText("Tiempo restante: 00:" + (tiempoRestante < 10 ? "0" : "") + tiempoRestante);
//        temporizadorTurno.start();
    }
     
     
     
    public ArrayList<Integer> getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(ArrayList<Integer> secuencia) {
        this.secuencia = secuencia;
    }

    public int getPasoActual() {
        return pasoActual;
    }

    public void setPasoActual(int pasoActual) {
        this.pasoActual = pasoActual;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public boolean isJugandoSecuencia() {
        return jugandoSecuencia;
    }

    public void setJugandoSecuencia(boolean jugandoSecuencia) {
        this.jugandoSecuencia = jugandoSecuencia;
    }

//    public Timer getTemporizadorTurno() {
//        return temporizadorTurno;
//    }
//
//    public void setTemporizadorTurno(Timer temporizadorTurno) {
//        this.temporizadorTurno = temporizadorTurno;
//    }

    public int getTiempoRestante() {
        return tiempoRestante;
    }

    public void setTiempoRestante(int tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }

    public int getVelocidadSecuencia() {
        return velocidadSecuencia;
    }

    public void setVelocidadSecuencia(int velocidadSecuencia) {
        this.velocidadSecuencia = velocidadSecuencia;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public int getMultiplicadorPuntos() {
        return multiplicadorPuntos;
    }

    public void setMultiplicadorPuntos(int multiplicadorPuntos) {
        this.multiplicadorPuntos = multiplicadorPuntos;
    }
    
    
}
