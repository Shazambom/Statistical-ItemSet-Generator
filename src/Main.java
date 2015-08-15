import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * Created by Shazambom on 8/11/2015.
 */
public class Main extends Application {
    public void start(Stage stage) {
        stage.setMinWidth(350);
        stage.setMinHeight(200);

        TextField filePath = new TextField("Put file path here");
        filePath.setMinWidth(170);
        TextField champName = new TextField("Put Champion's name here");
        champName.setMinWidth(170);
        Text info = new Text("Type in the file path to the parent directory that \nis holding all of the item sets to be analyzed.\nThe new item set will be put in that directory\nalong with an analysis of the other item sets.");
        TextField numItems = new TextField("# of items in new item set");
        numItems.setMinWidth(155);


        Button submit = new Button("Generate Item Set");
        submit.setOnAction(event -> {
            ItemSetGenerator gen = new ItemSetGenerator(filePath.getText(),
                    champName.getText(), Integer.parseInt(numItems.getText()));
            gen.genItemSet("_" + champName.getText().toUpperCase() + "CUSTOM");
        });

        VBox left = new VBox(15, filePath, champName, info);
        VBox right = new VBox(15, submit, numItems);
        HBox frame = new HBox(10, left, right);
        Scene scene = new Scene(frame);
        stage.setScene(scene);
        stage.setTitle("Item Set Statistical Generator");
        stage.show();

    }
}

