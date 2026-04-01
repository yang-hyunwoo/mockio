## ❗ 커스텀 예외 처리

[🔝 메인 목차로 이동](../../../readme.md)

### ✅ 예외 처리 흐름

- 서비스 로직에서 유효성 또는 권한 문제 발생 시 `CustomApiException`, `CustomApiFieldException` 등을 발생시킴
- 전역 예외 처리기 `@RestControllerAdvice`에서 해당 예외를 `Response.error(...)` 포맷으로 응답
- 응답 형식은 `Response<T>` 객체에 맞춰 반환됨

<br><br>
### 🧱 예외 응답 형식 (예시)

```json
{
  "resultCode": "ERROR",
  "httpCode": 403,
  "message": "권한이 없습니다",
  "errCode": "ERR_FORBIDDEN",
  "errCodeMsg": "권한이 없습니다",
  "data": null,
  "timestamp": "2025-06-26 12:34:56"
}
```

### 사용 예

```java
public class aa {
    public void findGroupAuth(Long studyGroupId,
                              Long memberId
    ) {
        studyGroupMemberRepository.findGroupAuthNative(studyGroupId, memberId)
                .orElseThrow(() -> new CustomApiException(
                        BAD_REQUEST,
                        ERR_015,
                        ERR_015.getValue()
                ));
    }
}
```

### ✅ CustomApiException 클래스 (코드)
<details> 
<summary>클릭하여 펼치기</summary>

```java

package front.meetudy.exception;

import front.meetudy.constant.error.ErrorEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomApiException extends RuntimeException{

    private final HttpStatus status;
    private final ErrorEnum errorEnum;

    public CustomApiException(HttpStatus status,
                              ErrorEnum errorEnum,
                              String message
    ) {
        super(message);
        this.status = status;
        this.errorEnum = errorEnum;
    }

}

```
</details>

### @RestControllerAdvice 이용한 Exception 전역 처리 CustomExceptionHandler 클래스 (코드)
<details> 
<summary>클릭하여 펼치기</summary>

```java

package front.meetudy.exception;

import front.meetudy.constant.error.ErrorEnum;
import front.meetudy.util.MessageUtil;
import front.meetudy.util.response.Response;
import front.meetudy.util.response.ValidationErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

import static front.meetudy.constant.error.ErrorEnum.*;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class CustomExceptionHandler {

    private final MessageUtil messageUtil;

    /**
     * 일반적인 에러
     * @param e
     * @return
     */
    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<Response<String>> handleCustomApiException(CustomApiException e) {
        log.error("CustomApiException: {}", e.getMessage());
        return Response.error(e.getStatus(), e.getMessage(), e.getErrorEnum(), null);
    }

    //TODO 어떤 방식으로 내려줄지 고민
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response<ValidationErrorResponse>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException: {}" , e.getMessage());
        return Response.error(BAD_REQUEST, e.getMessage(), ERR_002, null);
    }

    /**
     * 변수 단일 유효성 에러 리턴
     * @param e
     * @return
     */
    @ExceptionHandler(CustomApiFieldException.class)
    public ResponseEntity<Response<ValidationErrorResponse>> handleCustomApiFieldException(CustomApiFieldException e) {
        log.error("CustomApiFieldException: {}", e.getMessage());
        return Response.validationError(e.getStatus(),e.getMessage(),e.getErrorEnum(),e.getField());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Response<ValidationErrorResponse>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException: {}" , e.getMessage());
        return Response.error(BAD_REQUEST, messageUtil.getMessage("error.not.data.type.ok"), ERR_018, null);
    }

    /**
     * 변수 리스트 유효성 에러 리턴
     * @param e
     * @return
     */
    @ExceptionHandler(CustomApiFieldListException.class)
    public ResponseEntity<Response<List<ValidationErrorResponse>>> handleCustomApiFieldListException(CustomApiFieldListException e) {
        log.error("CustomApiFieldListException: {}", e.getMessage());
        return Response.validationErrorList(e.getStatus(), e.getMessage(), e.getErrorEnum(), e.getErrors());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Response<String>> handleNotFound(NoHandlerFoundException ex) {
        log.error("404 에러 발생",ex);
        return Response.error(NOT_FOUND, messageUtil.getMessage("error.not.fount.ok"), ERR_404, null);
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Response<String>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        log.error("405 에러 발생",ex);
        return Response.error(METHOD_NOT_ALLOWED, messageUtil.getMessage("error.not.allow.method.ok"), ERR_405, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<String>> handleServerError(Exception ex) {
        log.error("500 에러 발생", ex);
        return Response.error(HttpStatus.INTERNAL_SERVER_ERROR, messageUtil.getMessage("error.server.ok"), ErrorEnum.ERR_500, null);
    }
}


```
</details>


### 📑 주요 에러 코드 목록
- ErrorEnum.java 참조

| 코드      | 의미                 | 설명                    |
|---------|--------------------|-------------------------|
| ERR_404 | Not Found          | 존재하지 않는 경로입니다.   |
| ERR_405 | Method Not Allowed | 허용되지 않은 HTTP 메서드입니다. |
| ERR_015 | 권한 없음              | 그룹 접근 권한 없음     |
| ERR_012 | return data null   | 존재하지 않는 데이터 입니다.|