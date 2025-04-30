package tools;

import agents.Agent;

import java.util.Map;

public class Protien extends Tools{
    public Protien(){
        this.toolName = "Whey Protien";
        this.skillIncrement = 5;
    }
    @Override
    public void assignTools(Agent a1){
        Map<String, Integer> map = a1.getStats();
        map.put("Power", map.get("Power") + 5);
        a1.setStats(map);
    }
}