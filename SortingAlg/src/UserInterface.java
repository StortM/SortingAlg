import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.*;
import java.util.List;

public class UserInterface extends Application { ;
    public static void main(String[] args) {
        Application.launch(args);
    }
    private ObservableList<XYChart.Data<String, Number>> bars;
    private BarChart<String, Number> chart;
    private FlowPane inputs;

    private static final int BAR_COUNT = 200;
    private static final int MAX_BAR_HEIGHT = 200;

    private static final String
            COLOR_ACTIVE = "-fx-bar-fill: #f64",
            COLOR_INITIAL = "-fx-bar-fill: #f64",
            COLOR_FINALIZED = "-fx-bar-fill: #f64";

    private static final int
            DELAY_MILLIS = 700;

    public static final BackgroundFill backFill = new BackgroundFill(Color.LIGHTSLATEGRAY,CornerRadii.EMPTY,Insets.EMPTY);
    Background background = new Background(backFill);

    boolean doneSort = false;

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Sorting Animations");
        stage.setWidth(800);
        stage.setHeight(600);

        final BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10));

        makeBarChart(pane);
        makeButtons(pane);
//        chart.setStyle("-fx-background-color: grey ;");
//        pane.setStyle("-fx-background-color: grey ;");

        Scene scene = new Scene(pane);

        //scene.getStylesheets().add("LineChart.css");

        stage.setScene(scene);
        stage.show();
    }

    private void makeBarChart(BorderPane pane){
        chart = new BarChart<>(new CategoryAxis(), new NumberAxis(0,MAX_BAR_HEIGHT,0));
        chart.setLegendVisible(false);
        chart.getYAxis().setTickLabelsVisible(false);
        chart.getYAxis().setOpacity(0);
        chart.getXAxis().setTickLabelsVisible(false);
        chart.getXAxis().setOpacity(0);
        chart.setVerticalGridLinesVisible(false);
        chart.setBarGap(0.5);
        chart.setCategoryGap(1);


        bars = FXCollections.observableArrayList();
        chart.getData().add(new XYChart.Series<>(bars));

        for (int i = 0; i < BAR_COUNT; i++) {
            XYChart.Data<String, Number> dataObject = new XYChart.Data<>(Integer.toString(i + 1), 1);
            bars.add(dataObject); // node will be present after this
            addPainting(dataObject.getNode(), COLOR_FINALIZED);
        }
        pane.setCenter(chart);
    }

    private void addPainting(Node newNode, String colorInitial) {
        if (newNode != null) {
            newNode.setStyle(colorInitial);
        }
    }


    private void sortArray() {
        
        Timeline sortLoop = new Timeline();

        int i = 0;
        sortLoop.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(Duration.seconds(i + 0.01), actionEvent -> {
            bubbleSort2();
        });
        sortLoop.getKeyFrames().add(kf);

        sortLoop.play();
        if (doneSort == true){
            sortLoop.stop();
        }
    }

    private void makeButtons(BorderPane pane) {
        inputs = new FlowPane();
        inputs.setHgap(5);
        inputs.setVgap(5);
        createButton("Randomize", () -> randomizeAll2());
        createButton("Bubble Sort 1", () -> bubbleSort2());
        createButton("Bubble Sort 2", () -> sortArray());

        pane.setBottom(inputs);
    }

    private void createButton(String label, Runnable method) {
        final Button test = new Button(label);
        test.setOnAction(event -> method.run());
        inputs.getChildren().add(test);
    }


    private void randomizeAll2() {
        List<XYChart.Data<String, Number>> list = bars;
        Random random = new Random();
        int i = random.nextInt(BAR_COUNT);

        Set<Integer> validate = new HashSet<>();
        validate.add(i);

        for (int j = 0; j < bars.size()-1; j++) {
            while(validate.contains(i)) {
                i = random.nextInt(BAR_COUNT);
            }
            validate.add(i);
            list.get(j).setYValue(i);
        }
    }

    /**
     * Bubble Sort algorithm
     */

    private void bubbleSort2(){
        try {
            List<XYChart.Data<String, Number>> list = bars;

            boolean breaker = false;
            for (int i = 0; i < list.size(); i++) {
                for (int j = 1; j < list.size() - i; j++) {
                    XYChart.Data<String, Number> first = list.get(j);
                    XYChart.Data<String, Number> second = list.get(j - 1);
                    XYChart.Data<String, Number> second2 = list.get(j + 1);

                    Platform.runLater(() -> {
                        first.getNode().setStyle("-fx-background-color: green ;");
                        second.getNode().setStyle("-fx-bar-fill: #f64");
                        second2.getNode().setStyle("-fx-bar-fill: #f64");
                    });

                    if (getValue(list, j - 1) > getValue(list, j)) {
                        switchFunc(list, j);
                        breaker = true;
                        break;
                    }
                }
                if (breaker == true) {
                    break;
                }
            }
        }
        catch (java.lang.IndexOutOfBoundsException e){
            doneSort = true;
        }
    }

    /**
     * @param list
     * @param index
     * Switching values of index and index -1
     */

    private void switchFunc(List<XYChart.Data<String, Number>> list, int index){
        //Get numbers and their integers
        int oldVal = (int) list.get(index-1).getYValue();
        int newVal = (int) list.get(index).getYValue();

        //Swtich tal
        list.get(index).setYValue(oldVal);
        list.get(index-1).setYValue(newVal);
    }

    private double getValue(List<XYChart.Data<String, Number>> list, int index) {
        return list.get(index).getYValue().doubleValue();
    }
}
