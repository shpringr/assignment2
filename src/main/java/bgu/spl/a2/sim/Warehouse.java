package bgu.spl.a2.sim;

import bgu.spl.a2.sim.tools.Tool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.Deferred;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class representing the warehouse in your simulation
 * <p>
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 */
public class Warehouse {

    Map<Tool, Integer> toolsAndQuantities;
    List<ManufactoringPlan> plans;
    /**
     * Constructor
     */
    public Warehouse() {
        toolsAndQuantities = new HashMap<>();
        plans = new ArrayList<>();
    }

    /**
     * Tool acquisition procedure
     * Note that this procedure is non-blocking and should return immediately     *
     * @param type - string describing the required tool
     * @return a deferred promise for the  requested tool
     */
    public Deferred<Tool> acquireTool(String type)
    {
        Deferred<Tool> toolDeferred = new Deferred<>();
        for(Tool tool : toolsAndQuantities.keySet()) {
                if (tool.getType().equals(type))
                {
                    reduceTool(tool);
                    toolDeferred.resolve(tool);
                }
        }

        if (!toolDeferred.isResolved())
        {
         toolDeferred.whenResolved(() -> {
             acquireTool(type);
         });
        }

        return toolDeferred;
    }

    private void reduceTool(Tool tool) {
        if (toolsAndQuantities.get(tool).equals(0))
            toolsAndQuantities.remove(tool);
        else
            toolsAndQuantities.put(tool, toolsAndQuantities.get(tool) - 1);
    }

    private void addTool(Tool tool) {
        if (!toolsAndQuantities.keySet().contains(tool))
            addTool(tool, 1);
        else
            addTool(tool,toolsAndQuantities.get(tool) + 1);
    }

    /**
     * Tool return procedure - releases a tool which becomes available in the warehouse upon completion.
     *
     * @param tool - The tool to be returned
     */
    public void releaseTool(Tool tool) {
        addTool(tool);
    }

    /**
     * Getter for ManufactoringPlans
     *
     * @param product - a string with the product name for which a ManufactoringPlan is desired
     * @return A ManufactoringPlan for product
     */
    public ManufactoringPlan getPlan(String product) {
        for (ManufactoringPlan plan : plans)
        {
            if (plan.getProductName().equals(product))
                return plan;
        }

        return null;
    }

    /**
     * Store a ManufactoringPlan in the warehouse for later retrieval
     *
     * @param plan - a ManufactoringPlan to be stored
     */
    public void addPlan(ManufactoringPlan plan) {
        plans.add(plan);
    }

    /**
     * Store a qty Amount of tools of type tool in the warehouse for later retrieval
     *
     * @param tool - type of tool to be stored
     * @param qty  - amount of tools of type tool to be stored
     */
    public void addTool(Tool tool, int qty) {
        toolsAndQuantities.put(tool, qty);
    }
}