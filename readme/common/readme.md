## 📦 Entity , Dto

[🔝 메인 목차로 이동](../../readme.md)

### 📦 Entity

1️⃣ 생성자 접근 제어
- 외부에서 직접 인스턴스를 생성하지 않도록
  @NoArgsConstructor(access = AccessLevel.PROTECTED)를 사용합니다.
- 객체 생성을 위해 Builder 패턴과 정적 팩토리 메서드(create~)를 활용 합니다.

2️⃣ 상속 구조
- 공통 필드(생성일, 수정일 등)는 BaseEntity를 상속받아 관리합니다.
- 작성자/수정자가 필요 없는 경우에는 BaseTimeEntity만 상속 가능 합니다.

3️⃣ 데이터 수정 방식
- 데이터 변경이 필요한 경우, 엔티티 내부에 명시적인 수정 메서드를 제공하여
- 영속성 컨텍스트의 변경 감지 기능을 활용 합니다.

4️⃣ equals/hashCode 구현
- 객체 간 동등성 보장을 위해 equals, hashCode를 재정의합니다.
- 비교는 id 필드만 기준으로 수행하여 일관성을 유지 합니다.

5️⃣ 연관 관계 설정
- 모든 연관 관계의 FetchType은 기본적으로 LAZY를 사용하여
  불필요한 즉시 로딩(EAGER)을 방지 합니다.

6️⃣삭제 처리 방식
- deleted 필드를 이용한 Soft Delete 방식을 사용하며  
  실제 DB 삭제는 하지 않고, 조회 시 조건 추가로 제외 처리 합니다.
- deleted = true일 경우, JPA 쿼리 또는 QueryDSL 등에서 WHERE 조건을 추가하여 필터링합니다.
- 필요 시 @Where(clause = "deleted = false") 또는 soft delete 전용 Repository 메서드 작성


### 📦 Dto

1️⃣ Dto 처리
- 모든 Dto는 Request / Response Dto로 파일을 나눕니다.

2️⃣ DTO 역할
- 요청(Request) DTO는 Controller 계층에서 입력 값을 받기 위한 용도
- 응답(Response) DTO는 Service or Entity의 결과를 클라이언트에 반환하기 위한 용도

3️⃣ DTO 네이밍 컨벤션
- XxxInsertRequest, XxxUpdateRequest, XxxResponse 등 명확한 구분을 위한 네이밍 전략을 사용

4️⃣ 유효성 검사
- Request Dto에서는 유효성 검사를 추가 하며
  단일 유효성 에러 리턴 시에
  groups=Step1~20.class를 추가하여 순서 보장을 해줍니다.

5️⃣ DTO toEntity 변환 메서드 위치
- toEntity(),from()  메서드는 보통 xxxMapper에 위치시킵니다.
  toEntity : (Controller → DTO로 받음 → Entity 변환 → Service 전달)
  from : (service → Entity로 받음 → ResDto로 변환 → Controller 전달)

6️⃣Dto 패키지 분리
- request : front.meetudy.dto.request
- response : front.meetudy.dto.response


---


## 📦 API 응답 구조

모든 API 응답은 아래와 같은 공통 구조를 따릅니다.

### 페이징 일 경우
```json

{
  "resultCode": "SUCCESS",
  "httpCode": 200,
  "message": "요청이 성공적으로 처리되었습니다.",
  "errCode": "ERR_XXX",
  "errCodeMsg": "값이 없습니다.",
  "data": {
    "content": [],
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0
  },
  "timestamp": "2025-06-26T07:25:08.636Z"
}

```
<br>

### 페이징 을 제외 한 경우
```json
{
  "resultCode": "SUCCESS",
  "httpCode": 200,
  "message": "요청이 성공적으로 처리되었습니다.",
  "errCode": "ERR_XXX",
  "errCodeMsg": "값이 없습니다.",
  "data": [],
  "timestamp": "2025-06-26T07:18:13.785Z"
}
```
<br><br>

| 필드명        | 타입          | 설명                          |
|------------|-------------|-----------------------------|
| resultCode | string      | 성공 여부(예:SUCCESS/ERROR)      |
| httpCode   | number      | HTTP 상태 코드 (예: 200,400 등)   |
| message    | string      | 응답 메시지                      |
| errCode    | String/null | 오류 코드(예:ERR_001),성공 시 null  |
| errCodeMsg | String/null | 오류 상세 메시지,성공 시 null         |
| data       | Object      | 실제 응답 데이터 (T)               |
| timestamp  | String      | 응답 시간 (yyyy-mm-dd HH:mm:ss) |

<br>

### ✅ Response<T> 클래스 (코드)
<details> 
<summary>클릭하여 펼치기</summary>

