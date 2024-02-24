package com.kokusz19.udinfopark.service

import com.kokusz19.udinfopark.model.ReservationSearchParams
import com.kokusz19.udinfopark.repository.ReservationRepository
import spock.lang.Subject

import java.time.LocalDate

class ReservationServiceTest extends TestBase {

	@Subject
	def service = new ReservationService(Mock(ReservationRepository))

	def "getAll"() {
		when:
			def result = service.getAll()
		then:
			1 * service.reservationRepository.findAll() >> [reservation]
			0 * _
		and:
			assert result == [reservation]
	}

	def "getOne"() {
		when: "found"
			def result = service.getOne(1)
		then:
			1 * service.reservationRepository.findById(1) >> Optional.of(reservation)
			0 * _
		and:
			assert result == reservation

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
		when: "not present - can create"
			def result = service.create(reservation)
		then:
			1 * service.reservationRepository.findByreservorNameAndServices(reservation.getReservorName(), reservation.getServices()) >> Optional.empty()
			1 * service.reservationRepository.save(reservation) >> {
				reservation.setReservationId(newReservationId)
				return reservation
			}
			0 * _
		and:
			assert result == newReservationId

		when: "present - can't create reservation with the same name"
			service.create(reservation)
		then:
			1 * service.reservationRepository.findByreservorNameAndServices(reservation.getReservorName(), reservation.getServices()) >> Optional.of(reservation)
			0 * _
		and:
			def ex = thrown(RuntimeException)
			assert ex.message == "Reservation already exists!"
	}

	def "update"() {
		when:
			def result = service.update(reservation.getReservationId(), reservation)
		then:
			1 * service.reservationRepository.save(reservation) >> reservation
			0 * _
		and:
			assert result == reservation
	}

	def "delete"() {
		when: "first run - was in the db before delete - could delete"
			def result = service.delete(reservation.getReservationId())
		then:
			1 * service.reservationRepository.findById(reservation.getReservationId()) >> Optional.of(reservation)
			1 * service.reservationRepository.delete(reservation)
			0 * _
		and:
			assert result

		when: "second run - was not in the db before delete - could not delete"
			result = service.delete(reservation.getReservationId())
		then:
			1 * service.reservationRepository.findById(reservation.getReservationId()) >> Optional.empty()
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
			1 * service.reservationRepository.findAll() >> [reservation]
			0 * _
		and:
			assert result == [reservation]

		when: "no searchParams 2"
			result = service.search(noSearchParams1)
		then:
			1 * service.reservationRepository.findAll() >> [reservation]
			0 * _
		and:
			assert result == [reservation]

		when: "invalid params"
			service.search(invalidSearchParams)
		then:
			0 * _
		and:
			def ex = thrown(RuntimeException)
			assert ex.message == "You can only pass in either the OnDate or the FromDate and ToDate!"
	}
}
