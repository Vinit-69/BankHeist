package tools;

import Custom.CustomHashMap;
import agents.Agent;

public class Kit extends Tools {
    public Kit() {
        this.toolName = "Hacker's kit";
        this.skillIncrement = 5;
    }

    @Override
    public void assignTools(Agent a1) {
        // Getting stats as a CustomHashMap
        CustomHashMap<String, Integer> map = a1.getStats();
        // Update the "Stealth" skill in the custom hashmap
        map.put("Stealth", map.get("Stealth") + skillIncrement);
        // Update the stats in the agent with the modified hashmap
        a1.setStats(map);
    }
}
