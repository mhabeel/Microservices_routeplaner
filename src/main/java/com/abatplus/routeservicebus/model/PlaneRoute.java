package com.abatplus.routeservicebus.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaneRoute extends Route {
    private  String id ;
   // private  int planeIdentifier ;
   // private  String airline ;
    private  double costPerKM ;
    private  int averageSpeed ;
    private  String start ;
    private  String destination ;
    private String airline;
    //private double dauer;
}
