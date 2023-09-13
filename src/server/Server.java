package server;

import util.NetworkUtil;
import cravio.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Server {
    private static final String RESTAURANT_ACCOUNTS = "D:\\JavaProject\\src\\cravio\\restaurantAccounts.txt";
    private ServerSocket serverSocket;
    public HashMap<String, String> restaurantMap;
    static RestaurantManager restaurantManager = new RestaurantManager();
    static private List<Restaurant> restaurantList;
    static private List<FoodItem> foodItemList;
    private Map<Restaurant, NetworkUtil> restaurantUtilMap;
    private Map<Customer, NetworkUtil> customerUtilMap;
    private Map<String, NetworkUtil> adminUtilMap;
    private List<Order> orderList = new ArrayList<>();
    private ReadThreadServer readThreadServer;
    private Scanner scanner;

    Server() throws Exception {
        scanner = new Scanner(System.in);
        restaurantMap = new HashMap<>();
        restaurantUtilMap = new HashMap<>();
        customerUtilMap = new HashMap<>();
        loadRestaurantAccounts();
    }

    public void addRestaurantOrFoodThread() {
        System.out.println("Welcome to Cravio Admin Panel!");
        System.out.println();
        Thread thread = new Thread(() -> {
            while(true)
            {
                System.out.println("Please enter your choice:");
                System.out.println("1. Add Restaurant");
                System.out.println("2. Add Food Item to the Menu of a Restaurant");
                String choice = scanner.nextLine();
                if(choice.equals("1"))
                {
                    addRestaurant();
                }
                else if(choice.equals("2"))
                {
                    addFoodItem();
                }
                else
                {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        });
        thread.start();
    }

    private void loadRestaurantAccounts() throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader(RESTAURANT_ACCOUNTS));
        while (true) {
            String line = br.readLine();
            if (line == null) break;
            String [] array = line.split(",", -1);
            restaurantMap.put(array[0], array[1]);
        }
        br.close();
    }

    public void setRestaurantUtilMap(Restaurant restaurant, NetworkUtil networkUtil) {
        restaurantUtilMap.put(restaurant, networkUtil);
    }

    public void setCustomerUtilMap(Customer customer, NetworkUtil networkUtil) {
        customerUtilMap.put(customer, networkUtil);
    }

    public NetworkUtil getRestaurantUtil(Restaurant restaurant) {
        return restaurantUtilMap.get(restaurant);
    }

    public NetworkUtil getCustomerUtil(Customer customer) {
        return customerUtilMap.get(customer);
    }

    public NetworkUtil getAdminUtil(String adminID) {
        return adminUtilMap.get(adminID);
    }

    public void setAdminUtilMap(String adminID, NetworkUtil networkUtil) {
        adminUtilMap.put(adminID, networkUtil);
    }

    public void serve(Socket clientSocket) throws IOException {
        NetworkUtil networkUtil = new NetworkUtil(clientSocket);
        readThreadServer = new ReadThreadServer(restaurantMap, networkUtil, restaurantList, foodItemList, this, orderList);
    }

    private void addRestaurant() {
        String name;
        boolean ans;
        int id = restaurantManager.getNewRestaurantID(restaurantList);
        do
        {
            System.out.print("Enter Restaurant Name: ");
            name = scanner.nextLine();
            ans = restaurantManager.isRestaurantTaken(name, restaurantList);
            if(ans)
            {
                System.out.println("A restaurant with this name is already present. Please try another name.");
                System.out.println();
            }
        } while(ans);
        System.out.print("Enter Restaurant Score: ");
        String score = scanner.nextLine();
        String price;
        do {
            System.out.print("Enter Restaurant Price ($/$$/$$$): ");
            price = scanner.nextLine();
            if(!price.equals("$") && !price.equals("$$") && !price.equals("$$$"))
            {
                System.out.println("Invalid price value. Please enter either $, $$ or $$$.");
                System.out.println();
            }
        } while (!price.equals("$") && !price.equals("$$") && !price.equals("$$$"));
        System.out.print("Enter Restaurant Zip Code: ");
        String zipCode = scanner.nextLine();
        String num;
        int catNum;
        do
        {
            System.out.print("Enter Number of Categories of Restaurant: ");
            num = scanner.nextLine();
            catNum = Integer.parseInt(num);
            if(catNum < 1 || catNum > 3)
            {
                System.out.println("Please enter a number between 1 and 3.");
            }
        } while(catNum < 1 || catNum > 3);
        String[] categories = new String[catNum];
        System.out.println("Enter Categories of Restaurant:");
        for(int i = 0; i < catNum; i++)
        {
            System.out.print(i+1 + ": ");
            categories[i] = scanner.nextLine();
        }
        System.out.print("Enter Password for Restaurant: ");
        String password = scanner.nextLine();
        restaurantManager.addRestaurant(new Restaurant(id, name, Double.parseDouble(score), price, Integer.parseInt(zipCode), categories), restaurantList);
        System.out.println("Restaurant added successfully!");
        restaurantMap.put(String.valueOf(id), password);
        System.out.println();
        readThreadServer.updateRestaurantList(restaurantList, restaurantMap);
    }

    private void addFoodItem() {
        String restaurantName;
        String foodCategory;
        String foodName;
        boolean duplicateFlag;
        int findID;
        do
        {
            do
            {
                System.out.print("Enter Restaurant Name: ");
                restaurantName = scanner.nextLine();
                findID = restaurantManager.findRestaurantID(restaurantName, restaurantList);
                if(findID == -1)
                {
                    System.out.println("There is no such restaurant with this name. Please try again.");
                    System.out.println();
                }
            } while(findID == -1);
            System.out.print("Enter Food Category: ");
            foodCategory = scanner.nextLine();
            System.out.print("Enter Food Name: ");
            foodName = scanner.nextLine();
            duplicateFlag = restaurantManager.checkDuplicateFood(foodName, foodCategory, findID, foodItemList);
            if(duplicateFlag)
            {
                System.out.println("A food item in the same category in the same restaurant is already present. Please try again.");
                System.out.println();
            }
        } while(duplicateFlag);
        System.out.print("Enter Food Price: ");
        String foodPrice = scanner.nextLine();
        restaurantManager.addFood(new FoodItem(findID, foodCategory, foodName, Double.parseDouble(foodPrice)), restaurantList, foodItemList);
        System.out.println("Food item added successfully!");
        System.out.println();
        readThreadServer.updateRestaurantList(restaurantList, restaurantMap);
        readThreadServer.updateFoodItemList(foodItemList);
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(33333);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                serve(clientSocket);
            }
        } catch (Exception e) {
            System.out.println("Server error: " + e);
        }
    }

    public static void main(String[] args) throws Exception {
        restaurantList = new ArrayList<>();
        foodItemList = new ArrayList<>();
        restaurantManager.loadRestaurants(restaurantList);
        restaurantManager.loadMenu(restaurantList, foodItemList);
        Server server = new Server();
        server.addRestaurantOrFoodThread();
        server.start();
    }
}
