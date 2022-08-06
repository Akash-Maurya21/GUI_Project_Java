package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public Label label;
    @FXML
    public ChoiceBox<String> choiceBox;
    @FXML
    public TextField userInputField;
    @FXML
    public Button button;

    private boolean isC_to_F=true;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        choiceBox.getItems().add("Celsius to Fahrenheit");
        choiceBox.getItems().add("Fahrenheit to Celsius");
        choiceBox.setValue("Celsius to Fahrenheit");
        choiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if(newValue.equals("Celsius to Fahrenheit"))
                isC_to_F=true;
            else
                isC_to_F=false;
        });
        button.setOnAction(actionEvent -> convert());
    }
    private void convert()
    {
        String input= userInputField.getText();
        float temperature=0.0f;
        try
        {
            temperature = Float.parseFloat(input);
        }
        catch (Exception e)
        {
            warn(e);
            return;
        }

        float newTemperature=0.0f;
        if(isC_to_F)
        {
            newTemperature=(temperature*9/5)+32;
        }
        else
        {
            newTemperature=(temperature-32)*5/9;
        }
        display(newTemperature);
    }

    private void warn(Exception e) {
        Alert alertResult= new Alert(Alert.AlertType.ERROR);
        alertResult.setTitle("Error occurred");
        alertResult.setContentText("Invalid Input !! Please Enter a Valid Input");
        alertResult.show();
    }

    private void display(float newTemperature) {
        String unit = isC_to_F?"F":"C";
        Alert alertResult= new Alert(Alert.AlertType.INFORMATION);
        alertResult.setTitle("Result");
        alertResult.setContentText("The new Temperature is : "+newTemperature+" "+unit);
        alertResult.show();
    }
}
