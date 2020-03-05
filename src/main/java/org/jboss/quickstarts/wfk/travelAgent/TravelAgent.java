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

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.quickstarts.wfk.customer.Customer;

import java.io.Serializable;
import java.util.Date;


/**
 * <p>This is a the Domain object. The Contact class represents how contact resources are represented in the application
 * database.</p>
 *
 * <p>The class also specifies how a contacts are retrieved from the database (with @NamedQueries), and acceptable values
 * for Contact fields (with @NotNull, @Pattern etc...)<p/>
 *
 * @author Xujie
 */
/*
 * The @NamedQueries included here are for searching against the table that reflects this object.  This is the most efficient
 * form of query in JPA though is it more error prone due to the syntax being in a String.  This makes it harder to debug.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = TravelAgent.FIND_ALL, query = "SELECT c FROM TravelAgent c ORDER BY c.id ASC"),
        @NamedQuery(name = TravelAgent.FIND_CUSTOMER_ALL, query = "SELECT c.customerId FROM TravelAgent c ORDER BY c.id"),
})
@XmlRootElement
@Table(name = "travelagent")
public class TravelAgent implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "TravelAgent.findAll";
    public static final String FIND_CUSTOMER_ALL = "TravelAgent.find_Customer_ALL";
//    public static final String FIND_BY_EMAIL = "TravelAgent.findByEmail";

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;


	public static String getFindCustomerAll() {
		return FIND_CUSTOMER_ALL;
	}
	@Column(name = "customerId")
	private int customerId;
	
	private int hotelId;
	@Column(name = "flightId")
	private int flightId;
	@Column(name = "taxiId")
	private int taxiId;
	/*private long customer;
    private long hotel;
    private long flight;
    private long taxi ;*/
    @NotNull
    @Past(message = "Bookingdates can not be in the before. Please choose one from the past")
    @Column(name = "Booking_date")
    @Temporal(TemporalType.DATE)
    private Date BookingDate;

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getCustomerId() {
		return customerId;
	}
	

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public int getHotelId() {
		return hotelId;
	}
	public void setHotelId(int hotelId) {
		this.hotelId = hotelId;
	}
	public int getFlightId() {
		return flightId;
	}
	public void setFlightId(int flightId) {
		this.flightId = flightId;
	}
	public int getTaxiId() {
		return taxiId;
	}
	public void setTaxiId(int taxiId) {
		this.taxiId = taxiId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public static String getFindAll() {
		return FIND_ALL;
	}
	public Date getBookingDate() {
		return BookingDate;
	}
	public void setBookingDate(Date bookingDate) {
		BookingDate = bookingDate;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((BookingDate == null) ? 0 : BookingDate.hashCode());
		result = prime * result + (int) (customerId ^ (customerId >>> 32));
		result = prime * result + flightId;
		result = prime * result + hotelId;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + taxiId;
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
		TravelAgent other = (TravelAgent) obj;
		if (BookingDate == null) {
			if (other.BookingDate != null)
				return false;
		} else if (!BookingDate.equals(other.BookingDate))
			return false;
		if (customerId != other.customerId)
			return false;
		if (flightId != other.flightId)
			return false;
		if (hotelId != other.hotelId)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (taxiId != other.taxiId)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "TravelAgent [id=" + id + ", customerId=" + customerId  + ", hotelId="
				+ hotelId + ", flightId=" + flightId + ", taxiId=" + taxiId + ", BookingDate=" + BookingDate + "]";
	}


	

   
}
