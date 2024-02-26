package com.kokusz19.udinfopark.service

import com.kokusz19.udinfopark.model.dto.Company
import com.kokusz19.udinfopark.model.dto.Reservation
import com.kokusz19.udinfopark.model.dto.ReservationSearchParams
import com.kokusz19.udinfopark.model.dto.ServiceReservation
import com.kokusz19.udinfopark.model.dto.Time
import com.kokusz19.udinfopark.repository.ReservationRepository
import spock.lang.Subject

import java.time.LocalDate

class ReservationServiceTest extends TestBase {

	@Subject
	def service = new ReservationService(
			Mock(ReservationRepository),
			Mock(ServiceService),
			Mock(CompanyService),
			Mock(ModelConverter),
			Mock(ServiceReservationService)
	)

	def "getAll"() {
		when:
			def result = service.getAll()
		then:
			1 * service.reservationRepository.findAll() >> [reservationDao]
			1 * service.modelConverter.convert(reservationDao) >> reservationDto
			0 * _
		and:
			assert result == [reservationDto]
	}

	def "getOne"() {
		when: "found"
			def result = service.getOne(1)
		then:
			1 * service.reservationRepository.findById(1) >> Optional.of(reservationDao)
			1 * service.modelConverter.convert(reservationDao) >> reservationDto
			0 * _
		and:
			assert result == reservationDto

		when: "not found"
			result = service.getOne(999)
		then:
			1 * service.reservationRepository.findById(999) >> Optional.empty()
			0 * _
		and:
			assert result == null
	}

	def "create"() {
		setup:
			def newReservationId = 50

		when: "One service reservation"
			def result = service.create(reservationDto)
		then:
			1 * service.companyService.getOne(1) >> companyDto
			1 * service.serviceService.getByCompanyId(1) >> [serviceDto]
			1 * service.serviceService.getOne(1) >> serviceDto
			1 * service.serviceReservationService.findByServiceIdAndDate(serviceDto, reservationDto.serviceReservations[0]) >> Optional.empty()
			1 * service.modelConverter.convert(reservationDto.serviceReservations[0]) >> serviceReservationDao
			1 * service.serviceReservationService.save(serviceReservationDao) >> serviceReservationDao
			1 * service.modelConverter.convert(serviceReservationDao) >> {
				reservationDto.serviceReservations = [serviceReservationDto]
				return serviceReservationDto
			}
			1 * service.modelConverter.convert(reservationDto) >> reservationDao
			1 * service.reservationRepository.save(reservationDao) >> {
				reservationDao.setReservationId(newReservationId)
				return reservationDao
			}
			0 * _
		and:
			assert result == newReservationId
	}

	def "update"() {
		when:
			def result = service.update(reservationDto.getReservationId(), reservationDto)
		then:
			1 * service.modelConverter.convert(reservationDto) >> reservationDao
			1 * service.reservationRepository.save(reservationDao) >> reservationDao
			1 * service.modelConverter.convert(reservationDao) >> reservationDto
			0 * _
		and:
			assert result == reservationDto
	}

	def "delete"() {
		when: "first run - was in the db before delete - could delete"
			def result = service.delete(reservationDto.getReservationId())
		then:
			1 * service.reservationRepository.findById(reservationDto.getReservationId()) >> Optional.of(reservationDao)
			1 * service.reservationRepository.delete(reservationDao)
			0 * _
		and:
			assert result

		when: "second run - was not in the db before delete - could not delete"
			result = service.delete(reservationDto.getReservationId())
		then:
			1 * service.reservationRepository.findById(reservationDto.getReservationId()) >> Optional.empty()
			0 * _
		and:
			assert !result
	}

