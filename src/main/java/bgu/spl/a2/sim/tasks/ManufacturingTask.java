package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Orel Hazan on 25/12/2016.
 */
public class ManufacturingTask extends Task<Product> {

    private Product product;

    public ManufacturingTask(Product product)
    {
        this.product = product;
    }

    @Override
    protected void start() {
        if (product.getParts().size() > 0) {

            List<ManufacturingTask> manufacturingTasks = new ArrayList<>();
            for (Product part : product.getParts()) {

                ManufacturingTask manufacturingTask = new ManufacturingTask(part);
                manufacturingTasks.add(manufacturingTask);
            }
            whenResolved(manufacturingTasks, () -> {
                        int finalId = 0;
                        for (ManufacturingTask task : manufacturingTasks) {
                            finalId += task.getResult().get().getFinalId();
                        }
                            ///ADD TOOLS
                        product.assembly(finalId);
                        complete(product);
                    }
            );
        }
        else
            {
                product.assembly(product.getStartId() //+ all tools.useOn
                         );
                complete(product);
        }
    }
}