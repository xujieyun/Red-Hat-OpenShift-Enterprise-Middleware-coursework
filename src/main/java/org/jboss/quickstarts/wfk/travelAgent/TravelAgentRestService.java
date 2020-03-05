package org.jboss.quickstarts.wfk.travelagent;



import io.swagger.annotations.*;
import org.jboss.quickstarts.wfk.util.RestServiceException;
import org.jboss.resteasy.annotations.cache.Cache;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
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
 * <p>The full path for accessing endpoints defined herein is: api/travelAgents/*</p>
 * 
 * @author Xujie
 * @see TravelAgentService
 * @see javax.ws.rs.core.Response
 */
@Path("/travelAgents")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/travelAgents", description = "Operations about travelAgents")
@Stateless
public class TravelAgentRestService {
    @Inject
    private @Named("logger") Logger log;
    
    @Inject
    private TravelAgentService service;

    /**
     * <p>Return all the TravelAgents.  They are sorted alphabetically by name.</p>
     *
     * <p>The url may optionally include query parameters specifying a TravelAgent's name</p>
     *
     * <p>Examples: <pre>GET api/travelAgents?firstname=John</pre>, <pre>GET api/travelAgents?firstname=John&lastname=Smith</pre></p>
     *
     * @return A Response containing a list of TravelAgents
     */
    @GET
    @ApiOperation(value = "Fetch all TravelAgents", notes = "Returns a JSON array of all stored TravelAgent objects.")
    public Response retrieveAllTravelAgents(@QueryParam("firstname") String firstname, @QueryParam("lastname") String lastname) {
        //Create an empty collection to contain the intersection of TravelAgents to be returned
        List<TravelAgent> travelAgents = null;

        if(firstname == null && lastname == null) {
            travelAgents = service.findAllOrderedByName();
        } /*else if(lastname == null) {
                travelAgents = service.findAllByFirstName(firstname);
        } else if(firstname == null) {
                travelAgents = service.findAllByLastName(lastname);
        } else {
                travelAgents = service.findAllByFirstName(firstname);
                travelAgents.retainAll(service.findAllByLastName(lastname));
        }*/

        return Response.ok(travelAgents).build();
    }

    /**
     * <p>Search for and return a TravelAgent identified by email address.<p/>
     *
     * <p>Path annotation includes very simple regex to differentiate between email addresses and Ids.
     * <strong>DO NOT</strong> attempt to use this regex to validate email addresses.</p>
     *
     *
     * @param email The string parameter value provided as a TravelAgent's email
     * @return A Response containing a single TravelAgent
     */
    /*@GET
    @Cache
    @Path("/Customer")
    @ApiOperation(
            value = "Fetch a TravelAgent by Customer",
            notes = "Returns a JSON representation of the TravelAgent object with the provided Customer."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message ="TravelAgent found"),
            @ApiResponse(code = 404, message = "TravelAgent with Customer not found")
    })
    public Response retrieveTravelAgentsByCustomer(@QueryParam("firstname") String firstname, @QueryParam("lastname") String lastname){
        //Create an empty collection to contain the intersection of TravelAgents to be returned
        List<TravelAgent> travelAgents = null;

            travelAgents = service.findAllOrderedByCustomerName();
         else if(lastname == null) {
                travelAgents = service.findAllByFirstName(firstname);
        } else if(firstname == null) {
                travelAgents = service.findAllByLastName(lastname);
        } else {
                travelAgents = service.findAllByFirstName(firstname);
                travelAgents.retainAll(service.findAllByLastName(lastname));
        }

        return Response.ok(travelAgents).build();
    }*/

    /**
     * <p>Search for and return a Contact identified by id.</p>
     *
     * @param id The long parameter value provided as a Contact's id
     * @return A Response containing a single Contact
     */
    @GET
    @Cache
    @Path("/{id:[0-9]+}")
    @ApiOperation(
            value = "Fetch a TravelAgent by id",
            notes = "Returns a JSON representation of the TravelAgent object with the provided id."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message ="TravelAgent found"),
            @ApiResponse(code = 404, message = "TravelAgent with id not found")
    })
    public Response retrieveTravelAgentById(
            @ApiParam(value = "Id of TravelAgent to be fetched", allowableValues = "range[0, infinity]", required = true)
            @PathParam("id")
            long id) {

        TravelAgent travelAgent = service.findById(id);
        if (travelAgent == null) {
            // Verify that the travelAgent exists. Return 404, if not present.
            throw new RestServiceException("No TravelAgent with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }
        log.info("findById " + id + ": found TravelAgent = " + travelAgent.toString());

        return Response.ok(travelAgent).build();
    }

    /**
     * <p>Creates a new travelAgent from the values provided. Performs validation and will return a JAX-RS response with
     * either 201 (Resource created) or with a map of fields, and related errors.</p>
     *
     * @param travelAgent The TravelAgent object, constructed automatically from JSON input, to be <i>created</i> via
     * {@link TravelAgentService#create(TravelAgent)}
     * @return A Response indicating the outcome of the create operation
     */
    @SuppressWarnings("unused")
    @POST
    @ApiOperation(value = "Add a new TravelAgent to the database")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "TravelAgent created successfully."),
            @ApiResponse(code = 400, message = "Invalid TravelAgent supplied in request body"),
            @ApiResponse(code = 409, message = "TravelAgent supplied in request body conflicts with an existing TravelAgent"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })
    public Response createTravelAgent(
            @ApiParam(value = "JSON representation of TravelAgent object to be added to the database", required = true)
            TravelAgent travelAgent) {


        if (travelAgent == null) {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        Response.ResponseBuilder builder;

        try {
            // Go add the new travelAgent.
            service.create(travelAgent);

            // Create a "Resource Created" 201 Response and pass the travelAgent back in case it is needed.
            builder = Response.status(Response.Status.CREATED).entity(travelAgent);


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

        log.info("createTravelAgent completed. TravelAgent = " + travelAgent.toString());
        return builder.build();
    }

    /**
     * <p>Deletes a travelAgent using the ID provided. If the ID is not present then nothing can be deleted.</p>
     *
     * <p>Will return a JAX-RS response with either 204 NO CONTENT or with a map of fields, and related errors.</p>
     *
     * @param id The Long parameter value provided as the id of the TravelAgent to be deleted
     * @return A Response indicating the outcome of the delete operation
     */
    @DELETE
    @Path("/{id:[0-9]+}")
    @ApiOperation(value = "Delete a TravelAgent from the database")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "The travelAgent has been successfully deleted"),
            @ApiResponse(code = 400, message = "Invalid TravelAgent id supplied"),
            @ApiResponse(code = 404, message = "TravelAgent with id not found"),
            @ApiResponse(code = 500, message = "An unexpected error occurred whilst processing the request")
    })
    public Response deleteTravelAgent(
            @ApiParam(value = "Id of TravelAgent to be deleted", allowableValues = "range[0, infinity]", required = true)
            @PathParam("id")
            long id) {

        Response.ResponseBuilder builder;

        TravelAgent travelAgent = service.findById(id);
        if (travelAgent == null) {
            // Verify that the travelAgent exists. Return 404, if not present.
            throw new RestServiceException("No TravelAgent with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }

        try {
            service.delete(travelAgent);

            builder = Response.noContent();

        } catch (Exception e) {
            // Handle generic exceptions
            throw new RestServiceException(e);
        }
        log.info("deleteTravelAgent completed. TravelAgent = " + travelAgent.toString());
        return builder.build();
    }
}
