package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class Main extends Application {
    private int size = 0; //要分配的内存大小
    private BuddySystem buddySystem = new BuddySystem(8);

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("伙伴系统");
        BorderPane pane = new BorderPane();
        FlowPane top = new FlowPane();
        Pane center = new StackPane();
        center.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
        Rectangle button = new Rectangle(60, 100, Color.BLUEVIOLET);
        center.setMaxHeight(100);
        Text text = new Text("滑动以选择大小");
        text.setFont(new Font(40));
        Pane centerTop = new Pane();
        center.getChildren().addAll(centerTop,text);
        centerTop.getChildren().add(button);
        GridPane bottom = new GridPane();
        bottom.setHgap(10);
        bottom.setVgap(10);
        bottom.setPadding(new Insets(10));

        //为了打印底部空闲内存情况
        int length = buddySystem.getFree_area().length;
        List<Pane> bottomRightHBox = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            Text numText = new Text(i + "");
            numText.setFont(new Font(30));
            bottom.add(numText, 0, i);
            Pane rightHBox = new HBox();
            ((HBox) rightHBox).setSpacing(10);
            bottom.add(rightHBox, 1, i);
            bottomRightHBox.add(rightHBox);
        }
        pane.setTop(top);
        pane.setCenter(center);
        pane.setBottom(bottom);
        top.setPadding(new Insets(10));
        top.setHgap(10);
        top.setVgap(10);
        Scene scene = new Scene(pane,1280,720);
        primaryStage.setScene(scene);
        primaryStage.show();
        flushFreeMem(bottomRightHBox,buddySystem.getFree_area(),scene);

        double height = 50;



        button.setOnMouseDragged((e) -> {
            double x = e.getX();
            double max = center.getWidth() - button.getWidth();
            if (x < 0) x = 0;
            else if (x > max) x = max;
            else x = e.getX();
            button.setX(x);
            size = (int) (x / max * Math.pow(2, buddySystem.getN()));
            text.setFill(Color.BLACK);
            text.setText("松开以分配 " + size + " 字节 内存");

        });

        button.setOnMouseReleased((e) -> {
            if (size != 0) {
                int n = (int) Math.ceil(MyMath.log2(size));
                if (n > buddySystem.getN()) {
                    System.err.println("超过最大内存!");
                } else if (buddySystem.f(n, n, size)) {
                    StackPane backPane = new StackPane();
                    Pane memPane = new Pane();
                    int trueSize = (int) Math.pow(2, n);
                    backPane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
                    backPane.setPrefWidth((height * trueSize) / 5);
                    backPane.prefWidthProperty().bind(scene.widthProperty().multiply(trueSize).divide(Math.pow(2,buddySystem.getN())));
                    Text needSizeSize = new Text(size + "/" + trueSize);
                    needSizeSize.setFont(new Font(20));
                    needSizeSize.setFill(Color.WHITE);
                    Rectangle rectangle = new Rectangle((height * size) / 5, height, Color.RED);
                    rectangle.widthProperty().bind(scene.widthProperty().multiply(size).divide(Math.pow(2,buddySystem.getN())));

                    backPane.setOnMouseClicked((event) -> {
                        int index = top.getChildren().indexOf(backPane);
                        top.getChildren().remove(index);
                        buddySystem.g(buddySystem.getUsedMem().remove(index));
                        flushFreeMem(bottomRightHBox,buddySystem.getFree_area(),scene);

//                        for (List list : buddySystem.getFree_area()) {
//                            System.out.printf("%-4d", list.size());
//                        }
//                        System.out.println();
                    });
                    memPane.getChildren().add(rectangle);
                    backPane.getChildren().add(memPane);
                    if(trueSize >= 16) backPane.getChildren().add(needSizeSize);
                    top.getChildren().add(backPane);
                    text.setText("滑动以选择大小");
                } else {
                    text.setFill(Color.RED);
                    text.setText("内存不足！");
                }
                flushFreeMem(bottomRightHBox,buddySystem.getFree_area(),scene);
//                for (List list : buddySystem.getFree_area()) {
//                    System.out.printf("%-4d", list.size());
//                }
//                System.out.println();
            }
            size = 0;
            button.setX(0);
        });
    }
    private void flushFreeMem(List<Pane> panes,List<MemBlock>[] freeArea,Scene scene){

        for(int i = 0; i < freeArea.length; i++){
            panes.get(i).getChildren().removeAll(panes.get(i).getChildren());
            for(MemBlock block : freeArea[i]){
                long size = (long) Math.pow(2,i);
                Pane freeMemPane = Tool.getFreeMemPane(Color.GREEN,size + "");
                freeMemPane.prefWidthProperty().bind(scene.widthProperty().multiply(size).divide(Math.pow(2,buddySystem.getN())));
                panes.get(i).getChildren().add(freeMemPane);
            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
