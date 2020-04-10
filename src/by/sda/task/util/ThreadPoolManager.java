package by.sda.task.util;

import java.util.Arrays;

/**
 *  Утилитный класс-менеджер, задач которого заключается в распределении нагрузки по потокам
 *  Всего рассматривается 2 крайних случая:
 *  1) когда число ядер меньше числа модулей
 *  2) обобщённый случай, в который по реализации укладываются перечисленные в задании условия, а именно
 *      а) равномерное распределение задач от модулей;
 *      б) преобладающее большинство задач от одного из модулей;
 *      в) появление 4-го модуля всегда выделяет ему отедльный поток, согласно количеству задач от него;
 */

public class ThreadPoolManager {

    /**
     * В массив вкладываются количество потоков на каждый из модулей.
     * 3 на основные и 4-ый на все дополнительные задачи
     */
    private static int[] distributedArray = new int[4];
    /**
     * Флаг на наличие других задач, помимо 3-х основных
     */
    private static boolean isAnyOtherModules = false;

    private ThreadPoolManager(){
        super();
    }

    public static void printPoolsSize(int[] distributedSizes){
        System.out.print("First : "+ distributedSizes[0]+" ;" );
        System.out.print(" Second : "+ distributedSizes[1]+" ;");
        System.out.print(" Third : "+ distributedSizes[2]+" ;");
        System.out.println(" Other : " + distributedSizes[3]+" ;");
    }

    /**
     * @param numberOfTasksArray - массив, хранящий число задач от каждого модуля
     * @param total - число задач в очереди
     * @param poolSize - число потоков в пуле
     * @return целочисленный массив распреденных потоков по модулям
     */


    private static boolean isValidInputData (int [] numberOfTasksArray,  int total, int poolSize){
        if( total == 0 || poolSize == 0 || numberOfTasksArray == null ){
            return false;
        }
        int sum = 0;
        for (int value : numberOfTasksArray) {
            if (value < 0) {
                return false;
            }
            sum += value;
        }
        return sum > 0 ;
    }

    public static int[] divideResponsibilities(int [] numberOfTasksArray,  int total, int poolSize){
        if( !isValidInputData(numberOfTasksArray,   total,  poolSize) ){
            return null;
        }
        double[] relationsArray = new double[4];
        if(numberOfTasksArray[3] != 0) {
            isAnyOtherModules = true;
        }
        for( int i = 0 ; i < relationsArray.length; ++i){
            relationsArray[i] = getPartOfTotal(numberOfTasksArray[i], total);
        }
        return distributeProportions(relationsArray, poolSize);
    }

    public static void clearData(){
        distributedArray = new int[4];
        isAnyOtherModules = false;
    }

    private static double getPartOfTotal(int part, int total){
        if(total == 0){
            return 0;
        }
        return ((double) part / total);
    }

    /**
     *  Общий случай распределения потоков
     * @param relationsArray - массив отношений числа от одного модуля к общему числу
     * @param poolSize - число потоков в пуле
     * @return целочисленный массив распреденных потоков по модулям
     */
    private static int[] distributeCommonCase(double[] relationsArray, int poolSize){
        int restPool = poolSize;
        double border = 0;
        while (restPool > 0) {
            if( border >  getMaxOfArr(relationsArray)){
                incrementMaxNotNull(relationsArray);
                restPool--;
            }
            for( int i = 0 ; i < 4 ; ++i){
                if( (relationsArray[i] != 0 && distributedArray[i] == 0 ) || relationsArray[i] > border){
                    distributedArray[i]++;
                    restPool--;
                }
                if(restPool == 0){
                    break;
                }
            }
            border += (1.0 / poolSize);
        }
        return distributedArray;
    }

    private static double getMaxOfArr(double[] relationsArray){
        return Arrays.stream(relationsArray).max().getAsDouble();
    }

    private static void incrementMaxNotNull(double[] relationsArray){
        for( int i = 0 ; i < 4 ; ++i){
            if(distributedArray[i] != 0 && relationsArray[i] == getMaxOfArr(relationsArray)){
                distributedArray[i]++;
                break;
            }
        }
    }

    /**
     *  Случай, когда потоков меньше, чем модулей
     * @param relationsArray - массив отношений числа от одного модуля к общему числу
     * @param poolSize - число потоков в пуле
     * @return целочисленный массив распреденных потоков по модулям
     */
    private static int[] distributedNotEnoughThreadsCase(double[] relationsArray, int poolSize){
        int localSum =  calculateLocalSum(relationsArray);
        if(  localSum != poolSize){
            distributedArray = distributedNotEnoughThreadsCase(relationsArray, poolSize);
        }
        return distributedArray;
    }

    private static int[] distributeProportions(double[] relationsArray, int poolSize ){
        int[] distributedSizes;
        int border = isAnyOtherModules? 3 : 2;
        if( poolSize <= border){
            distributedSizes = distributedNotEnoughThreadsCase(relationsArray, poolSize);
        }
        else{
            distributedSizes = distributeCommonCase(relationsArray, poolSize);
        }
        return distributedSizes;
    }

    private static int tossACoin(){
        return Math.random() > 0.5 ? 1 : 0 ;
    }

    private static int calculateLocalSum(double[] relationsArray){
        if(relationsArray[3] != 0){
            distributedArray[3] = 1;
        }
        int localSum = distributedArray[3];
        for(int i = 0 ; i < distributedArray.length - 1 ; ++ i){
            if( relationsArray[i] != 0 ){
                distributedArray[i] = tossACoin();
                localSum +=  distributedArray[i];
            }
        }
        return localSum;
    }

}