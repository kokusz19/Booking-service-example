package com.kokusz19.udinfopark.service

import com.kokusz19.udinfopark.model.dto.ReservationSearchParams
import com.kokusz19.udinfopark.repository.ReservationRepository
import spock.lang.Subject

import java.time.LocalDate

class ReservationServiceTest extends TestBase {

	@Subject
	def service = new ReservationService(
			Mock(ReservationRepository),
			Mock(ServiceService),
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
		when: "can create"
			def result = service.create(reservationDto)
		then:
			1 * service.serviceService.getOne(1) >> serviceDto
			1 * service.serviceReservationService.findByServiceIdAndDate(serviceReservationDto, serviceDto) >> Optional.empty()
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

		when: "not present - can create"
			service.create(reservationDto)
		then:
			1 * service.serviceService.getOne(1) >> serviceDto
			1 * service.serviceReservationService.findByServiceIdAndDate(serviceReservationDto, serviceDto) >> Optional.of(serviceReservationDto)
			0 * _
		and:
			def ex = thrown(IllegalStateException)
			assert ex.message == "Could not make the reservation, because one or more timewindows have already been booked or are overlapping with others."
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
}