	def "search"() {
		setup:
			def validSearchParams1 = new ReservationSearchParams(null, new Date(), new Date())
			def validSearchParams2 = new ReservationSearchParams(LocalDate.of(2024, 02, 24), null, null)
			def noSearchParams1 = null
			def noSearchParams2 = new ReservationSearchParams()
			def invalidSearchParams = new ReservationSearchParams(LocalDate.of(1,1,1), new Date(), new Date())

		// TODOs
		when: "valid searchParams 1"
			def result = service.search(validSearchParams1)
		then:
			0 * _
		and:
			println("TODO")

		when: "valid searchParams 2"
			result = service.search(validSearchParams2)
		then:
			0 * _
		and:
			println("TODO")

		when: "no searchParams 1"
			result = service.search(noSearchParams1)
		then:
			1 * service.reservationRepository.findAll() >> [reservationDao]
			1 * service.modelConverter.convert(reservationDao) >> reservationDto
			0 * _
		and:
			assert result == [reservationDto]

		when: "no searchParams 2"
			result = service.search(noSearchParams2)
		then:
			1 * service.reservationRepository.findAll() >> [reservationDao]
			1 * service.modelConverter.convert(reservationDao) >> reservationDto
			0 * _
		and:
			assert result == [reservationDto]
	}

