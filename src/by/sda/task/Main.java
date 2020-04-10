package by.sda.task;

import by.sda.task.factory.RunnableTaskFactory;
import by.sda.task.service.TaskHandler;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {

        BlockingDeque<Runnable> tasks = new LinkedBlockingDeque<>();
        TaskHandler myExecutor = new TaskHandler(10,10, 60L, TimeUnit.SECONDS, tasks);

        processFirstTaskOperation(myExecutor,100);
        processSecondTaskOperation(myExecutor,100);
        processThirdTaskOperation(myExecutor,100);
        processOtherTaskOperation(myExecutor,100);
        myExecutor.solveTasks(myExecutor.getQueue());

        myExecutor.shutdown();
    }

    private static void processFirstTaskOperation(ThreadPoolExecutor executor, int count){
        for( int i = 1 ; i <= count; ++i){
            Runnable task = RunnableTaskFactory.createFirstTask(i);
            executor.execute(task);
        }
    }

    private static void processSecondTaskOperation(ThreadPoolExecutor executor, int count){
        for( int i = 1 ; i <= count; ++i){
            Runnable task = RunnableTaskFactory.createSecondTask(i);
            executor.execute(task);
        }
    }

    private static void processThirdTaskOperation(ThreadPoolExecutor executor, int count){
        for( int i = 1 ; i <= count; ++i){
            Runnable task = RunnableTaskFactory.createThirdTask(i);
            executor.execute(task);
        }
    }

    private static void processOtherTaskOperation(ThreadPoolExecutor executor, int count){
        for( int i = 1 ; i <= count; ++i){
            Runnable task = RunnableTaskFactory.createFourthTask(i);
            executor.execute(task);
        }
    }
}
