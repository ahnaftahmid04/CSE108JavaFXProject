package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import util.LoginDTO;

public class StartingController {
    private Main main;
    private Button adminButton;
    private Button restaurantButton;
    private Button customerButton;

    void setMain(Main main) {
        this.main = main;
    }
    
    public void onRestaurantLogin(ActionEvent event) throws Exception {
        main.showLoginPage(LoginDTO.UserTypes.RESTAURANT);
    }
    public void onCustomerLogin(ActionEvent event) throws Exception {
        main.showLoginPage(LoginDTO.UserTypes.CUSTOMER);
    }
}
