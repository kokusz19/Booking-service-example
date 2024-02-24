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
			1 * modelConverter.companyService.getOne(reservationDto.companyId) >> companyDto
			1 * modelConverter.serviceService.findByIds(reservationDto.serviceIds) >> [serviceDao]
			0 * _
		and:
			assert result == reservationDao
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