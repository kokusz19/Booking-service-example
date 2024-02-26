package com.kokusz19.udinfopark.service


import spock.lang.Subject

class ModelConverterTest extends TestBase {

	@Subject
	def modelConverter = new ModelConverter(
			Mock(ServiceService),
			Mock(CompanyService)
	)

	def "company converter"() {
		when: "dao -> dto"
			def result = modelConverter.convert(companyDao)
		then:
			0 * _
		and:
			assert result == companyDto

		when: "dto -> dao"
			result = modelConverter.convert(companyDto)
		then:
			0 * _
		and:
			assert result == companyDao
	}

	def "reservation converter"() {
		when: "dao -> dto"
			def result = modelConverter.convert(reservationDao)
		then:
			0 * _
		and:
			assert result == reservationDto

		when: "dto -> dao"
			result = modelConverter.convert(reservationDto)
		then:
			// TODO: CACHE - company, service
			1 * modelConverter.serviceService.findByIds([1]) >> [serviceDao]
			2 * modelConverter.companyService.getOne(reservationDto.companyId) >> companyDto
			1 * modelConverter.serviceService.getOne(1) >> serviceDto
			0 * _
		and:
			assert result == reservationDao

		when: "dto -> dao - missing serviceIds"
			modelConverter.convert(reservationDto)
		then:
			1 * modelConverter.serviceService.findByIds([1]) >> []
			0 * _
		and:
			def ex = thrown(IllegalArgumentException)
			assert ex.message == "There are missing service ids in the request [ids=1]"

		when: "dto -> dao - missing company"
			modelConverter.convert(reservationDto)
		then:
			1 * modelConverter.serviceService.findByIds([1]) >> [serviceDao]
			1 * modelConverter.companyService.getOne(reservationDto.companyId) >> null
			0 * _
		and:
			ex = thrown(NullPointerException)
			assert ex.message == "Company could not be found with [id=1]"
	}

	def "service converter"() {
		when: "dao -> dto"
			def result = modelConverter.convert(serviceDao)
		then:
			0 * _
		and:
			assert result == serviceDto

		when: "dto -> dao"
			result = modelConverter.convert(serviceDto)
		then:
			1 * modelConverter.companyService.getOne(serviceDto.companyId) >> companyDto
			0 * _
		and:
			assert result == serviceDao

		when: "dto -> dao - missing company"
			modelConverter.convert(serviceDto)
		then:
			1 * modelConverter.companyService.getOne(serviceDto.companyId) >> null
			0 * _
		and:
			def ex = thrown(NullPointerException)
			assert ex.message == "Company not found with [id=1]"
	}

	def "time converter"() {
		when: "dao -> dto"
			def result = modelConverter.convert(timeDaoA)
		then:
			0 * _
		and:
			assert result == timeDtoA

		when: "dto -> dao"
			result = modelConverter.convert(timeDtoA)
		then:
			0 * _
		and:
			assert result == timeDaoA
	}
}