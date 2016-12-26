/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;

import bgu.spl.a2.WorkStealingThreadPool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tasks.ManufacturingTask;
import bgu.spl.a2.sim.tasks.Order;
import bgu.spl.a2.sim.tasks.ParseData;
import bgu.spl.a2.sim.tasks.Wave;
import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import com.google.gson.Gson;

import java.io.*;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;


/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

    private static WorkStealingThreadPool workStealingThreadPool;
    private static List<Wave> waves = new ArrayList<>();
    private static Warehouse warehouse= new Warehouse();

    /**
     * Begin the simulation
     * Should not be called before attachWorkStealingThreadPool()
     */
    public static ConcurrentLinkedQueue<Product> start() {

        ConcurrentLinkedQueue<Product> products = new ConcurrentLinkedQueue<>();

        workStealingThreadPool.start();

        for (Wave currWave: waves) {

            int numberOfTasks = 0;

            for (Order curr : currWave.getOrders())
            {
                numberOfTasks+= curr.getQty();
            }

            CountDownLatch l = new CountDownLatch(numberOfTasks);

            for (Order order : currWave.getOrders()) {

                for (int i = 0; i < order.getQty(); i++)
                {
                    Product currProduct = new Product(order.getStartId() + i , order.getProduct());
                    ManufacturingTask currTask = new ManufacturingTask(currProduct,warehouse);
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
    }

    /**
     * attach a WorkStealingThreadPool to the Simulator, this WorkStealingThreadPool will be used to run the simulation
     *
     * @param myWorkStealingThreadPool - the WorkStealingThreadPool which will be used by the simulator
     */
    public static void attachWorkStealingThreadPool(WorkStealingThreadPool myWorkStealingThreadPool) {
        workStealingThreadPool = myWorkStealingThreadPool;
    }


    private static void addTool(String toolType, int qty){
        if (toolType.equals("gs-driver")){
            GcdScrewDriver tool = new GcdScrewDriver();
            warehouse.addTool(tool, qty);
        }
        else if (toolType.equals("np-hammer")){
            NextPrimeHammer tool = new NextPrimeHammer();
            warehouse.addTool(tool, qty);
        }
        else if (toolType.equals("rs-pliers")){
            RandomSumPliers tool = new RandomSumPliers();
            warehouse.addTool(tool, qty);
        }
    }

    public static void main(String[] args) {
        String jasonFileLocation = args[0];
        Gson gson = new Gson();

        try {
            BufferedReader br = new BufferedReader(new FileReader(jasonFileLocation));

            //convert the json string back to object
            ParseData obj = gson.fromJson(br, ParseData.class);
            WorkStealingThreadPool workStealingThreadPoolTmp = new WorkStealingThreadPool(obj.getThreads());
            Simulator.attachWorkStealingThreadPool(workStealingThreadPoolTmp);

            //add tool to Warehouse
            for (int i = 0; i < obj.getTools().size() ; i++) {
                addTool(obj.getTools().get(i).getTool(),obj.getTools().get(i).getQty() );
            }
            ManufactoringPlan plan;
            for (int i = 0; i < obj.getPlans().size(); i++) {
                String product = obj.getPlans().get(i).getProduct();
                String[] parts= obj.getPlans().get(i).getParts();
                String[] tools = obj.getPlans().get(i).getTools();
                plan = new ManufactoringPlan(product,parts ,tools);
                warehouse.addPlan(plan);
            }

            Wave wave;
            int size = obj.getWaves().size();
            for (int i = 0; i < obj.getWaves().size(); i++) {
               wave = new Wave(obj.getWaves().get(i));
               waves.add(wave);
            }

            System.out.println(obj.toString());


            ConcurrentLinkedQueue<Product> simulationResult;
  //          simulationResult = Simulator.start();
            FileOutputStream fout = new FileOutputStream("result.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
    //        oos.writeObject(simulationResult);


        } catch (IOException e) {
            e.printStackTrace();
        }

        //return 0;
    }

}
