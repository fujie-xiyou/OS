package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;


public class Main extends Application {
    private int size = 0; //要分配的内存大小

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");

        BorderPane pane = new BorderPane();
        FlowPane top = new FlowPane();
        Pane center = new StackPane();
        center.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
        Rectangle button = new Rectangle(60,100,Color.BLUEVIOLET);
        center.setMaxHeight(100);
        Text text = new Text("滑动以选择大小");
        text.setFont(new Font(40));
        Pane centerTop = new Pane();
        center.getChildren().addAll(text,centerTop);
        centerTop.getChildren().add(button);
        GridPane bottom = new GridPane();

        BuddySystem buddySystem = new BuddySystem(7);

        double height = 50;

        button.setOnMouseDragged((e) -> {
            double x = e.getX();
            double max = center.getWidth() - button.getWidth();
            if (x < 0) x = 0;
            else if (x > max) x = max;
            else x = e.getX();
            button.setX(x);
            size = (int) (x / max * Math.pow(2,buddySystem.getN()));
            text.setFill(Color.BLACK);
            text.setText("松开以分配 " +  size+" 字节 内存");

        });

        button.setOnMouseReleased((e) -> {
            if(size != 0) {
                int n = (int) Math.ceil(MyMath.log2(size));
                if (n > buddySystem.getN()) {
                    System.err.println("超过最大内存!");
                } else if (buddySystem.f(n, n, size)) {
                    StackPane backPane = new StackPane();
                    Pane memPane = new Pane();
                    int trueSize = (int)Math.pow(2,n);
                    backPane.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));
                    backPane.setPrefWidth((height * trueSize) / 5);
                    Text needSizeSize = new Text(size +"/"+trueSize);
                    needSizeSize.setFont(new Font(20));
                    needSizeSize.setFill(Color.WHITE);
                    Rectangle rectangle = new Rectangle((height * size) / 5, height,Color.GREEN);
                    backPane.setOnMouseClicked((event) -> {
                        int index = top.getChildren().indexOf(backPane);
                        top.getChildren().remove(index);
                        buddySystem.g(buddySystem.getUsedMem().remove(index));
                        for (List list : buddySystem.getFree_area()){
                            System.out.printf("%-4d",list.size());
                        }
                        System.out.println();
                    });
                    memPane.getChildren().add(rectangle);
                    backPane.getChildren().addAll(memPane,needSizeSize);
                    top.getChildren().add(backPane);
                    text.setText("滑动以选择大小");
                } else {
                    text.setFill(Color.RED);
                    text.setText("内存不足！");
                }
                for (List list : buddySystem.getFree_area()){
                    System.out.printf("%-4d",list.size());
                }
                System.out.println();
            }
            size = 0;
            button.setX(0);
        });
        bottom.setHgap(10);
        bottom.setVgap(10);
        bottom.setPadding(new Insets(10));
        for(int i = 0; i < buddySystem.getFree_area().length; i++){
            Text numText = new Text(i + "");
            numText.setFont(new Font(30));
            bottom.add(numText,0,i);
            Pane freeMemStackPane = new StackPane();
            Pane freeMemPane = new Pane();
            Text freeMem = new Text("?");
            freeMem.setFont(new Font(30));
            freeMemStackPane.getChildren().addAll(freeMemPane,freeMem);
            Rectangle rectangle = new Rectangle(200,30,Color.GREEN);
            freeMemPane.getChildren().add(rectangle);
            freeMemPane.getChildren().add(new Rectangle(300,30,Color.GREEN));
            bottom.add(freeMemStackPane,1,i);
        }
        pane.setTop(top);
        pane.setCenter(center);
        pane.setBottom(bottom);
        top.setPadding(new Insets(10));
        top.setHgap(10);
        top.setVgap(10);
        primaryStage.setScene(new Scene(pane, 1280, 720));

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
