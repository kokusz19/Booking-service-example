package com.kokusz19.udinfopark.service


import spock.lang.Subject

class CompanyServiceTest extends TestBase {

	@Subject
	def service = new CompanyService(Mock(CompanyRepository))

	def "getAll"() {
		when:
			def result = service.getAll()
		then:
			1 * service.companyRepository.findAll() >> [company]
			0 * _
		and:
			assert result == [company]
	}

	def "getOne"() {
		when: "found"
			def result = service.getOne(1)
		then:
			1 * service.companyRepository.findById(1) >> Optional.of(company)
			0 * _
		and:
			assert result == company

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
			def result = service.create(company)
		then:
			1 * service.companyRepository.findByName(company.getName()) >> Optional.empty()
			1 * service.companyRepository.save(company) >> {
				company.setCompanyId(newCompanyId)
				return company
			}
			0 * _
		and:
			assert result == newCompanyId

		when: "present - can't create company with the same name"
			service.create(company)
		then:
			1 * service.companyRepository.findByName(company.getName()) >> Optional.of(company)
			0 * _
		and:
			def ex = thrown(RuntimeException)
			assert ex.message == "Company already exists!"
	}

	def "update"() {
		when:
			def result = service.update(company.getCompanyId(), company)
		then:
			1 * service.companyRepository.save(company) >> company
			0 * _
		and:
			assert result == company
	}

	def "delete"() {
		when: "first run - was in the db before delete - could delete"
			def result = service.delete(company.getCompanyId())
		then:
			1 * service.companyRepository.findById(company.getCompanyId()) >> Optional.of(company)
			1 * service.companyRepository.delete(company)
			0 * _
		and:
			assert result

		when: "second run - was not in the db before delete - could not delete"
			result = service.delete(company.getCompanyId())
		then:
			1 * service.companyRepository.findById(company.getCompanyId()) >> Optional.empty()
			0 * _
		and:
			assert !result
	}
}
