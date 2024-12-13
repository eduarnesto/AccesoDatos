package creacionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreacionBD {

	public static void main(String[] args) {
		try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String conexionUrl = "jdbc:mysql://dns11036.phdns11.es:3306/ad2425_earnesto?" +
                                 "user=earnesto&password=12345";

            Connection con = DriverManager.getConnection(conexionUrl);
            System.out.println("Conexión exitosa: " + con.toString());
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

	}

}
