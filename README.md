# routing-service_bus

## Aufgabestellung
- Implementierung eines Microservice-basierten Routenplaners für Fortbewegungsmittel (Taxi, Boot, Flugzeug, Bus)
- Bereitstellung von Routendaten für die Services aus der Theoriephase
- Als „Frontend“ kommt Postman zum Einsatz
- Über die spezifizierten Kernfunktionalitäten hinausgehende Funktionalitäten sind ausdrücklich erlaubt und werden bei der Bewertung berücksichtigt

## Routen-Service
- Die Aufgabe jeder Gruppe ist die Implementierung eines neuen Services zur Routenfindung zwischen zwei Locations
- Die Domain-Services aus der Theoriephase bieten Routen an
- Locations werden über den location-service bereitgestellt

  ![image](https://github.com/abatplus-vl-htw-wise-2324/routing-service_ship/assets/138193083/bab0b0e7-1f5c-4719-97dd-ec3fbd1c16ca)

### Faktoren für Fortbewegungsmittel
- Flugzeug: 1.1
- Taxi: 1.2
- Bus: 1.3
- Boot: 1.5

## location-service
- Vorhandener Service, der Orte, die verfügbaren Verkehrsmittel und Entfernungen bereitstellt
- /features entsprechen den Verkehrsmitteln (Taxi, Bus, …)
- /locations 
-  als Start-/Zielpunkte
    - bieten verschiedene Features (mit Verfügbarkeit)
    - /distances liefert die geografische Entfernung zwischen zwei Locations
- https://location.edu.smef.io/swagger-ui/index.html#/

## Funktionalität
- Abfragen einer Route
- Erfolgreiches Ermitteln einer Route mit Start- und Zielort, bei Wechselnden Verfügbarkeiten der Verkehrsmittel
- Beachtung der Faktoren für die Entfernungen bei den jeweiligen Verkehrsmitteln
## Zusatzfunktionalität:
- Möglichkeit, Verkehrsmittel zu selektieren (z.B. nur Taxi)
- Möglichkeit, schnellsten oder günstigsten Weg zu finden

## Technische Anforderungen
- Microservice-basierte Applikation (Spring Boot)
- Aufrufen weiterer Services 
- Bereitstellung einer API-Dokumentation (OpenAPI)
- Integration in eine bereitgestellte Infrastruktur mittels CI/CD
- Orchestrierung mittels Docker und Kubernetes
- Integrationstests

## Bewertungskriterien
- Service & Anwendung 70%
    - Funktionalität
    - Code Quality
    - Funktionierende Infrastruktur / CICD
    - Betrieb
    - Integrationstests
    - API-Doku
- Präsentation ("Produktpräsentation") 30%

## Fragen
- Bei Fragen zur Aufgabenstellung wenden Sie sich bitte per Email an das Betreuerteam:
    - David Dressler (david.dressler@abatplus.de)
    - Kai Jachmann (kai.jachmann@abatplus.de)
    - Pascal Bourguignon (pascal.bourguignon@abatplus.de)
    - Steffen Schery (steffen.schery@abatplus.de)   

