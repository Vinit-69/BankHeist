package tools;

import agents.Agent;

import java.util.Map;

public class Kit extends Tools{
    public Kit(){
        this.toolName = "Hacker's kit";
        this.skillIncrement = 5;
    }
    @Override
    public void assignTools(Agent a1){
        Map<String, Integer> map = a1.getStats();
        map.put("Stealth", map.get("Stealth") + 5);
        a1.setStats(map);
    }
}