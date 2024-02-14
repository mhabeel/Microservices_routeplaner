package com.abatplus.routeservicebus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutPlaner {

private List<BusRoute>  busRoutes;
private List<PlaneRoute>  planeRoutes;
private List<ShipRoute>  shipRoutes;
private List<TaxiRoute>  taxiRoutes;

    public void addPlaneRoute(PlaneRoute planeRoute) {
        planeRoutes.add(planeRoute);
    }

    public void addBusRoute(BusRoute busRoute) {
        busRoutes.add(busRoute);
    }

    public void addTaxiRoute(TaxiRoute taxiRoute) {
        taxiRoutes.add(taxiRoute);
    }

    public void addShipRoute(ShipRoute shipRoute) {
        shipRoutes.add(shipRoute);
    }


}
