package com.immi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

public class EmployeeDAO {
	public boolean checkIfEmployeeExist(String email) {
		Connection connection = DBConnection.getConnection();
		boolean isExist = false;
		try {
			String sql = "SELECT email FROM employees WHERE email='" + email + "' LIMIT 1";
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				isExist = true;
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isExist;
	}

	public void batchInsert(Collection<Employee> newEmployees) throws SQLException {
		Connection connection = DBConnection.getConnection();
		String sqlQuery = "INSERT INTO employees (`name`,`email`,`reporting`) VALUES (?,?,?)";
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sqlQuery);
			connection.setAutoCommit(false);
			for (Employee newEmployee : newEmployees) {
				pstmt.setString(1, newEmployee.getName());
				pstmt.setString(2, newEmployee.getEmail());
				pstmt.setString(3, newEmployee.getReportTo());
				pstmt.addBatch();
			}
			int[] result = pstmt.executeBatch();
			System.out.println("The number of rows inserted: " + result.length);
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
			connection.rollback();
		} finally {
			if (pstmt != null)
				pstmt.close();
			if (connection != null)
				connection.close();
		}
	}
}