```java

package com.mockio.common_spring.util.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mockio.common_core.error.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.List;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

/**
 * API 공통 응답 포맷팅
 * @param <T>
 */
@Getter
@AllArgsConstructor
@Schema(description = "API 공통 응답 포맷")
public class Response<T> {

  @Schema(description = "결과 코드", example = "SUCCESS")
  private String resultCode;

  @Schema(description = "HTTP 상태 코드", example = "200")
  private int httpCode;

  @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다.")
  private String message;

  @Schema(description = "에러 코드", example = "ERR_XXX")
  private String errCode;

  @Schema(description = "에러 코드 메시지", example = "값이 없습니다.")
  private String errCodeMsg;

  @Schema(description = "응답 데이터")
  private  T data;

  @Schema(description = "시간")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private OffsetDateTime timestamp;

  private static final String SUCCESS_CODE = "SUCCESS";

  private static final String ERROR_CODE = "ERROR";



  /**
   * 일반적인 에러
   * @param httpStatus
   * @param message
   * @param messageOrData
   * @return
   * @param <T>
   */
  public static <T> ResponseEntity<Response<T>> error(int httpStatus, String message, ErrorCode errorCode, T messageOrData) {
    return ResponseEntity.status(httpStatus).body(new Response<>(ERROR_CODE, httpStatus, message, errorCode.getCode(), errorCode.getMessage(), messageOrData, OffsetDateTime.now()));
  }

  /**
   * 변수 유효성 검사 에러
   * @param httpStatus
   * @param message
   * @param field
   * @return
   */
  public static ResponseEntity<Response<com.mockio.common_core.exception.ValidationErrorResponse>> validationError(int httpStatus, String message, ErrorCode errorCode , String field) {
    return ResponseEntity.status(httpStatus).body(new Response<>(ERROR_CODE, httpStatus, message, errorCode.getCode(), errorCode.getMessage(), new com.mockio.common_core.exception.ValidationErrorResponse(field, message), OffsetDateTime.now()));
  }

  public static ResponseEntity<Response<List<com.mockio.common_core.exception.ValidationErrorResponse>>> validationErrorList(int httpStatus, String message, ErrorCode errorCode, List<com.mockio.common_core.exception.ValidationErrorResponse> errors) {
    return ResponseEntity.status(httpStatus).body(
            new Response<>(
                    ERROR_CODE,
                    httpStatus,
                    message,
                    errorCode.getCode(),
                    errorCode.getMessage(),
                    errors,
                    OffsetDateTime.now()
            )
    );
  }

  public static <T> ResponseEntity<Response<T>> ok(String message, T data) {
    return ResponseEntity.status(HttpStatus.OK).body(successRead(message, data));
  }

  public static <T> ResponseEntity<Response<T>> ok(String headerValues, String message, T data) {
    return ResponseEntity.status(HttpStatus.OK).header(SET_COOKIE,headerValues).body(successRead(message, data));
  }

  public static <T> ResponseEntity<Response<T>> create(String message, T data) {
    return ResponseEntity.status(HttpStatus.CREATED).body(successCreate(message, data));
  }
  public static <T> ResponseEntity<Response<T>> create(String headerValues , String message, T data) {
    return ResponseEntity.status(HttpStatus.CREATED).header(SET_COOKIE,headerValues).body(successCreate(message, data));
  }

  public static <T> ResponseEntity<Response<T>> update(String message, T data) {
    return ResponseEntity.status(HttpStatus.OK).body(successUpdate(message, data));
  }

  public static <T> ResponseEntity<Response<T>> update(String message) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(successUpdate(message));
  }

  public static <T> ResponseEntity<Response<T>> delete(String message, T data) {
    return ResponseEntity.status(HttpStatus.OK).body(successDelete(message, data));
  }

  public static ResponseEntity<Void> deleteNoContent() {
    return ResponseEntity.noContent().build();
  }

  protected static Response<String> error(int httpStatus, ErrorCode errorCode ,String message) {
    return new Response<>(ERROR_CODE, httpStatus, message, errorCode.getCode(), errorCode.getMessage(), null, OffsetDateTime.now());
  }

  protected static <T> Response<T> successRead(String message, T data) {
    return new Response<>(SUCCESS_CODE, HttpStatus.OK.value(), message,null,null,data,OffsetDateTime.now());
  }

  protected static <T> Response<T> successCreate(String message, T data) {
    return new Response<>(SUCCESS_CODE, HttpStatus.CREATED.value(), message,null,null, data,OffsetDateTime.now());
  }

  protected static <T> Response<T> successUpdate(String message, T data) {
    return new Response<>(SUCCESS_CODE, HttpStatus.OK.value(), message,null,null, data,OffsetDateTime.now());
  }
  protected static <T> Response<T> successUpdate(String message) {
    return new Response<>(SUCCESS_CODE, HttpStatus.NO_CONTENT.value(), message,null,null,null,OffsetDateTime.now());
  }

  protected static <T> Response<T> successDelete(String message, T data) {
    return new Response<>(SUCCESS_CODE, HttpStatus.OK.value(), message,null,null, data,OffsetDateTime.now());
  }

  @Override
  public String toString() {
    return "Response{" +
            "resultCode='" + resultCode + '\'' +
            ", httpCode=" + httpCode +
            ", message='" + message + '\'' +
            ", errCode='" + errCode + '\'' +
            ", errCodeMsg='" + errCodeMsg + '\'' +
            ", data=" + data +
            ", timestamp=" + timestamp +
            '}';
  }

}

```
</details>



### 📑 주요 유효성 어노테이션

| 어노테이션          | 설명                |
|----------------|-------------------|
| Sanitize       | 특수문자 및 xss  검사    |
| EnumValidation | Enum class 유효성 검사 |
| NotBlank       | 빈값 유효성 검사         |
| Length         | 길이 유효성 검사         |
| Email          | 이메일 유효성 검사        |
| KoreanEnglish         | 한글,영문 유효성 검사      |
| Numeric         | 숫자 유효성 검사         |
| PhoneNumber         | 휴대폰번호 유효성 검사      |
| Password         | 비밀번호 유효성 검사       |