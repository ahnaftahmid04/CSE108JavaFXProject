package sample;

import javafx.application.Platform;
import util.LoginDTO;
import util.LoginDTO.UserTypes;
import cravio.*;

import java.io.IOException;

public class ReadThread implements Runnable {
    private final Thread thr;
    private final Main main;

    public ReadThread(Main main) {
        this.main = main;
        this.thr = new Thread(this);
        thr.start();
    }

    public void run() {
        try {
            while (true) {
                Object o = main.getNetworkUtil().read();
                if (o != null) {
                    if (o instanceof LoginDTO) {
                        LoginDTO loginDTO = (LoginDTO) o;
                        System.out.println(loginDTO.getUserName());
                        System.out.println(loginDTO.isStatus());
                        main.setLists(loginDTO.getRestaurantList(), loginDTO.getFoodItemList());
                        // the following is necessary to update JavaFX UI components from user created threads
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if(loginDTO.isStatus() && loginDTO.getUserType() == UserTypes.RESTAURANT)
                                {
                                    try {
                                        main.showRestaurantPage(loginDTO.getRestaurant(), loginDTO.getOrderList());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                else if(loginDTO.isStatus() && loginDTO.getUserType() == UserTypes.CUSTOMER)
                                {
                                    try {
                                        main.showHomePage(loginDTO.getUserName());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                else {
                                    main.showAlert();
                                }
                            }
                        });
                    }
                    if(o instanceof Order)
                    {
                        Order order = (Order) o;
                        main.addOrderToRestaurant(order);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                main.getNetworkUtil().closeConnection();
                System.out.println("Read Connection closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



