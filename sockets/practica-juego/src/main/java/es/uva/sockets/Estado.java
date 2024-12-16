package es.uva.sockets;

import java.util.ArrayList;

import java.util.Random;

public class Estado {
    public final ArrayList<Jugador> jugadores;
    public Coordenadas tesoro = null;
    public final ArrayList<Coordenadas> buscadas;
    private final int size;
    private boolean terminado;

    public Estado(int size, boolean Servidor) {
        this.size = size;
        terminado = false;
        this.jugadores = new ArrayList<>();
        this.buscadas = new ArrayList<>();
        // TODO: Coordenadas aleatorias para el tesoro
        if (Servidor) {
            this.tesoro = getCoordenadasNuevas();
        }
    }

    // Los métodos que modifican el estado son synchronized,
    // Esto quiere decir que un hilo debe esperar a que otro
    // hilo acabe de utilizarlo
    // Lo necesitamos ya que vamos a gestionar cada conexión
    // con un cliente en un hilo deistinto
    public synchronized void terminar(){
        terminado = true;
    }

    public synchronized boolean estaTerminado(){
        return terminado;
    }


    public synchronized void nuevoJugador(Jugador jugador) {
        jugadores.add(jugador);
    }

    public synchronized void buscar(int id) {
        // TODO busca el tesoro el jugador con este id
        // Si se encuentra finaliza el juego
        if (tesoro == null) {
            buscadas.add(jugadores.get(id).getCoordenadas());
        } else {
            if(tesoro.equals(jugadores.get(id).getCoordenadas())) {
                terminar();
            }
            buscadas.add(jugadores.get(id).getCoordenadas());
        }

    }

    public synchronized void mover(int id, Direccion dir) {
        // TODO mueve a el jugador id en la direccion dir
        jugadores.get(id).mover(dir);
    }

    public void mostrar() {
        // Limpiar pantalla
        System.out.print("\033[H\033[2J");
        System.out.flush();
        // Linea horizontal
        for (int col = 0; col < size; col++) {
            System.out.print("---");
        }
        System.out.println("-");
        for (int row = 0; row < size; row++) {
            // Columnas
            for (int col = 0; col < size; col++) {
                System.out.print("| "); // Cada celda representada por "| |"
                // Si no hay nada imprimimos vacio
                char toPrint = ' ';
                // Si ya esta cavado imprimos una x
                for (Coordenadas coordenadas : buscadas) {
                    if (coordenadas.equals(new Coordenadas(col, row))) {
                        toPrint = 'x';
                    }
                }
                // Si hay un jugador imprimimos su representacion
                for (Jugador jugador : jugadores) {
                    if (jugador.coordenadas.equals(new Coordenadas(col, row))) {
                        toPrint = jugador.getChar();
                    }
                }
                System.out.print(toPrint);
            }
            System.out.println("|"); // End of row with a final column border

            // Print a row separator
            for (int col = 0; col < size; col++) {
                System.out.print("---");
            }
            System.out.println("-");
        }
    }

    public Coordenadas getCoordenadasNuevas() {
        Random rand = new Random();
        if(jugadores.size() == size*size) {
            return null;
        }
        int[] nuevasCoord = new int[2];
        boolean condicion;
        do{
            condicion = false;
            nuevasCoord[0] = rand.nextInt(size);
            nuevasCoord[1] = rand.nextInt(size);
            for(Jugador jugador : jugadores) {
                if(nuevasCoord[0]==jugador.coordenadas.getX() && nuevasCoord[1]==jugador.coordenadas.getY()) {
                    condicion = true;
                    break;
                }
            }
        }while(condicion);

        return new Coordenadas(nuevasCoord[0], nuevasCoord[1]);
    }
}
