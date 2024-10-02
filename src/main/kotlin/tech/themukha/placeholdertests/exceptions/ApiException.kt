package tech.themukha.placeholdertests.exceptions

class ApiException(
    expectedResponseCode: Int,
    actualResponseCode: Int,
    responseBody: String? = null
) : AssertionError(
    "Expected response code $expectedResponseCode, but got $actualResponseCode.\nResponse body:\n$responseBody"
)