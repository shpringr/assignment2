package bgu.spl.a2.sim.tasks;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Wave {

    private List<Order> orders = null;

    public Wave(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "Wave{" +
                "orders=" + orders +
                '}';
    }
}
