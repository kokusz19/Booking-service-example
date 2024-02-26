package com.kokusz19.udinfopark.service

import com.kokusz19.udinfopark.model.dao.*
import spock.lang.Specification

class TestBase extends Specification {

	def timeDtoA = new com.kokusz19.udinfopark.model.dto.Time(12, 0)
	def timeDtoB = new com.kokusz19.udinfopark.model.dto.Time(18, 59)
	def timeDaoA = new Time(12, 0)
	def timeDaoB = new Time(18, 59)

	def companyDto = new com.kokusz19.udinfopark.model.dto.Company(
			1,
			"name",
			"address",
			timeDtoA,
			timeDtoB)
	def companyDao = new Company(
			1,
			"name",
			"address",
			timeDaoA,
			timeDaoB)

	def serviceDto = new com.kokusz19.udinfopark.model.dto.Service(
			1,
			"name",
			1,
			"description",
			30)
	def serviceDao = new Service(
			1,
			"name",
			companyDao,
			"description",
			30)

	def serviceReservationDto = new com.kokusz19.udinfopark.model.dto.ServiceReservation(
			1,
			1,
			new Date(124, 2, 24, 12, 0)
	)
	def serviceReservationDao = new ServiceReservation(
			1,
			serviceDao,
			new Date(124, 2, 24, 12, 0)
	)

	def reservationDto = new com.kokusz19.udinfopark.model.dto.Reservation(
			1,
			1,
			[serviceReservationDto],
			"FirstName LastName",
			"PhoneNumber",
			"EmailAddress"
	)
	def reservationDao = new Reservation(
			1,
			companyDao,
			[serviceReservationDao],
			"FirstName LastName",
			"PhoneNumber",
			"EmailAddress"
	)
}