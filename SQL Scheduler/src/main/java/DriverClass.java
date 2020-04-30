
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DriverClass extends TimerTask {

    @Override
    public void run() {
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
        pool.shutdown();

        try {
            DBConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
/*        try {
            pool.awaitTermination(10000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

    }

    public static void main(String[] args) {

        TimerTask timerTask = new DriverClass();
        //running timer task as daemon thread
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 10*1000);
        System.out.println("TimerTask started");

        timerTask.run();

    }


}
