package bgu.spl.a2.sim.json;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Order {

    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("qty")
    @Expose
    private Integer qty;
    @SerializedName("startId")
    @Expose
    private Long startId;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Long getStartId() {
        return startId;
    }

    public void setStartId(Long startId) {
        this.startId = startId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "product='" + product + '\'' +
                ", qty=" + qty +
                ", startId=" + startId +
                '}';
    }

    //
//    public Map<String, Integer> getProductsAndQuantities() {
//        return productsAndQuantities;
//    }
//
//    public void setProductsAndQuantities(Map<String, Integer> productsAndQuantities) {
//        this.productsAndQuantities = productsAndQuantities;
//    }
}
