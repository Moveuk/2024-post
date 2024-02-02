package xyz.moveuk.post.global.exception

import xyz.moveuk.post.global.exception.dto.ErrorCode


data class RestApiException(val errorCode: ErrorCode) : RuntimeException()