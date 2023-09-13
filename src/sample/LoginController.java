package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import util.LoginDTO;

import java.io.IOException;


public class LoginController {

    private Main main;
    private LoginDTO.UserTypes userType;

    @FXML
    private TextField userText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private Button resetButton;

    @FXML
    private Button loginButton;

    @FXML
    private Label loginType;

    @FXML
    private Label userID;

    @FXML
    private Button backButton;

    @FXML
    private Label passwordLabel;

    @FXML
    void loginAction(ActionEvent event) {
        String userName = userText.getText();
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUserName(userName);
        loginDTO.setUserType(userType);
        if(userType == LoginDTO.UserTypes.RESTAURANT)
        {
            String password = passwordText.getText();
            loginDTO.setPassword(password);
        }
        else
        {
            main.setCustomer(userName);
        }
        try {
            main.getNetworkUtil().write(loginDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void resetAction(ActionEvent event) {
        userText.setText(null);
        passwordText.setText(null);
    }

    void setMain(Main main) {
        this.main = main;
    }

    public void setLoginUserType(LoginDTO.UserTypes userType) {
        this.userType = userType;
        loginType.setText(userType.toString() + " Login");
        if(this.userType == LoginDTO.UserTypes.RESTAURANT)
        {
            userID.setText("Restaurant ID");
            passwordLabel.setVisible(true);
            passwordText.setVisible(true);
        }
        else
        {
            passwordText.setVisible(false);
            passwordLabel.setVisible(false);
        }
    }

    @FXML
    void onBackButtonClick(ActionEvent event) throws Exception{
        main.showStartingPage();
    }
}
