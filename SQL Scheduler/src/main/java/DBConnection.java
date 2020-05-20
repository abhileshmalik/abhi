import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DBConnection {

    private static volatile DBConnection instance = null;

    static Connection myConn = null;
    static Statement myStmt = null;
    static ResultSet myRs = null;

    private DBConnection() throws IOException, SQLException {
        // 1. Load the properties file
        Properties props = new Properties();
        props.load(new FileInputStream("connection.properties"));
        // props.load(new FileInputStream("d:/workdev/foobar/mystuff/demo.properties"));

        // 2. Read the props
        String theUser = props.getProperty("user");
        String thePassword = props.getProperty("password");
        String theDburl = props.getProperty("dburl");

        System.out.println("Connecting to database...");

        // 3. Get a connection to database
        myConn = DriverManager.getConnection(theDburl, theUser, thePassword);

        System.out.println("Database connection successful!\n");

        // 2. Create a statement
        myStmt = myConn.createStatement();

    }

    public static DBConnection getInstance() throws IOException, SQLException {

        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null)
                instance = new DBConnection();
            }
        }
        return instance;

    }

    public static void closeConnection() throws SQLException {
            if (myRs != null) {
                myRs.close();
            }

            if (myStmt != null) {
                myStmt.close();
            }

            if (myConn != null) {
                myConn.close();
            }

        System.out.println("Database Connection closed...");
    }
}
