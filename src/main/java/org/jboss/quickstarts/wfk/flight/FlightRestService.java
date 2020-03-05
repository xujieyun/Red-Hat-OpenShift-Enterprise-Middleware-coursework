package org.jboss.quickstarts.wfk.flight;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.quickstarts.wfk.contact.UniqueEmailException;
import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.customer.CustomerService;
import org.jboss.quickstarts.wfk.util.RestServiceException;
import org.jboss.resteasy.annotations.cache.Cache;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * <p>This class produces a RESTful service exposing the functionality of {@link ContactService}.</p>
 *
 * <p>The Path annotation defines this as a REST Web Service using JAX-RS.</p>
 *
 * <p>By placing the Consumes and Produces annotations at the class level the methods all default to JSON.  However, they
 * can be overriden by adding the Consumes or Produces annotations to the individual methods.</p>
 *
 * <p>It is Stateless to "inform the container that this RESTful web service should also be treated as an EJB and allow
 * transaction demarcation when accessing the database." - Antonio Goncalves</p>
 *
 * <p>The full path for accessing endpoints defined herein is: api/contacts/*</p>
 * 
 * @author Xujie
 * @see ContactService
 * @see javax.ws.rs.core.Response
 */
@Path("/flights")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/flights", description = "Operations about flights")
@Stateless
public class FlightRestService {
    @Inject
    private @Named("logger") Logger log;
    
    @Inject
    private FlightService service;

    /**
     * <p>Return all the Contacts.  They are sorted alphabetically by name.</p>
     *
     * <p>The url may optionally include query parameters specifying a Contact's name</p>
     *
     * <p>Examples: <pre>GET api/contacts?firstname=John</pre>, <pre>GET api/contacts?firstname=John&lastname=Smith</pre></p>
     *
     * @return A Response containing a list of Contacts
     */
    @GET
    @ApiOperation(value = "Fetch all Flights", notes = "Returns a JSON array of all stored Contact objects.")
    public Response retrieveAllContacts(@QueryParam("flightNumber") String flightNumber) {
        //Create an empty collection to contain the intersection of flights to be returned
        List<Flight> flights ;
        if(flightNumber == null) {
            flights = service.findAllOrderedByName();
        } else {
            flights = service.findAllByFlightNumber(flightNumber);
            flights.retainAll(service.findAllByFlightNumber(flightNumber));
    }

        return Response.ok(flights).build();
    }

    /**
     * <p>Creates a new flight from the values provided. Performs validation and will return a JAX-RS response with
     * either 201 (Resource created) or with a map of fields, and related errors.</p>
     *
     * @param flight The Flight object, constructed automatically from JSON input, to be <i>created</i> via
     * {@link FlightService#create(Flight)}
     * @return A Response indicating the outcome of the create operation
     */
    @SuppressWarnings("unused")
    @POST
    @ApiOperation(value = "Add a new Flight to the database")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Flight created successfully."),
            @ApiResponse(code = 400, message = "Invalid Flight supplied in request body"),
            @ApiResponse(code = 409, message = "Flight supplied in request body conflicts with an existing Flight"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })
    public Response createFlight(
            @ApiParam(value = "JSON representation of Flight object to be added to the database", required = true)
            Flight flight) {


        if (flight == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        Response.ResponseBuilder builder;

        try {
            // Go add the new flight.
            service.create(flight);

            // Create a "Resource Created" 201 Response and pass the flight back in case it is needed.
            builder = Response.status(Response.Status.CREATED).entity(flight);


        } catch (ConstraintViolationException ce) {
            //Handle bean validation issues
            Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);

        } catch (UniqueEmailException e) {
            // Handle the unique constraint violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "That email is already used, please use a unique email");
            throw new RestServiceException("Bad Request", responseObj, Response.Status.CONFLICT, e);
        }  catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }

        log.info("createFlight completed. Flight = " + flight.toString());
        return builder.build();
    }

    /**
     * <p>Updates the flight with the ID provided in the database. Performs validation, and will return a JAX-RS response
     * with either 200 (ok), or with a map of fields, and related errors.</p>
     *
     * @param flight The flight object, constructed automatically from JSON input, to be <i>updated</i> via
     * {@link flightService#update(flight)}
     * @param id The long parameter value provided as the id of the flight to be updated
     * @return A Response indicating the outcome of the create operation
     */
    @PUT
    @Path("/{id:[0-9]+}")
    @ApiOperation(value = "Update a Flight in the database")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Flight updated successfully"),
            @ApiResponse(code = 400, message = "Invalid Flight supplied in request body"),
            @ApiResponse(code = 404, message = "Flight with id not found"),
            @ApiResponse(code = 409, message = "Flight details supplied in request body conflict with another existing Flight"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })
    public Response updateFlight(
            @ApiParam(value = "Id of Flight to be updated", allowableValues = "range[0, infinity]", required = true)
            @PathParam("id")
            long id,
            @ApiParam(value = "JSON representation of Flight object to be updated in the database", required = true)
            Flight flight) {

        if (flight == null || flight.getId() == null) {
            throw new RestServiceException("Invalid Flight supplied in request body", Response.Status.BAD_REQUEST);
        }

        if (flight.getId() != null && flight.getId() != id) {
            // The client attempted to update the read-only Id. This is not permitted.
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("id", "The flight ID in the request body must match that of the Flight being updated");
            throw new RestServiceException("Flight details supplied in request body conflict with another Flight",
                    responseObj, Response.Status.CONFLICT);
        }

        if (service.findById(flight.getId()) == null) {
            // Verify that the flight exists. Return 404, if not present.
            throw new RestServiceException("No Flight with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }

        Response.ResponseBuilder builder;

        try {
            // Apply the changes the Flight.
            service.update(flight);

            // Create an OK Response and pass the flight back in case it is needed.
            builder = Response.ok(flight);


        } catch (ConstraintViolationException ce) {
            //Handle bean validation issues
            Map<String, String> responseObj = new HashMap<>();

            for (ConstraintViolation<?> violation : ce.getConstraintViolations()) {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);
        } catch (UniqueEmailException e) {
            // Handle the unique constraint violation
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "That email is already used, please use a unique email");
            throw new RestServiceException("Flight details supplied in request body conflict with another Flight",
                    responseObj, Response.Status.CONFLICT, e);
        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }

        log.info("updateFlight completed. Flight = " + flight.toString());
        return builder.build();
    }

}
