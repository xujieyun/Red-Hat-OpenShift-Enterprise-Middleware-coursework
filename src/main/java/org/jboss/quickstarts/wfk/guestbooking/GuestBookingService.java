package org.jboss.quickstarts.wfk.guestbooking;

import org.jboss.quickstarts.wfk.area.Area;
import org.jboss.quickstarts.wfk.area.AreaService;
import org.jboss.quickstarts.wfk.area.InvalidAreaCodeException;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

/**
 * <p>This Service assumes the Control responsibility in the ECB pattern.</p>
 *
 * <p>The validation is done here so that it may be used by other Boundary Resources. Other Business Logic would go here
 * as well.</p>
 *
 * <p>There are no access modifiers on the methods, making them 'package' scope.  They should only be accessed by a
 * Boundary / Web Service class with public methods.</p>
 *
 *
 * @author Xujie
 * @see BookingValidator
 * @see BookingRepository
 */
//The @Dependent is the default scope is listed here so that you know what scope is being used.
@Dependent
public class GuestBookingService {

    @Inject
    private @Named("logger") Logger log;

    @Inject
    private GuestBookingValidator validator;

    @Inject
    private GuestBookingRepository crud;

    private ResteasyClient client;

    /**
     * <p>Create a new client which will be used for our outgoing REST client communication</p>
     */
    public GuestBookingService() {
        // Create client service instance to make REST requests to upstream service
        client = new ResteasyClientBuilder().build();
    }

   

    /**
     * <p>Updates an existing Booking object in the application database with the provided GuestBooking object.<p/>
     *
     * <p>Validates the data in the provided GuestBooking object using a GuestBookingValidator object.<p/>
     *
     * @param GuestBooking The GuestBooking object to be passed as an update to the application database
     * @return The GuestBooking object that has been successfully updated in the application database
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    GuestBooking update(GuestBooking guestBooking) throws ConstraintViolationException, ValidationException, Exception {
        log.info("GuestBookingService.update() - Updating " + guestBooking.getCustomer().getFirstName() + " " + guestBooking.getCustomer().getLastName());
        
        // Check to make sure the data fits with the parameters in the GuestBooking model and passes validation.
        validator.validateGuestBooking(guestBooking);

        // Set client target location and define the proxy API class
        ResteasyWebTarget target = client.target("http://csc8104-states.b9ad.pro-us-east-1.openshiftapps.com");
        AreaService service = target.proxy(AreaService.class);

        /*try {
            Area area = service.getAreaById(Integer.parseInt(guestBooking.getPhoneNumber().substring(1, 4)));
            guestBooking.setState(area.getState());
        } catch (ClientErrorException e) {
            if (e.getResponse().getStatusInfo() == Response.Status.NOT_FOUND) {
                throw new InvalidAreaCodeException("The area code provided does not exist", e);
            } else {
                throw e;
            }
        }*/

        // Either update the guestBooking or add it if it can't be found.
        return crud.update(guestBooking);
    }

}
