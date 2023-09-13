package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import util.NetworkUtil;
import util.LoginDTO;

import java.io.IOException;
import java.util.List;
import cravio.*;

public class Main extends Application {

    private Stage stage;
    private NetworkUtil networkUtil;
    private List<Restaurant> restaurantList;
    private List<FoodItem> foodItemList;
    private RestaurantController restaurantController;
    private Customer customer;

    public Stage getStage() {
        return stage;
    }

    public NetworkUtil getNetworkUtil() {
        return networkUtil;
    }

    public void setCustomer(String userName)
    {
        customer = new Customer(userName);
    }

    public Customer getCustomer()
    {
        return customer;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        Image resIcon = new Image(getClass().getResourceAsStream("appIcon.png"));
        stage.getIcons().add(resIcon);
        connectToServer();
        showStartingPage();

        stage.setOnCloseRequest(event -> {
            event.consume();
            logout(stage);	
        });
    }

    private void connectToServer() throws IOException, ClassNotFoundException {
        String serverAddress = "127.0.0.1";
        int serverPort = 33333;
        networkUtil = new NetworkUtil(serverAddress, serverPort);
        new ReadThread(this);
    }

    public void showLoginPage(LoginDTO.UserTypes userType) throws Exception {
        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("login.fxml"));
        Parent root = loader.load();

        // Loading the controller
        LoginController controller = loader.getController();
        controller.setMain(this);
        controller.setLoginUserType(userType);

        // Set the primary stage
        stage.setTitle("Login");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    public void showRestaurantPage(Restaurant restaurant, List<Order> orderList) throws Exception {
        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("restaurant.fxml"));
        Parent root = loader.load();

        // Loading the controller
        restaurantController = loader.getController();
        restaurantController.setMain(this);
        restaurantController.setRestaurant(restaurant, orderList);

        // Set the primary stage
        stage.setTitle("Restaurant Dashboard");
        stage.setScene(new Scene(root, 1200, 600));
        stage.setResizable(false);
        stage.show();
    }

    public void showHomePage(String userName) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("home.fxml"));
        Parent root = loader.load();

        // Loading the controller
        HomeController controller = loader.getController();
        controller.setMain(this);
        controller.setUsername(userName);
        controller.init(restaurantList, foodItemList);

        // Set the primary stage
        stage.setTitle("Customer Dashboard");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    public void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Incorrect Credentials");
        alert.setHeaderText("Incorrect Credentials");
        alert.setContentText("The username and password you provided is not correct.");
        alert.showAndWait();
    }

    public void showStartingPage() throws Exception
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("starting.fxml"));
        Parent root = loader.load();

        // Loading the controller
        StartingController controller = loader.getController();
        controller.setMain(this);

        // Set the primary stage
        stage.setTitle("Starting");
        stage.setScene(new Scene(root, 500, 400));
        stage.setResizable(false);
        stage.show();
    }

    public void setLists(List<Restaurant> restaurantList, List<FoodItem> foodItemList) {
        this.restaurantList = restaurantList;
        this.foodItemList = foodItemList;
    }

    public void logout(Stage stage){	
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText("You're about to logout!");
		alert.setContentText("Do you want to save before exiting?");
		
		if (alert.showAndWait().get() == ButtonType.OK){
			System.out.println("You successfully logged out");
			stage.close();
		} 
	}

    public void addOrderToRestaurant(Order newOrder)
    {
        restaurantController.addOrder(newOrder);
    }

    public static void main(String[] args) {
        // This will launch the JavaFX application
        launch(args);
    }
}
