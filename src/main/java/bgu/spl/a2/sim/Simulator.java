package bgu.spl.a2.sim;

import bgu.spl.a2.WorkStealingThreadPool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tasks.ManufacturingTask;
import bgu.spl.a2.sim.json.Order;
import bgu.spl.a2.sim.json.ParseData;
import bgu.spl.a2.sim.json.Wave;
import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import com.google.gson.Gson;

import java.io.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;


/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

    private static WorkStealingThreadPool workStealingThreadPool;
    private static List<Wave> waves = new ArrayList<>();
    private static Warehouse warehouse = new Warehouse();

    /**
     * Begin the simulation
     * Should not be called before attachWorkStealingThreadPool()
     */
    public static ConcurrentLinkedQueue<Product> start() {

        ConcurrentLinkedQueue<Product> manufacturedProducts = new ConcurrentLinkedQueue<>();

        workStealingThreadPool.start();

        for (Wave currWave : waves) {

            List<Product> productsInWave = getWaveProducts(currWave);
            CountDownLatch l = new CountDownLatch(productsInWave.size());

            for (Product currProduct : productsInWave) 
            {
                ManufacturingTask currTask = new ManufacturingTask(currProduct, warehouse);
                
                Simulator.workStealingThreadPool.submit(currTask);

                currTask.getResult().whenResolved(l::countDown);
            }
            try 
            {
                l.await();
                
                for (Product product : productsInWave)
                    manufacturedProducts.add(product);
            }
            catch (InterruptedException e) 
            {
                e.printStackTrace();
            }
        }
        try
        {
            workStealingThreadPool.shutdown();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        return manufacturedProducts;
    }

    private static List<Product> getWaveProducts(Wave wave)
    {
        List<Product> productList = new ArrayList<>();

        for (Order order : wave.getOrders())
        {
            for (int i = 0; i < order.getQty(); i++) {
                productList.add(new Product(order.getStartId() + i, order.getProduct()));
            }
        }
        
        return productList;
    }

    /**
     * attach a WorkStealingThreadPool to the Simulator, this WorkStealingThreadPool will be used to run the simulation
     *
     * @param myWorkStealingThreadPool - the WorkStealingThreadPool which will be used by the simulator
     */
    public static void attachWorkStealingThreadPool(WorkStealingThreadPool myWorkStealingThreadPool) {
        workStealingThreadPool = myWorkStealingThreadPool;
    }

    private static void addTool(String toolType, int qty) {
        if (toolType.equals("gs-driver")) {
            GcdScrewDriver tool = new GcdScrewDriver();
            warehouse.addTool(tool, qty);
        } else if (toolType.equals("np-hammer")) {
            NextPrimeHammer tool = new NextPrimeHammer();
            warehouse.addTool(tool, qty);
        } else if (toolType.equals("rs-pliers")) {
            RandomSumPliers tool = new RandomSumPliers();
            warehouse.addTool(tool, qty);
        }
    }

    private static void parseData(ParseData obj) {

        //create WorkStealingThreadPool
        WorkStealingThreadPool workStealingThreadPoolTmp = new WorkStealingThreadPool(obj.getThreads());
        Simulator.attachWorkStealingThreadPool(workStealingThreadPoolTmp);

        //add tool to Warehouse
        for (int i = 0; i < obj.getTools().size(); i++) {
            addTool(obj.getTools().get(i).getTool(), obj.getTools().get(i).getQty());
        }

        //add Plan to Warehouse
        ManufactoringPlan plan;
        for (int i = 0; i < obj.getPlans().size(); i++) {
            String product = obj.getPlans().get(i).getProduct();
            String[] parts = obj.getPlans().get(i).getParts();
            String[] tools = obj.getPlans().get(i).getTools();
            plan = new ManufactoringPlan(product, parts, tools);
            warehouse.addPlan(plan);
        }

        //add Wave to Wave List
        Wave wave;
        int size = obj.getWaves().size();
        for (int i = 0; i < size; i++) {
            wave = new Wave(obj.getWaves().get(i));
            waves.add(wave);
        }
    }

    public static void main(String[] args) {
        try
        {
            String jasonFileLocation = args[0];
            Gson gson = new Gson();

            BufferedReader br = new BufferedReader(new FileReader(jasonFileLocation));
            ParseData obj = gson.fromJson(br, ParseData.class);
            parseData(obj);

            ConcurrentLinkedQueue<Product> simulationResult;
            simulationResult = Simulator.start();
            FileOutputStream fout = new FileOutputStream("result.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(simulationResult);
            oos.close();

            //TODO:BORRAR
            deserializeObject();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO:BORRAR
    private static void deserializeObject() {
        //deserialize the quarks.ser file
        try{
            InputStream file = new FileInputStream("result.ser");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            //deserialize the List
            ConcurrentLinkedQueue<Product> recoveredQuarks = (ConcurrentLinkedQueue<Product>)input.readObject();
            //display its data
            for (Iterator<Product> it = recoveredQuarks.iterator(); it.hasNext(); ) {
                Product quark = it.next();
                System.out.println(quark.toString());
            }
            System.out.println(recoveredQuarks.size());

        }
        catch(ClassNotFoundException ex){
        }
        catch(IOException ex){
        }
    }



    /*
        //StringWriter res = new StringWriter();
        // deserialize the list of Vehicules
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("result.ser"));
            ConcurrentLinkedQueue<Product> deserializedVehicles = (ConcurrentLinkedQueue<Product>) ois.readObject();
            ois.close();

            Iterator<Product> itr= deserializedVehicles.iterator();
            res.write("ProductName: " + ((Product)itr).getName() + " Product Id = " + ((Product)itr).getFinalId() + "\n" + "PartsList {");

            while(itr.hasNext()){
                res.write("ProductName: " + itr.next().getName() + " Product Id = " + itr.next().getFinalId() + "\n" + "PartsList {" );
            }

        } catch (Exception e) {
            e.printStackTrace(); // handle this appropriately
        }

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("result.txt"), "utf-8"))) {
            writer.write(String.valueOf(res));
        }
        catch (Exception ex){
            ex.printStackTrace(); // handle this appropriately
        }
*/

}