/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;

import bgu.spl.a2.WorkStealingThreadPool;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {
    /**
     * Begin the simulation
     * Should not be called before attachWorkStealingThreadPool()
     */
    public static ConcurrentLinkedQueue<Product> start() {
        //TODO: replace method body with real implementation
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }

    /**
     * attach a WorkStealingThreadPool to the Simulator, this WorkStealingThreadPool will be used to run the simulation
     *
     * @param myWorkStealingThreadPool - the WorkStealingThreadPool which will be used by the simulator
     */
    public static void attachWorkStealingThreadPool(WorkStealingThreadPool myWorkStealingThreadPool) {
        //TODO: replace method body with real implementation
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }

    public static int main(String[] args) {
        String jasonFileLocation = args[1];
        Gson gson = new Gson();

        try {

            BufferedReader br = new BufferedReader(
                    new FileReader(jasonFileLocation));

            //convert the json string back to object
            DataObject obj = gson.fromJson(br, DataObject.class);

            System.out.println(obj);

        } catch (IOException e) {
            e.printStackTrace();
        }



        //TODO: replace method body with real implementation
        throw new UnsupportedOperationException("Not Implemented Yet.");
    }

    public class GsonExample {
        public static void main(String[] args) {

            Gson gson = new Gson();

        }
    }


}
