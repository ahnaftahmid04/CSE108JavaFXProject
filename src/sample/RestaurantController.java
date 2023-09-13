package sample;

import java.util.List;

import cravio.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class RestaurantController {
    @FXML
    private Label restaurantID;
    @FXML
    private Label restaurantName;
    @FXML
    private Label score;
    @FXML
    private Label price;
    @FXML
    private Label zipCode;
    @FXML
    private ListView<String> categories;
    @FXML
    private TableView<FoodItem> foodCountTable = new TableView<FoodItem>();
    @FXML
    private TableColumn<FoodItem, String> allFoodItems = new TableColumn<FoodItem, String>("Food Items");
    @FXML
    private TableColumn<FoodItem, Integer> foodQuantity = new TableColumn<FoodItem, Integer>("Quantity");
    @FXML
    private Label foodCount;
    @FXML
    private Label pendingCount;
    @FXML
    private Label completedCount;
    @FXML
    private ListView<Order> completedList;
    @FXML
    private ListView<Order> pendingList;

    private ObservableList<FoodItem> foodItems = FXCollections.observableArrayList();
    private Restaurant restaurant;
    private Main main;
    private ObservableList<Order> orders;

    void setMain(Main main) {
        this.main = main;
    }

    void setRestaurant(Restaurant newRestaurant, List<Order> offLineOrders) {
        this.restaurant = newRestaurant;
        restaurantID.setText(String.valueOf(restaurant.getId()));
        restaurantName.setText(restaurant.getName());
        score.setText(String.valueOf(restaurant.getScore()));
        price.setText(String.valueOf(restaurant.getPrice()));
        zipCode.setText(String.valueOf(restaurant.getZipCode()));
        categories.getItems().addAll(restaurant.getCategories());
        foodItems.addAll(restaurant.getFoodMenuOfRestaurant());
        foodCount.setText(String.valueOf(restaurant.getFoodCount()));
        completedCount.setText("0");
        allFoodItems.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));
        foodQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        foodCountTable.setItems(foodItems);
        orders = FXCollections.observableArrayList(offLineOrders);
        pendingCount.setText(String.valueOf(orders.size()));
        pendingList.setItems(orders);
        pendingList.setCellFactory(new Callback<ListView<Order>, ListCell<Order>>() {
            @Override
            public ListCell<Order> call(ListView<Order> param) {
                return new OrderListCell();
            }
        });
    }

    @FXML
    void onLogout(ActionEvent event) {
        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText("You're about to logout!");
            alert.setContentText("Do you want to save before exiting?");
            
            if (alert.showAndWait().get() == ButtonType.OK){
                System.out.println("You successfully logged out");
            } 
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addOrder(Order order) {
        Platform.runLater(() -> {
            orders.add(order);
            pendingList.setItems(orders);
            pendingCount.setText(String.valueOf(Integer.parseInt(pendingCount.getText()) + 1));
        });
        pendingList.setCellFactory(new Callback<ListView<Order>, ListCell<Order>>() {
            @Override
            public ListCell<Order> call(ListView<Order> param) {
                return new OrderListCell();
            }
        });
    }

    private class OrderListCell extends ListCell<Order> {
        @Override
        protected void updateItem(Order order, boolean empty) {
            super.updateItem(order, empty);

            if (empty || order == null) {
                setGraphic(null);
            } else {
                Button confirmButton = new Button("Confirm Order");
                Label orderLabel = new Label(order.toString());

                confirmButton.setOnAction(event -> {
                    try{
                        pendingList.getItems().remove(order);
                        restaurant.removeOrder(order);
                        completedList.getItems().add(order);
                        completedCount.setText(String.valueOf(Integer.parseInt(completedCount.getText()) + 1));
                        pendingCount.setText(String.valueOf(Integer.parseInt(pendingCount.getText()) - 1));
                        for (FoodItem foodItem : order.getFoodItems()) {
                            for (FoodItem restaurantFoodItem : restaurant.getFoodMenuOfRestaurant()) {
                                if (foodItem.getName().equals(restaurantFoodItem.getName())) {
                                    restaurantFoodItem.incrementQuantity(foodItem.getQuantity());
                                    foodCountTable.refresh();
                                }
                            }
                        }
                    }
                    catch(Exception e){
                        System.out.println("inside confirmation button exception.");
                    }
                });

                VBox labelsBox = new VBox(orderLabel);
                labelsBox.setSpacing(5);
                HBox buttonBox = new HBox(confirmButton);
                buttonBox.setSpacing(10);

                VBox cellLayout = new VBox(labelsBox, buttonBox);
                cellLayout.setSpacing(10);

                setGraphic(cellLayout);
            }
        }
    }
    @FXML
    void onAddFoodClick(ActionEvent event) {
        try {
            System.out.println("Add Food Clicked");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
