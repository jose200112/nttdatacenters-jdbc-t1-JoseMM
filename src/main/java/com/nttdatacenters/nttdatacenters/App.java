package com.nttdatacenters.nttdatacenters;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Clase App
 * 
 * @author Jose
 *
 */
public class App {

	/** log **/
	private static Logger log = LoggerFactory.getLogger(App.class);

	/**
	 * Metodo main
	 * @param args Main
	 */
	public static void main(String[] args)  {
		stablishMDBConnection();
	}
	
	

	/**
	 * Metodo privado que conecta con la BBDD y realiza una consulta
	 */
	private static void stablishMDBConnection() {
		Connection dbConnection = null;
		Statement sentence = null;

		try {

			// Se establece el driver de conexión a BBDD.
			Class.forName("com.mysql.cj.jdbc.Driver");

			
			// Apertura de conexión con BBDD (URL / Usuario / Contraseña)
			dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nttdata_jdbc_ex", "root","jose2021");

			
			// Realización de consulta.
			sentence = dbConnection.createStatement();
			final String query = "select name AS teamName, stadium AS teamStadium, city AS teamCity, division AS teamDivision, num_national_trophies AS nationalTrophiesNumber from nttdata_mysql_soccer_team order by division";
			final ResultSet queryResult = sentence.executeQuery(query);

			
			// Iteración de resultados.
			StringBuilder teamInfo = new StringBuilder();
			queryIteration(queryResult, teamInfo);
			log.info("Resultado de la consulta: \n{}", teamInfo);

			
		} catch (ClassNotFoundException | SQLException | NullPointerException e) {
			e.printStackTrace();
		} finally {		
			connectionClose(dbConnection, sentence);
		}

	}
	
	
	
	
	/**
	 * Metodo privado que cierra la conexion y la consulta
	 * @param dbConnection conexion
	 * @param sentence sentencia
	 * @throws SQLException
	 */
	private static void connectionClose(Connection dbConnection, Statement sentence) {
		try {
			
			if (sentence != null) {
				// Cierre de la consulta.
				sentence.close();
			}
			
		} catch (NullPointerException | SQLException e) {
			log.error("Ha ocurrido un error ", e);
		
		} finally {
			
			if (dbConnection != null) {		
				try {
					// Cierre de conexión con BBDD.
					dbConnection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	
	
	
	
	
	/**
	 * Metodo privado que itera los resultados
	 * @param queryResult - resultado de consulta
	 * @param teamInfo - StringBuilder
	 * @throws SQLException
	 */
	private static void queryIteration(final ResultSet queryResult, StringBuilder teamInfo) throws SQLException {
		while (queryResult.next()) {

			teamInfo.append("Equipo: ");
			teamInfo.append(queryResult.getString("teamName"));

			teamInfo.append(" | Estadio: ");
			teamInfo.append(queryResult.getString("teamStadium"));

			teamInfo.append(" | Ciudad: ");
			teamInfo.append(queryResult.getString("teamCity"));

			teamInfo.append(" | Division: ");
			teamInfo.append(queryResult.getString("teamDivision"));

			teamInfo.append(" | Numero de trofeos: ");
			teamInfo.append(queryResult.getLong("nationalTrophiesNumber"));

			teamInfo.append("\n");

		}
	}
}
