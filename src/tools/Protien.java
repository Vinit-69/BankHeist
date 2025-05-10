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

        CustomHashMap<String, Integer> map = a1.getStats();

        map.put("Power", map.get("Power") + skillIncrement);

        a1.setStats(map);
    }
}
