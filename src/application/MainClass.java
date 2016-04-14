package application;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import utils.XMLUtils;
 
public class MainClass extends Application {
	
	private static String appName = "MTAMapMover";
	private static String appVersion = "0.1a";
	private static String appAuthor = "Sam@ke";
	
	private static TextField filePathTextField;
	private static Label infoLabel;
	private static TextField xAxisTextField;
	private static TextField yAxisTextField;
	private static TextField zAxisTextField;
	private static File lastDirectory;
	private static Button moveMapBtn;
	
    @Override
    public void start(final Stage primaryStage) {
    	
        primaryStage.setTitle(appName + " // " + appVersion + " by " + appAuthor);

        Image icon = new Image("mmm_icon.png");
        primaryStage.getIcons().add(icon);

    	final FileChooser fileChooser = new FileChooser();
    	fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Map Files", "*.map"));
    	    	
        final Button chooseMapBtn = new Button("Choose map ...");
        
        chooseMapBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
            	fileChooser.setInitialDirectory(lastDirectory);
                File file = fileChooser.showOpenDialog(primaryStage);
                
                if (file != null) {
                    try {
						preOpenFile(file);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
                }
            }
        });
        
        filePathTextField = new TextField();
        filePathTextField.setPrefWidth(250);
        filePathTextField.setPromptText("Choosed map file...");
        filePathTextField.setDisable(true);
        
        infoLabel = new Label("");
        infoLabel.setPrefWidth(120);
        
        Label xAxisLabel = new Label("Move on X-Axis:");
        xAxisLabel.setPrefWidth(120);
        
        xAxisTextField = new TextField();
        xAxisTextField.setPrefWidth(100);
        xAxisTextField.setText("0.0");
        
        Label yAxisLabel = new Label("Move on Y-Axis:");
        yAxisLabel.setPrefWidth(120);
        
        yAxisTextField = new TextField();
        yAxisTextField.setPrefWidth(100);
        yAxisTextField.setText("0.0");
        
        Label zAxisLabel = new Label("Move on Z-Axis:");
        zAxisLabel.setPrefWidth(120);
        
        zAxisTextField = new TextField();
        zAxisTextField.setPrefWidth(100);
        zAxisTextField.setText("0.0");
        
        moveMapBtn = new Button("Move map");
        moveMapBtn.setPrefWidth(140);
        
        moveMapBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
            	String xOffset = xAxisTextField.getText();
            	String yOffset = yAxisTextField.getText();
            	String zOffset = zAxisTextField.getText();
            	
            	moveMapBtn.setDisable(true);
            	
            	boolean isMoved = false;
            	
				try {
					isMoved = XMLUtils.moveMap(xOffset, yOffset, zOffset);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            
            	if (isMoved == true) {
            		moveMapBtn.setDisable(false);
            		infoLabel.setTextFill(Color.web("#008922"));
            		infoLabel.setText("Map was moved!");
            	} else {
            		infoLabel.setTextFill(Color.web("#EE0022"));
            		infoLabel.setText("Move map failed!");
            	}
            }
        });
     
        final GridPane inputGridPane = new GridPane();
        inputGridPane.setVgap(5);
        inputGridPane.setHgap(5);
        
        GridPane.setConstraints(chooseMapBtn, 0, 0);
        GridPane.setConstraints(filePathTextField, 1, 0);
        GridPane.setConstraints(infoLabel, 0, 3);
        GridPane.setConstraints(xAxisLabel, 0, 6);
        GridPane.setConstraints(xAxisTextField, 1, 6);
        GridPane.setConstraints(yAxisLabel, 0, 7);
        GridPane.setConstraints(yAxisTextField, 1, 7);
        GridPane.setConstraints(zAxisLabel, 0, 8);
        GridPane.setConstraints(zAxisTextField, 1, 8);
        GridPane.setConstraints(moveMapBtn, 1, 12);
        
        inputGridPane.getChildren().addAll(chooseMapBtn, filePathTextField, infoLabel, xAxisLabel, xAxisTextField, yAxisLabel, yAxisTextField, zAxisLabel, zAxisTextField, moveMapBtn);
        
        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(inputGridPane);
        rootGroup.setPadding(new Insets(10, 10, 10, 10));
        
        Scene scene = new Scene(rootGroup, 400, 220);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    private void preOpenFile(File file) throws Exception {
    	String filename = file.getName();
    	String filePath = file.getAbsolutePath();
    	String fileDirectory = file.getAbsolutePath().replace(filename, "");
    	
    	filePathTextField.setText(filename);
    	lastDirectory = new File(fileDirectory);
    	
    	int foundPositions = XMLUtils.preLoadMap(filePath);
    	
    	infoLabel.setText("Positions found: " + foundPositions);
    	
    	if (foundPositions > 0) {
    		moveMapBtn.setDisable(false);
    		infoLabel.setTextFill(Color.web("#008922"));
    	} else {
    		moveMapBtn.setDisable(true);
    		infoLabel.setTextFill(Color.web("#EE0022"));
    	}
    }
}