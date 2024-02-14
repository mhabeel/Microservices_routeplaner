package com.abatplus.routeservicebus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Distance {

    private Location from;
    private Location to;

    private double distance;
}
