package com.kokusz19.udinfopark.service

import com.kokusz19.udinfopark.repository.CompanyRepository
import spock.lang.Subject

class CompanyServiceTest extends TestBase {

	@Subject
	def service = new CompanyService(
			Mock(CompanyRepository),
			Mock(ModelConverter))

	def "getAll"() {
		when:
			def result = service.getAll()
		then:
			1 * service.companyRepository.findAll() >> [companyDao]
			1 * service.modelConverter.convert(companyDao) >> companyDto
			0 * _
		and:
			assert result == [companyDto]
	}

	def "getOne"() {
		when: "found"
			def result = service.getOne(1)
		then:
			1 * service.companyRepository.findById(1) >> Optional.of(companyDao)
			1 * service.modelConverter.convert(companyDao) >> companyDto
			0 * _
		and:
			assert result == companyDto

		when: "not found"
			result = service.getOne(999)
		then:
			1 * service.companyRepository.findById(999) >> Optional.empty()
			0 * _
		and:
			assert result == null
	}

	def "create"() {
		setup:
			def newCompanyId = 50
		when: "not present - can create"
			def result = service.create(companyDto)
		then:
			1 * service.companyRepository.findByName(companyDto.getName()) >> Optional.empty()
			1 * service.modelConverter.convert(companyDto) >> companyDao
			1 * service.companyRepository.save(companyDao) >> {
				companyDao.setCompanyId(newCompanyId)
				return companyDao
			}
			0 * _
		and:
			assert result == newCompanyId

		when: "present - can't create company with the same name"
			service.create(companyDto)
		then:
			1 * service.companyRepository.findByName(companyDto.getName()) >> Optional.of(companyDto)
			0 * _
		and:
			def ex = thrown(IllegalArgumentException)
			assert ex.message == "Company already exists!"
	}

	def "update"() {
		when:
			def result = service.update(companyDto.getCompanyId(), companyDto)
		then:
			1 * service.modelConverter.convert(companyDto) >> companyDao
			1 * service.companyRepository.save(companyDao) >> companyDao
			1 * service.modelConverter.convert(companyDao) >> companyDto
			0 * _
		and:
			assert result == companyDto
	}

	def "delete"() {
		when: "first run - was in the db before delete - could delete"
			def result = service.delete(companyDto.getCompanyId())
		then:
			1 * service.companyRepository.findById(companyDto.getCompanyId()) >> Optional.of(companyDao)
			1 * service.companyRepository.delete(companyDao)
			0 * _
		and:
			assert result

		when: "second run - was not in the db before delete - could not delete"
			result = service.delete(companyDto.getCompanyId())
		then:
			1 * service.companyRepository.findById(companyDto.getCompanyId()) >> Optional.empty()
			0 * _
		and:
			assert !result
	}
}
