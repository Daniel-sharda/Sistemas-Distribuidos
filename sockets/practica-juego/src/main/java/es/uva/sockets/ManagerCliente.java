package es.uva.sockets;
import java.net.*; //Import mío ya que no funcionan los sockets
import java.io.*;
// import java.sql.SQLOutput;

public class ManagerCliente extends Thread {
    // Clase para que el encargado de cada cliente
    // Se ejecute en un hilo diferente

    private final Socket socket;
    private final ServidorJuego servidor;
    private final int idJugador;
    // Se pueden usar mas atributos ...
    private PrintWriter out;
    private BufferedReader in;

    public ManagerCliente(Socket socket, ServidorJuego servidor, int idJugador) {
        this.socket = socket;
        this.servidor = servidor;
        this.idJugador = idJugador;
        // Se pueden usar mas atributos ...
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {}

    }

    public void enviarMensaje(String message) {
        // TODO: enviar un mensaje. NOTA: a veces hace falta usar flush.

        out.println(message);
        out.flush();
    }

    @Override
    public void run() {
        // Mantener todos los procesos necesarios hasta el final
        // de la partida (alguien encuentra el tesoro)
        while (!servidor.estado.estaTerminado() && !socket.isClosed()) {
            procesarMensajeCliente();
        }
    }

    public void procesarMensajeCliente() {
        // TODO: leer el mensaje del cliente
        // y procesarlo usando interpretarMensaje
        // Si detectamos el final del socket
        // gestionar desconexion ...
        String mensaje;
        try {
            mensaje = in.readLine();
            if (mensaje != null) {
                interpretarMensaje(mensaje);
            }
        } catch (IOException e) {}
    }

    public void interpretarMensaje(String mensaje) {
        // TODO: Esta función debe realizar distintas
        // Acciones según el mensaje recibido
        // Manipulando el estado del servidor
        // Si el mensaje recibido no tiene el formato correcto
        // No ocurre nada
        if (mensaje != null) {
            String[] palabras = mensaje.split(" ");
            if(palabras[0].equals("MOVE"))   {
                System.out.println("moverse");
                switch (palabras[1]) {
                    case "UP":
                        servidor.estado.mover(idJugador, Direccion.UP);
                        servidor.broadcast("MOVE UP " + idJugador);
                        break;
                    case "DOWN":
                        servidor.estado.mover(idJugador, Direccion.DOWN);
                        servidor.broadcast("MOVE DOWN " + idJugador);
                        break;
                    case "LEFT":
                        servidor.estado.mover(idJugador, Direccion.LEFT);
                        servidor.broadcast("MOVE LEFT " + idJugador);
                        break;
                    case "RIGHT":
                        servidor.estado.mover(idJugador, Direccion.RIGHT);
                        servidor.broadcast("MOVE RIGHT " + idJugador);
                        break;
                }
            } else if (palabras[0].equals("DIG")) {
                servidor.estado.buscar(idJugador);
                if (servidor.estado.estaTerminado()) {
                    servidor.broadcast("DIG " + idJugador + " 1");
                } else {
                    servidor.broadcast("DIG " + idJugador + " 0");
                }
            }
        }
    }
}