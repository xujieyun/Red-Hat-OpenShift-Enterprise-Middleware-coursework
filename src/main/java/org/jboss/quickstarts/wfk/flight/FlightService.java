package org.jboss.quickstarts.wfk.flight;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

import org.jboss.quickstarts.wfk.area.Area;
import org.jboss.quickstarts.wfk.area.AreaService;
import org.jboss.quickstarts.wfk.area.InvalidAreaCodeException;
import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.customer.CustomerRepository;
import org.jboss.quickstarts.wfk.customer.CustomerValidator;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public class FlightService {

    @Inject
    private @Named("logger") Logger log;

    @Inject
    private FlightValidator validator;

    @Inject
    private FlightRepository crud;

    private ResteasyClient client;

    /**
     * <p>Create a new client which will be used for our outgoing REST client communication</p>
     */
    public FlightService() {
        // Create client service instance to make REST requests to upstream service
        client = new ResteasyClientBuilder().build();
    }

    /**
     * <p>Returns a List of all persisted {@link Flight} objects, sorted alphabetically by last name.<p/>
     *
     * @return List of Flight objects
     */
    List<Flight> findAllOrderedByName() {
        return crud.findAllOrderedByName();
    }

    /**
     * <p>Returns a single Flight object, specified by a Long id.<p/>
     *
     * @param id The id field of the Flight to be returned
     * @return The Flight with the specified id
     */
    Flight findById(Long id) {
        return crud.findById(id);
    }

    /**
     * <p>Returns a single Flight object, specified by a String firstName.<p/>
     *
     * @param firstName The firstName field of the Flight to be returned
     * @return The first Flight with the specified firstName
     */
    List<Flight> findAllByFlightNumber(String flightNumber) {
        return crud.findAllByFirstName(flightNumber);
    }

    /**
     * <p>Writes the provided Flight object to the application database.<p/>
     *
     * <p>Validates the data in the provided Flight object using a {@link FlightValidator} object.<p/>
     *
     * @param Flight The Flight object to be written to the database using a {@link FlightRepository} object
     * @return The Flight object that has been successfully written to the application database
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    Flight create(Flight flight) throws ConstraintViolationException, ValidationException, Exception {
        log.info("FlightService.create() - Creating " + flight.getFlightNumber());
        
        // Check to make sure the data fits with the parameters in the Flight model and passes validation.
        validator.validateFlight(flight);
        try {
//            Area area = service.getAreaById(Integer.parseInt(flight.getPhoneNumber().substring(1, 4)));
//            flight.setState("success");
        } catch (ClientErrorException e) {
            if (e.getResponse().getStatusInfo() == Response.Status.NOT_FOUND) {
            	throw new InvalidAreaCodeException("The area code provided does not exist", e);
                } else {
                throw e;
            }
        }

        // Write the flight to the database.
        return crud.create(flight);
    }

    /**
     * <p>Updates an existing Flight object in the application database with the provided Flight object.<p/>
     *
     * <p>Validates the data in the provided Flight object using a FlightValidator object.<p/>
     *
     * @param Flight The Flight object to be passed as an update to the application database
     * @return The Flight object that has been successfully updated in the application database
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    Flight update(Flight flight) throws ConstraintViolationException, ValidationException, Exception {
        log.info("FlightService.update() - Updating " + flight.getFlightNumber());
        
        // Check to make sure the data fits with the parameters in the Flight model and passes validation.
        validator.validateFlight(flight);

        // Set client target location and define the proxy API class
        ResteasyWebTarget target = client.target("http://csc8104-states.b9ad.pro-us-east-1.openshiftapps.com");
        AreaService service = target.proxy(AreaService.class);

        try {
//            Area area = service.getAreaById(Integer.parseInt(flight.getPhoneNumber().substring(1, 4)));
//            flight.setState(area.getState());
        } catch (ClientErrorException e) {
            if (e.getResponse().getStatusInfo() == Response.Status.NOT_FOUND) {
                throw new InvalidAreaCodeException("The area code provided does not exist", e);
            } else {
                throw e;
            }
        }

        // Either update the Flight or add it if it can't be found.
        return crud.update(flight);
    }


}
