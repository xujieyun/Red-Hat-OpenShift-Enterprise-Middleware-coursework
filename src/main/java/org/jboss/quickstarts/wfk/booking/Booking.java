package org.jboss.quickstarts.wfk.booking;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.quickstarts.wfk.customer.Customer;
import org.jboss.quickstarts.wfk.flight.Flight;

/**
 * <p>This is a the Domain object. The Booking class represents how booking resources are represented in the application
 * database.</p>
 *
 * <p>The class also specifies how a bookings are retrieved from the database (with @NamedQueries), and acceptable values
 * for Booking fields (with @NotNull, @Pattern etc...)<p/>
 *
 * @author Xujie
 */
/*
 * The @NamedQueries included here are for searching against the table that reflects this object.  This is the most efficient
 * form of query in JPA though is it more error prone due to the syntax being in a String.  This makes it harder to debug.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = Booking.FIND_ALL, query = "SELECT c FROM Booking c ORDER BY c.id"),
        @NamedQuery(name = Booking.FIND_BY_CUSTOMER, query = "SELECT c FROM Booking c ORDER BY c.customer"),
})
@XmlRootElement
@Table(name = "booking"/*, uniqueConstraints = @UniqueConstraint(columnNames = "email")*/)
public class Booking implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "Booking.id";
    public static final String FIND_BY_CUSTOMER = "Booking.customer";

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    
//    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )// owning side
    @JoinColumn(name="customer")
//  @Column(name = "first_name")
    @ManyToOne
    private Customer customer;
    
//    /*@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )// owning side
//    @JoinColumn(name="flight")*/
//  @Column(name = "flight")
    @ManyToOne
    @JoinColumn(name="flight")
    private Flight flight;


    @NotNull
    @Past(message = "Booking dates can not be in the past. Please choose one from the future")
    @Column(name = "booking_date")
    @Temporal(TemporalType.DATE)
    private Date bookingDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
 
    public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public Date getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bookingDate == null) ? 0 : bookingDate.hashCode());
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((flight == null) ? 0 : flight.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Booking other = (Booking) obj;
		if (bookingDate == null) {
			if (other.bookingDate != null)
				return false;
		} else if (!bookingDate.equals(other.bookingDate))
			return false;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (flight == null) {
			if (other.flight != null)
				return false;
		} else if (!flight.equals(other.flight))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Booking [id=" + id + ", customer=" + customer + ", flight=" + flight + ", bookingDate=" + bookingDate
				+ "]";
	}
	

}
