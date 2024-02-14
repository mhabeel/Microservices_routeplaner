package com.abatplus.routeservicebus.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxiRoute extends Route {
    private  String id ;
   private  String source ;


    private  String destination ;
    private double price;
    private double avg_vel;
    private String driver_name;

   // private double dauer;

}
