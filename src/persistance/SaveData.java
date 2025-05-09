package persistance;

import Custom.CustomHashMap;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SaveData {
    private String name;
    private int agentType;
    private int toolType;

    // Constructor
    public SaveData(String name, int agentType, int toolType) {
        this.name = name;
        this.agentType = agentType;
        this.toolType = toolType;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public int getAgentType() {
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

    // Save the data to a file
    public void save() throws IOException {
        try (FileWriter fw = new FileWriter("data/savefile.txt", true)) {
            fw.write(name + "," + agentType + "," + toolType + "\n");
        }
    }

    // Override existing save file data
    public void overrideSave() throws IOException {
        File inputFile = new File("data/savefile.txt");
        File tempFile = new File("data/tempfile.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

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
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);
    }

    // Delete a save entry
    public static void delete(String name) throws IOException {
        File inputFile = new File("data/savefile.txt");
        File tempFile = new File("data/tempfile.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith(name + ",")) {
                    bw.write(line + "\n");
                }
            }
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);
    }

    // Load the most recent save from the file
    public static SaveData loadFromFile() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("data/savefile.txt"))) {
            String line = br.readLine();
            if (line == null || line.isBlank()) {
                throw new IOException("Empty Save File");
            }

            String[] str = line.split(",");
            if (str.length != 3) {
                throw new IOException("Invalid Save Format");
            }

            String name = str[0];
            int agentType = Integer.parseInt(str[1].trim());
            int toolType = Integer.parseInt(str[2].trim());

            return new SaveData(name, agentType, toolType);
        }
    }

    // Load all profiles from the save file
    public static List<SaveData> loadEveryProfile() throws IOException {
        List<SaveData> profiles = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("data/savefile.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip blank lines

                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String name = parts[0];
                    int agentType = Integer.parseInt(parts[1].trim());
                    int toolType = Integer.parseInt(parts[2].trim());
                    profiles.add(new SaveData(name, agentType, toolType));
                }
            }
        }

        return profiles;
    }

    // Additional helper method to load agent stats into CustomHashMap (if required in your context)
    public static CustomHashMap<String, Integer> loadAgentStatsFromSave(String name) throws IOException {
        List<SaveData> profiles = loadEveryProfile();
        for (SaveData profile : profiles) {
            if (profile.getName().equals(name)) {
                // Here, you could load agent stats based on `profile.getAgentType()`
                CustomHashMap<String, Integer> agentStats = new CustomHashMap<>();
                agentStats.put("Stealth", 10); // Example, you can adjust based on actual logic
                agentStats.put("Power", 5);
                return agentStats;
            }
        }
        return null; // Return null if not found
    }
}
