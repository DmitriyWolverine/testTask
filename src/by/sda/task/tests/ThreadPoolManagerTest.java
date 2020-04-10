package by.sda.task.tests;

import by.sda.task.util.ThreadPoolManager;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ThreadPoolManagerTest extends Assert {
    private int[] array1;
    private int[] array2;
    private int[] array3;
    private int[] array4;
    private int[] array5;
    private int[] array6;
    private int[] array7;
    private int[] array8;
    private Map<Integer, int[]> arrayMap = new HashMap<>();

    @Before
    public void generateTestData() {
        array1 = new int[]{90_000, 5_000, 5_000, 0};
        array2 = new int[]{300, 300, 300, 300};
        array3 = new int[]{400, 400, 200, 0};
        array4 = new int[]{1, 1, 1, 1000};
        array5 = new int[]{1, 2, 1, 54};
        array6 = new int[]{10, 300, 0, 534};
        array7 = new int[]{0, 0, 0, 0};
        array8 = new int[]{-43, -54, 435, -1};
    }

    private void testOneArray(int[] array, int sum) {
        for( int i = 1;i <= 100; ++i ){
            int[] res = ThreadPoolManager.divideResponsibilities(array, sum, i);
            ThreadPoolManager.clearData();
            int localNumberOfThreads = getSum(res);
            assertEquals("Test number " + i + " has gone wrong", i, localNumberOfThreads);
        }
    }

    private int getSum( int[] array1) {
        int sum = 0;
        for (int j = 0; j < 4; ++j) {
            sum += array1[j];
        }
        return sum;
    }

    @Test
    public void testArray1TotalSum(){
        int sum = getSum(array1);
        testOneArray(array1,sum);
    }

    @Test
    public void testArray2TotalSum(){
        int sum = getSum(array2);
        testOneArray(array2,sum);
    }
    @Test
    public void testArray3TotalSum(){
        int sum = getSum(array3);
        testOneArray(array3,sum);
    }
    @Test
    public void testArray4TotalSum(){
        int sum = getSum(array4);
        testOneArray(array4,sum);
    }
    @Test
    public void testArray5TotalSum(){
        int sum = getSum(array5);
        testOneArray(array5,sum);
    }
    @Test
    public void testArray6TotalSum(){
        int sum = getSum(array6);
        testOneArray(array6,sum);
    }

    @Test
    public void testArray7isNull(){
        for( int i = 1 ; i <= 100; ++i ){
            int sum = getSum(array7);
            int[] res = ThreadPoolManager.divideResponsibilities(array7, sum, i);
            ThreadPoolManager.clearData();
            assertNull("Test number " + i + " has gone wrong", res);
        }
    }

    @Test
    public void testArray8StrangeData(){
        for( int i = 1 ; i <= 100; ++i ){
            int sum = getSum(array8);
            int[] res = ThreadPoolManager.divideResponsibilities(array8, sum, i);
            ThreadPoolManager.clearData();
            assertNull("Test number " + i + " has gone wrong", res);
        }
    }

    private void initMap(int[] array){
        for(int i = 5 ;i <= 100; ++i ) {
            arrayMap.put(i,array);
        }
    }

    @Test
    public void testArray1Distribution(){
        initMap(array1);
        for( Integer integer : arrayMap.keySet()){
            Assert.assertTrue( isCorrectDistribution(integer , arrayMap.get(integer) ) );
        }
        arrayMap.clear();
    }

    @Test
    public void testArray2Distribution(){
        initMap(array2);
        for( Integer integer : arrayMap.keySet()){

            Assert.assertTrue( isCorrectDistribution(integer , arrayMap.get(integer) ) );
        }
        arrayMap.clear();
    }

    @Test
    public void testArray3Distribution(){
        initMap(array3);
        for( Integer integer : arrayMap.keySet()){
            Assert.assertTrue( isCorrectDistribution(integer , arrayMap.get(integer) ) );
        }
        arrayMap.clear();
    }

    @Test
    public void testArray4Distribution(){
        initMap(array4);
        for( Integer integer : arrayMap.keySet()){
            Assert.assertTrue( isCorrectDistribution(integer , arrayMap.get(integer) ) );
        }
        arrayMap.clear();
    }

    @Test
    public void testArray5Distribution(){
        initMap(array5);
        for( Integer integer : arrayMap.keySet()){
            Assert.assertTrue( isCorrectDistribution(integer , arrayMap.get(integer) ) );
        }
        arrayMap.clear();
    }

    @Test
    public void testArray6Distribution(){
        initMap(array6);
        for( Integer integer : arrayMap.keySet()){
            Assert.assertTrue( isCorrectDistribution(integer , arrayMap.get(integer) ) );
        }
        arrayMap.clear();
    }


    private boolean isCorrectDistribution(int threadsNumber, int[] tasksDistribution){
        int[] distributed = ThreadPoolManager.divideResponsibilities(tasksDistribution,getSum(tasksDistribution),threadsNumber);
        assert distributed != null;
        for(int i = 0; i < distributed.length ; ++i){
            for(int j = i+1 ; j < distributed.length ; ++j) {
                if( i!=j &&
                        ((distributed[i] >= distributed[j])  ^ (tasksDistribution[i] >= tasksDistribution[j]) )
                && (distributed[i] <= distributed[j])  ^ (tasksDistribution[i] <= tasksDistribution[j])){
                    return false;
                }
            }
        }
        ThreadPoolManager.clearData();
        return true;
    }
}
