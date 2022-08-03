package com.eratart.domain.exception

abstract class AppException(message: String?) : Throwable(message)

abstract class ApiException(code: Int? = null, message: String? = null) : AppException(message)

class BackendException(code: Int, message: String) : ApiException(code, message)
class UnauthorizedException(code: Int) : ApiException(code)
class WrongRefreshException(code: Int) : ApiException(code)
class NotFoundException(code: Int) : ApiException(code)
class TimeoutException : ApiException()
class OtherApiException(message: String) : ApiException(message = message)

abstract class UploaderException : AppException(null)

class VoiceUploaderException : UploaderException()
class PhotoUploaderException : UploaderException()
class VideoUploaderException : UploaderException()
