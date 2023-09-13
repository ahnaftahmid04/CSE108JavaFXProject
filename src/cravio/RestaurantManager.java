package cravio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class RestaurantManager {
    private static final String RESTAURANT_FILE = "D:\\JavaProject\\src\\cravio\\restaurant.txt";
    private static final String MENU_FILE = "D:\\JavaProject\\src\\cravio\\menu.txt";

    public void loadRestaurants(List<Restaurant> newRestaurantList) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(RESTAURANT_FILE));
        while (true) {
            String line = br.readLine();
            if (line == null) break;
            String [] array = line.split(",", -1);
            int count;
            if(array[array.length - 1].isEmpty())
            {
                count = array.length-6;
            }
            else    
            {
                count = array.length-5;
            }
            String[] restaurantCategories = new String[count];
            /*
            for(int i = 0; i < count; i++)
            {
                restaurantCategories[i] = array[i+5];
            }
            */
            System.arraycopy(array, 5, restaurantCategories, 0, count);
            newRestaurantList.add(new Restaurant(Integer.parseInt(array[0]), array[1], Double.parseDouble(array[2]), array[3], Integer.parseInt(array[4]), restaurantCategories));
        }
        br.close();
    }

    public void saveRestaurants(List<Restaurant> newRestaurantList) throws Exception 
    {
        BufferedWriter bw = new BufferedWriter(new FileWriter(RESTAURANT_FILE));
        for(Restaurant r : newRestaurantList)
        {
            bw.write(r.getId() + "," + r.getName() + "," + r.getScore() + "," + r.getPrice() + "," + r.getZipCode() + ",");
            String[] categories = r.getCategories();
            for(int i = 0; i < categories.length; i++)
            {
                if(i == categories.length-1)
                {
                    bw.write(categories[i]);
                }
                else
                {
                    bw.write(categories[i] + ",");
                }
            }
            bw.write(System.lineSeparator());
        }
        bw.close();
    }

    public void addRestaurant(Restaurant newRestaurant, List<Restaurant> newRestaurantList)
    {
        newRestaurantList.add(newRestaurant);
    }

    public List<Restaurant> searchRestaurantByName(String newName, List<Restaurant> newRestaurantList)
    {
        List<Restaurant> resList = new ArrayList<>();
        for(Restaurant r : newRestaurantList)
        {
            if(r.getName().toLowerCase().contains(newName.toLowerCase()))
            {
                resList.add(r);
            }
        }
        return resList;
    }

    public List<Restaurant> searchRestaurantByScore(double a, double b, List<Restaurant> newRestaurantList)
    {
        List<Restaurant> resList = new ArrayList<>();
        for(Restaurant r : newRestaurantList)
        {
            if(r.getScore() >= a && r.getScore() <= b)
            {
                resList.add(r);
            }
        }
        return resList;
    }

    public List<Restaurant> searchRestaurantByCategory(String newCategory, List<Restaurant> newRestaurantList)
    {
        List<Restaurant> resList = new ArrayList<>();
        for(Restaurant r : newRestaurantList)
        {
            String[] allCategories = r.getCategories();
            for(String c : allCategories)
            {
                if(c.toLowerCase().contains(newCategory.toLowerCase()))
                {
                    resList.add(r);
                    break;
                }
            }
        }
        return resList;
    }

    public List<Restaurant> searchRestaurantByPrice(String newPrice, List<Restaurant> newRestaurantList)
    {
        List<Restaurant> resList = new ArrayList<>();
        for(Restaurant r : newRestaurantList)
        {
            if(r.getPrice().equals(newPrice))
            {
                resList.add(r);
            }
        }
        return resList;
    }

    public List<Restaurant> searchRestaurantByZipCode(int newZipCode, List<Restaurant> newRestaurantList)
    {
        List<Restaurant> resList = new ArrayList<>();
        for(Restaurant r : newRestaurantList)
        {
            if(r.getZipCode() == newZipCode)
            {
                resList.add(r);
            }
        }
        return resList;
    }

    public List<String> getAllRestaurantCategories(List<Restaurant> newRestaurantList)
    {
        List<String> allCategories = new ArrayList<>();
        for(Restaurant r : newRestaurantList)
        {
            String[] newCategories = r.getCategories();
            for(String c : newCategories)
            {
                boolean flag = false;
                for(String s : allCategories)
                {
                    if(s.equals(c))
                    {
                        flag = true;
                        break;
                    }
                }
                if(!flag)
                {
                    allCategories.add(c);
                }
            }
        }
        return allCategories;
    }

    public String displayRestaurantByCategory(String newResName, List<Restaurant> newRestaurantList)
    {
        String resList = "";
        int count = 0;
        for(Restaurant k : newRestaurantList)
        {
            String[] resCategories = k.getCategories();
            for(String c : resCategories)
            {
                if(newResName.equals(c))
                {
                    if(count != 0)
                    {
                        resList += "\n";
                    }
                    resList += k.getName();
                    ++count;
                    break;
                }
            }
        }
        return resList;
    }

    public int findRestaurantID(String newResName, List<Restaurant> newRestaurantList)
    {
        for(Restaurant r : newRestaurantList)
        {
            if(r.getName().equalsIgnoreCase(newResName))
            {
                return r.getId();
            }
        }
        return -1;
    }

    public boolean isRestaurantTaken(String newResName, List<Restaurant> newRestaurantList)
    {
        for(Restaurant r : newRestaurantList)
        {
            if(r.getName().equalsIgnoreCase(newResName))
            {
                return true;
            }
        }
        return false;
    }

    public String findRestaurantName(int newResID, List<Restaurant> newRestaurantList)
    {
        return newRestaurantList.get(newResID-1).getName();
    }

    public int getNewRestaurantID(List<Restaurant> newRestaurantList)
    {
        return newRestaurantList.get(newRestaurantList.size()-1).getId()+1;
    }

    public void loadMenu(List<Restaurant> newRestaurantList, List<FoodItem> newMenuList) throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader(MENU_FILE));
        while (true) {
            String line = br.readLine();
            if (line == null) break;
            String [] array = line.split(",", -1);
            int resID = Integer.parseInt(array[0]);
            FoodItem newFoodItem = new FoodItem(resID, array[1], array[2], Double.parseDouble(array[3]));
            newMenuList.add(newFoodItem);
            newRestaurantList.get(resID-1).addFoodItemToRestaurant(newFoodItem);
        }
        br.close();
    }

    public void saveMenu(List<FoodItem> newMenuList) throws Exception
    {
        BufferedWriter bw = new BufferedWriter(new FileWriter(MENU_FILE));
        for(FoodItem f : newMenuList)
        {
            bw.write(f.getRestaurantId() + "," + f.getCategory() + "," + f.getName() + "," + f.getPrice());
            bw.write(System.lineSeparator());
        }
        bw.close();
    }

    public void addFood(FoodItem newFood, List<Restaurant> newRestaurantList, List<FoodItem> newMenuList)
    {
        newMenuList.add(newFood);
        newRestaurantList.get(newFood.getRestaurantId()-1).addFoodItemToRestaurant(newFood);
    }

    public List<FoodItem> searchByFoodName(String newName, List<FoodItem> newMenuList)
    {
        List<FoodItem> foodList = new ArrayList<>();
        for(FoodItem f : newMenuList)
        {
            if(f.getName().toLowerCase().contains(newName.toLowerCase()))
            {
                foodList.add(f);
            }
        }
        return foodList;
    }

    public List<FoodItem> searchFoodInRestaurant(String newFood, String newResName, List<Restaurant> newRestaurantList) 
    {
        int newResID = findRestaurantID(newResName, newRestaurantList);
        List<FoodItem> foodList = new ArrayList<>();
        if(newResID == -1)
        {
            return null;
        }
        else
        {
            List<FoodItem> resMenu = newRestaurantList.get(newResID-1).getFoodMenuOfRestaurant();
            for(FoodItem f : resMenu)
            {
                if(f.getName().toLowerCase().contains(newFood.toLowerCase()))
                {
                    foodList.add(f);
                }
            }
            return foodList;
        }
    }

    public List<FoodItem> searchByFoodCategory(String newCategory, List<FoodItem> newMenuList)
    {
        List<FoodItem> foodList = new ArrayList<>();
        for(FoodItem f : newMenuList)
        {
            if(f.getCategory().toLowerCase().contains(newCategory.toLowerCase()))
            {
                foodList.add(f);
            }
        }
        return foodList;
    }

    public List<FoodItem> searchFoodCategoryInRestaurant(String newCategory, String newResName, List<Restaurant> newRestaurantList) 
    {
        int newResID = findRestaurantID(newResName, newRestaurantList);
        List<FoodItem> foodList = new ArrayList<>();
        if(newResID == -1)
        {
            return null;
        }
        else
        {
            List<FoodItem> resMenu = newRestaurantList.get(newResID-1).getFoodMenuOfRestaurant();
            for(FoodItem f : resMenu)
            {
                if(f.getCategory().toLowerCase().contains(newCategory.toLowerCase()))
                {
                    foodList.add(f);
                }
            }
            return foodList;
        }
    }

    public List<FoodItem> searchFoodByPrice(double a, double b, List<FoodItem> newMenuList)
    {
        List<FoodItem> foodList = new ArrayList<>();
        for(FoodItem f : newMenuList)
        {
            if(f.getPrice() >= a && f.getPrice() <= b)
            {
                foodList.add(f);
            }
        }
        return foodList;
    }

    public List<FoodItem> searchFoodByPriceInRestaurant(double a, double b, String newResName, List<Restaurant> newRestaurantList)
    {
        int newResID = findRestaurantID(newResName, newRestaurantList);
        List<FoodItem> foodList = new ArrayList<>();
        if(newResID == -1)
        {
            return null;
        }
        else
        {
            List<FoodItem> resMenu = newRestaurantList.get(newResID-1).getFoodMenuOfRestaurant();
            for(FoodItem f : resMenu)
            {
                if(f.getPrice() >= a && f.getPrice() <= b)
                {
                    foodList.add(f);
                }
            }
            return foodList;
        }
    }

    public List<FoodItem> displayCostlyFood(String newResName, List<Restaurant> newRestaurantList)
    {
        int newResID = findRestaurantID(newResName, newRestaurantList);
        List<FoodItem> foodList = new ArrayList<>();
        if(newResID == -1)
        {
            return null;
        }
        else
        {
            double max = -1;
            List<FoodItem> resMenu = newRestaurantList.get(newResID-1).getFoodMenuOfRestaurant();
            for(FoodItem f : resMenu)
            {
                if(max < f.getPrice())
                {
                    max = f.getPrice();
                }
            }
            for(FoodItem f : resMenu)
            {
                if(f.getPrice() == max)
                {
                    foodList.add(f);
                }
            }
            return foodList;
        }
    }

    public int getFoodItemCountInRestaurant(Restaurant newRes)
    {
        return newRes.getFoodMenuOfRestaurant().size();
    }

    public boolean checkDuplicateFood(String newFoodName, String newFoodCategory, int newResID, List<FoodItem> newMenuList)
    {
        for(FoodItem f : newMenuList)
        {
            if(f.getName().equalsIgnoreCase(newFoodName) && f.getCategory().equalsIgnoreCase(newFoodCategory) && f.getRestaurantId() == newResID)
            {
                return true;
            }
        }
        return false;
    }
}
