package sample;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Tool {
    public static Pane getFreeMemPane(Color color,String text){
      //  Pane freeMemPane = new Pane(new Rectangle(width,30, color));
        Text freeMem = new Text(text);
        freeMem.setFont(new Font(30));
        Pane freeMemStackPane = new StackPane(freeMem);
        freeMemStackPane.setBackground(new Background(new BackgroundFill(color,null,null)));
        return freeMemStackPane;
    }
}
