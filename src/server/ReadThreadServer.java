package server;

import util.LoginDTO;
import util.NetworkUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import cravio.*;

public class ReadThreadServer implements Runnable {
    private final Thread thr;
    private final NetworkUtil networkUtil;
    private HashMap<String, String> restaurantMap;
    private List<Restaurant> restaurantList;
    private List<FoodItem> foodItemList;
    private Server server;
    private List<Order> orderList;

    public void updateRestaurantList(List<Restaurant> restaurantList, HashMap<String, String> restaurantMap) {
        this.restaurantList = restaurantList;
        this.restaurantMap = restaurantMap;
    }

    public void updateFoodItemList(List<FoodItem> foodItemList) {
        this.foodItemList = foodItemList;
    }

    public ReadThreadServer(HashMap<String, String> restaurantMap, NetworkUtil networkUtil,
    List<Restaurant> restaurantList, List<FoodItem> foodItemList, Server server, List<Order> orderList) {
        this.restaurantMap = restaurantMap;
        this.networkUtil = networkUtil;
        this.restaurantList = restaurantList;
        this.foodItemList = foodItemList;
        this.thr = new Thread(this);
        this.server = server;
        this.orderList = orderList;
        thr.start();
    }

    public void run() {
        try {
            while (true) {
                Object o = networkUtil.read();
                if (o != null) {
                    if (o instanceof LoginDTO) {
                        LoginDTO loginDTO = (LoginDTO) o;
                        if(loginDTO.getUserType() == LoginDTO.UserTypes.CUSTOMER)
                        {
                            loginDTO.setStatus(true);
                        }
                        else
                        {
                            String password = restaurantMap.get(loginDTO.getUserName());
                            loginDTO.setStatus(loginDTO.getPassword().equals(password));
                        }
                        loginDTO.setRestaurantList(restaurantList);
                        loginDTO.setFoodItemList(foodItemList);
                        if(loginDTO.isStatus()) {
                            if(loginDTO.getUserType() == LoginDTO.UserTypes.RESTAURANT) {
                                loginDTO.setRestaurant(restaurantList.get(Integer.parseInt(loginDTO.getUserName()) - 1));
                                List<Order> orders = new ArrayList<Order>();
                                for(Order order : orderList) {
                                    if(order.getRestaurant().equals(loginDTO.getRestaurant())) {
                                        orders.add(order);
                                    }
                                }
                                for(Order order : orders) {
                                    orderList.remove(order);
                                }
                                loginDTO.setOrderList(orders);
                                server.setRestaurantUtilMap(loginDTO.getRestaurant(), networkUtil);
                            }
                            else if(loginDTO.getUserType() == LoginDTO.UserTypes.CUSTOMER) {
                                server.setCustomerUtilMap(loginDTO.getCustomer(), networkUtil);
                            }
                        }
                        networkUtil.write(loginDTO);
                    }
                    if(o instanceof Order)
                    {
                        Order order = (Order) o;
                        Restaurant restaurant = order.getRestaurant();
                        NetworkUtil restaurantUtil = server.getRestaurantUtil(restaurant);
                        if(restaurantUtil == null) {
                            orderList.add(order);
                        }
                        else
                        {
                            restaurant.addOrder(order);
                            restaurantUtil.write(order);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                networkUtil.closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



