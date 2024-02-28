package com.kokusz19.udinfopark.service

import com.kokusz19.udinfopark.dependency.TestUserRealmKeyCloakFeignClient
import com.kokusz19.udinfopark.model.dto.security.JwtTokenRequest
import com.kokusz19.udinfopark.model.dto.security.JwtTokenResponse
import spock.lang.Specification
import spock.lang.Subject

class TokenServiceTest extends Specification {

	@Subject
	def tokenService = new TokenService(Mock(TestUserRealmKeyCloakFeignClient))

	def "getToken"() {
		setup:
			def jwtTokenRequest = new JwtTokenRequest(
					"main-client",
					"TRYAjkj6JvVQrW6DjHM67g75yR1mVXE0",
					"password",
					"testUser",
					"testuser")
			def jwtTokenResponse = new JwtTokenResponse("accessToken")

		when:
			def result = tokenService.getToken(jwtTokenRequest)
		then:
			1 * tokenService.testUserRealmKeyCloakFeignClient.generateTokens({
				assert it["client_id"]      == [jwtTokenRequest.client_id()]
				assert it["client_secret"]  == [jwtTokenRequest.client_secret()]
				assert it["grant_type"]     == [jwtTokenRequest.grant_type()]
				assert it["username"]       == [jwtTokenRequest.username()]
				assert it["password"]       == [jwtTokenRequest.password()]
			}) >> jwtTokenResponse
			0 * _
		and:
			assert result == jwtTokenResponse
	}
}
