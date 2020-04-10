package by.sda.task.factory;

import by.sda.task.modules.FirstModule;
import by.sda.task.modules.FourthModule;
import by.sda.task.modules.SecondModule;
import by.sda.task.modules.ThirdModule;

public class RunnableTaskFactory {

    public static Runnable createFirstTask(int i){
        return new FirstModule(i);
    }
    public static Runnable createSecondTask(int i){
        return new SecondModule(i);
    }
    public static Runnable createThirdTask(int i){
        return new ThirdModule(i);
    }
    public static Runnable createFourthTask(int i){
        return new FourthModule(i);
    }
}
