package bgu.spl.a2.sim.tasks;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wave {

    @SerializedName("product")
    @Expose
    private String product;
    @SerializedName("qty")
    @Expose
    private Integer qty;
    @SerializedName("startId")
    @Expose
    private Integer startId;

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

    public Integer getStartId() {
        return startId;
    }

    public void setStartId(Integer startId) {
        this.startId = startId;
    }

}
