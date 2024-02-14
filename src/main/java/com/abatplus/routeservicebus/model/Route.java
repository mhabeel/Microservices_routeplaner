package com.abatplus.routeservicebus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Route {

    private String verkehrsmittel;

    private double distance;

    private double dauer;


    public static double getPriceForRoute(Route route) {
       double result = 0;
        if(route instanceof BusRoute) {
            result = ((BusRoute) route).getBuses().get(0).getPreis();
        }
        if(route instanceof TaxiRoute) {
            result =  ((TaxiRoute)route).getPrice();
        }
        if(route instanceof PlaneRoute) {
            result = ((PlaneRoute) route).getCostPerKM();
        }
        if(route instanceof ShipRoute) {
            result = ((ShipRoute) route).getPricePerKm();
        }
         return result;
    }

}
