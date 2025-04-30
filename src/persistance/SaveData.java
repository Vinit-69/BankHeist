package persistance;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SaveData {
    private String name;
    private int agentType;
    private int toolType;
    public SaveData(String name, int agentType, int toolType){
        this.name = name;
        this.agentType = agentType;
        this.toolType = toolType;
    }
    public String getName(){
        return name;
    }
    public int getAgentType(){
        return agentType;
    }

    public int getToolType() {
        return toolType;
    }

    public void setAgentType(int agentType) {
        this.agentType = agentType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setToolType(int toolType) {
        this.toolType = toolType;
    }
    public void save() throws IOException {
        String name = this.name;
        int agentType = this.agentType;
        int toolType = this.toolType;

        FileWriter fw = new FileWriter("data/savefile.txt", true);
        fw.write(name + "," + agentType + "," + toolType + "\n");
        fw.close();
    }
    public void overrideSave() throws IOException {
        File inputFile = new File("data/savefile.txt");
        File tempFile = new File("data/tempfile.txt");

        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));

        String line;
        boolean found = false;

        while ((line = br.readLine()) != null) {
            if (line.startsWith(name + ",")) {
                bw.write(name + "," + agentType + "," + toolType + "\n");
                found = true;
            } else {
                bw.write(line + "\n");
            }
        }

        if (!found) {
            bw.write(name + "," + agentType + "," + toolType + "\n");
        }

        br.close();
        bw.close();

        inputFile.delete();
        tempFile.renameTo(inputFile);
    }
    public static void delete(String name) throws IOException {
        File inputFile = new File("data/savefile.txt");
        File tempFile = new File("data/tempfile.txt");

        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));

        String line;
        while ((line = br.readLine()) != null) {
            if (!line.startsWith(name + ",")) {
                bw.write(line + "\n");
            }
        }

        br.close();
        bw.close();

        inputFile.delete();
        tempFile.renameTo(inputFile);
    }
    public static SaveData loadFromFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("data/savefile.txt"));
        String line = br.readLine();
        if(line.isBlank())  throw new IOException("Empty Save File");
        String []str = line.split(",");
        if(str.length != 3) throw new IOException("Invalid Save Format");
        String name = str[0];
        int agentType = Integer.parseInt((str[1]).trim());
        int toolType = Integer.parseInt((str[2]).trim());
        return new SaveData(name, agentType, toolType);
    }
    public static List<SaveData> loadEveryProfile() throws IOException {
        List<SaveData> profiles = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("data/savefile.txt"));
        String line;

        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue; // skip blank lines
            String[] parts = line.split(",");
            if (parts.length == 3) {
                String name = parts[0];
                int agentType = Integer.parseInt(parts[1].trim());
                int toolType = Integer.parseInt(parts[2].trim());
                profiles.add(new SaveData(name, agentType, toolType));
            }
        }

        br.close();
        return profiles;
    }

}
