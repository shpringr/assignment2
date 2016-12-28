package bgu.spl.a2.test;

import bgu.spl.a2.Task;
import bgu.spl.a2.WorkStealingThreadPool;

import java.util.*;
import java.util.concurrent.CountDownLatch;

public class MergeSort extends Task<int[]> {

    private final int[] array;

    public MergeSort(int[] array) {

        this.array = array;
        }

    @Override
    protected void start() {
        if (array.length > 1)
        {
            int[] spwanedArrayFirst = Arrays.copyOfRange(array, 0, array.length / 2);
            int[] spwanedArraySecond = Arrays.copyOfRange(array, array.length / 2, array.length);

            MergeSort spawnTask1 = new MergeSort(spwanedArrayFirst);
            MergeSort spawnTask2 = new MergeSort(spwanedArraySecond);

            spawn(spawnTask1, spawnTask2);
            Collection<MergeSort> tasks = new ArrayList<>();
            tasks.add(spawnTask1);
            tasks.add(spawnTask2);

            whenResolved(tasks, () -> {
                        int[] result;
                        result = mergeTwoSortedArrays(spawnTask1.getResult().get(), spawnTask2.getResult().get());
                        complete(result);
                    }
            );
        }
        else
            complete(array);
    }

    private int[] mergeTwoSortedArrays(int[] firstSortedArray, int[] secondSortedArray) {
        int[] sortedArray = new int[firstSortedArray.length + secondSortedArray.length];

        int i = 0;
        int j = 0;

        for (int k = 0; k < sortedArray.length; k++) {
            if (i >= firstSortedArray.length) {
                sortedArray[k] = secondSortedArray[j];
                j++;
            }
            else if (j >= secondSortedArray.length) {
                sortedArray[k] = firstSortedArray[i];
                i++;
            }
            else if (firstSortedArray[i] < secondSortedArray[j]) {
                sortedArray[k] = firstSortedArray[i];
                i++;
            }
            else {
                sortedArray[k] = secondSortedArray[j];
                j++;
            }
        }

        return sortedArray;
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            WorkStealingThreadPool pool = new WorkStealingThreadPool(10);
            int n = 10; //you may check on different number of elements if you like
            int[] array = new Random().ints(n).toArray();

            MergeSort task = new MergeSort(array);

            CountDownLatch l = new CountDownLatch(1);
            pool.start();
            pool.submit(task);
            task.getResult().whenResolved(() -> {
            l.countDown();
            });

            l.await();
            pool.shutdown();
        }
    }
}