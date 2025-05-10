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

        CustomHashMap<String, Integer> map = a1.getStats();

        map.put("Stealth", map.get("Stealth") + skillIncrement);

        a1.setStats(map);
    }
}
