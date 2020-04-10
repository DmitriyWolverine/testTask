package by.sda.task.tests;

import by.sda.task.factory.RunnableTaskFactory;
import by.sda.task.service.TaskHandler;

import java.util.concurrent.*;

/**
 * Здесь ничего не работает
 *
 */

public class TaskHandlerPerformanceTest {

    private int corePoolSize;
    private int numberOfTasks;

    public TaskHandlerPerformanceTest(int corePoolSize, int numberOfTasks) {
        this.corePoolSize = corePoolSize;
        this.numberOfTasks = numberOfTasks;
    }

    public void runTest(){
        System.out.println("Parameters: pool size = "+ corePoolSize+"; number of tasks = "+numberOfTasks);
        System.out.println("Time Test for Equal Number Of Tasks with takes "+timeTestEqualNumberOfTasks() + " seconds");
        System.out.println("Time Test for One Dominating part takes "+timeTestOneModuleDominating() +" seconds");
        System.out.println("Time Test for Default ThreadPoolExecutor takes "+timeTestForDefaultExecutor() + " seconds");
    }

    public long timeTestEqualNumberOfTasks(){
        TaskHandler myExecutor = new TaskHandler(corePoolSize,corePoolSize, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
        long start = System.nanoTime();


        processEqualNumberOfTasks(myExecutor,numberOfTasks);
        myExecutor.solveTasks(myExecutor.getQueue());
        myExecutor.shutdown();

        return (System.nanoTime() - start) / (1000_000_000);
    }

    public long timeTestOneModuleDominating(){
        TaskHandler myExecutor = new TaskHandler(corePoolSize,corePoolSize, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
        long start = System.nanoTime();
        processOneModuleDominating(myExecutor,numberOfTasks);
        myExecutor.solveTasks(myExecutor.getQueue());

        myExecutor.shutdown();
        return (System.nanoTime() - start) / (1000_000_000);
    }

    public long timeTestForDefaultExecutor(){
        ThreadPoolExecutor myExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(corePoolSize);
        long start = System.nanoTime();

        processEqualNumberOfTasks(myExecutor,numberOfTasks);

        myExecutor.shutdown();
        return (System.nanoTime() - start) / (1000_000_000);
    }


    private void processEqualNumberOfTasks(ThreadPoolExecutor executor, int count){
        int oneModuleNumber = count / 4 ;
        processFirstTaskOperation(executor, oneModuleNumber);
        processSecondTaskOperation(executor, oneModuleNumber);
        processThirdTaskOperation(executor, oneModuleNumber);
        processOtherTaskOperation(executor, oneModuleNumber);
    }

    private void processOneModuleDominating(ThreadPoolExecutor executor, int count){
        int firstModuleNumber = (int) (count - 0.9*count) ;
        int otherParts = (count - firstModuleNumber) / 2;
        int rest = count - firstModuleNumber - otherParts*2;
        processFirstTaskOperation(executor, firstModuleNumber);
        processSecondTaskOperation(executor, otherParts);
        processThirdTaskOperation(executor, otherParts);
        processOtherTaskOperation(executor, rest);
    }

    private void processFirstTaskOperation(ThreadPoolExecutor executor, int count){
        for( int i = 1 ; i <= count; ++i){
            Runnable task = RunnableTaskFactory.createFirstTask(i);
            executor.execute(task);
        }
    }

    private void processSecondTaskOperation(ThreadPoolExecutor executor, int count){
        for( int i = 1 ; i <= count; ++i){
            Runnable task = RunnableTaskFactory.createSecondTask(i);
            executor.execute(task);
        }
    }

    private void processThirdTaskOperation(ThreadPoolExecutor executor, int count){
        for( int i = 1 ; i <= count; ++i){
            Runnable task = RunnableTaskFactory.createThirdTask(i);
            executor.execute(task);
        }
    }

    private void processOtherTaskOperation(ThreadPoolExecutor executor, int count){
        for( int i = 1 ; i <= count; ++i){
            Runnable task = RunnableTaskFactory.createFourthTask(i);
            executor.execute(task);
        }
    }

}
