package tools;

import Custom.CustomHashMap;
import agents.Agent;

public abstract class Tools {
    String toolName;
    int skillIncrement;

    // Updated to accept CustomHashMap
    public void assignTools(Agent a1) {}

    public String getToolName() {
        return toolName;
    }

    public int getSkillIncrement() {
        return skillIncrement;
    }
}
