package ui;

import agents.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import persistance.SaveData;
import tools.*;

import java.io.IOException;
import java.util.*;

public class HeistPlannerGUI {

    public static HashMap<Agent, Tools> characterList = new HashMap<>();

    public Agent createCharacterGUI() throws IOException {
        Dialog<Agent> dialog = new Dialog<>();
        dialog.setTitle("Create Character");

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label classLabel = new Label("Class:");
        ComboBox<String> classBox = new ComboBox<>();
        classBox.getItems().addAll("Hacker", "BruteForcer");

        Label toolLabel = new Label("Tool:");
        ComboBox<String> toolBox = new ComboBox<>();
        toolBox.getItems().addAll("Hacker's Kit", "Whey Protein");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.addRow(0, nameLabel, nameField);
        grid.addRow(1, classLabel, classBox);
        grid.addRow(2, toolLabel, toolBox);

        dialog.getDialogPane().setContent(grid);
        ButtonType createButton = new ButtonType("Create", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButton, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButton) {
                String name = nameField.getText().trim();
                String charClass = classBox.getValue();
                String tool = toolBox.getValue();

                if (name.isEmpty() || charClass == null || tool == null) return null;

                Agent agent = charClass.equals("Hacker") ? new Hacker(name) : new BruteForcer(name);
                Tools toolObj = tool.equals("Hacker's Kit") ? new Kit() : new Protien();
                toolObj.assignTools(agent);
                characterList.put(agent, toolObj);

                int classCode = charClass.equals("Hacker") ? 1 : 2;
                int toolCode = tool.equals("Hacker's Kit") ? 2 : 1;
                SaveData data = new SaveData(name, classCode, toolCode);
                try {
                    data.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return agent;
            }
            return null;
        });

        Optional<Agent> result = dialog.showAndWait();
        return result.orElse(null);
    }

    public Agent displayCharactersGUI() {
        if (characterList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No existing characters.");
            alert.showAndWait();
            return null;
        }

        Dialog<Agent> dialog = new Dialog<>();
        dialog.setTitle("Select Character");

        ListView<Agent> listView = new ListView<>();
        listView.getItems().addAll(characterList.keySet());

        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Agent item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String agentType = item.getUniqueSkill().equals("Injection") ? "Hacker" : "BruteForcer";
                    setText(item.getAgentName() + " - " + agentType);
                }
            }
        });

        dialog.getDialogPane().setContent(listView);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return listView.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        Optional<Agent> result = dialog.showAndWait();
        return result.orElse(null);
    }

    public void deleteCharactersGUI() {
        if (characterList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No created characters.");
            alert.showAndWait();
            return;
        }

        ListView<Agent> listView = new ListView<>(FXCollections.observableArrayList(characterList.keySet()));
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Agent item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String agentType = item.getUniqueSkill().equals("Injection") ? "Hacker" : "BruteForcer";
                    setText(item.getAgentName() + " - " + agentType);
                }
            }
        });

        Button deleteButton = new Button("Delete Selected");
        deleteButton.setOnAction(e -> {
            Agent selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                characterList.remove(selected);
                try {
                    SaveData.delete(selected.getAgentName());
                    listView.getItems().remove(selected);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        VBox layout = new VBox(10, listView, deleteButton);
        layout.setPadding(new Insets(20));
        layout.setPrefSize(400, 300);

        Stage stage = new Stage();
        stage.setTitle("Delete Character");
        stage.setScene(new Scene(layout));
        stage.showAndWait();
    }

    public static void process() throws IOException {
        List<SaveData> dataList = SaveData.loadEveryProfile();
        SaveData data;
        for (SaveData saveData : dataList) {
            data = saveData;
            String name = data.getName();
            int agentType = data.getAgentType();
            int toolType = data.getToolType();

            Agent a;
            if (agentType == 1) {
                a = new Hacker(name);
            } else {
                a = new BruteForcer(name);
            }

            Tools t = (toolType == 1) ? new Protien() : new Kit();
            characterList.put(a, t);
        }
    }
}
