package bgu.spl.a2;

import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.tools.NextPrimeHammer;

import java.math.BigInteger;
import java.util.Random;

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

    public long useOn(Product p) {
        long value=0;
        for(Product part : p.getParts()){
            value+=Math.abs(gcd(part.getFinalId(), reverse(part.getFinalId())));
        }
        return value;
    }

    private static long gcd(long a, long b) {
        BigInteger b1 = BigInteger.valueOf(a);
        BigInteger b2 = BigInteger.valueOf(b);
        BigInteger gcd = b1.gcd(b2);
        return gcd.longValue();
    }

    private static long reverse(long input)
    {
        long reversedNum = 0;
        long input_long = input;

        while (input_long != 0)
        {
            reversedNum = reversedNum * 10 + input_long % 10;
            input_long = input_long / 10;
        }
        return reversedNum;
    }

    public static long randomP(long p) {
        Random rnd = new Random(p);
        int count = (int) (p % 10000);
        long sum = 0;

        for (int i=0; i<=count; i++){
            long tmp = rnd.nextInt();
            sum += tmp;
        }

        return sum;
    }

    public static void main(String[] args) {
    long id = 50123450;
    long value=0;

    value=Math.abs(gcd(id, reverse(id)));

    System.out.println((id)%10000);
    System.out.println(randomP(id));
    System.out.println(nextPrime(id));
    System.out.println(reverse(id));
    System.out.println(value);
    }


}
