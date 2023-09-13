package cravio;

import java.io.Serializable;
import java.util.Objects;

public class Customer implements Serializable{
    private String username;

    public Customer(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj instanceof Customer)
        {
            Customer customer = (Customer) obj;
            return this.username.equals(customer.getUsername());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(username);
    }
}
