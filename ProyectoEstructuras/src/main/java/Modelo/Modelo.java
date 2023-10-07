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
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;


/**
 * @author Dell
 */
public class Modelo {

    private ArrayList<Integer> secuencia;
    private int pasoActual;
    private int nivel;
    private boolean jugando;
    private int tiempoRestante;
    private int velocidadDeSecuencia;
    private int puntos;
    private int multiplicadorDePuntos;

    public Modelo() {
        tiempoRestante = 15;
        velocidadDeSecuencia = 1000;
        puntos = 0;
        multiplicadorDePuntos = 1;
        secuencia = new ArrayList<>();
        nivel = 0;
        jugando = false;
        cargarDatos();
    }

//Método para reproducir sonidos
    public void reproducirSonido(String nombreArchivo) {
        try {
            File archivoSonido = new File(nombreArchivo);
            if (archivoSonido.exists()) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(archivoSonido);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } else {
                System.err.println("El archivo de sonido '" + nombreArchivo + "' no existe.");
            }
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
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

    public boolean isJugando() {
        return jugando;
    }

    public void setJugando(boolean jugando) {
        this.jugando = jugando;
    }

    public int getTiempoRestante() {
        return tiempoRestante;
    }

    public void setTiempoRestante(int tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }

    public int getVelocidadDeSecuencia() {
        return velocidadDeSecuencia;
    }

    public void setVelocidadDeSecuencia(int velocidadDeSecuencia) {
        this.velocidadDeSecuencia = velocidadDeSecuencia;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getMultiplicadorDePuntos() {
        return multiplicadorDePuntos;
    }

    public void setMultiplicadorDePuntos(int multiplicadorDePuntos) {
        this.multiplicadorDePuntos = multiplicadorDePuntos;
    }

    public void cargarDatos() {
        String rutaArchivo = "src/main/java/Modelo/CargadoDeArchivos.xml"; // Ruta del archivo XML
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            File archivoXML = new File(rutaArchivo);
            Document document = saxBuilder.build(archivoXML);

            Element datosElement = document.getRootElement();

            String tempo = datosElement.getChildText("Tempo");
            String velocidadDeSecuencia = datosElement.getChildText("VelocidadDeSecuencia");
            String MultiplicadorDePuntos = datosElement.getChildText("MultiplicadorDePuntos");
            tiempoRestante = Integer.valueOf(tempo);
            this.velocidadDeSecuencia = Integer.valueOf(velocidadDeSecuencia);
            multiplicadorDePuntos = Integer.valueOf(MultiplicadorDePuntos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
