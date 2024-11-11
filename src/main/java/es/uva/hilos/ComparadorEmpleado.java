package es.uva.hilos;

import java.util.Comparator;

public class ComparadorEmpleado implements Comparator<Empleado> {
    public int compare(Empleado empleado1, Empleado empleado2) {
        if(empleado1.getPrioridad() > empleado2.getPrioridad()) {
            return 1;
        } else if (empleado1.getPrioridad() < empleado2.getPrioridad()) {
            return -1;
        } else {
            return 0;
        }
    }
}
