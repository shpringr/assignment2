package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;
import java.util.Random;

public class RandomSumPliers implements Tool {

    public String getType() {
        return "rs-pliers";
    }


    public long useOn(Product p) {
        long value=0;
        for(Product part : p.getParts()){
            value+=Math.abs(randomP(part.getFinalId()));
        }
        return value;
    }


    private long randomP(long p) {
        Random rnd = new Random(p);
        int count = (int) (p % 10000);
        long sum = 0;

        for (int i=0; i<count; i++){
            long tmp = rnd.nextInt();
            sum += tmp;
        }

        return sum;
    }


}
