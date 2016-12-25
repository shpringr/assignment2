package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;
import java.util.Random;

public class RandomSumPliers implements Tool {

    public String getType() {
        return "rs-pliers";
    }

    public long useOn(Product p) {
        Random rnd = new Random(p.getStartId());
        int count = (int) (p.getStartId() % 10000);
        long sum = 0;

        for (int i=0; i<=count; i++){
            long tmp = rnd.nextLong();
            sum += tmp;
        }

        return sum;
    }
}
