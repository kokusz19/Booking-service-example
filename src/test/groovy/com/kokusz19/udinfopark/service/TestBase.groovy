package com.kokusz19.udinfopark.service

import com.kokusz19.udinfopark.model.Company
import com.kokusz19.udinfopark.model.Reservation
import com.kokusz19.udinfopark.model.Service
import com.kokusz19.udinfopark.model.Time
import spock.lang.Specification


class TestBase extends Specification {
	def company = new Company(
			1,
			"name",
			"address",
			new Time(12, 0),
			new Time(23, 59))
	def tmpService = new Service(
			1,
			"name",
			company,
			"description",
			300)
	def reservation = new Reservation(
			1,
			company,
			[tmpService],
			"FirstName LastName",
			"PhoneNumber",
			"EmailAddress"
	)
}