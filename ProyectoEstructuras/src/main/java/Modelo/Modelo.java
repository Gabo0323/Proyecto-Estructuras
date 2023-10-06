/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
/**
 *
 * @author Dell
 */


public class Modelo {
    private ArrayList<Integer> secuencia;
    private int pasoActual;
    private int nivel;
    private boolean jugandoSecuencia;
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

    }

    
     public void reproducirSonidoCorrecto() {
        try {
            File archivoSonido = new File("yes.wav");
            if (archivoSonido.exists()) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(archivoSonido);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } else {
                System.err.println("El archivo 'yes.wav' no existe.");
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    public void reproducirSonidoIncorrecto() {
        try {
            File archivoSonido = new File("no.wav");
            if (archivoSonido.exists()) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(archivoSonido);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } else {
                System.err.println("El archivo 'no.wav' no existe.");
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
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
