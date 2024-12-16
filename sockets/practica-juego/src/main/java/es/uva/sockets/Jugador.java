package es.uva.sockets;

public class Jugador {
    public final int id;
    public Coordenadas coordenadas;

    public Jugador(int id, Coordenadas coordenadas) {
        this.id = id;
        this.coordenadas = coordenadas;
    }

    public char getChar(){
        return (char) ('A' + (id - 1) % 26);
    }

    public String coordenadasJugador() {
        return id + " " + coordenadas.getX() + " " + coordenadas.getY();
    }

    public void mover(Direccion dir) {
        this.coordenadas=this.coordenadas.mover(dir);
    }

    public Coordenadas getCoordenadas() {
        return coordenadas;
    }
}
