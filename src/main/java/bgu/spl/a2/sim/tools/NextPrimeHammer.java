package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;


public class NextPrimeHammer implements Tool {

    public String getType() {
        return "np-hammer";
    }

    public long useOn(Product p) {
        long value=0;
        for(Product part : p.getParts()){
            value+=Math.abs(nextPrime(part.getFinalId()));
        }
        return value;
    }

    private long nextPrime(long next) {
        boolean isPrime = false;
        int start = 2;

        while (!isPrime) {
            next += 1;
            int m = (int) Math.ceil(Math.sqrt(next));


            isPrime = true;
            for (int i = start; i <= m; i++) {
                if (next % i == 0) {
                    isPrime = false;
                    break;
                }
            }
        }
        return next;
    }
}
