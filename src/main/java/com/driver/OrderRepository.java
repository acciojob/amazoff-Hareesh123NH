package com.driver;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private HashMap<String, Order> orderMap;
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
        orderMap.put(order.getId(),orderMap.getOrDefault(order.getId(),order));
    }

    public void savePartner(String partnerId){
        // your code here
        // create a new partner with given partnerId and save it
        DeliveryPartner partner=new DeliveryPartner(partnerId);
        partnerMap.put(partner.getId(),partnerMap.getOrDefault(partner.getId(),partner));
    }

    public void saveOrderPartnerMap(String orderId, String partnerId){
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)){
            // your code here
            //add order to given partner's order list
            //increase order count of partner
            //assign partner to this order
            HashSet<String> orders=partnerToOrderMap.getOrDefault(partnerId,new HashSet<>());
            orders.add(orderId);
            DeliveryPartner partner=partnerMap.get(partnerId);
            partner.setNumberOfOrders(partner.getNumberOfOrders()+1);
            orderToPartnerMap.put(orderId,partnerId);
        }
    }

    public Order findOrderById(String orderId){
        // your code here
        if(!orderMap.containsKey(orderId)){
            return null;
        }
        return orderMap.get(orderId);
    }

    public DeliveryPartner findPartnerById(String partnerId){
        // your code here
        if(!partnerMap.containsKey(partnerId)){
            return null;
        }
        return partnerMap.get(partnerId);
    }

    public Integer findOrderCountByPartnerId(String partnerId){
        // your code here
        return partnerMap.getOrDefault(partnerId,new DeliveryPartner(0)).getNumberOfOrders();
    }

    public List<String> findOrdersByPartnerId(String partnerId){
        // your code here
        HashSet<String> orders=partnerToOrderMap.getOrDefault(partnerId,new HashSet<>());
        return new ArrayList<>(orders);

    }

    public List<String> findAllOrders(){
        // your code here
        // return list of all orders

        return new ArrayList<>(orderMap.keySet());
    }

    public void deletePartner(String partnerId){
        // your code here
        // delete partner by ID
        if(partnerToOrderMap.containsKey(partnerId)){
            partnerToOrderMap.remove(partnerId);
        }
        if ((partnerMap.containsKey(partnerId))){
            partnerMap.remove(partnerId);
        }

        for(String order:orderToPartnerMap.keySet()){
            if(orderToPartnerMap.get(order).equals(partnerId)){
                orderToPartnerMap.remove(order);
            }
        }
    }

    public void deleteOrder(String orderId){
        // your code here
        // delete order by ID
        if (orderToPartnerMap.containsKey(orderId)){
            orderToPartnerMap.remove(orderId);
        }
       if (orderMap.containsKey(orderId)){
           orderMap.remove(orderId);
       }
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

        return orderToPartnerMap.size();

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