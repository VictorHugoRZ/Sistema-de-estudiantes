package org.example.datos;

import org.example.dominio.Estudiante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
//Podemos importar de manera estatica los metodos que sean necesarios para no acceder a ellos
//de la manera tradicional, es decir: Conexion.getConnection();
import static org.example.conexion.Conexion.getConnection;

//DAO - Data Access Object (Patron de diseño)
public class EstudianteDAO {
    //CRUD - CREATE - READ - UPDATE - DELETE
    //------------------------------------------------------------------------------------------------------------------

    //Funcion para listar estudiantes desde la base de datos, esta retorna una Lista de tipo Estudiante
    public List<Estudiante> listarEstudiantes() { //READ
        //Definimos nuestra lista de estudiantes
        List<Estudiante> estudiantes = new ArrayList<>();

        //Definimos algunos objetos necesarios para poder comunicarnos con la base de datos

        //Nos ayuda a preparar la sentencia SQL para interactual con la base de datos
        //Con esto queremos decir que sera un objeto de tipo PreparedStatement
        PreparedStatement ps;
        //Es un objeto que nos permite almacenar el resultado obtenido de la base de datos
        ResultSet rs;

        //Creamos nuestro objeto de tipo conexion
        Connection connection = getConnection();

        //Definimos la consulta que vamos a realizar
        String sql = "SELECT * FROM estudiantes_db.estudiante ORDER BY id_estudiante";

        //Hacemos uso de un bloque Try Catch para poder manejar excepciones
        try {
            //El objeto de tipo PreparedStatement usa la conexion para preparar la sentencia SQL de la linea 34
            ps = connection.prepareStatement(sql);
            //El objeto de tipo ResultSet usa la sentencia SQL anteriormente preparada para ejecutar la consulta
            rs = ps.executeQuery();

            //Usamos un ciclo While para iterar los posibles resultados e ir seteandolos con variables de tipo Estudiante
            while (rs.next()) { //Mientras exista contenido en el objeto ResultSet seguiremos iterando
                //Definimos un estudiante con el constructor vacio
                var estudiante = new Estudiante();

                //Seteamos los valores del estudiante obteniendo los datos de las columnas con los metodos getInt y
                //getString del objeto ResultSet
                estudiante.setIdEstudiante(rs.getInt("id_estudiante"));
                estudiante.setNombre(rs.getString("nombre"));
                estudiante.setApellido(rs.getString("apellido"));
                estudiante.setTelefono(rs.getString("telefono"));
                estudiante.setEmail(rs.getString("email"));

                //Agregamos este nuevo estudiante a nuestra lista de estudiantes (linea 20)
                estudiantes.add(estudiante);
            }
        } catch (Exception e) {
            System.out.println("Ocurrio un error al seleccionar datos: " + e.getMessage());
        }
        //Hacemos uso del bloque Finally para cerrar la conexion con nuestra base de datos
        finally {
            //Intentamos realizar el cierre de la conexion o de lo contrario, manejar sus excepciones
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Ocurrio un error al cerrar la conexion: " + e.getMessage());
            }
        }
        //Retornamos la lista de Estudiantes que ahora contiene estudiantes tras ejecutar la consulta y guardar
        //esos valores en la misma
        return estudiantes;
    }

    //------------------------------------------------------------------------------------------------------------------

    //Funcion para buscar estudiantes por ID
    public boolean buscarEstudiantePorId(Estudiante estudiante) { //READ
        //Preparamos nuestros objetos para guardar y ejecutar la sentencia SQL
        PreparedStatement ps;
        ResultSet rs;
        Connection connection = getConnection();
        //Definimos la sentencia SQL
        String sql = "SELECT * FROM estudiantes_db.estudiante WHERE id_estudiante = ?";
        try {

            ps = connection.prepareStatement(sql); //Preparamos la sentencia
            //Asignamos el valor del Id que recibimos por parametro en la funcion asignandolo al Query de la linea 84
            //De esta manera sustituimos las incognitas (?) con los datos conforme los vamos seleccionando con el
            //numero de la izquierda
            ps.setInt(1, estudiante.getIdEstudiante());
            rs = ps.executeQuery(); //Ejecutamos la consulta

            if(rs.next()) { //Revisamos si existe contenido al ejecutar la consulta
                //Seteamos los atributos extrayendo los datos que nos da la base de datos
                estudiante.setNombre(rs.getString("nombre"));
                estudiante.setApellido(rs.getString("apellido"));
                estudiante.setTelefono(rs.getString("telefono"));
                estudiante.setEmail(rs.getString("email"));
                //Cortamos la ejecucion retornando True
                return true;
            }
        } catch (Exception e) { //Manejo de excepciones
            System.out.println("Ocurrio un error al buscar por Id: " + e.getMessage());
        }
        finally { //Finalmente cerramos la conexion que generamos con la base de datos
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Ocurrio un error al cerrar la conexion: " + e.getMessage());
            }
        }
        return false;
    }

    //------------------------------------------------------------------------------------------------------------------

    public boolean agregarEstudiante(Estudiante estudiante) { //CREATE
        //Preparamos el objeto para ejecutar la sentancia en la base de datos
        PreparedStatement ps;
        //Abrimos la conexion con la base de datos
        Connection connection = getConnection();
        //Definimos nuestra Query
        String sql = "INSERT INTO estudiantes_db.estudiante(nombre, apellido, telefono, email) " +
                "VALUES(?, ?, ?, ?)";
        try {
            //Preparamos la sentancia con la conexion
            ps = connection.prepareStatement(sql);
            //Sustituimos los valores de las incognitas por orden numerico obteniendo los datos del objeto que le
            //pasamos como parametro a nuestra funcion
            ps.setString(1, estudiante.getNombre());
            ps.setString(2, estudiante.getApellido());
            ps.setString(3, estudiante.getTelefono());
            ps.setString(4, estudiante.getEmail());
            //Para ejecutar sentencias distintas a simples consultas, en este caso insertar valores en una tabla,
            //solo ejecutaremos la sentencia con el objeto PreparedStatement y no con ResultSet
            ps.execute();
            //Retornamos verdadero si se ha realizado correctamente la funcion
            return true;
        } catch (Exception e) { //Manejo de excepciones
            System.out.println("Ocurrio un problema: " + e.getMessage());
        }
        finally { //Finalmente cerramos la conexion con la base de datos
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Ocurrio un error al cerrar la conexion: " + e.getMessage());
            }
        }
        return false;
    }

    //------------------------------------------------------------------------------------------------------------------

    public boolean modificarEstudiante(Estudiante estudiante) { //UPDATE
        //Preparamos el objeto para ejecutar la sentancia en la base de datos
        PreparedStatement ps;
        //Abrimos la conexion con la base de datos
        Connection connection = getConnection();
        //Definimos nuestra Query
        String sql = "UPDATE estudiantes_db.estudiante SET nombre=?, apellido=?, telefono=?, email=? WHERE id_estudiante = ?";

        try {
            //Preparamos la sentencia
            ps = connection.prepareStatement(sql);
            //Sustituimos valores de incognitas con los valores del objeto de tipo Estudiante que recibimos por parametro
            ps.setString(1, estudiante.getNombre());
            ps.setString(2, estudiante.getApellido());
            ps.setString(3, estudiante.getTelefono());
            ps.setString(4, estudiante.getEmail());
            ps.setInt(5, estudiante.getIdEstudiante());
            //Ejecutamos la sentencia SQL con los datos correctos
            ps.execute();
            return true;
        } catch (Exception e) { //Manejo de excepciones
            System.out.println("Ocurrio un error: " + e.getMessage());
        }
        finally { //Finalmente cerramos la conexion con la base de datos
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Ocurrio un error al cerrar la conexion con la base de datos: " + e.getMessage());
            }
        }
        return false;
    }

    //------------------------------------------------------------------------------------------------------------------

    public boolean eliminarEstudiante(Estudiante estudiante) { //DELETE
        //Preparamos el objeto para ejecutar la sentancia en la base de datos
        PreparedStatement ps;
        //Abrimos la conexion con la base de datos
        Connection connection = getConnection();
        //Definimos nuestra Query
        String sql = "DELETE FROM estudiantes_db.estudiante WHERE id_estudiante = ?";

        try {
            //Preparamos la sentencia
            ps = connection.prepareStatement(sql);
            //Sustituimos las incognitas por los valores del objeto de tipo Estudiante que nos pasan por parametro
            ps.setInt(1, estudiante.getIdEstudiante());
            //Ejecutamos la sentencia
            ps.execute();
            return true;
        } catch (Exception e) { //Manejo de excepciones
            System.out.println("Ocurrio un problema al eliminar el estudiante: " + e.getMessage());
        }
        finally { //Finalmente cerramos la conexion con la base de datos
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Ocurrio un problema al cerrar la conexion con la base de datos: " + e.getMessage());
            }
        }
        return false;
    }

    //------------------------------------------------------------------------------------------------------------------

    //Realizamos pruebas con los metodos que se crearon
    public static void main(String[] args) {
        //Creamos una instancia de nuestra clase EstudianteDAO() para poder llamar a los metodos que van a
        //interactuar con la base de datos
        var estudianteDAO = new EstudianteDAO();

        //Agregamos estudiantes - CREATE
        //Definimos a nuestro estudiante nuevo creando una instancia de la clase Estudiante con el constructor que
        //no requiere ID
        var nuevoEstudiante = new Estudiante("Leonardo", "Ramirez", "56 3699 8922", "leon@hotmail.com");
        //Agregamos al estudiante con el metodo agregarEstudiante()
        var estudianteAgregado = estudianteDAO.agregarEstudiante(nuevoEstudiante);
        if (estudianteAgregado) { //Si se agrego correctamente
            System.out.println("Estudiante agregado correctamente: " + nuevoEstudiante);
        } else { //Si ocurrio un error
            System.out.println("Error al agregar al estudiante: " + nuevoEstudiante);
        }

        //Modificamos algun registro existente - UPDATE
        var correccionEstudiante = new Estudiante(1, "Victor Hugo", "Ramirez", "55 1716 8649", "v.zamoravictor@gmail.com");
        var estudianteModificado = estudianteDAO.modificarEstudiante(correccionEstudiante);
        if (estudianteModificado) {
            System.out.println("Se ha modificado correctamente al estudiante: " + correccionEstudiante);
        } else {
            System.out.println("Ha ocurrido un error al modificar al estudiante: " + correccionEstudiante);
        }

        //Eliminamos algun registro(estudiante) existente - DELETE
        var estudianteAEliminar = new Estudiante(3);
        var estudianteEliminado = estudianteDAO.eliminarEstudiante(estudianteAEliminar);
        if(estudianteEliminado) {
            System.out.println("Se ha eliminado correctamente al estudiante: " + estudianteAEliminar);
        } else {
            System.out.println("No se ha podido eliminar al estudiante: " + estudianteAEliminar);
        }

        //Listamos los estudiantes desde la base de datos - READ
        System.out.println("Listado de estudiantes: ");
        //Definimos una lista de tipo Estudiante que tendra el valor de la llamada a la funcion listarEstudiantes()
        List<Estudiante> estudiantes = estudianteDAO.listarEstudiantes();
        //Mostramos por consola los resultados
        estudiantes.forEach(System.out::println);

        //Buscamos por Id - READ
        var estudiante1 = new Estudiante(2);
        System.out.println("Estudiante antes de la busqueda: " + estudiante1);

        var encontrado = estudianteDAO.buscarEstudiantePorId(estudiante1);
        if(encontrado) {
            System.out.println("Estudiante encontrado: " + estudiante1);
        } else {
            System.out.println("No se encontro al estudiante: " + estudiante1.getIdEstudiante());
        }
    }
}
