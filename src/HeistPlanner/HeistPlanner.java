package HeistPlanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import agents.*;
import persistance.SaveData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import agents.*;
import tools.*;

public class HeistPlanner {
    Scanner sc = new Scanner(System.in);
    public static HashMap<Agent, Tools> characterList = new HashMap<>();
    public Agent createCharacter(Agent a1) throws IOException {
    public Agent createCharacter(Agent a1){
        int charClass;
        String name;
        int toolChoice;
        Tools t1;

        while(true){
            System.out.println("Choose Character Name: ");
            name = sc.nextLine();
            while(true) {
                System.out.println("Choose Character Class: ");
                System.out.println("1. Hacker");
                System.out.println("2. BruteForcer");
                charClass = sc.nextInt();
                if(charClass == 1 || charClass == 2)    break;
                System.out.println("Please Choose 1 or 2");
            }
            while(true){
                System.out.println("Choose a tool to help you in the heist: ");
                System.out.println("1. Hacker's Kit");
                System.out.println("2. Whey Protein");
                toolChoice = sc.nextInt();
                if(toolChoice == 1 || toolChoice == 2)  break;
                System.out.println("Please Choose 1 or 2");
            }
            System.out.println("1. Register Agent");
            System.out.println("2. Restart");
            int createChoice = sc.nextInt();
            if(createChoice == 2)   continue;
            if(charClass == 1)  a1 = new Hacker(name);
            else a1 = new BruteForcer(name);
            if(toolChoice == 1) t1 = new Kit();
            else t1 = new Protien();
            t1.assignTools(a1);
            characterList.put(a1, t1);
            SaveData data = new SaveData(name, charClass, toolChoice);
            data.save();
            break;
        }
        return a1;
    }
    public void deleteCharacter(Agent a1) throws IOException {
        characterList.remove(a1);
        SaveData.delete(a1.getAgentName());
    }
    public void editCharacter(Agent a1) throws IOException {
    public void deleteCharacter(Agent a1){
        characterList.remove(a1);
    }
    public void editCharacter(Agent a1){
        while(true){
            System.out.println("1. Change Name ");
            System.out.println("2. Change Class ");
            System.out.println("3. Change Tools ");
            System.out.println("4. Exit");
            int choice = sc.nextInt();
            int classChoice = 0;
            int toolChoice = 0;
            String name = "Default";
            Tools t2;
            switch(choice){
                case 1:
                    System.out.println("Enter New Name: ");
                    name = sc.nextLine();
                    a1.setAgentName(name);
                    if(a1.getUniqueSkill().equals("Injection"))  classChoice = 1;
                    else classChoice = 2;
                    t2 = characterList.get(a1);
                    if(t2.getToolName().equals("Hacker's Kit")) toolChoice = 2;
                    else toolChoice = 1;
            switch(choice){
                case 1:
                    System.out.println("Enter New Name: ");
                    String name = sc.nextLine();
                    a1.setAgentName(name);
                    break;
                case 2:
                    System.out.println("1. Hacker");
                    System.out.println("2. BruteForcer");
                    classChoice = sc.nextInt();
                    int classChoice = sc.nextInt();
                    switch(classChoice){
                        case 1:
                            a1 = new Hacker(a1.getAgentName());
                            characterList.replace(a1, characterList.get(a1));
                            break;
                        case 2:
                            a1 = new BruteForcer((a1.getAgentName()));
                            characterList.replace(a1, characterList.get(a1));
                            break;
                        default:
                            System.out.println("Invalid Choice");
                    }
                    t2 = characterList.get(a1);
                    if(t2.getToolName().equals("Hacker's Kit")) toolChoice = 2;
                    else toolChoice = 1;
                    name = a1.getAgentName();
                    break;
                case 3:
                    System.out.println("1. Hacker's Kit");
                    System.out.println("2. Whey Protein");
                    toolChoice = sc.nextInt();
                    int toolChoice = sc.nextInt();
                    Tools t1 = characterList.get(a1);
                    switch(toolChoice){
                        case 1:
                            t1 = new Kit();
                            characterList.replace(a1, t1);
                            break;
                        case 2:
                            t1 = new Protien();
                            characterList.replace(a1, t1);
                            break;
                        default:
                            System.out.println("Invalid Choice");
                    }
                    if(a1.getUniqueSkill().equals("Injection"))  classChoice = 1;
                    else classChoice = 2;
                    name = a1.getAgentName();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid Choice");
            }
            SaveData data = new SaveData(name, classChoice, toolChoice);
            data.overrideSave();
        }
    }
    public Agent displayCharacters(){
        if(characterList.isEmpty()){
            System.out.println("No Characters Created Yet!");
            return null;
        }
        int index = 1;
        for(Agent a1: characterList.keySet()){
            System.out.print(index + ". " + a1.getAgentName() + " ");
            if(a1.getUniqueSkill().equals("Injection")){
                System.out.println("Agent Class: " + "Hacker");
            }
            else{
                System.out.println("Agent Class: " + "BruteForcer");
            }
            index++;
            System.out.println();
            System.out.println("1. Choose Character");
            System.out.println("2. Next Character");
            int choose = sc.nextInt();
            if(choose == 1) return a1;
        }
        System.out.println("No Character Choosen");
        return null;
    }
    public static void process() throws IOException {
        List<SaveData> dataList = SaveData.loadEveryProfile();
        SaveData data;
        for (SaveData saveData : dataList) {
            data = saveData;
            String name = data.getName();
            int agentType = data.getAgentType();
            int toolType = data.getToolType();
            if (agentType == 1) {
                Agent a = new Hacker(name);
                Tools t;
                if (toolType == 1) {
                    t = new Protien();

                } else {
                    t = new Kit();
                }
                HeistPlanner.characterList.put(a, t);
            } else {
                Agent a = new BruteForcer(name);
                Tools t;
                if (toolType == 1) {
                    t = new Protien();

                } else {
                    t = new Kit();
                }
                HeistPlanner.characterList.put(a, t);
            }
        }
    }
}
