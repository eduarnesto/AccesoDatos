package ejercicio31;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Ejercicio31 {

	public static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws Exception {
		// Variable para guardar la opcion elegida por el usuario
		int opcPrincipal;
		int opcTablas = 4;
		Connection con = null;

		menuPrincipal();
		opcPrincipal = sc.nextInt();

		while (opcPrincipal != 0) {
			if (opcPrincipal != 1) {
				do {
					menuTablas();
					opcTablas = sc.nextInt();
					sc.nextLine();
				} while (opcTablas < 1 || opcTablas > 4);
			}

			switch (opcPrincipal) {
			case 1: {
				con = conectar();
				break;
			}
			case 2: {
				if (creacionTablas(con, opcTablas)) {
					System.out.println("Se han podido crear la tabla");
				} else {
					System.out.println("No se ha podido crear la tabla");
				}
				break;
			}
			case 3: {
				if (insertar(con, opcTablas)) {
					System.out.println("Se ha insertado los datos correctamente");
				} else {
					System.out.println("No se han podido insertar los datos");
				}
				break;
			}
			case 4: {
				listado(con, opcTablas);
				break;
			}
			case 5: {
				modificarDatos(con, opcTablas);
				break;
			}
			case 6: {
				con.prepareStatement("Begin transaction").executeQuery();
				borrarDatos(con, opcTablas);
				
				if (confirmarBorrado()) {
					con.prepareStatement("Commit").executeQuery();
				} else {
					con.prepareStatement("Rollback").executeQuery();
				}
				
				break;
			}
			case 7: {
				borrarTablas(con, opcTablas);
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + opcPrincipal);
			}

			menuPrincipal();
			opcPrincipal = sc.nextInt();
		}

	}

	private static Connection conectar() throws SQLException {
		String conexionUrl = "jdbc:mysql://dns11036.phdns11.es:3306/ad2425_earnesto?" + "user=earnesto&password=12345";
		return DriverManager.getConnection(conexionUrl);
	}

	public static void menuPrincipal() {
		System.out.println("_______________");
		System.out.println("Elige una opcion:");
		System.out.println("1.- Conectar");
		System.out.println("2.- Crear tabla");
		System.out.println("3.- Insertar");
		System.out.println("4.- Listar");
		System.out.println("5.- Modificar");
		System.out.println("6.- Borrar");
		System.out.println("7.- Eliminar tablas");
		System.out.println("0.- Salir");
		System.out.println("_______________");
	}

	public static void menuTablas() {
		System.out.println("_______________");
		System.out.println("Elige una tabla:");
		System.out.println("1.- Usuarios");
		System.out.println("2.- Posts");
		System.out.println("3.- Likes");
		System.out.println("4.- Todas");
		System.out.println("_______________");
	}

	public static void menuUsuario() {
		System.out.println("_______________");
		System.out.println("1.- Id");
		System.out.println("2.- Nombre");
		System.out.println("3.- Apellidos");
		System.out.println("4.- Email");
		System.out.println("0.- Ninguna");
		System.out.println("_______________");
	}

	public static void menuPost() {
		System.out.println("_______________");
		System.out.println("1.- Id");
		System.out.println("2.- IdUsuario");
		System.out.println("3.- FechaCreacion");
		System.out.println("4.- FechaActualizacion");
		System.out.println("0.- Ninguna");
		System.out.println("_______________");
	}

	public static void menuLikes() {
		System.out.println("_______________");
		System.out.println("1.- Id");
		System.out.println("2.- IdUsuario");
		System.out.println("3.- IdPost");
		System.out.println("0.- Ninguna");
		System.out.println("_______________");
	}

	public static boolean creacionTablas(Connection con, int opc) {
		boolean creado = false;
		Statement statement = null;
		String tablaUsuarios = "Create table if not exists Usuarios (\r\n" + "	idUsuarios INT Primary Key,\r\n"
				+ "    Nombre VARCHAR(45),\r\n" + "    Apellidos VARCHAR(45),\r\n" + "    Username VARCHAR(12),\r\n"
				+ "    Password VARCHAR(128),\r\n" + "    email VARCHAR(50)\r\n" + ")";
		String tablaPosts = "Create table if not exists Posts (\r\n" + "	idPosts INT Primary Key,\r\n"
				+ "    idUsuarios INT,\r\n" + "    created_at DATE,\r\n" + "    updated_at DATE,\r\n"
				+ "    foreign key (idUsuarios) references Usuarios(idUsuarios)\r\n" + ")";
		String tablaLikes = "Create table if not exists Likes (\r\n" + "	idLikes INT Primary Key,\r\n"
				+ "    idUsuarios INT,\r\n" + "    idPosts INT,\r\n"
				+ "    foreign key (idUsuarios) references Usuarios(idUsuarios),\r\n"
				+ "    foreign key (idPosts) references Posts(idPosts)\r\n" + ")";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			statement = con.createStatement();

			switch (opc) {
			case 1: {
				statement.executeUpdate(tablaUsuarios);
				break;
			}
			case 2: {
				statement.executeUpdate(tablaPosts);
				break;
			}
			case 3: {
				statement.executeUpdate(tablaLikes);
				break;
			}
			case 4: {
				statement.executeUpdate(tablaUsuarios);
				statement.executeUpdate(tablaPosts);
				statement.executeUpdate(tablaLikes);
				break;
			}
			}
			creado = true;
		} catch (SQLException e) {

		} catch (ClassNotFoundException e) {

		}

		return creado;
	}

	public static boolean insertar(Connection con, int opc) {
		boolean insertado = false;

		switch (opc) {
		case 1: {
			insertado = insertarUsuario(con);
			break;
		}
		case 2: {
			insertado = insertarPost(con);
			break;
		}
		case 3: {
			insertado = insertarLikes(con);
			break;
		}
		default:

		}
		return insertado;
	}

	public static int preguntarId() {
		System.out.println("Introduce el id");
		int id = sc.nextInt();
		sc.nextLine();
		return id;
	}

	public static String preguntarNombre() {
		System.out.println("Introduce el nombre");
		return sc.nextLine();
	}

	public static String preguntarApellido() {
		System.out.println("Introduce el apellido");
		return sc.nextLine();
	}

	public static String preguntarUsername() {
		System.out.println("Introduce el username");
		return sc.nextLine();
	}

	public static String preguntarPassword() {
		System.out.println("Introduce la password");
		return sc.nextLine();
	}

	public static String preguntarEmail() {
		System.out.println("Introduce el email");
		return sc.nextLine();
	}

	public static int preguntarIdUsuario() {
		System.out.println("Introduce el id del usuario");
		int id = sc.nextInt();
		sc.nextLine();
		return id;
	}

	public static String preguntarFechaCreacion() {
		System.out.println("Introduce la fecha de creacion");
		return sc.nextLine();
	}

	public static String preguntarFechaActualizacion() {
		System.out.println("Introduce la fecha de actualización");
		return sc.nextLine();
	}

	public static int preguntarIdPost() {
		System.out.println("Introduce el id del post");
		int id = sc.nextInt();
		sc.nextLine();
		return id;
	}

	public static boolean insertarUsuario(Connection con) {
		boolean insertado = false;

		Statement statement;

		String insertarUsuario = "Insert into Usuarios Values(" + preguntarId() + ", " + preguntarNombre() + ", "
				+ preguntarApellido() + ", " + preguntarUsername() + ", " + preguntarPassword() + ", "
				+ preguntarEmail() + ")";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			statement = con.createStatement();

			statement.executeUpdate(insertarUsuario);

			insertado = true;
		} catch (SQLException e) {

		} catch (ClassNotFoundException e) {

		}

		return insertado;
	}

	public static boolean insertarPost(Connection con) {
		boolean insertado = false;

		Statement statement;

		String insertarPost = "Insert into Posts Values" + preguntarId() + ", " + preguntarIdUsuario() + ", "
				+ preguntarFechaCreacion() + ", " + preguntarFechaActualizacion() + ")";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			statement = con.createStatement();

			statement.executeUpdate(insertarPost);

			insertado = true;
		} catch (SQLException e) {

		} catch (ClassNotFoundException e) {

		}

		return insertado;
	}

	public static boolean insertarLikes(Connection con) {
		boolean insertado = false;

		Statement statement;

		String insertarLikes = "Insert into Posts (Values" + preguntarId() + ", " + preguntarIdUsuario() + ", "
				+ preguntarIdPost() + ")";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			statement = con.createStatement();

			statement.executeUpdate(insertarLikes);

			insertado = true;
		} catch (SQLException e) {

		} catch (ClassNotFoundException e) {

		}

		return insertado;
	}

	public static void listado(Connection con, int opc) throws SQLException {
		String query = "select * ";
		
		switch (opc) {
		case 1: {
			query += listadoUsuarios(con);
			break;
		}
		case 2: {
			query += listadoPosts(con);
			break;
		}
		case 3: {
			query += listadoLikes(con);
			break;
		}
		case 4: {
			query += listadoUsuarios(con);
			listado(con, 2);
			listado(con, 3);
			break;
		}
		
		default:
			throw new IllegalArgumentException("Unexpected value: " + opc);
		}

		PreparedStatement s = con.prepareStatement(query);

		ResultSet resultado = s.executeQuery();

		// Obtener los metadatos de la consulta (información sobre las columnas)
        ResultSetMetaData metaData = resultado.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Imprimir los nombres de las columnas
        for (int i = 1; i <= columnCount; i++) {
            System.out.printf("%-20s", metaData.getColumnName(i)); // Imprime el nombre de la columna
        }
        System.out.println(); // Nueva línea después de los nombres de las columnas
		
		while (resultado.next()) {
			for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-20s", resultado.getString(i)); // Imprimir el valor de la columna
            }
            System.out.println(); // Nueva línea después de cada fila
		}
		System.out.println();

	}

	public static String listadoUsuarios(Connection con) throws SQLException {
		int opc;
		String where = "from Usuarios ";
		System.out.println("Elige una opcion para filtrar los usuarios");
		menuUsuario();
		opc = sc.nextInt();
		
		sc.nextLine();

		switch (opc) {
		case 1: {
			where += "where idUsuarios = ";
			break;
		}
		case 2: {
			where += "where nombre = ";
			break;
		}
		case 3: {
			where += "where apellidos = ";
			break;
		}
		case 4: {
			where += "where email = ";
			break;
		}
		default:
			where = "from Usuarios ";
			break;
		}

		if (!where.equals("from Usuarios ")) {
			System.out.println("Introduzca el valor del parámetro");
			where += sc.nextLine();
		}
		
		return where;
	}
	
	public static String listadoPosts(Connection con) throws SQLException {
		int opc;
		String where = "from Posts ";
		System.out.println("Elige una opcion para filtrar los posts");
		menuPost();
		opc = sc.nextInt();
		
		sc.nextLine();

		switch (opc) {
		case 1: {
			where += "where idPosts = ";
			break;
		}
		case 2: {
			where += "where idUsuarios = ";
			break;
		}
		case 3: {
			where += "where created_at = ";
			break;
		}
		case 4: {
			where += "where updated_at = ";
			break;
		}
		default:
			where += "";
		}

		if (!where.equals("from Posts ")) {
			System.out.println("Introduzca el valor del parámetro");
			where += sc.nextLine();
		}

		return where;
	}
	
	public static String listadoLikes(Connection con) throws SQLException {
		int opc;
		String where = "from Likes ";
		System.out.println("Elige una opcion para filtrar los likes");
		menuLikes();
		opc = sc.nextInt();
		
		sc.nextLine();

		switch (opc) {
		case 1: {
			where += "where idLikes = ";
			break;
		}
		case 2: {
			where += "where idUsuarios = ";
			break;
		}
		case 3: {
			where += "where idPosts = ";
			break;
		}
		default:
			where += "";
		}

		if (!where.equals("from Likes ")) {
			System.out.println("Introduzca el valor del parámetro");
			where += sc.nextLine();
		}

		return where;
	}

	public static void borrarDatos(Connection con, int opc) throws SQLException {
		String query = "Delete ";
		
		switch (opc) {
		case 1: {
			query += listadoUsuarios(con);
			break;
		}
		case 2: {
			query += listadoPosts(con);
			break;
		}
		case 3: {
			query += listadoLikes(con);
			break;
		}
		case 4: {
			query += listadoUsuarios(con);
			listado(con, 2);
			listado(con, 3);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + opc);
		}

		PreparedStatement s = con.prepareStatement(query);

		s.executeQuery();
	}

	public static boolean confirmarBorrado() {
		boolean confirmacion;
		String respuesta="";
		System.out.println("¿Confirmar los cambios?");
		respuesta = sc.nextLine();
		
		if (respuesta.equalsIgnoreCase("si")) {
			confirmacion = true;
		} else {
			confirmacion = false;
		}
		
		return confirmacion;
	}
	
	public static void borrarTablas(Connection con, int opc) throws SQLException {
		String query = "Drop table ";
		
		switch (opc) {
		case 1: {
			query += "Usuarios";
			break;
		}
		case 2: {
			query += "Posts";
			break;
		}
		case 3: {
			query += "Likes";
			break;
		}
		case 4: {
			query += (con);
			listado(con, 2);
			listado(con, 3);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + opc);
		}

		PreparedStatement s = con.prepareStatement(query);

		s.executeQuery();
	}
	
	public static void modificarDatos(Connection con, int opc) throws SQLException {
		String query;
		String tabla;
		String columna;
		String valor;
		String condicion;
		String valorCondicion;
		
		switch (opc) {
		case 1: {
			tabla = "Usuarios";
			
			System.out.println("Selecciona la columna que quieres modificar");
			menuUsuario();
			columna = datosUsuarios(con, sc.nextInt());
			
			System.out.println("Escriba el nuevo valor");
			valor = sc.nextLine();
			
			System.out.println("Selecciona la columna por la que filtrar");
			menuUsuario();
			condicion = datosUsuarios(con, sc.nextInt());
			
			System.out.println("Escriba el valor para filtrar");
			valorCondicion = sc.nextLine();

			break;
		}
		case 2: {
			tabla = "Posts";
			
			System.out.println("Selecciona la columna que quieres modificar");
			menuPost();
			columna = datosPosts(con, sc.nextInt());
			
			System.out.println("Escriba el nuevo valor");
			valor = sc.nextLine();
			
			System.out.println("Selecciona la columna por la que filtrar");
			menuPost();
			condicion = datosPosts(con, sc.nextInt());
			
			System.out.println("Escriba el valor para filtrar");
			valorCondicion = sc.nextLine();

			break;
		}
		case 3: {
			tabla = "Likes";
			
			System.out.println("Selecciona la columna que quieres modificar");
			menuLikes();
			columna = datosLikes(con, sc.nextInt());
			
			System.out.println("Escriba el nuevo valor");
			valor = sc.nextLine();
			
			System.out.println("Selecciona la columna por la que filtrar");
			menuLikes();
			condicion = datosLikes(con, sc.nextInt());
			
			System.out.println("Escriba el valor para filtrar");
			valorCondicion = sc.nextLine();

			break;
		}
		case 4: {
			tabla = "Usuarios";
			
			System.out.println("Selecciona la columna que quieres modificar");
			menuUsuario();
			columna = datosUsuarios(con, sc.nextInt());
			
			System.out.println("Escriba el nuevo valor");
			valor = sc.nextLine();
			
			System.out.println("Selecciona la columna por la que filtrar");
			menuUsuario();
			condicion = datosUsuarios(con, sc.nextInt());
			
			System.out.println("Escriba el valor para filtrar");
			valorCondicion = sc.nextLine();

			listado(con, 2);
			listado(con, 3);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + opc);
		}

		query = "Update " + tabla + " Set " + columna + " = " + valor + " where " + condicion + " = " + valorCondicion;
		
		PreparedStatement s = con.prepareStatement(query);

		s.executeQuery();
	}
	
	public static String datosUsuarios(Connection con, int opc) throws SQLException {
		String dato;

		switch (opc) {
		case 1: {
			dato = "idUsuarios";
			break;
		}
		case 2: {
			dato = "nombre";
			break;
		}
		case 3: {
			dato = "apellidos";
			break;
		}
		case 4: {
			dato = "email";
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + opc);
		}

		return dato;
	}
	
	public static String datosPosts(Connection con, int opc) throws SQLException {
		String dato;
		
		switch (opc) {
		case 1: {
			dato = "idPosts";
			break;
		}
		case 2: {
			dato = "idUsuarios";
			break;
		}
		case 3: {
			dato = "created_at";
			break;
		}
		case 4: {
			dato = "updated_at";
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + opc);
		}

		return dato;
	}
	
	public static String datosLikes(Connection con, int opc) throws SQLException {
		String dato;

		switch (opc) {
		case 1: {
			dato = "idLikes";
			break;
		}
		case 2: {
			dato = "idUsuarios";
			break;
		}
		case 3: {
			dato = "idPosts";
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + opc);
		}

		return dato;
	}
}
