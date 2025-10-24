/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxapplication1;

/**
 *
 * @author BEST SOLUTION SALEM
 */
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class CloudStorageApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cloud Storage Navigator");

        // Create the root item
        TreeItem<String> rootItem = new TreeItem<>("Cloud Root");
        rootItem.setExpanded(true);

        // Sample folders and files
        TreeItem<String> documents = createFolder("Documents");
        TreeItem<String> photos = createFolder("Photos");
        TreeItem<String> videos = createFolder("Videos");

        // Add files to Documents
        documents.getChildren().addAll(
                createFile("Resume.docx"),
                createFile("ProjectProposal.pdf")
        );

        // Add subfolders and files to Photos
        TreeItem<String> vacation = createFolder("Vacation");
        vacation.getChildren().addAll(
                createFile("Beach.png"),
                createFile("Mountains.jpg")
        );
        photos.getChildren().addAll(vacation, createFile("ProfilePic.jpg"));

        // Add files to Videos
        videos.getChildren().add(createFile("DemoVideo.mp4"));

        // Add main folders to root
        rootItem.getChildren().addAll(documents, photos, videos);

        // Create TreeView
        TreeView<String> treeView = new TreeView<>(rootItem);

        // Selection listener
        treeView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("Selected: " + newSelection.getValue());
            }
        });

        BorderPane root = new BorderPane();
        root.setCenter(treeView);

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Helper methods to create folders and files
    private TreeItem<String> createFolder(String name) {
        TreeItem<String> folder = new TreeItem<>(name);
        folder.setExpanded(false);
        return folder;
    }

    private TreeItem<String> createFile(String name) {
        return new TreeItem<>(name);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
