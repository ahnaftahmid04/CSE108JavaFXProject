package cravio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private Restaurant restaurant;
    private List<FoodItem> foodItem;
    private Customer customer;

    public Order(Restaurant restaurant, Customer customer)
    {
        this.restaurant = restaurant;
        this.customer = customer;
        this.foodItem = new ArrayList<>();
    }

    public void setRestaurant(Restaurant restaurant)
    {
        this.restaurant = restaurant;
    }

    public void setFoodItem(List<FoodItem> foodItem)
    {
        this.foodItem = foodItem;
    }

    public Restaurant getRestaurant()
    {
        return restaurant;
    }

    public List<FoodItem> getFoodItems()
    {
        return foodItem;
    }

    public double getTotalPrice()
    {
        double totalPrice = 0;
        for (FoodItem item : foodItem)
        {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        return totalPrice;
    }

    public void addFoodItem(FoodItem item)
    {
        foodItem.add(item);
    }

    public void removeFoodItem(FoodItem item)
    {
        foodItem.remove(item);
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public String toString()
    {
        String str = "";
        str += "From Customer: " + customer.getUsername() + "\n";
        str += "To Restaurant: " + restaurant.getName() + "\n";
        str += "Food Items: \n\n";
        int i = 1;
        for (FoodItem item : foodItem)
        {
            str += "Food Item " + i + ": " + "\n" + item.toString() + "\n" + "Quantity: " + item.getQuantity() + "\n" + "\n";
            i++;
        }
        return str;
    }
}
