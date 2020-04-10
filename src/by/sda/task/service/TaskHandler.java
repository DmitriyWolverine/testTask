package by.sda.task.service;

import by.sda.task.modules.*;
import by.sda.task.util.ThreadPoolManager;

import java.util.NoSuchElementException;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Основной класс-обработчки поступающих задач
 */
public class TaskHandler extends ThreadPoolExecutor {

    private ThreadPoolExecutor tpe1;
    private ThreadPoolExecutor tpe2;
    private ThreadPoolExecutor tpe3;
    private ThreadPoolExecutor tpe4;

    private ReentrantLock reentrantLock = new ReentrantLock();
    private int[] numberOfTaskValue = new int[4];

    public TaskHandler(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
    }

    private void analyseDistribution(BlockingQueue<Runnable> localQueue){
        int total;
        if( (total = localQueue.size()) > 0) {
            reentrantLock.lock();
            try{
                numberOfTaskValue[0] = (int) localQueue.parallelStream().filter(task -> task instanceof FirstModule).count();
                numberOfTaskValue[1] = (int) localQueue.parallelStream().filter(task -> task instanceof SecondModule).count();
                numberOfTaskValue[2] = (int) localQueue.parallelStream().filter(task -> task instanceof ThirdModule).count();
                numberOfTaskValue[3] = Math.max(0, total - numberOfTaskValue[2] - numberOfTaskValue[1] - numberOfTaskValue[0]);
                int[] distributedArray = ThreadPoolManager.divideResponsibilities(numberOfTaskValue, total, getPoolSize());
                innerInitializer(distributedArray);
                ThreadPoolManager.printPoolsSize(distributedArray);
                ThreadPoolManager.clearData();
            }
            finally {
                reentrantLock.unlock();
            }
        }
    }

    private void innerInitializer(int[] poolSizes){
        if(poolSizes[0] > 0)
            tpe1 = (ThreadPoolExecutor) Executors.newFixedThreadPool(poolSizes[0]);
        if(poolSizes[1] > 0)
            tpe2 = (ThreadPoolExecutor) Executors.newFixedThreadPool(poolSizes[1]);
        if(poolSizes[2] > 0)
            tpe3 = (ThreadPoolExecutor) Executors.newFixedThreadPool(poolSizes[2]);
        if( poolSizes[3] > 0 ) {
            tpe4 = (ThreadPoolExecutor) Executors.newFixedThreadPool(poolSizes[3]);
        }
    }

    public void solveTasks(BlockingQueue<Runnable> localQueue){
        analyseDistribution(localQueue);
        try{
            reentrantLock.lock();
            if (!isShutdown()){
                super.shutdown();
            }
            while(!localQueue.isEmpty()) {
                if (tpe1 != null)
                    solveOneModuleTask(localQueue, FirstModule.ID_STR, tpe1);
                if (tpe2 != null)
                    solveOneModuleTask(localQueue, SecondModule.ID_STR, tpe2);
                if (tpe3 != null)
                    solveOneModuleTask(localQueue, ThirdModule.ID_STR, tpe3);
                if (tpe4 != null)
                    solveOneModuleTask(localQueue, FourthModule.ID_STR, tpe4);
            }
        }
        finally {
            reentrantLock.unlock();
        }
    }

    private void solveOneModuleTask(BlockingQueue<Runnable> localQueue, String moduleId, ThreadPoolExecutor tpe){
        try {
            Runnable moduleTask = localQueue.stream().
                    filter( runnable -> ((AbstractModule)runnable).getModuleId().equals(moduleId)).findFirst().get();
            tpe.execute(moduleTask);
            localQueue.remove(moduleTask);
        }
       catch (NoSuchElementException ex){
           System.err.println(moduleId +" is not stored yet");
       }
    }

    @Override
    public void execute(Runnable command) {
        super.execute(command);
    }

    @Override
    public void shutdown() {
        if(tpe1 != null)
            tpe1.shutdown();
        if(tpe2 != null)
            tpe2.shutdown();
        if(tpe3 != null)
            tpe3.shutdown();
        if(tpe4 != null)
            tpe4.shutdown();
    }
}