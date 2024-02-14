package com.abatplus.routeservicebus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusRoute extends Route {
private int routeId;
private String aktuelOrt;
private String zielOrt;
    //private double dauer;

private List<Bus> buses;

}
