package tools;

import Custom.CustomHashMap;
import agents.Agent;

public class Protien extends Tools {
    public Protien() {
        this.toolName = "Whey Protien";
        this.skillIncrement = 5;
    }

    @Override
    public void assignTools(Agent a1) {
        // Getting stats as a CustomHashMap
        CustomHashMap<String, Integer> map = a1.getStats();
        // Update the "Power" skill in the custom hashmap
        map.put("Power", map.get("Power") + skillIncrement);
        // Update the stats in the agent with the modified hashmap
        a1.setStats(map);
    }
}
