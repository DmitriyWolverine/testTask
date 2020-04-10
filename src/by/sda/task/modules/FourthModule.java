package by.sda.task.modules;

import java.util.concurrent.TimeUnit;

public class FourthModule extends AbstractModule {
    public static final String ID_STR = "Fourth Module";
    public FourthModule(long number) {
        super(ID_STR, number);
    }

    @Override
    public void run() {
        try {
            long duration = 1L;
            printTaskIdAndNumber();
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected double doTheJob(){
       return Math.acos(Math.random()*100);
    }
}
