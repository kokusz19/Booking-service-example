package com.kokusz19.udinfopark.service


import spock.lang.Subject

class ServiceServiceTest extends TestBase {

	@Subject
	def service = new ServiceService(Mock(ServiceRepository))

	def "getAll"() {
		when:
			def result = service.getAll()
		then:
			1 * service.serviceRepository.findAll() >> [tmpService]
			0 * _
		and:
			assert result == [tmpService]
	}

	def "getOne"() {
		when: "found"
			def result = service.getOne(1)
		then:
			1 * service.serviceRepository.findById(1) >> Optional.of(tmpService)
			0 * _
		and:
			assert result == tmpService

		when: "not found"
			result = service.getOne(999)
		then:
			1 * service.serviceRepository.findById(999) >> Optional.empty()
			0 * _
		and:
			assert result == null
	}

	def "create"() {
		setup:
			def newServiceId = 50
		when: "not present - can create"
			def result = service.create(tmpService)
		then:
			1 * service.serviceRepository.findByName(tmpService.getName()) >> Optional.empty()
			1 * service.serviceRepository.save(tmpService) >> {
				tmpService.setServiceId(newServiceId)
				return tmpService
			}
			0 * _
		and:
			assert result == newServiceId

		when: "present - can't create service with the same name"
			service.create(tmpService)
		then:
			1 * service.serviceRepository.findByName(tmpService.getName()) >> Optional.of(tmpService)
			0 * _
		and:
			def ex = thrown(RuntimeException)
			assert ex.message == "Service already exists!"
	}

	def "update"() {
		when:
			def result = service.update(tmpService.getServiceId(), tmpService)
		then:
			1 * service.serviceRepository.save(tmpService) >> tmpService
			0 * _
		and:
			assert result == tmpService
	}

	def "delete"() {
		when: "first run - was in the db before delete - could delete"
			def result = service.delete(tmpService.getServiceId())
		then:
			1 * service.serviceRepository.findById(tmpService.getServiceId()) >> Optional.of(tmpService)
			1 * service.serviceRepository.delete(tmpService)
			0 * _
		and:
			assert result

		when: "second run - was not in the db before delete - could not delete"
			result = service.delete(tmpService.getServiceId())
		then:
			1 * service.serviceRepository.findById(tmpService.getServiceId()) >> Optional.empty()
			0 * _
		and:
			assert !result
	}
}
