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

    private Map<Tool, Integer> toolsAndQuantities;
    private List<ManufactoringPlan> plans;
    private final Object lock = new Object();
    private Map<String, List<Deferred<Tool>>> toolsToWaitingDeffereds;
    /**
     * Constructor
     */
    public Warehouse() {
        toolsAndQuantities = new HashMap<>();
        plans = new ArrayList<>();
        toolsToWaitingDeffereds = new HashMap<>();
    }

    /**
     * Tool acquisition procedure
     * Note that this procedure is non-blocking and should return immediately     *
     *
     * @param type - string describing the required tool
     * @return a deferred promise for the  requested tool
     */
    public Deferred<Tool> acquireTool(String type) {

        synchronized (lock) {
            Deferred<Tool> toolDeferred = new Deferred<>();

            for (Tool tool : toolsAndQuantities.keySet()) {
                if (tool.getType().equals(type)) {
                    reduceToolFromInventory(tool);
                    toolDeferred.resolve(tool);
                }
            }

            if (!toolDeferred.isResolved())
            {
                putDeferredInMap(type, toolDeferred);
            }

            return toolDeferred;
        }
    }

    private void putDeferredInMap(String type, Deferred<Tool> toolDeferred) {
        if (toolsToWaitingDeffereds.containsKey(type))
            toolsToWaitingDeffereds.get(type).add(toolDeferred);
        else
            {
            List<Deferred<Tool>> deferredTools = new ArrayList<>();
            deferredTools.add(toolDeferred);
            toolsToWaitingDeffereds.put(type, deferredTools);
        }
    }

    private void reduceToolFromInventory(Tool tool)
    {
        if (toolsAndQuantities.get(tool).equals(0))
            toolsAndQuantities.remove(tool);
        else
            toolsAndQuantities.put(tool, toolsAndQuantities.get(tool) - 1);
    }

    /**
     * Tool return procedure - releases a tool which becomes available in the warehouse upon completion.
     *
     * @param tool - The tool to be returned
     */
    public void releaseTool(Tool tool)
    {
        addToolToInventory(tool);
        resolveWaiting(tool);
    }

    private void resolveWaiting(Tool tool)
    {
        if (toolsToWaitingDeffereds.containsKey(tool.getType()))
            toolsToWaitingDeffereds.get(tool.getType()).remove(0).resolve(tool);
    }

    private void addToolToInventory(Tool tool) {
        if (!toolsAndQuantities.containsKey(tool))
            addTool(tool, 1);
        else
            addTool(tool, toolsAndQuantities.get(tool) + 1);
    }

    /**
     * Getter for ManufactoringPlans
     *
     * @param product - a string with the product name for which a ManufactoringPlan is desired
     * @return A ManufactoringPlan for product
     */
    public ManufactoringPlan getPlan(String product)
    {
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