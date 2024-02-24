package com.kokusz19.udinfopark.service

import com.kokusz19.udinfopark.model.dao.Company
import com.kokusz19.udinfopark.model.dao.Reservation
import com.kokusz19.udinfopark.model.dao.Service
import com.kokusz19.udinfopark.model.dao.Time
import spock.lang.Specification

class TestBase extends Specification {

	def timeDtoA = new com.kokusz19.udinfopark.model.dto.Time(12, 0)
	def timeDtoB = new com.kokusz19.udinfopark.model.dto.Time(23, 59)
	def timeDaoA = new Time(12, 0)
	def timeDaoB = new Time(23, 59)

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
			300)
	def serviceDao = new Service(
			1,
			"name",
			companyDao,
			"description",
			300)

	def reservationDto = new com.kokusz19.udinfopark.model.dto.Reservation(
			1,
			1,
			[1],
			"FirstName LastName",
			"PhoneNumber",
			"EmailAddress"
	)
	def reservationDao = new Reservation(
			1,
			companyDao,
			[serviceDao],
			"FirstName LastName",
			"PhoneNumber",
			"EmailAddress"
	)
}