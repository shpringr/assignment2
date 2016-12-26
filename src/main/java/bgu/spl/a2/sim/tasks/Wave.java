package bgu.spl.a2.sim.tasks;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class Wave {
//
//    @SerializedName("product")
//    @Expose
//    private String product;
//    @SerializedName("qty")
//    @Expose
//    private Integer qty;
    @SerializedName("startId")
    @Expose
    private Integer startId;
    private Map<String, Integer> productsAndQuantities;

//    public String getProduct() {
//        return product;
//    }
//
//    public void setProduct(String product) {
//        this.product = product;
//    }
//
//    public Integer getQty() {
//        return qty;
//    }
//
//    public void setQty(Integer qty) {
//        this.qty = qty;
//    }

    public Integer getStartId() {
        return startId;
    }

    public void setStartId(Integer startId) {
        this.startId = startId;
    }

    public Map<String, Integer> getProductsAndQuantities() {
        return productsAndQuantities;
    }

    public void setProductsAndQuantities(Map<String, Integer> productsAndQuantities) {
        this.productsAndQuantities = productsAndQuantities;
    }
}
