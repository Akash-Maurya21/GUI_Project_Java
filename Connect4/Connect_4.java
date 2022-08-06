package com.myGame.Connect4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Connect_4 extends Application {
    private Controller controller;
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game_layout.fxml"));
        GridPane rootNode = loader.load();
        controller=loader.getController();
        controller.createPlayground();
        MenuBar myMenu=createMenu();
        Pane menuPane=(Pane)rootNode.getChildren().get(0);
        myMenu.prefWidthProperty().bind(primaryStage.widthProperty());
        menuPane.getChildren().add(myMenu);
        Scene scene = new Scene(rootNode);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect_4");
        primaryStage.show();
    }
    private MenuBar createMenu()
    {
        Menu fileMenu=new Menu("File");
        MenuItem newGame=new MenuItem("New Game");
        newGame.setOnAction(event->controller.resetGame());
        final MenuItem reset=new MenuItem("Reset Game");
        reset.setOnAction(event->controller.resetGame());
        SeparatorMenuItem separatorMenuItem=new SeparatorMenuItem();
        MenuItem exit=new MenuItem("Exit");
        exit.setOnAction(actionEvent -> exitGame());
        fileMenu.getItems().addAll(newGame,reset,exit);
        MenuBar menuBar=new MenuBar();

        Menu helpMenu=new Menu("Help");

        MenuItem aboutGame=new MenuItem("About Connect_4");
        aboutGame.setOnAction(actionEvent -> {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect_4");
        alert.setHeaderText("How To Play ?");
        alert.setContentText("Connect Four is a two-player connection game in which\n" +
                " the players first choose a " +
                "color and then take turns dropping colored discs\n from the top into a " +
                "seven-column, six-row vertically\n suspended grid. " +
                "The pieces fall straight down, occupying the next available \nspace within the column. " +
                "The objective of the game is to be the \nfirst to form a horizontal, vertical, or " +
                "diagonal line of \nfour of one's own discs. " +
                "Connect Four is a solved game.\n " +
                "The first player can always win by playing the right moves.");
        alert.show();
        });
        MenuItem aboutMe=new MenuItem("About Me");
        aboutMe.setOnAction(actionEvent -> {
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About The Developer");
            alert.setHeaderText("Akash Maurya");
            alert.setContentText("Coder Lover");
            alert.show();
        });
        helpMenu.getItems().addAll(aboutGame,aboutMe);
        menuBar.getMenus().addAll(fileMenu,helpMenu);
        return menuBar;
    }
    private void exitGame()
    {
        Platform.exit();
        System.exit(0);
    }


    public static void main(String[] args) {
        launch(args);
    }
}