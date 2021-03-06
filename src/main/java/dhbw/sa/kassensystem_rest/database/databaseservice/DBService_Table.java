package dhbw.sa.kassensystem_rest.database.databaseservice;

import dhbw.sa.kassensystem_rest.database.entity.Table;
import dhbw.sa.kassensystem_rest.exceptions.MySQLServerConnectionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Klasse mit Methoden zum Einsehen und Bearbeiten der Table-Tabelle.
 *
 * @author Marvin Mai
 */
class DBService_Table
{
	static void addTable(Connection connection, Table table)
	{
		try {
			String query =  "INSERT INTO " + DatabaseProperties.getDatabase() + ".tables(tableID, name, seats, available) " +
					"VALUES(DEFAULT, ?, ?, ?)";
			PreparedStatement pst = connection.prepareStatement(query);

			pst.setString(1, table.getName());
			pst.setInt(2, table.getSeats());
			pst.setBoolean(3, table.isAvailable());
			pst.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static ArrayList<Table> getAllTables(Connection connection, boolean onlyAvailable)
	{
		ArrayList<Table> tables = new ArrayList<>();

		try {
			String query = "SELECT tableID, name, seats, available " +
					"FROM " + DatabaseProperties.getDatabase() + ".tables";
			if(onlyAvailable)
				query += " WHERE available = TRUE";
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while(rs.next()) {
				//get each table from DB
				int tableID = rs.getInt("tableID");
				String name = rs.getString("name");
				int seats = rs.getInt("seats");
				boolean available = rs.getBoolean("available");
				tables.add(new Table(tableID, name, seats, available));
			}
			return tables;
		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static Table getTableById(Connection connection, int tableID)
	{
		Table table = null;
		try {
			String query = "SELECT tableID, name, seats, available " +
					"FROM " + DatabaseProperties.getDatabase() + ".tables " +
					"WHERE tableID = " + tableID;
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			while(rs.next()) {
				//get each table from DB
				tableID = rs.getInt("tableID");
				String name = rs.getString("name");
				int seats = rs.getInt("seats");
				boolean available = rs.getBoolean("available");

				table = new Table(tableID, name, seats, available);
			}
			return table;
		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static void updateTable(Connection connection, Table table, int tableID)
	{
		try {
			String query =  "UPDATE " + DatabaseProperties.getDatabase() + ".tables " +
					"SET name = ?, seats = ?, available = ? " +
					"WHERE tableID = " + tableID;
			PreparedStatement pst = connection.prepareStatement(query);

			pst.setString(1, table.getName());
			pst.setInt(2, table.getSeats());
			pst.setBoolean(3, table.isAvailable());
			pst.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
	}

	static boolean existsTableWithID(Connection connection, int tableID)
	{
		try {
			String query = "SELECT tableID from " + DatabaseProperties.getDatabase() + ".tables " +
					"WHERE tableID = " + tableID;
			PreparedStatement pst = connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			if(rs.next() && rs.getInt("tableID") != 0)
				return true;

		} catch(SQLException e) {
			e.printStackTrace();
			DatabaseService_Interface.connect();
			throw new MySQLServerConnectionException();
		}
		return false;
	}
}
