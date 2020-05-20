
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DriverClass {


    public static void main(String[] args) {

        ExecutorService pool= Executors.newFixedThreadPool(2);
        //ExecutorService pool = Executors.newCachedThreadPool();

        Runnable readfile1 = new ReadFile.ReadFile1();
        Runnable readfile2 = new ReadFile.ReadFile2();
        Runnable readfile3 = new ReadFile.ReadFile3();
        Runnable readfile4 = new ReadFile.ReadFile4();

        pool.execute(readfile1);
        pool.execute(readfile2);
        pool.execute(readfile3);
        pool.execute(readfile4);

        try {                                                            // If we use this it will let the current task to
           pool.awaitTermination(10000, TimeUnit.MILLISECONDS);  // first then it will close the thread pool service,
        } catch (InterruptedException e) {                              // But since we are using timer so we dont need this function right now.
            e.printStackTrace();
        }

        pool.shutdown();

        try {
            DBConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
