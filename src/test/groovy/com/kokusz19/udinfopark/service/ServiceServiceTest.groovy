package com.kokusz19.udinfopark.service

import com.kokusz19.udinfopark.repository.ServiceRepository
import spock.lang.Subject

class ServiceServiceTest extends TestBase {

	@Subject
	def service = new ServiceService(
			Mock(ServiceRepository),
			Mock(ModelConverter))

	def "getAll"() {
		when:
			def result = service.getAll()
		then:
			1 * service.serviceRepository.findAll() >> [serviceDao]
			1 * service.modelConverter.convert(serviceDao) >> serviceDto
			0 * _
		and:
			assert result == [serviceDto]
	}

	def "getOne"() {
		when: "found"
			def result = service.getOne(1)
		then:
			1 * service.serviceRepository.findById(1) >> Optional.of(serviceDao)
			1 * service.modelConverter.convert(serviceDao) >> serviceDto
			0 * _
		and:
			assert result == serviceDto

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
			def result = service.create(serviceDto)
		then:
			1 * service.serviceRepository.findByName(serviceDto.getName()) >> Optional.empty()
			1 * service.modelConverter.convert(serviceDto) >> serviceDao
			1 * service.serviceRepository.save(serviceDao) >> {
				serviceDao.setServiceId(newServiceId)
				return serviceDao
			}
			0 * _
		and:
			assert result == newServiceId

		when: "present - can't create service with the same name"
			service.create(serviceDto)
		then:
			1 * service.serviceRepository.findByName(serviceDto.getName()) >> Optional.of(serviceDao)
			0 * _
		and:
			def ex = thrown(IllegalArgumentException)
			assert ex.message == "Service already exists!"
	}

	def "update"() {
		when:
			def result = service.update(serviceDto.getServiceId(), serviceDto)
		then:
			1 * service.modelConverter.convert(serviceDto) >> serviceDao
			1 * service.serviceRepository.save(serviceDao) >> serviceDao
			1 * service.modelConverter.convert(serviceDao) >> serviceDto
			0 * _
		and:
			assert result == serviceDto
	}

	def "delete"() {
		when: "first run - was in the db before delete - could delete"
			def result = service.delete(serviceDto.getServiceId())
		then:
			1 * service.serviceRepository.findById(serviceDto.getServiceId()) >> Optional.of(serviceDao)
			1 * service.serviceRepository.delete(serviceDao)
			0 * _
		and:
			assert result

		when: "second run - was not in the db before delete - could not delete"
			result = service.delete(serviceDto.getServiceId())
		then:
			1 * service.serviceRepository.findById(serviceDto.getServiceId()) >> Optional.empty()
			0 * _
		and:
			assert !result
	}
}
