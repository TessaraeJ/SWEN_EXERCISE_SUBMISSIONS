import UIKit
import CoreLocation
import MapKit

class ViewController: UIViewController, CLLocationManagerDelegate, MKMapViewDelegate {
    let locationManager = CLLocationManager()
    
    let searchController = UISearchController(searchResultsController: nil)
    @IBOutlet weak var mapView: MKMapView!
    var matchingItems: [MKMapItem] = [MKMapItem]()
    
    @IBAction func getLocation(_ sender: Any) {
        let status = CLLocationManager.authorizationStatus()
        
        switch status {
        case .notDetermined:
                locationManager.requestWhenInUseAuthorization()
                return
            
        case .denied, .restricted:
            let alert = UIAlertController(title: "Location Services disabled", message: "Please enable Location Services in Settings", preferredStyle: .alert)
            let okAction = UIAlertAction(title: "OK", style: .default, handler: nil)
            alert.addAction(okAction)
            
            present(alert, animated: true, completion: nil)
            return
        case .authorizedAlways, .authorizedWhenInUse:
            break
       
        }
        
        locationManager.delegate = self
        locationManager.startMonitoringSignificantLocationChanges()
        locationManager.stopUpdatingLocation()
        locationManager.startMonitoringVisits()
    }
    
    @IBAction func findMe(_ sender: UIBarButtonItem){
        
        let mylocation = self.mapView.userLocation
        let center = CLLocationCoordinate2D(latitude: mylocation.coordinate.latitude, longitude: mylocation.coordinate.longitude)
        let region = MKCoordinateRegion(center: center, span: MKCoordinateSpan(latitudeDelta: 0.01, longitudeDelta: 0.01))
        self.mapView.setRegion(region, animated: true)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //search bar settings
        searchController.searchResultsUpdater = self
        searchController.obscuresBackgroundDuringPresentation = false
        searchController.searchBar.placeholder = "Search Local Location"
        navigationItem.searchController = searchController
        definesPresentationContext = true
        
        //code to track user current location
        mapView.showsUserLocation = true
        mapView.mapType = MKMapType(rawValue: 0)!
        mapView.userTrackingMode = MKUserTrackingMode(rawValue: 2)!
        
        // Do any additional setup after loading the view, typically from a nib.
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        
        var placemark: CLPlacemark!
        lookUpCurrentLocation(completionHandler: {(place) in
                                        if place == nil{
                                            print("Could Not Name Location")
                                        }else{
                                            placemark = place
                                        }
                                    })

            if let currentLocation = locations.last {
                print("Current location:\(String(describing: placemark)) \(currentLocation)")
        }
        
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print(error)
    }
    
    func lookUpCurrentLocation(completionHandler: @escaping (CLPlacemark?)
        -> Void ) {
        // Use the last reported location.
        if let lastLocation = self.locationManager.location {
            let geocoder = CLGeocoder()
            
            // Look up the location name (placemark) and pass it to the completion handler
            geocoder.reverseGeocodeLocation(lastLocation,
                                            completionHandler: { (placemarks, error) in
                                                if error == nil {
                                                    let firstLocation = placemarks?[0]
                                                    completionHandler(firstLocation)
                                                }
                                                else {
                                                    // An error occurred during geocoding.
                                                    completionHandler(nil)
                                                }
            })
        }
        else {
            // No location was available.
            completionHandler(nil)
        }
    }
    // extracting coordinate points (latitude and longitude) from valid name string
    func getCoordinate( addressString : String,
                        completionHandler: @escaping(CLLocationCoordinate2D, NSError?) -> Void ) {
        let geocoder = CLGeocoder()
        geocoder.geocodeAddressString(addressString) { (placemarks, error) in
            if error == nil {
                if let placemark = placemarks?[0] {
                    let location = placemark.location!
                    
                    completionHandler(location.coordinate, nil)
                    return
                }
            }
            
            completionHandler(kCLLocationCoordinate2DInvalid, error as NSError?)
        }
    }
    
    func mapView(_: MKMapView, annotationView:
        MKAnnotationView, calloutAccessoryControlTapped: UIControl) {
        print("Control tapped")
    }
    
    func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
        
        let identifier = "marker"
        var view: MKMarkerAnnotationView
        
        if let dequeuedView = mapView.dequeueReusableAnnotationView(
            withIdentifier: identifier)
            as? MKMarkerAnnotationView {
            dequeuedView.annotation = annotation
            view = dequeuedView
        } else {
            view = MKMarkerAnnotationView(annotation: annotation, reuseIdentifier: identifier)
            view.clusteringIdentifier = "myCluster"
        }
        return view
    }
    
    func mapView(_ mapView: MKMapView, didSelect view: MKAnnotationView) {
       //set up route
    }
}

extension ViewController: UISearchResultsUpdating {
    func updateSearchResults(for searchController: UISearchController) {
        if searchController.searchBar.text! != "" {
            
            matchingItems.removeAll()
            let request = MKLocalSearch.Request()
            request.naturalLanguageQuery = searchController.searchBar.text
            request.region = mapView.region
            
            let search = MKLocalSearch(request: request)
            
            search.start(completionHandler: {(response, error) in
                
                if let results = response {
                    
                    if let err = error {
                        print("Error occurred in search: \(err.localizedDescription)")
                    } else if results.mapItems.count == 0 {
                        print("No matches found")
                    } else {
                        print("Matches found")
                        
                        for item in results.mapItems {
                            self.matchingItems.append(item as MKMapItem)
                            
                            //mapping an annotation for a loction unto mapView
                            let annotation = MKPointAnnotation()
                            annotation.coordinate = item.placemark.coordinate
                            annotation.title = item.name
                            self.mapView.addAnnotation(annotation)
                        }
                    }
                }
            })
        }
    }
}