	def "validateServiceReservations"() {
		setup:
			def multipleServiceReservation = new Reservation(companyId: 1, serviceReservations: [new ServiceReservation(1, new Date(124, 2, 24, 12, 0)), new ServiceReservation(1, new Date(124, 2, 24, 14, 0))])
			def multipleServiceReservationTimeDifferenceNotEnough = new Reservation(companyId: 1, serviceReservations: [new ServiceReservation(1, new Date(124, 2, 24, 12, 0)), new ServiceReservation(1, new Date(124, 2, 24, 12, 0))])
			def singleServiceReservationOverflowToNextDay = new Reservation(companyId: 1, serviceReservations: [new ServiceReservation(1, new Date(124, 2, 24, 23, 30))])
			def singleServiceReservationBeforeOpen = new Reservation(companyId: 1, serviceReservations: [new ServiceReservation(1, new Date(124, 2, 24, 6, 30))])
			def singleServiceReservationAfterClose = new Reservation(companyId: 1, serviceReservations: [new ServiceReservation(1, new Date(124, 2, 24, 22, 30))])

		when: "One service reservation - happy case"
			service.validateServiceReservations(reservationDto)
		then:
			1 * service.companyService.getOne(1) >> companyDto
			1 * service.serviceService.getByCompanyId(1) >> [serviceDto]
			1 * service.serviceService.getOne(1) >> serviceDto
			1 * service.serviceReservationService.findByServiceIdAndDate(serviceDto, reservationDto.serviceReservations[0]) >> Optional.empty()
			0 * _

		when: "Multiple service reservation - happy case"
			service.validateServiceReservations(multipleServiceReservation)
		then:
			1 * service.companyService.getOne(1) >> companyDto
			1 * service.serviceService.getByCompanyId(1) >> [serviceDto]
			2 * service.serviceService.getOne(1) >> serviceDto
			1 * service.serviceReservationService.findByServiceIdAndDate(serviceDto, multipleServiceReservation.serviceReservations[0]) >> Optional.empty()
			1 * service.serviceReservationService.findByServiceIdAndDate(serviceDto, multipleServiceReservation.serviceReservations[1]) >> Optional.empty()
			0 * _

		when: "Company not found"
			service.validateServiceReservations(reservationDto)
		then:
			1 * service.companyService.getOne(1)
			0 * _
		and:
			def ex = thrown(NullPointerException)
			assert ex.message == "Company not found [id=1]"

		when: "No services under company"
			service.validateServiceReservations(reservationDto)
		then:
			1 * service.companyService.getOne(1) >> companyDto
			1 * service.serviceService.getByCompanyId(1) >> []
			0 * _
		and:
			ex = thrown(IllegalArgumentException)
			assert ex.message == "There are no services under this company!"

		when: "Overlapping time windows"
			service.validateServiceReservations(reservationDto)
		then:
			1 * service.companyService.getOne(1) >> companyDto
			1 * service.serviceService.getByCompanyId(1) >> [serviceDto]
			1 * service.serviceService.getOne(1) >> serviceDto
			1 * service.serviceReservationService.findByServiceIdAndDate(serviceDto, serviceReservationDto) >> Optional.of(serviceReservationDto)
			0 * _
		and:
			ex = thrown(IllegalStateException)
			assert ex.message == "Could not make the reservation, because one or more timewindows have already been booked or are overlapping with others."

		when: "Multiple service reservation - Time difference less, then necessary - single service under company"
			service.validateServiceReservations(multipleServiceReservationTimeDifferenceNotEnough)
		then:
			1 * service.companyService.getOne(1) >> companyDto
			1 * service.serviceService.getByCompanyId(1) >> [serviceDto]
			2 * service.serviceService.getOne(1) >> serviceDto
			1 * service.serviceReservationService.findByServiceIdAndDate(serviceDto, multipleServiceReservationTimeDifferenceNotEnough.serviceReservations[0]) >> Optional.empty()
			1 * service.serviceReservationService.findByServiceIdAndDate(serviceDto, multipleServiceReservationTimeDifferenceNotEnough.serviceReservations[1]) >> Optional.empty()
			0 * _
		and:
			ex = thrown(IllegalArgumentException)
			ex.message.contains("There should be at least 0 minutes of difference between service reservations")

		when: "Multiple service reservation - Time difference less, then necessary - multiple services under company"
			service.validateServiceReservations(multipleServiceReservationTimeDifferenceNotEnough)
		then:
			1 * service.companyService.getOne(1) >> companyDto
			1 * service.serviceService.getByCompanyId(1) >> [serviceDto, serviceDto]
			2 * service.serviceService.getOne(1) >> serviceDto
			1 * service.serviceReservationService.findByServiceIdAndDate(serviceDto, multipleServiceReservationTimeDifferenceNotEnough.serviceReservations[0]) >> Optional.empty()
			1 * service.serviceReservationService.findByServiceIdAndDate(serviceDto, multipleServiceReservationTimeDifferenceNotEnough.serviceReservations[1]) >> Optional.empty()
			0 * _
		and:
			ex = thrown(IllegalArgumentException)
			ex.message.contains("There should be at least 5 minutes of difference between service reservations")

		when: "One service reservation - Overflow to next day"
			service.validateServiceReservations(singleServiceReservationOverflowToNextDay)
		then:
			1 * service.companyService.getOne(1) >> new Company(closeAt: new Time(23, 59))
			1 * service.serviceService.getByCompanyId(1) >> [serviceDto]
			1 * service.serviceService.getOne(1) >> serviceDto
			1 * service.serviceReservationService.findByServiceIdAndDate(serviceDto, singleServiceReservationOverflowToNextDay.serviceReservations[0]) >> Optional.empty()
			0 * _
		and:
			ex = thrown(IllegalArgumentException)
			ex.message.contains("Reservation can't overflow to next day!")

		when: "One service reservation - Before company opens"
			service.validateServiceReservations(singleServiceReservationBeforeOpen)
		then:
			1 * service.companyService.getOne(1) >> companyDto
			1 * service.serviceService.getByCompanyId(1) >> [serviceDto]
			1 * service.serviceService.getOne(1) >> serviceDto
			1 * service.serviceReservationService.findByServiceIdAndDate(serviceDto, singleServiceReservationBeforeOpen.serviceReservations[0]) >> Optional.empty()
			0 * _
		and:
			ex = thrown(IllegalArgumentException)
			ex.message.contains("Reservation can't underflow the open time!")

		when: "One service reservation - After company closes"
			service.validateServiceReservations(singleServiceReservationAfterClose)
		then:
			1 * service.companyService.getOne(1) >> companyDto
			1 * service.serviceService.getByCompanyId(1) >> [serviceDto]
			1 * service.serviceService.getOne(1) >> serviceDto
			1 * service.serviceReservationService.findByServiceIdAndDate(serviceDto, singleServiceReservationAfterClose.serviceReservations[0]) >> Optional.empty()
			0 * _
		and:
			ex = thrown(IllegalArgumentException)
			ex.message.contains("Reservation can't overflow the close time!")
	}
}
