package bgu.spl.a2.test;

import bgu.spl.a2.Task;
import bgu.spl.a2.WorkStealingThreadPool;

/**
 * Created by Orel Hazan on 20/12/2016.
 */
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SumMatrix extends Task<int[]>{

    public class SumRow extends Task<Integer> {
        private int[][] array;
        private int r;
        public SumRow(int[][] array,int r) {
            this.array = array;
            this.r=r;
            check = array[r];
        }
        protected void start(){
            int sum=0;
            for(int j=0 ;j<array[0].length;j++)
                sum+=array[r][j];
            complete(sum);
        }
    }

    private int[][] array;
    public SumMatrix(int[][] array) {
        this.array = array;
        check = array;
    }
    protected void start() {
        int sum = 0;
        List<Task<Integer>> tasks = new ArrayList<>();
        int rows = array.length;
        for (int i = 0; i < rows; i++) {
            SumRow newTask = new SumRow(array, i);
            spawn(newTask);
            tasks.add(newTask);
        }
        whenResolved(tasks, () -> {
                    int[] res = new int[rows];
                    for (int j = 0; j < rows; j++) {
                        res[j] = tasks.get(j).getResult().get();
                    }
                    complete(res);
                }
        );
    }
    public static void main(String[] args)throws InterruptedException
    {
        WorkStealingThreadPool pool = new WorkStealingThreadPool(8);
        int[][] array = new int[][]{{1,2,5,-2},{3,4,7,8},{4,6,8,9},{0,0,9,0}
                ,{4,6,8,9},{0,0,9,0},{4,6,8,9},{0,0,9,0},{4,6,8,9},{0,0,9,0},{4,6,8,9},{0,0,9,0},{4,6,8,9},{0,0,9,0},{4,6,8,9},{0,0,9,0}};
// some stuff

        CountDownLatch l = new CountDownLatch(1);
        SumMatrix myTask = new SumMatrix(array);
        pool.start();
        pool.submit(myTask);
//some stuff
        myTask.getResult().whenResolved(() -> {
            l.countDown();
            System.out.println(Arrays.toString(myTask.getResult().get()));
        });

        l.await();
        pool.shutdown(); //stopping all the threads

    }
}
