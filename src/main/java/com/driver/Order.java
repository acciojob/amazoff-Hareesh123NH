package com.driver;
import java.lang.String;
import java.util.Arrays;
import java.util.List;

public class Order {

    private String id;
    private int deliveryTime;


    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        this.id=id;
        this.deliveryTime=timeConvertInt(deliveryTime);

    }

    public int timeConvertInt(String time){

        List<String> parts= Arrays.asList(time.split(":"));
        int hours = Integer.parseInt(parts.get(0));
        int minutes = Integer.parseInt(parts.get(1));

        return  (hours * 60) + minutes;
    }


    public String getId() {
        return id;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }
}
