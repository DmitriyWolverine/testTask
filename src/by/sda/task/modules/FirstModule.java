package by.sda.task.modules;

import java.util.concurrent.TimeUnit;

public class FirstModule extends AbstractModule {
    public static final String ID_STR = "First Module";

    public FirstModule(long number) {
        super(ID_STR, number);
    }

    public void run() {
        try {
            Long duration = 1l;
            printTaskIdAndNumber();
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private long calculateFactorial(long number){
        if(number == 1){
            return 1;
        }
        return number*calculateFactorial(--number);
    }

    private void printCalculatingResult(long number){
        System.out.println(number+"! = "+calculateFactorial(number));
    }
}