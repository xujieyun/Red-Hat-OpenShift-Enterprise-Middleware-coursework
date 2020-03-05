package org.jboss.quickstarts.wfk.travelagent;

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.jboss.quickstarts.wfk.area.AreaService;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
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
 * @see TravelAgentValidator
 * @see TravelRepository
 */
//The @Dependent is the default scope is listed here so that you know what scope is being used.
@Dependent
public class TravelAgentService {

    @Inject
    private @Named("logger") Logger log;

    @Inject
    private TravelAgentValidator validator;

    @Inject
    private TravelRepository crud;

    private ResteasyClient client;

    /**
     * <p>Create a new client which will be used for our outgoing REST client communication</p>
     */
    public TravelAgentService() {
        // Create client service instance to make REST requests to upstream service
        client = new ResteasyClientBuilder().build();
    }

    /**
     * <p>Returns a List of all persisted {@link TravelAgent} objects, sorted alphabetically by last name.<p/>
     *
     * @return List of TravelAgent objects
     */
    List<TravelAgent> findAllOrderedByName() {
        return crud.findAllOrderedByName();
    }
    List<TravelAgent> findAllOrderedByCustomerName() {
        return crud.findAllOrderedByCustomerName();
    }

    /**
     * <p>Returns a single TravelAgent object, specified by a Long id.<p/>
     *
     * @param id The id field of the TravelAgent to be returned
     * @return The TravelAgent with the specified id
     */
    TravelAgent findById(Long id) {
        return crud.findById(id);
    }

    /**
     * <p>Returns a single TravelAgent object, specified by a String email.</p>
     *
     * <p>If there is more than one TravelAgent with the specified email, only the first encountered will be returned.<p/>
     *
     * @param email The email field of the TravelAgent to be returned
     * @return The first TravelAgent with the specified email
     */
    /*TravelAgent findByEmail(String email) {
        return crud.findByEmail(email);
    }*/

    /**
     * <p>Returns a single TravelAgent object, specified by a String firstName.<p/>
     *
     * @param firstName The firstName field of the TravelAgent to be returned
     * @return The first TravelAgent with the specified firstName
     */
    List<TravelAgent> findAllById(Long id) {
        return crud.findAllById(id);
    }

    /**
     * <p>Returns a single TravelAgent object, specified by a String lastName.<p/>
     *
     * @param lastName The lastName field of the TravelAgents to be returned
     * @return The TravelAgents with the specified lastName
     */
    /*List<TravelAgent> findAllByLastName(String lastName) {
        return crud.findAllByLastName(lastName);
    }*/

    /**
     * <p>Writes the provided TravelAgent object to the application database.<p/>
     *
     * <p>Validates the data in the provided TravelAgent object using a {@link TravelAgentValidator} object.<p/>
     *
     * @param travelagent The TravelAgent object to be written to the database using a {@link TravelRepository} object
     * @return The TravelAgent object that has been successfully written to the application database
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    TravelAgent create(TravelAgent travelagent) throws ConstraintViolationException, ValidationException, Exception {
        log.info("TravelAgentService.create() - Creating " + travelagent.getId());
        
        // Check to make sure the data fits with the parameters in the TravelAgent model and passes validation.
        validator.validateTravelAgent(travelagent);

        //Create client service instance to make REST requests to upstream service
        ResteasyWebTarget target = client.target("http://csc8104-states.b9ad.pro-us-east-1.openshiftapps.com");
        AreaService service = target.proxy(AreaService.class);

        /*try {
            Area area = service.getAreaById(Integer.parseInt(travelagent.getPhoneNumber().substring(1, 4)));
            travelagent.setState(area.getState());
        } catch (ClientErrorException e) {
            if (e.getResponse().getStatusInfo() == Response.Status.NOT_FOUND) {
                throw new InvalidAreaCodeException("The area code provided does not exist", e);
            } else {
                throw e;
            }
        }*/

        // Write the travelagent to the database.
        return crud.create(travelagent);
    }

    /**
     * <p>Updates an existing TravelAgent object in the application database with the provided TravelAgent object.<p/>
     *
     * <p>Validates the data in the provided TravelAgent object using a TravelAgentValidator object.<p/>
     *
     * @param travelagent The TravelAgent object to be passed as an update to the application database
     * @return The TravelAgent object that has been successfully updated in the application database
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    /*TravelAgent update(TravelAgent travelagent) throws ConstraintViolationException, ValidationException, Exception {
        log.info("TravelAgentService.update() - Updating " + travelagent.getFirstName() + " " + travelagent.getLastName());
        
        // Check to make sure the data fits with the parameters in the TravelAgent model and passes validation.
        validator.validateTravelAgent(travelagent);

        // Set client target location and define the proxy API class
        ResteasyWebTarget target = client.target("http://csc8104-states.b9ad.pro-us-east-1.openshiftapps.com");
        AreaService service = target.proxy(AreaService.class);

        try {
            Area area = service.getAreaById(Integer.parseInt(travelagent.getPhoneNumber().substring(1, 4)));
            travelagent.setState(area.getState());
        } catch (ClientErrorException e) {
            if (e.getResponse().getStatusInfo() == Response.Status.NOT_FOUND) {
                throw new InvalidAreaCodeException("The area code provided does not exist", e);
            } else {
                throw e;
            }
        }

        // Either update the travelagent or add it if it can't be found.
        return crud.update(travelagent);
    }*/

    /**
     * <p>Deletes the provided TravelAgent object from the application database if found there.<p/>
     *
     * @param travelagent The TravelAgent object to be removed from the application database
     * @return The TravelAgent object that has been successfully removed from the application database; or null
     * @throws Exception
     */
    TravelAgent delete(TravelAgent travelagent) throws Exception {
        log.info("delete() - Deleting " + travelagent.toString());

        TravelAgent deletedTravelAgent = null;

        if (travelagent.getId() != null) {
            deletedTravelAgent = crud.delete(travelagent);
        } else {
            log.info("delete() - No ID was found so can't Delete.");
        }

        return deletedTravelAgent;
    }
}
