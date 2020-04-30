package jdbcDemo;

import java.sql.*;

public class TestDemo {

    public static void main(String[] args) throws SQLException {

        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        String dbUrl = "jdbc:mysql://localhost:3306/testDb";
        String user = "root";
        String pass = "igdefault";


        try {
            // 1. Get a connection to database
            myConn = DriverManager.getConnection(dbUrl, user, pass);

            System.out.println("Database connection successful!\n");

            // 2. Create a statement
            myStmt = myConn.createStatement();

            // 3. Insert a new student
            System.out.println("Inserting a new employee to database");

            int rowsAffected  = myStmt.executeUpdate(
                    "insert into student " +
                            "(name, age) " +
                            "values " +
                            "('Abhilesh', 23)");

            // 3. Execute SQL query
            myRs = myStmt.executeQuery("select * from student");

            // 4. Process the result set
            while (myRs.next()) {
                System.out.println(myRs.getString("name"));
            }
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
        finally {
            if (myRs != null) {
                myRs.close();
            }

            if (myStmt != null) {
                myStmt.close();
            }

            if (myConn != null) {
                myConn.close();
            }
        }
    }

}
