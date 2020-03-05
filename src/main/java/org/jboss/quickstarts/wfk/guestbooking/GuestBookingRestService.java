package org.jboss.quickstarts.wfk.guestbooking;

import io.swagger.annotations.*;

import org.jboss.quickstarts.wfk.booking.Booking;
import org.jboss.quickstarts.wfk.booking.BookingService;
import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.customer.CustomerService;
import org.jboss.quickstarts.wfk.util.RestServiceException;
//import org.jboss.quickstarts.wfk.util.RestServiceException;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.transaction.UserTransaction;
//import javax.transaction.UserTransaction;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * <p>This class produces a RESTful service exposing the functionality of {@link BookingService}.</p>
 *
 * <p>The Path annotation defines this as a REST Web Service using JAX-RS.</p>
 *
 * <p>By placing the Consumes and Produces annotations at the class level the methods all default to JSON.  However, they
 * can be overriden by adding the Consumes or Produces annotations to the individual methods.</p>
 *
 * <p>It is Stateless to "inform the container that this RESTful web service should also be treated as an EJB and allow
 * transaction demarcation when accessing the database." - Antonio Goncalves</p>
 *
 * <p>The full path for accessing endpoints defined herein is: api/guestBooking/*</p>
 * 
 * @author Xujie
 * @see guestBookingervice
 * @see javax.ws.rs.core.Response
 */
@Path("/guestBooking")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/guestBooking", description = "Operations about guestBooking")
@Stateless
@TransactionManagement(value = javax.ejb.TransactionManagementType.BEAN)
public class GuestBookingRestService {
    @Inject
    private @Named("logger") Logger log;
    
    /*@Inject
    private GuestBookingService service;*/
    
    @Inject
    private CustomerService customerService;
    
    @Inject
    private BookingService bookingService;
    
    @Inject
    private  UserTransaction userTransaction;
    /**
     * <p>Creates a new GuestBooking from the values provided. Performs validation and will return a JAX-RS response with
     * either 201 (Resource created) or with a map of fields, and related errors.</p>
     *
     * @param guestBooking The GuestBooking object, constructed automatically from JSON input, to be <i>created</i> via
     * {@link GuestBookingService#create(GuestBooking)}
     * @return A Response indicating the outcome of the create operation
     */
    @SuppressWarnings("unused")
    @POST
    @ApiOperation(value = "Add a new GuestBooking to the database")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "GuestBooking created successfully."),
            @ApiResponse(code = 400, message = "Invalid GuestBooking supplied in request body"),
            @ApiResponse(code = 409, message = "GuestBooking supplied in request body conflicts with an existing GuestBooking"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })
    public Response createGuestBooking(
            @ApiParam(value = "JSON representation of GuestBooking object to be added to the database", required = true)
            GuestBooking guestBooking) {


        if (guestBooking == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }
        Customer customer = guestBooking.getCustomer();
        Booking booking = guestBooking.getBooking();
        Response.ResponseBuilder builder;

        try {
//        	begin transaction
        	    userTransaction.begin();
//        	    update table-a
        	    customer = customerService.create(customer);
        	    booking.setCustomer(customer);
            	booking = bookingService.create(booking);
            	userTransaction.commit();
        	    builder = Response.status(Response.Status.CREATED).entity(booking);


        } catch (ConstraintViolationException ce) {
            //Handle bean validation issues
            Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);

        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }

        log.info("createGuestBooking completed. GuestBooking = " + guestBooking.toString());
        return builder.build();
    }
}