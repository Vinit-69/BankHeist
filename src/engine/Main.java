package engine;
import java.util.*;
public class Main {
    static abstract class Agent{
        protected String agentName;
        protected String[] traits;
        protected String[] skills;
        protected String uniqueSkill;
        protected String uniquePerk;
        protected Map<String, Integer> stats;
        Agent(String agentName) {
            this.agentName = agentName;
            stats = new HashMap<>();
            stats.put("Speed", 0);
            stats.put("Power", 0);
            stats.put("Stealth", 0);
        }
        public String getAgentName(){
            return agentName;
        }
        public String getUniqueSkill(){
            return uniqueSkill;
        }
        public String getUniquePerk(){
            return uniquePerk;
        }
        public String[] getTraits(){
            return traits;
        }
        public String[] getSkills(){
            return skills;
        }
        public Map<String, Integer> getStats(){
            return stats;
        }
        void setStats(Map<String, Integer> map){
            this.stats = map;
        }
        void agentProfile(){
            System.out.println("Name: " + agentName);
            System.out.print("Special traits : ");
            for(String str:traits)  System.out.print(str + " ");
            System.out.println();
            System.out.print("Agent Skills : ");
            for(String str:skills)  System.out.print(str + " ");
            System.out.println();
            System.out.println("Agent Unique Skill : " + uniqueSkill);
            System.out.println("Agent Unique Perk : " + uniquePerk);
            System.out.println("Stats : ");
            for(String str:stats.keySet()){
                System.out.println(str + " : " + stats.get(str));
            }
        }
    }
    static class Hacker extends Agent{
        String sound;
        Hacker(String agentName){
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
    static class BruteForcer extends Agent{
        String sound;
        BruteForcer(String agentName){
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
    static abstract class Tools{
        String toolName;
        int skillIncrement;
        void assignTools(Agent a1){};
    }
    static class Kit extends Tools{
        Kit(){
            this.toolName = "Hacker's kit";
            this.skillIncrement = 5;
        }
        @Override
        void assignTools(Agent a1){
            Map<String, Integer> map = a1.getStats();
            map.put("Stealth", map.get("Stealth") + 5);
            a1.setStats(map);
        }
    }
    static class Protien extends Tools{
        Protien(){
            this.toolName = "Whey Protien";
            this.skillIncrement = 5;
        }
        @Override
        void assignTools(Agent a1){
            Map<String, Integer> map = a1.getStats();
            map.put("Power", map.get("Power") + 5);
            a1.setStats(map);
        }
    }
    static abstract class SecurityNodes{
        String securityName;
        int skillCheck;
        Boolean check(Map<String, Integer> map){
            return false;
        }
    }
    static class Firewall extends SecurityNodes{
        Firewall(){
            this.securityName = "Firewall";
            this.skillCheck = 3;
        }
        @Override
        Boolean check(Map<String, Integer> map){
            return map.get("Power") >= skillCheck || map.get("Stealth") >= skillCheck;
        }
    }
    static class Lasers extends SecurityNodes{
        Lasers(){
            this.securityName = "Lasers";
            this.skillCheck = 5;
        }
        @Override
        Boolean check(Map<String, Integer> map){
            return map.get("Speed") >= skillCheck || map.get("Stealth") >= skillCheck;
        }
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Hello World");
        System.out.print("Enter Your Agent's Name : ");
        String name = sc.nextLine();
        Agent a1 = new BruteForcer(name);
        a1.agentProfile();
        Tools t1 = new Kit();
        t1.assignTools(a1);
        SecurityNodes s1 = new Firewall();
        System.out.println(s1.check(a1.getStats()));
        a1.agentProfile();
    }
}