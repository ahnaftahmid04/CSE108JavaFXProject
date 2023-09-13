package cravio;
import java.io.Serializable;

public class FoodItem implements Serializable{
    private int restaurantId;
    private String category;
    private String name;
    private double price;
    private int quantity;
    private boolean checked;

    public FoodItem(int restaurantId, String category, String name, double price)
    {
        this.restaurantId = restaurantId;
        this.category = category;
        this.name = name;
        this.price = price;
        this.quantity = 0;
    }

    public void setRestaurantId(int restaurantId)
    {
        this.restaurantId = restaurantId;
    }
    public void setCategory(String category)
    {
        this.category = category;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setPrice(double price)
    {
        this.price = price;
    }

    public int getRestaurantId()
    {
        return restaurantId;
    }
    public String getCategory()
    {
        return category;
    }
    public String getName()
    {
        return name;
    }
    public double getPrice()
    {
        return price;
    }
    public int getQuantity()
    {
        return quantity;
    }
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
    public void incrementQuantity(int count)
    {
        quantity += count;
    }
    @Override
    public String toString()
    {
        return "Category: " + category + "\n" +
                "Name: " + name + "\n" +
                "Price: " + price;
    }
    public String getFoodItemInfo(String restaurantName)
    {
        return "Restaurant: " + restaurantName + "\n" +
                "Category: " + category + "\n" +
                "Name: " + name + "\n" +
                "Price: " + price;
    }

    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }

    public boolean isChecked()
    {
        return checked;
    }
}
