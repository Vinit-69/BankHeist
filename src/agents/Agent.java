package agents;

import Custom.CustomHashMap;

public abstract class Agent {
    protected String agentName;
    protected String[] traits;
    protected String[] skills;
    protected String uniqueSkill;
    protected String uniquePerk;
    protected CustomHashMap<String, Integer> stats;

    Agent(String agentName) {
        this.agentName = agentName;
        stats = new CustomHashMap<>();
        stats.put("Speed", 0);
        stats.put("Power", 0);
        stats.put("Stealth", 0);
    }

    public String getAgentName() {
        return agentName;
    }

    public String getUniqueSkill() {
        return uniqueSkill;
    }

    public String getUniquePerk() {
        return uniquePerk;
    }

    public String[] getTraits() {
        return traits;
    }

    public String[] getSkills() {
        return skills;
    }

    public CustomHashMap<String, Integer> getStats() {
        return stats;
    }

    public void setStats(CustomHashMap<String, Integer> map) {
        this.stats = map;
    }

    public void agentProfile() {
        System.out.println("Name: " + agentName);
        System.out.print("Special traits : ");
        for (String str : traits) System.out.print(str + " ");
        System.out.println();
        System.out.print("Agent Skills : ");
        for (String str : skills) System.out.print(str + " ");
        System.out.println();
        System.out.println("Agent Unique Skill : " + uniqueSkill);
        System.out.println("Agent Unique Perk : " + uniquePerk);
        System.out.println("Stats : ");
        for (String str : stats.keySet()) {
            System.out.println(str + " : " + stats.get(str));
        }
    }
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
}
