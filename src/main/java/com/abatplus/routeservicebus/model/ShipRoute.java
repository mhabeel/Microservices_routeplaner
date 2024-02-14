package com.abatplus.routeservicebus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipRoute extends Route {
private String id;
private String startPort;
private String destinationPort;
private double pricePerKm;
private double averageSpeed;
private List<Ship> ships;
//private double dauer;
}
