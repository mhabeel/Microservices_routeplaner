package com.abatplus.routeservicebus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bus {

private Long busId;
private String busFahrer;
private double preis;
private double geschwindigkeit;
//private BusRoute route;
}
