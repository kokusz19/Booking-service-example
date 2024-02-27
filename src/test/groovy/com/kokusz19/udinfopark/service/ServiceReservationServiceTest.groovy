package com.kokusz19.udinfopark.service

import com.kokusz19.udinfopark.repository.ServiceReservationRepository
import spock.lang.Subject

class ServiceReservationServiceTest extends TestBase {

	@Subject
	def service = new ServiceReservationService(
			Mock(ServiceReservationRepository),
			Mock(ModelConverter)
	)

	def "findByServiceIdAndDate"() {
		setup:
			def serviceId = 1
		when:
			def result = service.getByServiceId(serviceId)
		then:
			1 * service.serviceReservationRepository.findByServiceId(serviceId) >> [serviceReservationDao]
			1 * service.modelConverter.convert(serviceReservationDao) >> serviceReservationDto
			0 * _
		and:
			assert result == [serviceReservationDto]
	}
}
