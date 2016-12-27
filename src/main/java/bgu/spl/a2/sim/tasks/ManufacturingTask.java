package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tools.Tool;

import java.util.ArrayList;
import java.util.List;

public class ManufacturingTask extends Task<Product> {

    private Product product;
    private Warehouse warehouse;

    public ManufacturingTask(Product product, Warehouse warehouse) {
        this.product = product;
        this.warehouse = warehouse;
        //TODO:BORRAR
        check = product.getName();
    }

    @Override
    protected void start()
    {
        ManufactoringPlan plan = warehouse.getPlan(product.getName());

        if (plan.getParts().length > 0) {

            List<ManufacturingTask> manufacturedTasks = manfactureParts(plan.getParts());

            whenResolved(manufacturedTasks, () -> {
                        long allValues = calcFinalIdOfTools(plan.getTools());
                        product.setFinalID(product.getStartId() + allValues);
                        complete(product);
                    }
            );
        }
        else
        {
            product.setFinalID(product.getStartId());
            complete(product);
        }
    }

    private long calcFinalIdOfTools(String[] tools) {

        long finalId = 0;

        for (String toolType : tools) {
            Deferred<Tool> toolDeferred = warehouse.acquireTool(toolType);

            //TODO: hara
            // wait till it acquire the tool
            while (!toolDeferred.isResolved())
            {
                toolDeferred = warehouse.acquireTool(toolType);
            }

            finalId += toolDeferred.get().useOn(product);
            warehouse.releaseTool(toolDeferred.get());
        }

        return finalId;
    }

    private List<ManufacturingTask> manfactureParts(String[] plans)
    {
        List<ManufacturingTask> manufacturingTasks = new ArrayList<>();

        for (String partName : plans) {
            Product part = new Product(product.getStartId()+1, partName);
            product.addPart(part);
            ManufacturingTask manufacturingTask = new ManufacturingTask(part, warehouse);
            spawn(manufacturingTask);
            manufacturingTasks.add(manufacturingTask);
        }

        return manufacturingTasks;
    }
}