package bgu.spl.a2;

import bgu.spl.a2.sim.tools.NextPrimeHammer;

/**
 * Created by ROTEM on 27/12/2016.
 */
public class MainTest {

    private static long nextPrime(long next) {
        boolean isPrime = false;
        long start = 2;

        while (!isPrime) {
            next += 1;
            long m = (int) Math.ceil(Math.sqrt(next));


            isPrime = true;
            for (long i = start; i <= m ; i++) {
                if (next % i == 0) {
                    isPrime = false;
                    break;
                }
            }
        }
        return next;
    }


    public static void main(String[] args) {
    long id = 502345607;
    System.out.print(nextPrime(id));
    }


}
