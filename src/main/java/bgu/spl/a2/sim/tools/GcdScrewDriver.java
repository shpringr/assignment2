package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;
import java.math.*;

public class GcdScrewDriver implements Tool {

    public String getType() {
        return "gs-driver";
    }

    public long useOn(Product p) {
        return gcdThing(p.getStartId(), reverse(p.getStartId()));
    }

    private long gcdThing(long a, long b) {
        BigInteger b1 = BigInteger.valueOf(a);
        BigInteger b2 = BigInteger.valueOf(b);
        BigInteger gcd = b1.gcd(b2);
        return gcd.longValue();
    }

    private long reverse(long input)
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

}
