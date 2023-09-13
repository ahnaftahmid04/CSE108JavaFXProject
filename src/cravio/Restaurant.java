package cravio;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.io.Serializable;

public class Restaurant implements Serializable{
    private int id;
    private String name;
    private double score;
    private String price;
    private int zipCode;
    private String[] categories;
    private List<FoodItem> foodMenu;
    private List<Order> orders;

    public Restaurant(int id, String name, double score, String price, int zipCode, String[] categories)
    {
        this.id = id;
        this.name = name;
        this.score = score;
        this.price = price;
        this.zipCode = zipCode;
        this.categories = categories;
        foodMenu = new ArrayList<FoodItem>();
        orders = new ArrayList<Order>();
    }

    public void setId(int id)
    {
        this.id = id;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setScore(double score)
    {
        this.score = score;
    }
    public void setPrice(String price)
    {
        this.price = price;
    }
    public void setZipCode(int zipCode)
    {
        this.zipCode = zipCode;
    }
    public void setCategories(String[] categories)
    {
        this.categories = new String[categories.length];
        this.categories = categories;
    }

    public int getId()
    {
        return id;
    }
    public String getName()
    {
        return name;
    }
    public double getScore()
    {
        return score;
    }
    public String getPrice()
    {
        return price;
    }
    public int getZipCode()
    {
        return zipCode;
    }
    public String[] getCategories()
    {
        return categories;
    }
    public void addFoodItemToRestaurant(FoodItem foodItem)
    {
        foodMenu.add(foodItem);
    }
    public List<FoodItem> getFoodMenuOfRestaurant()
    {
        return foodMenu;
    }
    public int getFoodCount()
    {
        return foodMenu.size();
    }

    public void addOrder(Order order)
    {
        orders.add(order);
    }

    public void removeOrder(Order order)
    {
        orders.remove(order);
    }

    public List<Order> getOrders()
    {
        return orders;
    }

    public String toString()
    {
        String res = "";
        res += "ID: " + id + "\n";
        res += "Name: " + name + "\n";
        res += "Score: " + score + "\n";
        res += "Price: " + price + "\n";
        res += "Zipcode: " + zipCode + "\n";
        res += "Categories: \n";
        int count = 0;
        for(String s : categories)
        {
            if(count == 0)
            {
                res += (count + 1) + ". " + s;
            }
            else
            {
                res += "\n" + (count + 1) + ". " + s;
            }
            ++count;
        }
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
        {
            return true;
        }
        if(obj instanceof Restaurant)
        {
            Restaurant restaurant = (Restaurant) obj;
            return restaurant.getId() == this.getId();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}