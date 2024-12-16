package es.uva.sockets;

import java.io.BufferedReader;
import java.net.Socket;

import java.io.*;
import java.util.Scanner;



public class ClienteJuego {
    // La clase cliente tiene las siguientes responsabilidades
    // Unirse al juego conectandose al servidor
    // Mantener un estado de juego actualizado interpretando los
    // mensajes del servidor (y mostrar el estado)
    // Convertir input del jugador en un mensaje que enviar al servidor
    // NOTA: para simplificar el manejo de input podemos considerar
    // que el usario manda cada comando en una linea distinta
    // (aunque sea muy incomodo)

    public final Estado estado;
    // TODO: Faltarán atributos ...

    Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner teclado;


    public ClienteJuego(int size) {
        // [OPCIONAL] TODO: Extiende el protocolo de comunicacion para
        // que el servidor envie el tamaño del mapa tras la conexion
        // de manera que el estado no se instancie hasta entonces
        // y conocer este parametro a priori no sea necesario.
        estado = new Estado(size, false);
        teclado = new Scanner(System.in);
    }

    public void iniciar(String host, int puerto) {
        // Metodo que reune todo y mantiene lo necesario en un bucle
        conectar(host, puerto);
        Thread procesadorMensajesServidor = new Thread(() -> {
            while (!estado.estaTerminado()) {
                procesarMensajeServidor();
            }
        });
        Thread procesadorInput = new Thread(() -> {
            while (!estado.estaTerminado()) {
//                procesarInput();
            }
        });
        procesadorMensajesServidor.start();
        procesadorInput.start();
        try {
            procesadorInput.join();
            procesadorMensajesServidor.join();
        } catch (InterruptedException e) {}
        // Si acaban los hilos es que el juego terminó
        cerrarConexion();
    }

    public void cerrarConexion() {
        // TODO: cierra todos los recursos asociados a la conexion con el servidor
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {}
    }

    public void conectar(String host, int puerto) {
        // TODO: iniciar la conexion con el servidor
        // (Debe guardar la conexion en un atributo)
        try {
            socket = new Socket(host, puerto);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {}
    }

    public void procesarInput(char letra) {
        // TODO: Comprueba la entrada estandar y
        // se procesa mediante intrepretar input,
        // Se genera un mensaje que se envia al servidor


//        char input = (char) teclado.nextByte();
//        out.println(interpretarInput(input));
//        tambien está comentada la línea 49
        out.println((interpretarInput(letra)));
    }

    public void procesarMensajeServidor() {
        // TODO: Comprueba la conexion y obtiene un mensaje
        // que se procesa con interpretarMensaje
        // Al recibir la actualizacion del servidor podeis
        // Usar el metodo mostrar del estado
        // Para enseñarlo

        if(!socket.isClosed()) {
            try {
                String mensaje = in.readLine();
                if (mensaje != null) {
                    interpretarMensaje(mensaje);
                }
                estado.mostrar();
            } catch (IOException e) {}
        }
    }

    public String interpretarInput(char tecla) {
        // TODO: WASD para moverse, Q para buscar
        // Este metodo debe devolver el comando necesario
        // Que enviar al servidor
        switch (Character.toUpperCase(tecla)) {
            case 'W':
                return "MOVE UP";
            case 'A':
                return "MOVE LEFT";
            case 'S':
                return "MOVE DOWN";
            case 'D':
                return "MOVE RIGHT";
            case 'Q':
                return "DIG";
        }
        return "";
    }

    public void interpretarMensaje(String mensaje) {
        // TODO: interpretar los mensajes del servidor actualizando el estado
        String[] palabras = mensaje.split(" ");
        if(palabras[0].equals("PLAYER") && palabras[1].equals("JOIN")) {
            try {
                int id = Integer.parseInt(palabras[2]);
                int posX = Integer.parseInt(palabras[3]);
                int posY = Integer.parseInt(palabras[4]);
                estado.nuevoJugador(new Jugador(id, new Coordenadas(posX, posY)));
            } catch (NumberFormatException e) {}
        } else if (palabras[0].equals("MOVE")) {
            try {
                int id = Integer.parseInt(palabras[2]);
                switch (palabras[1]) {
                    case "UP":
                        estado.mover(id, Direccion.UP);
                        break;
                    case "DOWN":
                        estado.mover(id, Direccion.DOWN);
                        break;
                    case "LEFT":
                        estado.mover(id, Direccion.LEFT);
                        break;
                    case "RIGHT":
                        estado.mover(id, Direccion.RIGHT);
                        break;
                }
            } catch (NumberFormatException e) {}
        } else if (palabras[0].equals("DIG")) {
            try {
                int id = Integer.parseInt(palabras[1]);
                int success = Integer.parseInt(palabras[2]);
                if (success == 1) {
                    estado.terminar();
                } else if (success == 0) {
                    estado.buscar(id);
                }
            } catch (NumberFormatException e) {}
        }
    }
}
