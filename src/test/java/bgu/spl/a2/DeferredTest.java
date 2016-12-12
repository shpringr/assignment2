package bgu.spl.a2;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


public class DeferredTest extends Deferred {

    private Deferred<Integer> deferredInt;
    private Deferred<String> deferredStr;
    private Deferred deferred;
    private Integer valueInt;
    private String valueStr;
    private Runnable rn;

    @Before
    public void setUp() {
        deferred = new Deferred<>();
        deferredInt = new Deferred<Integer>();
        deferredStr = new Deferred<String>();
        valueInt = 0;
        valueStr = "test";
        rn = new SimpleRunnable("testCallback");
    }

    @Test(expected = IllegalStateException.class)
    public void testGetExceptionString(){
        deferredStr.get();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetExceptionInt(){
        deferredInt.get();
    }

    @Test()
    public void testGet(){
        //test DeferredInt
        deferredInt.resolve(valueInt);
        assertEquals ("value are not the same", value , deferredInt.get());

        //test DeferredString
        deferredStr.resolve(valueStr);
        assertEquals ("value are not the same", value , deferredStr.get());

    }

    @Test
    public void testIsResolved() {

        //test DeferredInt
        assertFalse("Should be false",deferredInt.isResolved());
        deferredInt.resolve(valueInt);
        assertTrue("Should be true", deferredInt.isResolved());

        //test DeferredString
        assertFalse("Should be false",deferredStr.isResolved());
        deferredStr.resolve(valueStr);
        assertTrue("Should be true", deferredStr.isResolved());

    }

    @Test
    public void testResolveStrBefore() {
        try {
            deferredStr.get();
        }
        catch (IllegalStateException e)
        {
            deferredStr.resolve(valueStr);
            assertEquals ("value are not the same - String", valueStr , deferredStr.get());
        }
    }

    @Test
    public void testResolveIntBefore() {
        try {
            deferredInt.get();
        }
        catch (IllegalStateException e)
        {
            deferredInt.resolve(valueInt);
            assertEquals ("value are not the same - Int", valueInt , deferredInt.get());
        }
    }


    @Test(expected = IllegalStateException.class)
    public void testResolveStrAfter()
    {
            deferredStr.resolve(valueStr);
    }


    @Test(expected = IllegalStateException.class)
    public void testResolveIntAfter()
    {
        deferredInt.resolve(valueInt);
    }

    @Test
    public void testWhenResolvedIntFalse()
    {
        deferredInt.whenResolved(rn);
        assertTrue(deferredInt.getCallbacks().contains(rn));
    }


    @Test
    public void testWhenResolvedStrFalse()
    {
        deferredStr.whenResolved(rn);
        assertTrue(deferredStr.getCallbacks().contains(rn));
    }

    @Test
    public void testWhenResolvedIntTrue()
    {
        deferredInt.resolve(valueInt);
        deferredInt.whenResolved(rn);
        assertFalse(deferredInt.getCallbacks().contains(rn));
    }


    @Test
    public void testWhenResolvedStrTrue()
    {
        deferredStr.resolve(valueStr);
        deferredStr.whenResolved(rn);
        assertFalse(deferredStr.getCallbacks().contains(rn));
    }
}
