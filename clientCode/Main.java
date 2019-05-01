package sample;

import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;

public class Main extends Application {

    HashMap<String, Scene> sceneMap;
    Scene loginScene,playScene;
    Button connectToGame;
    Label userNameLabel;
    TextField enterUserName;
    String grabUser;
    BorderPane loginPane,playerPane;

    private NetworkConnection  conn = createClient();
    private TextArea messages = new TextArea();

    private Parent createPlayScrene(){
        messages.clear();


    }


    private Parent createloginScene() {
        messages.setPrefSize(50,50);
        VBox a = new VBox(10,userNameLabel, enterUserName, connectToGame);
        loginPane.setBottom(messages);
        loginPane.setCenter(a);
        return loginPane;
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        sceneMap = new HashMap<String, Scene>();
        userNameLabel = new Label("User: ");
        enterUserName = new TextField();
        connectToGame = new Button("Connect");
        loginPane = new BorderPane();
        playerPane = new BorderPane();

        loginScene = new Scene(createloginScene(), 200, 200);
        sceneMap.put("Login", loginScene);

        primaryStage.setScene(sceneMap.get("Login"));
        primaryStage.show();




        connectToGame.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {

                grabUser = enterUserName.getText();
                try {
                    conn.send("Join " + grabUser);

                }
                catch(Exception e){
                    messages.appendText("Error type that in again\n");
                }
                if(!messages.getText().equals("User name currently in use")){

                }


            }

        });




    }





    @Override
    public void init() throws Exception{
        if(conn != null)
            conn.startConn();
    }

    @Override
    public void stop() throws Exception{
        if(conn != null)
            conn.closeConn();
    }

    private Client createClient() {

        return new Client("127.0.0.1", 5555, data -> {
            Platform.runLater(()->{
                messages.appendText(data.toString() + "\n");
            });
        });
    }





}
