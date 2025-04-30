package tools;
import java.util.Map;
import agents.*;

public abstract class Tools{
    String toolName;
    int skillIncrement;
    public void assignTools(Agent a1){};
    public String getToolName(){
        return toolName;
    }

    public int getSkillIncrement() {
        return skillIncrement;
    }
}

