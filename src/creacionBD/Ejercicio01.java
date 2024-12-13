package creacionBD;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Ejercicio01 {

	public static void main(String[] args) throws Exception {
		Connection con = null;
		ResultSet resultado;
		String conexionUrl = "jdbc:mysql://dns11036.phdns11.es:3306/ad2425_earnesto?" + "user=earnesto&password=12345";
		con = DriverManager.getConnection(conexionUrl);
		
		//creacionBaseDeDatos();
		
		//insertarPersonas(con);
		
		// Ejercicio01
		// resultado = listadoPersonasPorEdad(con);

		// Ejercicio02
		// resultado = listadoNombresApellidosPorApellido(con);

		// Ejercicio03
		// resultado = listadoNombresApellidos30Anyos(con);

		// Ejercicio04 No hay personas que cumplan los criterios
		// resultado = listadoNombresJOrdenadoPorApellidos(con);

		// Ejercicio05
		// resultado = listadoNombresCApellidosAPorEdad(con);

		// Ejercicio06
		//resultado = mediaEdad(con);

		// Ejercicio07
		// resultado = listadoApellidosOhOMa(con);

		// Ejercicio08
		// resultado = listadoEdadEntre24Y32(con);

		// Ejercicio09
		// resultado = listadoEdadMayor65(con);

		// Ejercicio10
		//alterarTabla(con);

		// Ejercicio11
		//asignarLaboral(con);
		
		//Ejercicio12
		resultado = listadoPersonasConRimaAsonanteConHector(con);

		// Paso 4: Cerramos los objetos que usamos para realizar la conexión y obtener
		// resultado
		resultado.close();
	}

	private static ResultSet listadoPersonasPorEdad(Connection con) throws SQLException {
		PreparedStatement s = con.prepareStatement("select id, nombre, apellidos, edad from Personas order by edad");

		ResultSet resultado = s.executeQuery();

		while (resultado.next()) {
			System.out.println("Id: " + resultado.getInt("id"));
			System.out.println("Nombre: " + resultado.getString("nombre"));
			System.out.println("Apellidos: " + resultado.getString("apellidos"));
			System.out.println("Edad: " + resultado.getInt("edad"));
			System.out.println();
		}
		return resultado;
	}

	private static ResultSet listadoNombresApellidosPorApellido(Connection con) throws SQLException {
		PreparedStatement s = con.prepareStatement("select nombre, apellidos from Personas order by apellidos");

		ResultSet resultado = s.executeQuery();

		while (resultado.next()) {
			System.out.println("Nombre: " + resultado.getString("nombre"));
			System.out.println("Apellidos: " + resultado.getString("apellidos"));
			System.out.println();
		}
		return resultado;
	}

	private static ResultSet listadoNombresApellidos30Anyos(Connection con) throws SQLException {
		PreparedStatement s = con.prepareStatement("select nombre, apellidos, edad from Personas where edad > 30");

		ResultSet resultado = s.executeQuery();

		while (resultado.next()) {
			System.out.println("Nombre: " + resultado.getString("nombre"));
			System.out.println("Apellidos: " + resultado.getString("apellidos"));
			System.out.println("Edad: " + resultado.getInt("edad"));
			System.out.println();
		}
		return resultado;
	}

	private static ResultSet listadoNombresJOrdenadoPorApellidos(Connection con) throws SQLException {
		PreparedStatement s = con
				.prepareStatement("select nombre, apellidos from Personas where nombre like 'J%' order by apellidos");

		ResultSet resultado = s.executeQuery();

		while (resultado.next()) {
			System.out.println("Nombre: " + resultado.getString("nombre"));
			System.out.println("Apellidos: " + resultado.getString("apellidos"));
			System.out.println();
		}
		return resultado;
	}

	private static ResultSet listadoNombresCApellidosAPorEdad(Connection con) throws SQLException {
		PreparedStatement s = con.prepareStatement(
				"select nombre, apellidos, edad from Personas where nombre like 'C%' and apellidos like 'A%' order by edad desc");

		ResultSet resultado = s.executeQuery();

		while (resultado.next()) {
			System.out.println("Nombre: " + resultado.getString("nombre"));
			System.out.println("Apellidos: " + resultado.getString("apellidos"));
			System.out.println("Edad: " + resultado.getInt("edad"));
			System.out.println();
		}
		return resultado;
	}

	private static ResultSet mediaEdad(Connection con) throws SQLException {
		PreparedStatement s = con.prepareStatement("select AVG(edad) AS 'media' from Personas");

		ResultSet resultado = s.executeQuery();

		while (resultado.next()) {
			System.out.println("Media: " + resultado.getString("media"));
			System.out.println();
		}
		return resultado;
	}

	private static ResultSet listadoApellidosOhOMa(Connection con) throws SQLException {
		PreparedStatement s = con.prepareStatement(
				"select apellidos from Personas where apellidos like '%oh%' or apellidos like '%ma%'");

		ResultSet resultado = s.executeQuery();

		while (resultado.next()) {
			System.out.println("Apellidos: " + resultado.getString("apellidos"));
			System.out.println();
		}
		return resultado;
	}

	private static ResultSet listadoEdadEntre24Y32(Connection con) throws SQLException {
		PreparedStatement s = con.prepareStatement("select * from Personas where edad between 24 and 32");

		ResultSet resultado = s.executeQuery();

		while (resultado.next()) {
			System.out.println("Id: " + resultado.getInt("id"));
			System.out.println("Nombre: " + resultado.getString("nombre"));
			System.out.println("Apellidos: " + resultado.getString("apellidos"));
			System.out.println("Edad: " + resultado.getInt("edad"));
			System.out.println();
		}

		return resultado;
	}

	private static ResultSet listadoEdadMayor65(Connection con) throws SQLException {
		PreparedStatement s = con.prepareStatement("select * from Personas where edad > 65");

		ResultSet resultado = s.executeQuery();

		while (resultado.next()) {
			System.out.println("Id: " + resultado.getInt("id"));
			System.out.println("Nombre: " + resultado.getString("nombre"));
			System.out.println("Apellidos: " + resultado.getString("apellidos"));
			System.out.println("Edad: " + resultado.getInt("edad"));
			System.out.println();
		}

		return resultado;
	}

	private static void alterarTabla(Connection con) throws SQLException {
		Statement s = con.createStatement();

		s.executeUpdate(
				"ALTER TABLE Personas ADD COLUMN laboral ENUM('estudiante', 'ocupado', 'parado', 'jubilado') NOT NULL;");
	}

	private static void asignarLaboral(Connection con) throws SQLException {
		Statement s = con.createStatement();

		s.executeUpdate("Update Personas set laboral = 'ocupado'");

		s.executeUpdate("UPDATE Personas\r\n" + "SET laboral = CASE\r\n" + "    WHEN edad < 18 THEN 'estudiante'\r\n"
				+ "    WHEN edad > 65 THEN 'jubilado'\r\n"
				+ "    WHEN edad % 2 = 1 and laboral = 'ocupado' THEN 'Parado'\r\n" + "    ELSE 'ocupado'\r\n" + "END");
	}

	//Busca las personas cuyo nombre rimen de manera asonante con Hector ❤️
	private static ResultSet listadoPersonasConRimaAsonanteConHector(Connection con) throws SQLException {
		PreparedStatement s = con.prepareStatement("select * from Personas where nombre like '%e_o_' or nombre like '%e__o_' or nombre like '%e_o' or nombre like '%e__o'");

		ResultSet resultado = s.executeQuery();

		while (resultado.next()) {
			System.out.println("Id: " + resultado.getInt("id"));
			System.out.println("Nombre: " + resultado.getString("nombre"));
			System.out.println("Apellidos: " + resultado.getString("apellidos"));
			System.out.println("Edad: " + resultado.getInt("edad"));
			System.out.println();
		}

		return resultado;
	}
	
	public static void insertarPersonas(Connection con) throws FileNotFoundException, SQLException {
		File file = new File("C:\\Users\\earnesto\\CarpetaPersonal\\ConexionBD\\src\\creacionBD\\Personas.txt");
		Scanner sc = new Scanner(file);
		Statement s = con.createStatement();
		while (sc.hasNextLine()) {
			s.executeUpdate(sc.nextLine());
		}
		sc.close();
	}

	public static void creacionBaseDeDatos() throws Exception {
		Connection con = null;
		Statement statement = null;
		String conexionUrl = "jdbc:mysql://dns11036.phdns11.es:3306/ad2425_earnesto?" + "user=earnesto&password=12345";
		String myTableName = "CREATE TABLE Personas (" + "id INT," + "nombre VARCHAR(20)," + "apellidos VARCHAR(20),"
				+ "edad INT," + "PRIMARY KEY (id))";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(conexionUrl);
			statement = con.createStatement();
			// This line has the issue
			statement.executeUpdate(myTableName);
			System.out.println("Table Created");
		} catch (SQLException e) {
			System.out.println("An error has occured on Table Creation");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("An Mysql drivers were not found");
		}
	}
}