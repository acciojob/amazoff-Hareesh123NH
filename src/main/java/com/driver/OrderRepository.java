package com.driver;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, HashSet<String>> partnerToOrderMap;
    private HashMap<String, String> orderToPartnerMap;

    public OrderRepository(){
        this.orderMap = new HashMap<String, Order>();
        this.partnerMap = new HashMap<String, DeliveryPartner>();
        this.partnerToOrderMap = new HashMap<String, HashSet<String>>();
        this.orderToPartnerMap = new HashMap<String, String>();
    }

    public void saveOrder(Order order){
        // your code here
        orderMap.put(order.getId(),order);
    }

    public void savePartner(String partnerId){
        // your code here
        // create a new partner with given partnerId and save it
        DeliveryPartner partner=new DeliveryPartner(partnerId);
        partnerMap.put(partner.getId(),partner);
    }

    public void saveOrderPartnerMap(String orderId, String partnerId){
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)){
            // your code here
            //add order to given partner's order list
            //increase order count of partner
            //assign partner to this order
            HashSet<String> orders=new HashSet<>();
            if(partnerToOrderMap.containsKey(partnerId)){
                 orders=partnerToOrderMap.get(partnerId);
            }
            orders.add(orderId);
            DeliveryPartner partner=partnerMap.get(partnerId);
            partner.setNumberOfOrders(partner.getNumberOfOrders()+1);
            orderToPartnerMap.put(orderId,partnerId);
        }
    }

    public Order findOrderById(String orderId){
        // your code here
        return orderMap.get(orderId);
    }

    public DeliveryPartner findPartnerById(String partnerId){
        // your code here
        return partnerMap.get(partnerId);
    }

    public Integer findOrderCountByPartnerId(String partnerId){
        // your code here
        return partnerMap.get(partnerId).getNumberOfOrders();
    }

    public List<String> findOrdersByPartnerId(String partnerId){
        // your code here
        if(!partnerToOrderMap.containsKey(partnerId)){
            return new ArrayList<>();
        }
        HashSet<String> orders=partnerToOrderMap.get(partnerId);
        List<String> orderlist = new ArrayList<>(orders);
        return orderlist;

    }

    public List<String> findAllOrders(){
        // your code here
        // return list of all orders
        List<String> orders=new ArrayList<>(orderMap.keySet());
        return orders;
    }

    public void deletePartner(String partnerId){
        // your code here
        // delete partner by ID
        partnerToOrderMap.remove(partnerId);
        partnerMap.remove(partnerId);
        for(String order:orderToPartnerMap.keySet()){
            if(orderToPartnerMap.get(order).equals(partnerId)){
                orderToPartnerMap.remove(order);
            }
        }
    }

    public void deleteOrder(String orderId){
        // your code here
        // delete order by ID
        orderToPartnerMap.remove(orderId);
        orderMap.remove(orderId);
        for(String partner:partnerToOrderMap.keySet()){
            if(partnerToOrderMap.get(partner).contains(orderId)){
                partnerToOrderMap.get(partner).remove(orderId);
                DeliveryPartner pt=partnerMap.get(partner);
                pt.setNumberOfOrders(pt.getNumberOfOrders()-1);
            }
        }
    }

    public Integer findCountOfUnassignedOrders(){
        // your code here
        int count=0;
        for (String order:orderMap.keySet()){
            if(!orderToPartnerMap.containsKey(order)){
                count++;
            }
        }
        return count;
    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId){
        // your code here
        int count=0;
        for(String order:orderToPartnerMap.keySet()){
            if(orderToPartnerMap.get(order).equals(partnerId)){
                Order order1=orderMap.get(order);
                if(order1.timeConvertInt(timeString)>order1.getDeliveryTime()){
                    count++;
                }
            }
        }
        return count;
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId){
        // your code here
        // code should return string in format HH:MM

        if(partnerToOrderMap.containsKey(partnerId)){
            int time=0;
            for(String order:partnerToOrderMap.get(partnerId)){
                Order order1=orderMap.get(order);
                if(order1.getDeliveryTime()>time){
                    time=order1.getDeliveryTime();
                }
            }
            int hours = time/ 60;
            int minutes = time% 60;

            return String.format("%02d:%02d", hours, minutes);
        }
        return "";
    }
}