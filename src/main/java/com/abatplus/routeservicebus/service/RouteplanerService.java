package com.abatplus.routeservicebus.service;

import com.abatplus.routeservicebus.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class RouteplanerService {
    public static final double BUSFACTOR = 1.3;
    public static final double PLANFACTOR = 1.1;
    public static final double TAXIFACTOR = 1.2;

    public static final double SHIPFACTOR = 1.5;
    private RoutPlaner routPlaners;
    private static final String url = "https://bus.edu.smef.io";
    private static final String planeUrlApi = "https://plane.edu.smef.io";

    private static final String taxiUrlApi = "https://taxi.edu.smef.io";

    private static final String shipUrlApi = "https://ship.edu.smef.ior";
    private static final String locationUrl = "https://location.edu.smef.io";
    private WebClient webClientBus;
    private WebClient webClientTaxi;
    private WebClient webClientPLane;
    private WebClient webClientShip;
    private WebClient webclientLocation;

    public RouteplanerService(WebClient.Builder webClientBuilder) {
        this.webClientBus = webClientBuilder.baseUrl(url).build();
        this.webClientPLane = webClientBuilder.baseUrl(planeUrlApi).build();
        this.webClientTaxi = webClientBuilder.baseUrl(taxiUrlApi).build();
        this.webClientShip = webClientBuilder.baseUrl(shipUrlApi).build();
        this.webclientLocation = webClientBuilder.baseUrl(locationUrl).build();
        routPlaners = new RoutPlaner();
    }
    // public RouteplanerService(WebClient webClient){
    //      WebClient client = WebClient.create("https://bus.edu.smef.io");
    // }

    public List<BusRoute> getBusRoute() {
        List<BusRoute> result = webClientBus.get()
                .uri("/api/routs").accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(BusRoute.class).collectList().block();
        return result;
    }

    public List<PlaneRoute> getPlaneRoute() {
        List<PlaneRoute> result = webClientPLane.get()
                .uri("/api/planeroutes/retrieveAllRoutes").accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(PlaneRoute.class).collectList().block();
        return result;
    }

    public List<TaxiRoute> getTaxiRoute() {
        List<TaxiRoute> result = webClientTaxi.get()
                .uri("/api/v1/routes").accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(TaxiRoute.class).collectList().block();
        return result;
    }

    public List<ShipRoute> getShipRoute() {
        List<ShipRoute> result = null;

        try {
             result = webClientShip.get()
                    .uri("/v1/routes").accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToFlux(ShipRoute.class).collectList().block();

        } catch(Exception e){
            e.printStackTrace();
            return null;
        }

        return result;
    }

    public List<Location> getLocations() {
        List<Location> result = webclientLocation.get()
                .uri("/locations").accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Location.class)
                .collectList().block();
        return result;
    }

    public Location getLocation(String id) {
        Location result = webclientLocation.get()
                .uri("/locations/{id}", id).accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Location.class).block();
        return result;
    }

    public String getLocationId(String name) {
        String id = String.valueOf(getLocations().stream().filter(b -> b.getName().equalsIgnoreCase(name)).findFirst().get().getId());

        return id;

    }

    public String getLocationName(String id) {
        String name = String.valueOf(getLocations().stream().filter(b -> b.getId().equals(id)).findFirst().get().getName());

        return name;
    }


    public ResponseEntity<List<Route>> getRouteByLocations(String from, String to) {
        double distance;
        String fromTd = "";
        String toId = "";
        List<Route> result = new ArrayList<>();
        try {
            TaxiRoute taxi = null;
                   try {
                        taxi = getAllRouts().getTaxiRoutes().stream().filter(a -> a.getSource().equalsIgnoreCase(from) & a.getDestination().equalsIgnoreCase(to))
                               .findFirst().orElse(null);
                   }catch (Exception e){
                     e.printStackTrace();
                     taxi = null;
                   }


            if (taxi != null) {
                fromTd = getLocationId(taxi.getSource());
                toId = getLocationId(taxi.getDestination());
                distance = getDistance(fromTd, toId).getDistance() * TAXIFACTOR;
                taxi.setDistance(distance);
                taxi.setDauer(distance / taxi.getAvg_vel());
                taxi.setVerkehrsmittel("Taxi");
                result.add(taxi);
            }
            BusRoute bus = null;
            try {
                 bus = getAllRouts().getBusRoutes().stream().filter(a -> a.getAktuelOrt().equalsIgnoreCase(from) & a.getZielOrt().equalsIgnoreCase(to))
                        .findFirst().orElse(null);
            }catch (Exception e){
                e.printStackTrace();

            }



            if (bus != null) {
                fromTd = getLocationId(bus.getAktuelOrt());
                toId = getLocationId(bus.getZielOrt());
                distance = getDistance(fromTd, toId).getDistance() * BUSFACTOR;
                bus.setDistance(distance);
                bus.setDauer(distance / bus.getBuses().stream().findFirst().get().getGeschwindigkeit());


                bus.setVerkehrsmittel("bus");
                result.add(bus);
            }
            ShipRoute ship = null;
            try{
                ship = getAllRouts().getShipRoutes().stream().filter(a -> a.getStartPort().equalsIgnoreCase(from) & a.getDestinationPort().equalsIgnoreCase(to))
                        .findFirst().orElse(null);
               }catch (Exception e){
                   e.printStackTrace();
                   ship = null;
               }


            if (ship != null) {
                fromTd = getLocationId(ship.getStartPort());
                toId = getLocationId(ship.getDestinationPort());
                distance = getDistance(fromTd, toId).getDistance() * SHIPFACTOR;
                ship.setDistance(distance);
                ship.setDauer(ship.getDistance() / ship.getAverageSpeed());


                ship.setVerkehrsmittel("ship");
                result.add(ship);
            }
            PlaneRoute plane = null;
            try {
                plane = getAllRouts().getPlaneRoutes().stream().filter(a -> a.getStart().equalsIgnoreCase(from) & a.getDestination().equalsIgnoreCase(to))
                        .findFirst().orElse(null);
            }catch (Exception e){
                   e.printStackTrace();
                   plane = null;
            }


            if (plane != null) {
                fromTd = getLocationId(plane.getStart());
                toId = getLocationId(plane.getDestination());
                distance = getDistance(fromTd, toId).getDistance() * PLANFACTOR;
                plane.setDistance(distance);
                plane.setDauer(plane.getDistance() / plane.getAverageSpeed());
                plane.setVerkehrsmittel("plane");
                result.add(plane);
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (WebClientResponseException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public Route getRouteById(String id) {
        TaxiRoute taxi = null;
        ShipRoute ship = null;
        BusRoute bus = null;
        PlaneRoute plane = null;
        double distance;
        String fromTd = "";
        String toId = "";
        Route result = null;
        if (id.matches("\\d+")) {
             bus = getAllRouts().getBusRoutes().stream().filter(a -> a.getRouteId() == Integer.valueOf(id))
                    .findFirst().orElse(null);

            if (bus != null) {
                fromTd = getLocationId(bus.getAktuelOrt());
                toId = getLocationId(bus.getZielOrt());
                distance = getDistance(fromTd, toId).getDistance() * BUSFACTOR;
                bus.setDistance(distance);
                bus.setDauer(distance / bus.getBuses().stream().findFirst().get().getGeschwindigkeit());

                bus.setVerkehrsmittel("bus");
                result = bus;
            }

        }

        try {
             taxi = getAllRouts().getTaxiRoutes().stream().filter(a -> a.getId().equals(id))
                    .findFirst().orElse(null);
        }catch (Exception e){
            e.printStackTrace();
            taxi = null;
        }



        if (taxi != null) {
            fromTd = getLocationId(taxi.getSource());
            toId = getLocationId(taxi.getDestination());
            distance = getDistance(fromTd, toId).getDistance() * TAXIFACTOR;
            taxi.setDistance(distance);
            taxi.setDauer(taxi.getDistance() / taxi.getAvg_vel());

            taxi.setVerkehrsmittel("Taxi");
            result = taxi;
        }

           try{
                ship = getAllRouts().getShipRoutes().stream().filter(a -> a.getId().equals(id))
                       .findFirst().orElse(null);
           }catch (Exception e){
               e.printStackTrace();
               ship = null;

           }



        if (ship != null) {
            fromTd = getLocationId(ship.getStartPort());
            toId = getLocationId(ship.getDestinationPort());
            distance = getDistance(fromTd, toId).getDistance() * SHIPFACTOR;
            ship.setDistance(distance);
            ship.setDauer(ship.getDistance() / ship.getAverageSpeed());
            ship.setVerkehrsmittel("ship");
            result = ship;
        }

        try{
            plane = getAllRouts().getPlaneRoutes().stream().filter(a -> a.getId().equals(id))
                    .findFirst().orElse(null);
        }catch (Exception e){
            e.printStackTrace();
            plane = null;

        }


        if (plane != null) {
            fromTd = getLocationId(plane.getStart());
            toId = getLocationId(plane.getDestination());
            distance = getDistance(fromTd, toId).getDistance() * PLANFACTOR;
            plane.setDistance(distance);
            plane.setDauer(plane.getDistance() / plane.getAverageSpeed());
            plane.setVerkehrsmittel("plane");
            result = plane;
        }


        return result;


    }

    public Distance getDistance(String from, String to) {

        Distance result = webclientLocation.get()
                .uri("/distances?from={from}&to={to}", from, to).accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Distance.class)
                .block();

        return result;
    }

    public Route getSelectedRoute(String from, String to, String type) {
        TaxiRoute taxi = null;
        ShipRoute ship = null;
        BusRoute bus = null;
        PlaneRoute plane = null;
        double distance;
        Route result = null;
        String fromTd = "";
        String toId = "";

        try {
            taxi = getAllRouts().getTaxiRoutes().stream().filter(a -> a.getSource().equalsIgnoreCase(from) & a.getDestination().equalsIgnoreCase(to))
                    .findFirst().orElse(null);
        }catch (Exception e){
           e.printStackTrace();
           taxi = null;
        }



        if (taxi != null) {
            fromTd = getLocationId(taxi.getSource());
            toId = getLocationId(taxi.getDestination());
            distance = getDistance(fromTd, toId).getDistance() * TAXIFACTOR;
            taxi.setDistance(distance);
            taxi.setDauer(distance / taxi.getAvg_vel());
            taxi.setVerkehrsmittel("Taxi");

            if (type.equalsIgnoreCase("taxi")) {
                result = taxi;
                // data.add(taxi);
            }

        }
        try {
            bus = getAllRouts().getBusRoutes().stream().filter(a -> a.getAktuelOrt().equalsIgnoreCase(from) & a.getZielOrt().equalsIgnoreCase(to))
                    .findFirst().orElse(null);
        }catch (Exception e){
            e.printStackTrace();
            bus = null;
        }


        if (bus != null) {
            fromTd = getLocationId(bus.getAktuelOrt());
            toId = getLocationId(bus.getZielOrt());
            distance = getDistance(fromTd, toId).getDistance() * BUSFACTOR;
            bus.setDistance(distance);
            bus.setDauer(bus.getDistance() / bus.getBuses().get(0).getGeschwindigkeit());
            bus.setVerkehrsmittel("bus");

            if (type.equalsIgnoreCase("bus")) {
                result = bus;
                // data.add(bus);
            }

        }
            try {
                 ship = getAllRouts().getShipRoutes().stream().filter(a -> a.getStartPort().equalsIgnoreCase(from) & a.getDestinationPort().equalsIgnoreCase(to))
                        .findFirst().orElse(null);
            }catch(Exception e){
                e.printStackTrace();
                ship = null;
            }


                if (ship != null) {
                    fromTd = getLocationId(ship.getStartPort());
                    toId = getLocationId(ship.getDestinationPort());
                    distance = getDistance(fromTd, toId).getDistance() * SHIPFACTOR;
                    ship.setDistance(distance);
                    ship.setDauer(ship.getDistance() / ship.getAverageSpeed());
                    ship.setVerkehrsmittel("ship");
                    if (type.equalsIgnoreCase("ship")) {
                        result = ship;

                    }

                }
        try {
             plane = getAllRouts().getPlaneRoutes().stream().filter(a -> a.getStart().equalsIgnoreCase(from) & a.getDestination().equalsIgnoreCase(to))
                    .findFirst().orElse(null);
        }catch(WebClientResponseException e){
            e.printStackTrace();
            plane = null;
        }



        if (plane != null) {
            fromTd = getLocationId(plane.getStart());
            toId = getLocationId(plane.getDestination());
            distance = getDistance(fromTd, toId).getDistance() * PLANFACTOR;
            plane.setDistance(distance);
            plane.setDauer(plane.getDistance() / plane.getAverageSpeed());
            plane.setVerkehrsmittel("plane");

            if (type.equalsIgnoreCase("plane")) {
                result = plane;
                // data.add(plane);
            }

        }


        return result;

    }

    public ResponseEntity<List<Route>> getFastestRoutes(String from, String to) {

        ResponseEntity<List<Route>> data = getRouteByLocations(from, to);

        if (data.hasBody()) {
            List<Route> sortedRoutes = data.getBody()
                    .stream()
                    .sorted(Comparator.comparing(Route::getDauer))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(sortedRoutes);
        } else {
            // Handle the case where there is no data
            return ResponseEntity.noContent().build();
        }
    }

    public ResponseEntity<List<Route>> getCheapestRoutes(String from, String to) {
        ResponseEntity<List<Route>> data = getRouteByLocations(from, to);

        if (data.hasBody()) {
            List<Route> sortedRoutes = data.getBody()
                    .stream()
                    .sorted(Comparator.comparing(Route::getPriceForRoute))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(sortedRoutes);
        } else {
            // Handle the case where there is no data
            return ResponseEntity.noContent().build();
        }

    }

    public RoutPlaner getAllRouts() {
        this.routPlaners.setShipRoutes(getShipRoute());


        this.routPlaners.setBusRoutes(getBusRoute());

        this.routPlaners.setPlaneRoutes(getPlaneRoute());
        this.routPlaners.setTaxiRoutes(getTaxiRoute());

        return this.routPlaners;
    }

}
