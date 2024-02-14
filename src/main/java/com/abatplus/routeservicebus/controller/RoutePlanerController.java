package com.abatplus.routeservicebus.controller;


import com.abatplus.routeservicebus.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.abatplus.routeservicebus.service.RouteplanerService;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class RoutePlanerController {
    private RouteplanerService service;

    @Autowired
    public RoutePlanerController(RouteplanerService service) {
        this.service = service;
    }


    // Route beim aktuellOrt und Zielort abfragen
    @GetMapping(value = "/locations/{from}/{to}")
    public ResponseEntity<List<Route>> getRoutsByLocations(@PathVariable String from, @PathVariable String to) {
        ResponseEntity<List<Route>> data = service.getRouteByLocations(from, to);
        return data;
    }

    //eine Route beim ID abfragen
    @GetMapping(value = "/route/{id}")
    public Route getRouteById(@PathVariable String id) {

        return service.getRouteById(id);
    }

    //Entfernung zwischen zwei Orte abfragen
    @GetMapping(value = "/distance/{from}/{to}")
    public ResponseEntity<?> getDistance(@PathVariable String from, @PathVariable String to) {
        Distance distance = service.getDistance(from, to);

        if (distance != null) {
            return ResponseEntity.ok(distance);
        } else {
            String errorMessage = "No data found for the given locations.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

    //eine Route durch selektieren von Verkersmittel abfragen
    @GetMapping(value = "route/select/{from}/{to}/{type}")
    public Route selectRoute(@PathVariable String from, @PathVariable String to, @PathVariable String type) {

        return service.getSelectedRoute(from, to, type);
    }

    //sotierte Routen durch die schnellsten abfragen
    @GetMapping(value = "route/fastest/{from}/{to}")
    public ResponseEntity<List<Route>> getFastestRoutes(@PathVariable String from, @PathVariable String to) {
        ResponseEntity<List<Route>> data = service.getFastestRoutes(from, to);

        if (data == null) {
            return ResponseEntity.noContent().build();
        }

        return data;
    }

    //Billigste Route abfragen
    @GetMapping(value = "route/cheapest/{from}/{to}")
    public ResponseEntity<List<Route>> getCheapestRoutes(@PathVariable String from, @PathVariable String to) {
        ResponseEntity<List<Route>> data = service.getCheapestRoutes(from, to);

        if (data == null) {
            return ResponseEntity.noContent().build();
        }

        return data;
    }
}
