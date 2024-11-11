package es.uva.hilos;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;


public class Centralita {
	// Desde a la centralita pueden llegar llamadas que despues
	// se asignan a los empleados disponibles según prioridad

	// TODO: Harán falta más atriubutos ...
	private final ArrayList<Empleado> empleados = new ArrayList<>();
	private final PriorityQueue<Empleado> colaEmpleados = new PriorityQueue<>(new ComparadorEmpleado()); // https://codegym.cc/groups/posts/java-priority-queue

	public void conEmpleado(Empleado e) {
		colaEmpleados.add(e);
	}

	public Runnable atenderLlamadaConEmpleado(Empleado empleado, Llamada llamada) {
		// TODO: Obligatorio devolver un Runnable. Se recomienda utilzar
		// funciones lambda.

		return () -> {
			try {
				empleado.atenderLlamada(llamada);
				colaEmpleados.add(empleado);
			} catch (InterruptedException e) {
			}
		};
	}


	public void atenderLlamada(Llamada llamada) {
		// TODO: Este método debería seleccionar un empleado disponible según prioridad
		// y correr en un nuevo hilo atenderLlamadaConEmpleado.
		// Este método no bloquea la ejecución si hay empleados disponibles para atender la llamada
		// si no hay empleados disponibles tendremos que esperar a que haya uno.
		Empleado empleado = colaEmpleados.poll();
		while (empleado == null) {
			empleado = colaEmpleados.poll();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
		Runnable hilo = atenderLlamadaConEmpleado(empleado, llamada);
		new Thread(hilo).start();
//
//		try {
//			empleado = colaEmpleados.remove();
//		} catch (Exception e) {
//			System.out.println("No quedanan empleados");
//		}
	}
}