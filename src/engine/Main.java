package src.engine;
import HeistPlanner.HeistPlanner;
import agents.*;
import tools.*;
import security.*;
import java.io.*;
import persistance.*;
import java.util.*;
public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        HeistPlanner planA = new HeistPlanner();
        Agent a1 = null;
        Tools t1;
        while(true){
            HeistPlanner.process();
            while(true){
                System.out.println("1. Play");
                System.out.println("2. Exit");
                int choice = sc.nextInt();
                if(choice == 1){
                    break;
                }
                else if(choice == 2){
                    return;
                }
                else{
                    System.out.println("Invalid Choice");
                }
            }
            while(true){
                System.out.println("1. Create New Character");
                System.out.println("2. Choose Existing Character");
                int choice = sc.nextInt();
                if(choice == 1){
                    a1 = planA.createCharacter(null);
                    break;
                }
                else if(choice == 2){
                    if(HeistPlanner.characterList.isEmpty()){
                        System.out.println("No existing Characters");
                    }
                    else{
                        //problem here, causing skipping of loop, or if no character choosen, throws exception.
                        a1 = planA.displayCharacters();
                        if(a1 == null)  continue;
                        break;
                    }
                }
                else{
                    System.out.println("Invalid Choice");
                }
            }
            System.out.println("1. Display Character Profile");
            System.out.println("2. Exit Game");
            int choice = sc.nextInt();
            if(choice == 1){
                assert a1 != null;
                a1.agentProfile();
            }
            return;
        }
    }
}