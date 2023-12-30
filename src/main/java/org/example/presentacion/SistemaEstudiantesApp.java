package org.example.presentacion;

import org.example.datos.EstudianteDAO;
import org.example.dominio.Estudiante;

import java.util.Scanner;

public class SistemaEstudiantesApp {
    public static void main(String[] args) {
        var scaner = new Scanner(System.in);
        var salir = false;
        var estudianteDao = new EstudianteDAO();

        while (!salir) {
            try {
                mostrarMenu();
                salir = opciones(scaner, estudianteDao);
            } catch (Exception e) {
                System.out.println("Ocurrio un error: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private static void mostrarMenu() {
        System.out.print("""
                *** Sistema de estudiantes ***
                1. Listar Estudiantes
                2. Buscar Estudiante
                3. Agregar Estudiante
                4. Modificar Estudiante
                5. Eliminar Estudiante
                6. Salir
                Ingrese la opcion deseada:
                """);
    }

    private static boolean opciones(Scanner scaner, EstudianteDAO estudianteDao) {
        var opcion = Integer.parseInt(scaner.nextLine());
        var salir = false;

        switch (opcion) {
            case 1 -> { //Listar estudiantes - READ
                System.out.println("Listado de estudiantes: ");
                var estudiantes = estudianteDao.listarEstudiantes();
                estudiantes.forEach(System.out::println);
            }
            case 2 -> { //Buscar estudiante por Id - READ
                System.out.println("Ingrese el Id del estudiante que desea buscar: ");
                var id = Integer.parseInt(scaner.nextLine());
                var estudianteId = new Estudiante(id);
                var buscarEstudiante = estudianteDao.buscarEstudiantePorId(estudianteId);
                if (buscarEstudiante) {
                    System.out.println("Se encontro al " + estudianteId);
                } else {
                    System.out.println("Estudiante no encontrado: " + estudianteId);
                }
            }
            case 3 -> { //Agregar estudiante - CREATE
                System.out.println("Agregar estudiante: ");
                System.out.print("Nombre: ");
                var nombre = scaner.nextLine();
                System.out.print("Apellido: ");
                var apellido = scaner.nextLine();
                System.out.print("Telefono: ");
                var telefono = scaner.nextLine();
                System.out.print("Email: ");
                var email = scaner.nextLine();
                //Creamos nuestro objeto estudiante
                var estudiante = new Estudiante(nombre, apellido, telefono, email);
                var agregado = estudianteDao.agregarEstudiante(estudiante);
                if (agregado) {
                    System.out.println("Se ha agregado correctamente al estudiante: " + estudiante);
                } else {
                    System.out.println("No se ha podido agregar correctamente al estudiante: " + estudiante);
                }
            }
            case 4 -> { //Modificar estudiante - UPDATE
                System.out.println("Modificar estudiante");
                System.out.println("Id del estudiante: ");
                var idEstudiante = Integer.parseInt(scaner.nextLine());
                System.out.print("Nombre: ");
                var nombre = scaner.nextLine();
                System.out.print("Apellido: ");
                var apellido = scaner.nextLine();
                System.out.print("Telefono: ");
                var telefono = scaner.nextLine();
                System.out.print("Email: ");
                var email = scaner.nextLine();
                //Creamos el objeto estudiante para modificar
                var estudiante = new Estudiante(idEstudiante, nombre, apellido, telefono, email);
                var modificado = estudianteDao.modificarEstudiante(estudiante);
                if (modificado) {
                    System.out.println("Se ha modificado correctamente al estudiante: " + estudiante);
                } else {
                    System.out.println("No se ha podido modificar al estudiante: " + estudiante);
                }
            }
            case 5 -> { //Eliminar estudiante - DELETE
                System.out.println("Eliminar estudiante");
                System.out.print("Proporciona el Id del estudiante que deseas eliminar: ");
                var idEstudiante = Integer.parseInt(scaner.nextLine());
                //Creamos el objeto Estudiante a eliminar
                var estudiante =  new Estudiante(idEstudiante);
                var eliminado = estudianteDao.eliminarEstudiante(estudiante);
                if (eliminado) {
                    System.out.println("Se ha eliminado correctamente al estudiante: " + estudiante);
                } else {
                    System.out.println("No se ha podido eliminar de manera correcta el estudiante: " + estudiante);
                }
            }
            case 6 -> {
                System.out.println("Hasta pronto!...");
                salir = true;
            }
            default -> {
                System.out.println("Ingresa una opcion valida...");
            }
        }
        return salir;
    }
}