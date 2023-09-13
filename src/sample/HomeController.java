package sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cravio.Customer;
import cravio.FoodItem;
import cravio.Order;
import cravio.Restaurant;
import cravio.RestaurantManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class HomeController{

    private RestaurantManager restaurantManager;
    private Restaurant myRestaurant;
    private Main main;
    private List<Restaurant> restaurantList;
    private List<FoodItem> foodItemList;
    private List<String> allResCategories;
    private ObservableList<FoodItem> inResFoodResults;
    private ObservableList<String> allCategories = FXCollections.observableArrayList();
    private ObservableList<Restaurant> restaurantObservableList = FXCollections.observableArrayList();
    private String[] restaurantSearch = {
            "All Restaurants",
            "Search Restaurant by Name",
            "Search Restaurant by Score",
            "Search Restaurant by Category",
            "Search Restaurant by Price",
            "Search Restaurant by Zip Code"
    };
    private String[] foodItemSearch = {
            "Search Food Item by Name",
            "Search Food Item by Name in a Given Restaurant",
            "Search Food Item by Category",
            "Search Food Item by Category in a Given Restaurant",
            "Search Food Item by Price Range",
            "Search Food Item by Price Range in a Given Restaurant",
            "Costliest Food Item(s) on the Menu in a Given Restaurant"
    };
    private String[] allFoodInRestaurantChoices = {
        "All Food Items",
        "Search Food Item by Name",
        "Search Food Item by Category",
        "Search Food Item by Price Range",
        "Costliest Food Item(s) on the Menu"
    };
    private Order myOrder;
    private Customer customer;

    @FXML
    private AnchorPane mainDashboard;
    @FXML
    private AnchorPane orderDashboard;
    @FXML
    private Label username;
    @FXML
    private TableView<String> catWiseTable;
    @FXML
    private TableColumn<String, String> catCol;
    @FXML
    private TableColumn<String, String> resCol;
    @FXML
    private TableView<Restaurant> numFoodTable;
    @FXML
    private TableColumn<Restaurant, String> resNameCol;
    @FXML
    private TableColumn<Restaurant, Integer> quantityCol;
    @FXML
    private ChoiceBox<String> restaurantSearchOptions;
    @FXML
    private ListView<Restaurant> searchResResults;
    @FXML
    private Label oneSearchType;
    @FXML
    private Label twoSearchType;
    @FXML
    private TextField oneSearchRes;
    @FXML
    private TextField twoSearchRes;
    @FXML
    private Button submitButton;
    @FXML
    private ChoiceBox<String> foodChoiceBox;
    @FXML
    private Label oneFoodType;
    @FXML
    private Label twoFoodType;
    @FXML
    private TextField oneFoodRes;
    @FXML
    private TextField twoFoodRes;
    @FXML
    private Button foodSubmitButton;
    @FXML
    private ListView<String> foodSearchResults;
    @FXML
    private Label threeFoodType;
    @FXML
    private TextField threeFoodRes;
    @FXML
    private Label infoResName;
    @FXML
    private Label infoScore;
    @FXML
    private Label infoPrice;
    @FXML
    private Label infoZipCode;
    @FXML
    private ListView<String> infoCategories;
    @FXML
    private ChoiceBox<String> foodInResChoice;
    @FXML
    private Label oneResFoodLabel;
    @FXML
    private Label twoResFoodLabel;
    @FXML
    private TextField oneResFoodField;
    @FXML
    private TextField twoResFoodField;
    @FXML
    private Button inResFoodSubmit;
    @FXML
    private ListView<FoodItem> customFoodList;
    @FXML
    private Label totalPrice;
    @FXML
    private TextArea orderTextArea;

    void setUsername(String newName) {
        username.setText(newName);
    }

    @FXML
    void logoutAction(ActionEvent event) {
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

    public class InResFoodItem extends ListCell<FoodItem> {
        private CheckBox checkBox;
        private Text foodDetails;
        private HBox content;
        private Button okButton;
        private Spinner<Integer> spinner;
    
        public InResFoodItem() {
            checkBox = new CheckBox();
            foodDetails = new Text();
            spinner = new Spinner<>(1, 100, 1);
            spinner.setVisible(false);
            spinner.setEditable(true);
            spinner.setPrefWidth(50);
            spinner.setMaxWidth(50);
            spinner.setMinWidth(50);
            okButton = new Button("OK");
            okButton.setVisible(false);
            spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (getItem() != null) {
                    getItem().setQuantity(newValue);
                }
            });
            checkBox.setSelected(false);
            content = new HBox(checkBox, foodDetails, spinner, okButton);
            content.setSpacing(10);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            checkBox.setOnAction(event -> {
                if (getItem() != null) {
                    getItem().setChecked(checkBox.isSelected());
                    if(checkBox.isSelected()){
                        spinner.setVisible(true);
                        okButton.setVisible(true);
                        getItem().setQuantity(spinner.getValue());
                    }
                    else{
                        spinner.setVisible(false);
                        okButton.setVisible(false);
                        getItem().setQuantity(0);
                        myOrder.removeFoodItem(getItem());
                        totalPrice.setText("$" + myOrder.getTotalPrice());
                        orderTextArea.setText(myOrder.toString());
                    }
                }
            });
            okButton.setOnAction(event -> {
                if (getItem() != null) {
                    getItem().setQuantity(spinner.getValue());
                    myOrder.addFoodItem(getItem());
                    totalPrice.setText("$" + myOrder.getTotalPrice());
                    orderTextArea.setText(myOrder.toString());
                }
            });
        }
    
        @Override
        protected void updateItem(FoodItem food, boolean empty) {
            super.updateItem(food, empty);
    
            if (empty || food == null) {
                setGraphic(null);
            } else {
                checkBox.setSelected(food.isChecked());
                foodDetails.setText(food.toString());
                spinner.getValueFactory().setValue(food.getQuantity());
                setGraphic(content);
            }
        }
    }

    public void init(List<Restaurant> restaurantList, List<FoodItem> foodItemList) {
        restaurantManager = new RestaurantManager();
        this.restaurantList = restaurantList;
        this.foodItemList = foodItemList;
        allResCategories = restaurantManager.getAllRestaurantCategories(restaurantList);
        allCategories.addAll(allResCategories);
        catCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        resCol.setCellValueFactory(cellData -> new SimpleStringProperty(restaurantManager.displayRestaurantByCategory(cellData.getValue(), restaurantList)));
        catWiseTable.setItems(allCategories);
        restaurantObservableList.addAll(restaurantList);
        resNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        quantityCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getFoodCount()).asObject());
        numFoodTable.setItems(restaurantObservableList);
        restaurantSearchOptions.getItems().addAll(restaurantSearch);
        restaurantSearchOptions.setOnAction(this::getRestaurantSearchOption);
        oneSearchRes.setVisible(false);
        twoSearchRes.setVisible(false);
        oneSearchType.setVisible(false);
        twoSearchType.setVisible(false);
        submitButton.setVisible(false);
        searchResResults.getItems().addAll(restaurantList);
        foodChoiceBox.getItems().addAll(foodItemSearch);
        foodChoiceBox.setOnAction(this::getFoodItemSearchOption);
        oneFoodType.setVisible(false);
        twoFoodType.setVisible(false);
        oneFoodRes.setVisible(false);
        twoFoodRes.setVisible(false);
        threeFoodType.setVisible(false);
        threeFoodRes.setVisible(false);
        foodSubmitButton.setVisible(false);
    }

    public void getRestaurantSearchOption(ActionEvent event)
    {
        String selectedOption = restaurantSearchOptions.getValue();
        if (selectedOption.equals("All Restaurants"))
        {
            oneSearchRes.setVisible(false);
            twoSearchRes.setVisible(false);
            oneSearchType.setVisible(false);
            twoSearchType.setVisible(false);
            submitButton.setVisible(false);
            searchResResults.getItems().clear();
            searchResResults.getItems().addAll(restaurantList);
        }
        else if (selectedOption.equals("Search Restaurant by Name"))
        {
            oneSearchRes.setVisible(true);
            twoSearchRes.setVisible(false);
            oneSearchType.setVisible(true);
            twoSearchType.setVisible(false);
            submitButton.setVisible(true);
            oneSearchType.setText("Enter Restaurant Name");
            searchResResults.getItems().clear();
            oneSearchRes.setText("");
        }
        else if (selectedOption.equals("Search Restaurant by Score"))
        {
            oneSearchRes.setVisible(true);
            twoSearchRes.setVisible(true);
            oneSearchType.setVisible(true);
            twoSearchType.setVisible(true);
            submitButton.setVisible(true);
            oneSearchType.setText("Enter Initial Score");
            twoSearchType.setText("Enter Final Score");
            searchResResults.getItems().clear();
            oneSearchRes.setText("");
            twoSearchRes.setText("");
        }
        else if (selectedOption.equals("Search Restaurant by Category"))
        {
            oneSearchRes.setVisible(true);
            twoSearchRes.setVisible(false);
            oneSearchType.setVisible(true);
            twoSearchType.setVisible(false);
            submitButton.setVisible(true);
            oneSearchType.setText("Enter Category");
            searchResResults.getItems().clear();
            oneSearchRes.setText("");
        }
        else if (selectedOption.equals("Search Restaurant by Price"))
        {
            oneSearchRes.setVisible(true);
            twoSearchRes.setVisible(false);
            oneSearchType.setVisible(true);
            twoSearchType.setVisible(false);
            submitButton.setVisible(true);
            oneSearchType.setText("Enter Price ($ / $$ / $$$)");
            searchResResults.getItems().clear();
            oneSearchRes.setText("");
        }
        else if (selectedOption.equals("Search Restaurant by Zip Code"))
        {
            oneSearchRes.setVisible(true);
            twoSearchRes.setVisible(false);
            oneSearchType.setVisible(true);
            twoSearchType.setVisible(false);
            submitButton.setVisible(true);
            oneSearchType.setText("Enter Zip Code");
            searchResResults.getItems().clear();
            oneSearchRes.setText("");
        }
    }

    @FXML
    void onSubmit(ActionEvent event)
    {
        String selectedOption = restaurantSearchOptions.getValue();
        if (selectedOption.equals("Search Restaurant by Name"))
        {
            searchResResults.getItems().clear();
            List<Restaurant> resAnsList = restaurantManager.searchRestaurantByName(oneSearchRes.getText(), restaurantList);
            if(resAnsList.isEmpty())
            {
                showAlert("No such restaurant with this name.");
            }
            else
            {
                searchResResults.getItems().addAll(resAnsList);
            }
        }
        else if (selectedOption.equals("Search Restaurant by Score"))
        {
            searchResResults.getItems().clear();
            List<Restaurant> resAnsList = restaurantManager.searchRestaurantByScore(Double.parseDouble(oneSearchRes.getText()), Double.parseDouble(twoSearchRes.getText()), restaurantList);
            if(resAnsList.isEmpty())
            {
                showAlert("No such restaurant with this score range");
            }
            else
            {
                searchResResults.getItems().addAll(resAnsList);
            }
        }
        else if (selectedOption.equals("Search Restaurant by Category"))
        {
            searchResResults.getItems().clear();
            List<Restaurant> resAnsList = restaurantManager.searchRestaurantByCategory(oneSearchRes.getText(), restaurantList);
            if(resAnsList.isEmpty())
            {
                showAlert("No such restaurant with this category");
            }
            else
            {
                searchResResults.getItems().addAll(resAnsList);
            }
        }
        else if (selectedOption.equals("Search Restaurant by Price"))
        {
            searchResResults.getItems().clear();
            List<Restaurant> resAnsList = restaurantManager.searchRestaurantByPrice(oneSearchRes.getText(), restaurantList);
            if(resAnsList.isEmpty())
            {
                showAlert("No such restaurant with this price");
            }
            else
            {
                searchResResults.getItems().addAll(resAnsList);
            }
        }
        else if (selectedOption.equals("Search Restaurant by Zip Code"))
        {
            searchResResults.getItems().clear();
            List<Restaurant> resAnsList = restaurantManager.searchRestaurantByZipCode(Integer.parseInt(oneSearchRes.getText()), restaurantList);
            if(resAnsList.isEmpty())
            {
                showAlert("No such restaurant with this zip code");
            }
            else
            {
                searchResResults.getItems().addAll(resAnsList);
            }
        }
    }

    public void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Search Restaurant Error");
        alert.setHeaderText("Search Restaurant Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    void initialize() {
        mainDashboard.setVisible(true);
        orderDashboard.setVisible(false);
        oneResFoodLabel.setVisible(false);
        twoResFoodLabel.setVisible(false);
        oneResFoodField.setVisible(false);
        twoResFoodField.setVisible(false);
        inResFoodSubmit.setVisible(false);
        totalPrice.setText("$" + 0);
        totalPrice.setVisible(true);
        searchResResults.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Selected Restaurant: " + newValue.getName());
                this.myRestaurant = newValue;
                myOrder = new Order(newValue, customer);
                mainDashboard.setVisible(false);
                orderDashboard.setVisible(true);
                infoResName.setText(newValue.getName());
                infoScore.setText(String.valueOf(newValue.getScore()));
                infoPrice.setText(newValue.getPrice());
                infoZipCode.setText(String.valueOf(newValue.getZipCode()));
                infoCategories.getItems().clear();
                infoCategories.getItems().addAll(newValue.getCategories());
                infoCategories.setCellFactory(param -> new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            setText(item);
                            setStyle("-fx-font-size: 16px;");
                        }
                    }
                });
                foodInResChoice.getItems().clear();
                foodInResChoice.getItems().addAll(allFoodInRestaurantChoices);
                foodInResChoice.setOnAction(this::getFoodInResChoice);
                inResFoodResults = FXCollections.observableArrayList(newValue.getFoodMenuOfRestaurant());
                customFoodList.setItems(inResFoodResults);
                customFoodList.setCellFactory(param -> new InResFoodItem());
            }
        });
    }

    public void getFoodItemSearchOption(ActionEvent event)
    {
        String selectedOption = foodChoiceBox.getValue();
        if(selectedOption.equals("Search Food Item by Name"))
        {
            oneFoodType.setVisible(true);
            twoFoodType.setVisible(false);
            oneFoodRes.setVisible(true);
            twoFoodRes.setVisible(false);
            oneFoodType.setText("Enter Food Item Name");
            twoFoodType.setText("");
            oneFoodRes.setText("");
            twoFoodRes.setText("");
            threeFoodType.setVisible(false);
            threeFoodRes.setVisible(false);
            foodSubmitButton.setVisible(true);
            foodSearchResults.getItems().clear();
        }
        else if(selectedOption.equals("Search Food Item by Name in a Given Restaurant"))
        {
            oneFoodType.setVisible(true);
            twoFoodType.setVisible(true);
            oneFoodRes.setVisible(true);
            twoFoodRes.setVisible(true);
            oneFoodType.setText("Enter Food Item Name");
            twoFoodType.setText("Enter Restaurant Name");
            oneFoodRes.setText("");
            twoFoodRes.setText("");
            threeFoodType.setVisible(false);
            threeFoodRes.setVisible(false);
            foodSubmitButton.setVisible(true);
            foodSearchResults.getItems().clear();
        }
        else if(selectedOption.equals("Search Food Item by Category"))
        {
            oneFoodType.setVisible(true);
            twoFoodType.setVisible(false);
            oneFoodRes.setVisible(true);
            twoFoodRes.setVisible(false);
            oneFoodType.setText("Enter Food Item Category");
            twoFoodType.setText("");
            oneFoodRes.setText("");
            twoFoodRes.setText("");
            threeFoodType.setVisible(false);
            threeFoodRes.setVisible(false);
            foodSubmitButton.setVisible(true);
            foodSearchResults.getItems().clear();
        }
        else if(selectedOption.equals("Search Food Item by Category in a Given Restaurant"))
        {
            oneFoodType.setVisible(true);
            twoFoodType.setVisible(true);
            oneFoodRes.setVisible(true);
            twoFoodRes.setVisible(true);
            oneFoodType.setText("Enter Food Item Category");
            twoFoodType.setText("Enter Restaurant Name");
            oneFoodRes.setText("");
            twoFoodRes.setText("");
            threeFoodType.setVisible(false);
            threeFoodRes.setVisible(false);
            foodSubmitButton.setVisible(true);
            foodSearchResults.getItems().clear();
        }
        else if(selectedOption.equals("Search Food Item by Price Range"))
        {
            oneFoodType.setVisible(true);
            twoFoodType.setVisible(true);
            oneFoodRes.setVisible(true);
            twoFoodRes.setVisible(true);
            oneFoodType.setText("Enter Initial Price");
            twoFoodType.setText("Enter Final Price");
            oneFoodRes.setText("");
            twoFoodRes.setText("");
            threeFoodType.setVisible(false);
            threeFoodRes.setVisible(false);
            foodSubmitButton.setVisible(true);
            foodSearchResults.getItems().clear();
        }
        else if(selectedOption.equals("Search Food Item by Price Range in a Given Restaurant"))
        {
            oneFoodType.setVisible(true);
            twoFoodType.setVisible(true);
            oneFoodRes.setVisible(true);
            twoFoodRes.setVisible(true);
            oneFoodType.setText("Enter Initial Price");
            twoFoodType.setText("Enter Final Price");
            oneFoodRes.setText("");
            twoFoodRes.setText("");
            threeFoodType.setVisible(true);
            threeFoodRes.setVisible(true);
            threeFoodRes.setText("");
            threeFoodType.setText("Enter Restaurant Name");
            foodSubmitButton.setVisible(true);
            foodSearchResults.getItems().clear();
        }
        else if(selectedOption.equals("Costliest Food Item(s) on the Menu in a Given Restaurant"))
        {
            oneFoodType.setVisible(true);
            twoFoodType.setVisible(false);
            oneFoodRes.setVisible(true);
            twoFoodRes.setVisible(false);
            oneFoodType.setText("Enter Restaurant Name");
            twoFoodType.setText("");
            oneFoodRes.setText("");
            twoFoodRes.setText("");
            threeFoodType.setVisible(false);
            threeFoodRes.setVisible(false);
            threeFoodRes.setText("");
            threeFoodType.setText("");
            foodSubmitButton.setVisible(true);
            foodSearchResults.getItems().clear();
        }
    }

    @FXML
    void onFoodSubmit(ActionEvent event)
    {
        String selectedOption = foodChoiceBox.getValue();
        if(selectedOption.equals("Search Food Item by Name"))
        {
            foodSearchResults.getItems().clear();
            List<FoodItem> foodAnsList = restaurantManager.searchByFoodName(oneFoodRes.getText(), foodItemList);
            List<String> foodAnsListString = new ArrayList<>();
            if(foodAnsList.isEmpty())
            {
                showAlert("No such food item with this name.");
            }
            else
            {
                for(FoodItem foodItem : foodAnsList)
                {
                    foodAnsListString.add(foodItem.getFoodItemInfo(restaurantManager.findRestaurantName(foodItem.getRestaurantId(), restaurantList)));
                }
                foodSearchResults.getItems().addAll(foodAnsListString);
            }
        }
        else if(selectedOption.equals("Search Food Item by Name in a Given Restaurant"))
        {
            foodSearchResults.getItems().clear();
            List<FoodItem> foodAnsList = restaurantManager.searchFoodInRestaurant(oneFoodRes.getText(), twoFoodRes.getText(), restaurantList);
            List<String> foodAnsListString = new ArrayList<>();
            if(foodAnsList == null)
            {
                showAlert("No such restaurant with this name.");
            }
            else if(foodAnsList.isEmpty())
            {
                showAlert("No such food item with this name in this restaurant.");
            }
            else
            {
                String resName = restaurantManager.findRestaurantName(foodAnsList.get(0).getRestaurantId(), restaurantList);
                for(FoodItem foodItem : foodAnsList)
                {
                    foodAnsListString.add(foodItem.getFoodItemInfo(resName));
                }
                foodSearchResults.getItems().addAll(foodAnsListString);
            }
        }
        else if(selectedOption.equals("Search Food Item by Category"))
        {
            foodSearchResults.getItems().clear();
            List<FoodItem> foodAnsList = restaurantManager.searchByFoodCategory(oneFoodRes.getText(), foodItemList);
            List<String> foodAnsListString = new ArrayList<>();
            if(foodAnsList.isEmpty())
            {
                showAlert("No such food item with this category.");
            }
            else
            {
                for(FoodItem foodItem : foodAnsList)
                {
                    foodAnsListString.add(foodItem.getFoodItemInfo(restaurantManager.findRestaurantName(foodItem.getRestaurantId(), restaurantList)));
                }
                foodSearchResults.getItems().addAll(foodAnsListString);
            }
        }
        else if(selectedOption.equals("Search Food Item by Category in a Given Restaurant"))
        {
            foodSearchResults.getItems().clear();
            List<FoodItem> foodAnsList = restaurantManager.searchFoodCategoryInRestaurant(oneFoodRes.getText(), twoFoodRes.getText(), restaurantList);
            List<String> foodAnsListString = new ArrayList<>();
            if(foodAnsList == null)
            {
                showAlert("No such restaurant with this name.");
            }
            else if(foodAnsList.isEmpty())
            {
                showAlert("No such food item with this category in this restaurant.");
            }
            else
            {
                String resName = restaurantManager.findRestaurantName(foodAnsList.get(0).getRestaurantId(), restaurantList);
                for(FoodItem foodItem : foodAnsList)
                {
                    foodAnsListString.add(foodItem.getFoodItemInfo(resName));
                }
                foodSearchResults.getItems().addAll(foodAnsListString);
            }
        }
        else if(selectedOption.equals("Search Food Item by Price Range"))
        {
            foodSearchResults.getItems().clear();
            List<FoodItem> foodAnsList = restaurantManager.searchFoodByPrice(Double.parseDouble(oneFoodRes.getText()), Double.parseDouble(twoFoodRes.getText()), foodItemList);
            List<String> foodAnsListString = new ArrayList<>();
            if(foodAnsList.isEmpty())
            {
                showAlert("No such food item with this price range.");
            }
            else
            {
                for(FoodItem foodItem : foodAnsList)
                {
                    foodAnsListString.add(foodItem.getFoodItemInfo(restaurantManager.findRestaurantName(foodItem.getRestaurantId(), restaurantList)));
                }
                foodSearchResults.getItems().addAll(foodAnsListString);
            }
        }
        else if(selectedOption.equals("Search Food Item by Price Range in a Given Restaurant"))
        {
            foodSearchResults.getItems().clear();
            List<FoodItem> foodAnsList = restaurantManager.searchFoodByPriceInRestaurant(Double.parseDouble(oneFoodRes.getText()), Double.parseDouble(twoFoodRes.getText()), threeFoodRes.getText(), restaurantList);
            List<String> foodAnsListString = new ArrayList<>();
            if(foodAnsList == null)
            {
                showAlert("No such restaurant with this name.");
            }
            else if(foodAnsList.isEmpty())
            {
                showAlert("No such food item with this price range in this restaurant.");
            }
            else
            {
                String resName = restaurantManager.findRestaurantName(foodAnsList.get(0).getRestaurantId(), restaurantList);
                for(FoodItem foodItem : foodAnsList)
                {
                    foodAnsListString.add(foodItem.getFoodItemInfo(resName));
                }
                foodSearchResults.getItems().addAll(foodAnsListString);
            }
        }
        else if(selectedOption.equals("Costliest Food Item(s) on the Menu in a Given Restaurant"))
        {
            foodSearchResults.getItems().clear();
            List<FoodItem> foodAnsList = restaurantManager.displayCostlyFood(oneFoodRes.getText(), restaurantList);
            List<String> foodAnsListString = new ArrayList<>();
            if(foodAnsList == null)
            {
                showAlert("No such restaurant with this name.");
            }
            else if(foodAnsList.isEmpty())
            {
                showAlert("No such food item in this restaurant.");
            }
            else
            {
                String resName = restaurantManager.findRestaurantName(foodAnsList.get(0).getRestaurantId(), restaurantList);
                for(FoodItem foodItem : foodAnsList)
                {
                    foodAnsListString.add(foodItem.getFoodItemInfo(resName));
                }
                foodSearchResults.getItems().addAll(foodAnsListString);
            }
        }
    }

    public void getFoodInResChoice(ActionEvent event)
    {
        // do according to the function getFoodItemSearchOption
        String selectedOption = foodInResChoice.getValue();
        if(selectedOption.equals("All Food Items"))
        {
            oneResFoodLabel.setVisible(false);
            twoResFoodLabel.setVisible(false);
            oneResFoodField.setVisible(false);
            twoResFoodField.setVisible(false);
            inResFoodSubmit.setVisible(false);
            oneResFoodLabel.setText("");
            inResFoodResults.clear();
            inResFoodResults = FXCollections.observableArrayList(myRestaurant.getFoodMenuOfRestaurant());
            customFoodList.getItems().clear();
            customFoodList.setItems(inResFoodResults);
            customFoodList.setCellFactory(param -> new InResFoodItem());
        }
        else if(selectedOption.equals("Search Food Item by Name"))
        {
            oneResFoodLabel.setVisible(true);
            twoResFoodLabel.setVisible(false);
            oneResFoodField.setVisible(true);
            twoResFoodField.setVisible(false);
            oneResFoodLabel.setText("Enter Food Item Name");
            twoResFoodLabel.setText("");
            oneResFoodField.setText("");
            twoResFoodField.setText("");
            inResFoodSubmit.setVisible(true);
            inResFoodResults.clear();
            customFoodList.getItems().clear();
        }
        else if(selectedOption.equals("Search Food Item by Category"))
        {
            oneResFoodLabel.setVisible(true);
            twoResFoodLabel.setVisible(false);
            oneResFoodField.setVisible(true);
            twoResFoodField.setVisible(false);
            oneResFoodLabel.setText("Enter Food Item Category");
            twoResFoodLabel.setText("");
            oneResFoodField.setText("");
            twoResFoodField.setText("");
            inResFoodSubmit.setVisible(true);
            inResFoodResults.clear();
            customFoodList.getItems().clear();
        }
        else if(selectedOption.equals("Search Food Item by Price Range"))
        {
            oneResFoodLabel.setVisible(true);
            twoResFoodLabel.setVisible(true);
            oneResFoodField.setVisible(true);
            twoResFoodField.setVisible(true);
            oneResFoodLabel.setText("Enter Initial Price");
            twoResFoodLabel.setText("Enter Final Price");
            oneResFoodField.setText("");
            twoResFoodField.setText("");
            inResFoodSubmit.setVisible(true);
            inResFoodResults.clear();
            customFoodList.getItems().clear();
        }
        else if(selectedOption.equals("Costliest Food Item(s) on the Menu"))
        {
            oneResFoodLabel.setVisible(false);
            twoResFoodLabel.setVisible(false);
            oneResFoodField.setVisible(false);
            twoResFoodField.setVisible(false);
            oneResFoodLabel.setText("");
            twoResFoodLabel.setText("");
            oneResFoodField.setText("");
            twoResFoodField.setText("");
            inResFoodSubmit.setVisible(false);
            inResFoodResults.clear();
            inResFoodResults = FXCollections.observableArrayList(restaurantManager.displayCostlyFood(myRestaurant.getName(), restaurantList));
            customFoodList.getItems().clear();
            customFoodList.setItems(inResFoodResults);
            customFoodList.setCellFactory(param -> new InResFoodItem());
        }
    }

    @FXML
    void onResFoodSubmit(ActionEvent event)
    {
        // do according to the function onFoodSubmit
        String selectedOption = foodInResChoice.getValue();
        if(selectedOption.equals("Search Food Item by Name"))
        {
            //inResFoodResults.getItems().clear();
            List<FoodItem> foodAnsList = restaurantManager.searchByFoodName(oneResFoodField.getText(), myRestaurant.getFoodMenuOfRestaurant());
            if(foodAnsList.isEmpty())
            {
                showAlert("No such food item with this name.");
            }
            else
            {
                inResFoodResults = FXCollections.observableArrayList(foodAnsList);
                customFoodList.setItems(inResFoodResults);
                customFoodList.setCellFactory(param -> new InResFoodItem());
            }
        }
        else if(selectedOption.equals("Search Food Item by Category"))
        {
            //inResFoodResults.getItems().clear();
            List<FoodItem> foodAnsList = restaurantManager.searchByFoodCategory(oneResFoodField.getText(), myRestaurant.getFoodMenuOfRestaurant());
            if(foodAnsList.isEmpty())
            {
                showAlert("No such food item with this category.");
            }
            else
            {
                inResFoodResults = FXCollections.observableArrayList(foodAnsList);
                customFoodList.setItems(inResFoodResults);
                customFoodList.setCellFactory(param -> new InResFoodItem());
            }
        }
        else if(selectedOption.equals("Search Food Item by Price Range"))
        {
            //inResFoodResults.getItems().clear();
            List<FoodItem> foodAnsList = restaurantManager.searchFoodByPrice(Double.parseDouble(oneResFoodField.getText()), Double.parseDouble(twoResFoodField.getText()), myRestaurant.getFoodMenuOfRestaurant());
            if(foodAnsList.isEmpty())
            {
                showAlert("No such food item with this price range.");
            }
            else
            {
                inResFoodResults = FXCollections.observableArrayList(foodAnsList);
                customFoodList.setItems(inResFoodResults);
                customFoodList.setCellFactory(param -> new InResFoodItem());
            }
        }
    }

    @FXML
    void onGoBack(ActionEvent event)
    {
        mainDashboard.setVisible(true);
        orderDashboard.setVisible(false);
    }

    @FXML
    void onConfirmOrder(ActionEvent event) throws IOException
    {
        if(myOrder.getFoodItems().isEmpty())
        {
            showAlert("No food item selected.");
        }
        else
        {
            main.getNetworkUtil().write(myOrder);
            showConfirmation();
            // making the order object null
            myOrder = new Order(myRestaurant, customer);
            orderTextArea.setText("");
            // making the order text box null
            totalPrice.setText("$" + myOrder.getTotalPrice());
            // making all the food items unchecked
            for(FoodItem foodItem : inResFoodResults)
            {
                foodItem.setChecked(false);
                foodItem.setQuantity(0);
            }
            customFoodList.setItems(inResFoodResults);
            customFoodList.setCellFactory(param -> new InResFoodItem());
        }
    }

    public void showConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Order Confirmation");
        alert.setHeaderText("Order Confirmation");
        alert.setContentText("Your order has been placed successfully.");
        alert.showAndWait();
    }

    void setMain(Main main) {
        this.main = main;
        customer = main.getCustomer();
    }
}