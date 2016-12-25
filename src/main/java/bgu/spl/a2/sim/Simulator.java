/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;

import bgu.spl.a2.WorkStealingThreadPool;
import bgu.spl.a2.sim.tasks.ManufacturingTask;
import bgu.spl.a2.sim.tasks.ParseData;
import bgu.spl.a2.sim.tasks.Wave;
import com.google.gson.Gson;

import java.io.*;


import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;


/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

    private static WorkStealingThreadPool workStealingThreadPool;
    private static List<Wave> waves;

    /**
     * Begin the simulation
     * Should not be called before attachWorkStealingThreadPool()
     */
    public static ConcurrentLinkedQueue<Product> start() {

        ConcurrentLinkedQueue<Product> products = new ConcurrentLinkedQueue<>();

        for (Wave currWave: waves) {

            workStealingThreadPool.start();
            int numberOfTasks = 0;

            for (Integer curr : currWave.getProductsAndQuantities().values())
            {
                numberOfTasks+= curr;
            }

            CountDownLatch l = new CountDownLatch(numberOfTasks);

            for (String productName : currWave.getProductsAndQuantities().keySet()) {

                for (int i = 0; i < currWave.getProductsAndQuantities().get(productName); i++)
                {
                    Product currProduct = new Product(currWave.getStartId() + i , productName);
                    ManufacturingTask currTask = new ManufacturingTask(currProduct);
                    Simulator.workStealingThreadPool.submit(currTask);

                    currTask.getResult().whenResolved(() -> {
                        l.countDown();
                        products.add(currTask.getResult().get());
                    });

                }
            }

            try {
                l.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            workStealingThreadPool.shutdown(); //stopping all the threads
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return products;
//
//        int startId = 50123450;
//        int qty = 100;
//        Product product = new Product(startId, "yphone30");
//        for (int i = 0; i < qty; i++)
//            Simulator.workStealingThreadPool.submit(new ManufacturingTask(startId + i, product));

    }

    /**
     * attach a WorkStealingThreadPool to the Simulator, this WorkStealingThreadPool will be used to run the simulation
     *
     * @param myWorkStealingThreadPool - the WorkStealingThreadPool which will be used by the simulator
     */
    public static void attachWorkStealingThreadPool(WorkStealingThreadPool myWorkStealingThreadPool) {
        workStealingThreadPool = myWorkStealingThreadPool;
    }

    public static int main(String[] args) {
        String jasonFileLocation = args[1];
        Gson gson = new Gson();

        try {

            BufferedReader br = new BufferedReader(
                    new FileReader(jasonFileLocation));

            //convert the json string back to object
            ParseData obj = gson.fromJson(br, ParseData.class);
            WorkStealingThreadPool myWorkStealingThreadPool = new WorkStealingThreadPool(obj.getThreads());

            Simulator.attachWorkStealingThreadPool(new WorkStealingThreadPool(4));
            ConcurrentLinkedQueue<Product> simulationResult;
            simulationResult = Simulator.start();
            FileOutputStream fout = new FileOutputStream("result.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(simulationResult);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
