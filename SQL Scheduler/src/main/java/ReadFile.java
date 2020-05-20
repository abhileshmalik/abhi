
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;


public class ReadFile {


    static class ReadFile1 implements Runnable {

        @Override
        public void run() {
            try {
                DBConnection.getInstance();

                File myObj = new File("commands1.txt");
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    DBConnection.myStmt.executeUpdate(data);
                    System.out.println(data);
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class ReadFile2 implements Runnable {

        @Override
        public void run() {
            try {
                DBConnection.getInstance();
                File myObj = new File("commands2.txt");
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    DBConnection.myStmt.executeUpdate(data);
                    System.out.println(data);
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class ReadFile3 implements Runnable {

        @Override
        public void run() {
            try {
                DBConnection.getInstance();
                File myObj = new File("commands3.txt");
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    DBConnection.myStmt.executeUpdate(data);
                    System.out.println(data);
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class ReadFile4 implements Runnable {

        @Override
        public void run() {
            try {
                DBConnection.getInstance();
                File myObj = new File("commands4.txt");
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    Thread.sleep(1000);
                    String data = myReader.nextLine();
                    DBConnection.myStmt.executeUpdate(data);
                    System.out.println(data);
                }
                myReader.close();
            } catch (FileNotFoundException | InterruptedException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

}
