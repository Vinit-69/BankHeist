package agents;

public class BruteForcer extends Agent {
    String sound;

    public BruteForcer(String agentName) {
        super(agentName);
        this.sound = "Metal";
        this.traits = new String[]{"Relentless", "Unstoppable", "Powerful"};
        this.skills = new String[]{"Noisy", "Slow", "High-Power"};
        this.uniqueSkill = "Crash";
        this.uniquePerk = "Overclock (High Damage, High Risk)";
        stats.put("Speed", 5);
        stats.put("Power", 10);
        stats.put("Stealth", 3);
    }
}
