package by.sda.task.modules;

import java.util.concurrent.TimeUnit;

public class SecondModule extends AbstractModule {
    public static final String ID_STR = "Second Module";

    public SecondModule(long number) {
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
        return  Math.random() * 10_000;
    }
}