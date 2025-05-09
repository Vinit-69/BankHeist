package agents;

public class Hacker extends Agent {
    String sound;

    public Hacker(String agentName) {
        super(agentName);
        this.sound = "Synth";
        this.traits = new String[]{"Smart", "Subtle", "Finesse"};
        this.skills = new String[]{"Silent", "Fast", "Low-Power"};
        this.uniqueSkill = "Injection";
        this.uniquePerk = "Ghost Protocol (Turn Invisible)";
        stats.put("Speed", 9);
        stats.put("Power", 3);
        stats.put("Stealth", 6);
    }
}
