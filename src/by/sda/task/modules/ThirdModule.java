package by.sda.task.modules;

import java.util.concurrent.TimeUnit;

public class ThirdModule extends AbstractModule {
    public static final String ID_STR = "Third Module";

    public ThirdModule(long number) {
        super(ID_STR , number);
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

    private double doTheJob(){
        return  Math.tan(Math.random()*100);
    }
}