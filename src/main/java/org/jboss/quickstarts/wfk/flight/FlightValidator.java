package org.jboss.quickstarts.wfk.flight;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;

import org.jboss.quickstarts.wfk.contact.UniqueEmailException;
import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.customer.CustomerRepository;

public class FlightValidator {
    @Inject
    private Validator validator;

    @Inject
    private FlightRepository crud;

    /**
     * <p>Validates the given Flight object and throws validation exceptions based on the type of error. If the error is standard
     * bean validation errors then it will throw a ConstraintValidationException with the set of the constraints violated.<p/>
     *
     *
     * <p>If the error is caused because an existing Flight with the same email is registered it throws a regular validation
     * exception so that it can be interpreted separately.</p>
     *
     *
     * @param Flight The Flight object to be validated
     * @throws ConstraintViolationException If Bean Validation errors exist
     * @throws ValidationException If Flight with the same email already exists
     */
    void validateFlight(Flight flight) throws ConstraintViolationException, ValidationException {
        // Create a bean validator and check for issues.
        Set<ConstraintViolation<Flight>> violations = validator.validate(flight);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }


}
