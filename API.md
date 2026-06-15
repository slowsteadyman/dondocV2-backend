0. 카테고리 목록 조회
   **엔드포인트**

GET http://localhost:8080/api/categories

**Headers**

userId: 1

### Response

| key  | 설명          | value 타입 | 옵션           | Nullable | 예시      |
| ---- | ------------- | ---------- | -------------- | -------- | --------- |
| id   | 카테고리 ID   | Long       | -              | X        | 6         |
| name | 카테고리 이름 | String     | -              | X        | "식비"    |
| type | 거래 유형     | String     | INCOME/EXPENSE | X        | "EXPENSE" |

**Response Example**

```json
{
  "success": true,
  "data": [
    { "id": 1, "name": "월급", "type": "INCOME" },
    { "id": 2, "name": "부수입", "type": "INCOME" },
    { "id": 3, "name": "투자수익", "type": "INCOME" },
    { "id": 4, "name": "용돈", "type": "INCOME" },
    { "id": 5, "name": "기타수입", "type": "INCOME" },
    { "id": 6, "name": "식비", "type": "EXPENSE" },
    { "id": 7, "name": "교통비", "type": "EXPENSE" },
    { "id": 8, "name": "쇼핑", "type": "EXPENSE" },
    { "id": 9, "name": "문화생활", "type": "EXPENSE" },
    { "id": 10, "name": "의료/건강", "type": "EXPENSE" },
    { "id": 11, "name": "교육", "type": "EXPENSE" },
    { "id": 12, "name": "주거/통신", "type": "EXPENSE" },
    { "id": 13, "name": "기타지출", "type": "EXPENSE" }
  ],
  "message": "카테고리 조회 성공"
}
```

### Status

| status | response content |
| ------ | ---------------- |
| 200    | 조회 성공        |
| 401    | 인증 토큰 없음   |

1. 내 정보 조회
   **엔드포인트**

GET http://localhost:8080/api/users/me

**Headers**

userId: 1

**Path Parameter**

**Query Parameter**

### Request Body

### Response

| key                   | 설명                 | value 타입 | 옵션 | Nullable | 예시    |
| --------------------- | -------------------- | ---------- | ---- | -------- | ------- |
| success               | 성공 여부            | Boolean    | -    | X        | true    |
| data                  | 응답 데이터          | Object     | -    | X        | {}      |
| name                  | 내 닉네임            | String     | -    | X        | 돼지    |
| age                   | 내 나이              | Integer    | -    | X        | 25      |
| currentPigLevel       | 내 돼지 레벨         | Integer    | -    | X        | 3       |
| currentHouseLevel     | 내 집 레벨           | Integer    | -    | X        | 3       |
| currentCharacterLevel | 내 집사 레벨         | Integer    | -    | X        | 2       |
| monthlyIncome         | 내 월 소득 예측      | Long       | -    | X        | 500000  |
| targetExpenseRatio    | 내 월 목표 지출 비율 | Number     | -    | X        | 50      |
| monthlyBudget         | 월 지출 목표 예산    | Long       | -    | X        | 1500000 |
| dailyBudget           | 일 지출 목표 예산    | Long       | -    | X        | 48387   |

**Request Example**

```jsx
GET / api / users / me;

Request;
Headers: userId: 1;
```

**Response Example**

```jsx
Response 200
	Body:
		{
		  "success": true,
		  "data": {
			  "name": "돼지",
			  "age": 25,
			  "currentPigLevel": 3,
			  "currentHouseLevel": 3,
			  "currentCharacterLevel": 2,
			  "monthlyIncome": 500000,
			  "targetExpenseRatio": 50,
			  "monthlyBudget": 1500000,
			  "dailyBudget": 48387
		  },
		  "message": "내 정보 조회 성공"
		}

Response 401
	Body: {
		"success": false,
		"message": "인증 실패."
	}
```

### Status

| status | response content        |
| ------ | ----------------------- |
| 200    | 조회 성공               |
| 401    | 인증 실패 (userId 없음) |

2. 사용자 기본 설정
   **엔드포인트**

PATCH http://localhost:8080/api/users/me

**Headers**

userId: 1

**Path Parameter**

**Query Parameter**

### Request

| key                | 설명                  | value 타입 | 옵션 | Nullable | 예시     |
| ------------------ | --------------------- | ---------- | ---- | -------- | -------- |
| age                | 사용자 나이           | int        |      | O        | 23       |
| monthlyIncome      | 사용자 월 수입        | Long       |      | O        | 2000000  |
| targetExpenseRatio | 사용자 목표 지출 비율 | Number     |      | O        | 80       |
| name               | 사용자 이름           | string     |      | O        | “홍길동” |

**Query parameter**

### Response

| key                | 설명              | value 타입 | 옵션 | Nullable | 예시                          |
| ------------------ | ----------------- | ---------- | ---- | -------- | ----------------------------- |
| success            | 설정 성공 여부    | boolean    |      | X        | true                          |
| message            | 응답 메시지       | string     |      | X        | “프로필 설정에 성공했습니다.” |
| data               | 응답 데이터       | object     |      | X        |                               |
| data.monthlyBudget | 월 지출 목표 예산 | Long       |      | X        | 1600000                       |
| data.dailyBudget   | 일 지출 목표 예산 | Long       |      | X        | 51613                         |

**Example**

**Request Body**

```json
{
  "name": "홍길동",
  "age": 23,
  "monthlyIncome": 2000000,
  "targetExpenseRatio": 80
}
```

**Response Body**

프로필 설정 성공(200 OK)

```json
{
  "success": true,
  "message": "프로필 설정이 완료되었습니다.",
  "data": {
    "id": 1,
    "name": "홍길동",
    "age": 23,
    "monthlyIncome": 2000000,
    "targetExpenseRatio": 80,
    "monthlyBudget": 1600000,
    "dailyBudget": 51613
  }
}
```

### Status

| status | response content |
| ------ | ---------------- |
| 200    | 프로필 설정 성공 |
| 400    |                  |

3. 월별 거래 내역 조회
   **엔드포인트**

GET [http://localhost:8080/api/records?yearMonth=2026-05&type=EXPENSE](http://localhost:8080/api/records?yearMonth=2026-05&type=EXPENSE)

**Headers**

userId: 1

**Path Parameter**

**Query Parameter**

| **파라미터명** | 설명      | 타입   | 필수 | 예시                            |
| -------------- | --------- | ------ | ---- | ------------------------------- |
| yearMonth      | 조회 년월 | String | O    | "2026-05”                       |
| type           | 거래 유형 | String | X    | "INCOME", "EXPENSE", null(전체) |

### Request Body

| key | 설명 | value 타입 | 옵션 | Nullable | 예시 |
| --- | ---- | ---------- | ---- | -------- | ---- |
|     |      |            |      |          |      |

### Response

| key          | 설명             | value 타입 | 옵션           | Nullable | 예시                  |
| ------------ | ---------------- | ---------- | -------------- | -------- | --------------------- |
| success      | 성공 여부        | Boolean    | -              | X        | true                  |
| data         | 응답 데이터      | Object     | -              | X        | {}                    |
| summary      | 월별 요약        | Object     | -              | X        | {}                    |
| totalIncome  | 총 수입          | Long       | -              | X        | 3000000               |
| totalExpense | 총 지출          | Long       | -              | X        | 150000                |
| balance      | 합계 (수입-지출) | Long       | -              | X        | 2850000               |
| records      | 거래 내역 목록   | Array      | -              | X        | [{}]                  |
| id           | 거래 ID          | Long       | -              | X        | 1                     |
| type         | 거래 유형        | String     | INCOME/EXPENSE | X        | "EXPENSE”             |
| date         | 거래 날짜        | String     | YYYY-MM-DD     | X        | "2026-05-25”          |
| category     | 카테고리 이름    | String     | -              | X        | "식비”                |
| amount       | 금액             | Long       | -              | X        | 15000                 |
| description  | 내용             | String     | -              | O        | "점심 식사”           |
| memo         | 메모             | String     | -              | O        | "회사 근처 식당”      |
| message      | 응답 메시지      | String     | -              | X        | "거래 내역 조회 성공” |

**Request Example**

```jsx
GET /api/records?yearMonth=2026-05&type=EXPENSE
```

**Response Example**

```jsx
{
  "success": true,
  "data": {
    "summary": {
      "totalIncome": 3000000,
      "totalExpense": 150000,
      "balance": 2850000
    },
    "records": [
      {
        "id": 1,
        "type": "EXPENSE",
        "date": "2026-05-25",
        "category": {
				  "id": 6,
				  "name": "식비"
				}
        "amount": 15000,
        "description": "점심 식사",
        "memo": "회사 근처 식당"
      },
      {
        "id": 2,
        "type": "EXPENSE",
        "date": "2026-05-24",
        "category": {
				  "id": 5,
				  "name": "교통비"
				}
        "amount": 5000,
        "description": "택시",
        "memo": "야근 후"
      }
    ]
  },
  "message": "거래 내역 조회 성공"
}
```

### Status

| status | response content                  |
| ------ | --------------------------------- |
| 200    | 조회 성공                         |
| 400    | 잘못된 요청 (yearMonth 형식 오류) |
| 401    | 인증 토큰 없음                    |
| 404    | 사용자를 찾을 수 없음             |

4. 거래 추가하기
   **엔드포인트**

POST http://localhost:8080/api/records

**Headers**

userId: 1
Content-Type: application/json

**Query Parameter**

### Request Body

| key         | 설명        | value 타입 | 옵션           | Nullable | 예시             |
| ----------- | ----------- | ---------- | -------------- | -------- | ---------------- |
| type        | 거래 유형   | String     | INCOME/EXPENSE | X        | "EXPENSE”        |
| categoryId  | 카테고리 ID | Long       | -              | X        | 6                |
| date        | 거래 날짜   | String     | YYYY-MM-DD     | X        | "2026-05-25”     |
| amount      | 금액        | Long       | -              | X        | 15000            |
| description | 내용        | String     | -              | O        | "점심 식사”      |
| memo        | 메모        | String     | -              | O        | "회사 근처 식당” |

### Response

| key         | 설명             | value 타입 | 옵션           | Nullable | 예시             |
| ----------- | ---------------- | ---------- | -------------- | -------- | ---------------- |
| success     | 성공 여부        | Boolean    | -              | X        | true             |
| data        | 생성된 거래 정보 | Object     | -              | X        | {}               |
| id          | 거래 ID          | Long       | -              | X        | 1                |
| type        | 거래 유형        | String     | INCOME/EXPENSE | X        | "EXPENSE”        |
| date        | 거래 날짜        | String     | YYYY-MM-DD     | X        | "2026-05-25”     |
| category    | 카테고리 이름    | String     | -              | X        | "식비”           |
| amount      | 금액             | Long       | -              | X        | 15000            |
| description | 내용             | String     | -              | O        | "점심 식사”      |
| memo        | 메모             | String     | -              | O        | "회사 근처 식당” |
| message     | 응답 메시지      | String     | -              | X        | "거래 추가 성공” |

**Request Example**

```jsx
{
  "type": "EXPENSE",
  "categoryId": 6,
  "date": "2026-05-25",
  "amount": 15000,
  "description": "점심 식사",
  "memo": "회사 근처 식당"
}
```

**Response Example**

```jsx
{
  "success": true,
  "data": {
    "id": 1,
    "type": "EXPENSE",
    "date": "2026-05-25",
    "category": {
		  "id": 6,
		  "name": "식비"
		},
    "amount": 15000,
    "description": "점심 식사",
    "memo": "회사 근처 식당"
  },
  "message": "거래 추가 성공"
}
```

### Status

| status | response content           |
| ------ | -------------------------- |
| 201    | 추가 성공                  |
| 400    | 잘못된 요청 (필수 값 누락) |
| 401    | 인증 토큰 없음             |
| 404    | 카테고리를 찾을 수 없음    |

5. 거래 수정하기
   **엔드포인트**

PATCH http://localhost:8080/api/records/1

**Headers**

userId: 1
Content-Type: application/json

**Path Parameter**

| **파라미터명** | 설명    | 타입 | 필수 | 예시 |
| -------------- | ------- | ---- | ---- | ---- |
| recordId       | 거래 ID | Long | O    | 1    |

### Request Body

| key         | 설명        | value 타입 | 옵션           | Nullable | 예시         |
| ----------- | ----------- | ---------- | -------------- | -------- | ------------ |
| type        | 거래 유형   | String     | INCOME/EXPENSE | O        | "EXPENSE”    |
| categoryId  | 카테고리 ID | Long       | -              | O        | 6            |
| date        | 거래 날짜   | String     | YYYY-MM-DD     | O        | "2026-05-25” |
| amount      | 금액        | Long       | -              | O        | 20000        |
| description | 내용        | String     | -              | O        | "저녁 식사”  |
| memo        | 메모        | String     | -              | O        | "회식”       |

### Response

| key         | 설명             | value 타입 | 옵션           | Nullable | 예시             |
| ----------- | ---------------- | ---------- | -------------- | -------- | ---------------- |
| success     | 성공 여부        | Boolean    | -              | X        | true             |
| data        | 수정된 거래 정보 | Object     | -              | X        | {}               |
| id          | 거래 ID          | Long       | -              | X        | 1                |
| type        | 거래 유형        | String     | INCOME/EXPENSE | X        | "EXPENSE”        |
| date        | 거래 날짜        | String     | YYYY-MM-DD     | X        | "2026-05-25”     |
| category    | 카테고리 이름    | String     | -              | X        | "식비”           |
| amount      | 금액             | Long       | -              | X        | 20000            |
| description | 내용             | String     | -              | O        | O"저녁 식사”     |
| memo        | 메모             | String     | -              | O        | "회식”           |
| message     | 응답 메시지      | String     | -              | X        | "거래 수정 성공” |

**Request Example**

```jsx
{
  "type": "EXPENSE",
  "categoryId": 6,
  "date": "2026-05-25",
  "amount": 20000,
  "description": "저녁 식사",
  "memo": "회식"
}
```

**Response Example**

```jsx
{
  "success": true,
  "data": {
    "id": 1,
    "type": "EXPENSE",
    "date": "2026-05-25",
    "category": {
		  "id": 6,
		  "name": "식비"
		}
    "amount": 20000,
    "description": "저녁 식사",
    "memo": "회식"
  },
  "message": "거래 수정 성공"
}
```

### Status

| status | response content           |
| ------ | -------------------------- |
| 201    | 수정 성공                  |
| 400    | 잘못된 요청 (필수 값 누락) |
| 401    | 인증 토큰 없음             |
| 403    | 본인 거래가 아님           |

6. 거래 삭제하기
   **엔드포인트**

DELETE http://localhost:8080/api/records/1

**Headers**

userId: 1

**Path Parameter**

| **파라미터명** | 설명    | 타입 | 필수 | 예시 |
| -------------- | ------- | ---- | ---- | ---- |
| recordId       | 거래 ID | Long | O    | 1    |

### Request Body

| key | 설명 | value 타입 | 옵션 | Nullable | 예시 |
| --- | ---- | ---------- | ---- | -------- | ---- |
|     |      |            |      |          |      |

### Response

| key     | 설명           | value 타입 | 옵션 | Nullable | 예시             |
| ------- | -------------- | ---------- | ---- | -------- | ---------------- |
| success | 성공 여부      | Boolean    | -    | X        | true             |
| data    | 삭제된 거래 ID | Long       | -    | X        | 1                |
| message | 응답 메시지    | String     | -    | X        | "거래 삭제 성공” |

**Request Example**

```jsx
DELETE / api / records / 1;
```

**Response Example**

```jsx
{
  "success": true,
  "data": 1,
  "message": "거래 삭제 성공"
}
```

### Status

| status | response content    |
| ------ | ------------------- |
| 200    | 삭제 성공           |
| 401    | 인증 토큰 없음      |
| 403    | 본인 거래가 아님    |
| 404    | 거래를 찾을 수 없음 |

7. 월별 요약 통계 조회
   **엔드포인트**

GET [http://localhost:8080/api/records/summary?month=2026-04](http://localhost:8080/api/statistics/summary?month=2026-04)

**Headers**

userId: 1

**Path Parameter**

## **화면 사용 위치**

| **화면 요소**          | **사용하는 응답 필드** |
| ---------------------- | ---------------------- |
| 전체요약 - 총수입      | totalIncome            |
| 전체요약 - 총지출      | totalExpense           |
| 전체요약 - 순수익      | netIncome              |
| 전체요약 - 저축률      | savingRate             |
| 전체요약 - 거래건수    | transactionCount       |
| 전체요약 - 일평균 지출 | avgDailyExpense        |
| 수입상세               | incomeDetail           |
| 지출상세               | expenseDetail          |
| 우측 상단 월 선택      | month Query Parameter  |

### Request Body

| key   | 설명      | value 타입 | 옵션  | Nullable | 예시      |
| ----- | --------- | ---------- | ----- | -------- | --------- |
| month | 조회할 월 | String     | Query | X        | “2026-04” |

### Response

| key                  | 설명                     | value 타입 | 옵션 | Nullable | 예시                       |
| -------------------- | ------------------------ | ---------- | ---- | -------- | -------------------------- |
| success              | 성공 여부                | Boolean    | -    | X        | true                       |
| data                 | 응답 데이터              | Object     | -    | X        | {}                         |
| month                | 조회 월                  | String     | -    | X        | “2026-04”                  |
| totalIncome          | 총수입                   | Long       | -    | X        | 3000000                    |
| totalExpense         | 총지출                   | Long       | -    | X        | 850000                     |
| netIncome            | 순수익                   | Long       | -    | X        | 2150000                    |
| savingRate           | 저축률                   | Number     | -    | X        | 72                         |
| transactionCount     | 거래건수                 | Number     | -    | X        | 24                         |
| avgDailyExpense      | 일평균 지출              | Long       | -    | X        | 28333                      |
| monthlyBudget        | 월 지출 목표 예산        | Long       | -    | X        | 1500000                    |
| budgetUsedPercent    | 예산 대비 지출률         | Number     | -    | X        | 57                         |
| recommendDailyBudget | 남은 기간 하루 권장 지출 | Long       | -    | X        | 21667                      |
| incomeDetail         | 수입상세 목록            | Array      | -    | X        | []                         |
| expenseDetail        | 지출상세 목록            | Array      | -    | X        | []                         |
| message              | 응답 메시지              | String     | -    | X        | “월별 요약 통계 조회 성공” |

### incomeDetail / expenseDetail

| key      | 설명                     | value타입 | 옵션 | Nullable | 예시   |
| -------- | ------------------------ | --------- | ---- | -------- | ------ |
| category | 카테고리명               | String    | -    | x        | “식비” |
| amount   | 해당 카테고리 총액       | Long      | -    | X        | 30000  |
| ratio    | 해당 타입 총액 대비 비율 | Number    | -    | X        | 35     |

**Request Example**

**Response Example**

```jsx
{
  "success": true,
  "data": {
    "month": "2026-04",
    "totalIncome": 3000000,
    "totalExpense": 850000,
    "netIncome": 2150000,
    "savingRate": 72,
    "transactionCount": 24,
    "avgDailyExpense": 28333,
    "monthlyBudget": 1500000,
    "budgetUsedPercent": 57,
    "recommendDailyBudget": 21667,
    "incomeDetail": [
      {
        "category": {
				  "id": 6,
				  "name": "월급"
				},
        "amount": 3000000,
        "ratio": 100,
      }
    ],
    "expenseDetail": [
      {
        "category": {
				  "id": 6,
				  "name": "식비"
				},
        "amount": 300000,
        "ratio": 35,
      },
      {
        "category": {
				  "id": 6,
				  "name": "교통비"
				},
        "amount": 120000,
        "ratio": 14,
      }
    ]
  },
  "message": "월별 요약 통계 조회 성공"
}
```

### Status

| status | response content         |
| ------ | ------------------------ |
| 200    | 월별 요약 통계 조회 성공 |
| 400    | 올바르지 않은 월 형식    |
| 401    | 인증되지 않은 사용자     |
| 500    | 서버 오류                |

8. 일별 요약 통계 조회
   **엔드포인트**

GET http://localhost:8080/api/records/summary/daily?month=2026-04

**Headers**

userId: 1

**Path Parameter**

## **화면 사용 위치**

| **화면 요소**     | **사용하는 응답 필드** |
| ----------------- | ---------------------- |
| 일별 날짜         | date                   |
| 일별 수입         | income                 |
| 일별 지출         | expense                |
| 돼지 레벨         | pigLevel               |
| 우측 상단 월 선택 | month Query Parameter  |

### Request Body

| key   | 설명      | value 타입 | 옵션  | Nullable | 예시      |
| ----- | --------- | ---------- | ----- | -------- | --------- |
| month | 조회할 월 | String     | Query | X        | “2026-04” |

### Response

| **key**  | **설명**         | **value 타입** | **옵션** | **Nullable** | **예시**              |
| -------- | ---------------- | -------------- | -------- | ------------ | --------------------- |
| success  | 성공 여부        | Boolean        | -        | X            | true                  |
| data     | 일별 통계 목록   | Array          | -        | X            | []                    |
| date     | 날짜             | String         | -        | X            | "2026-04-07"          |
| income   | 해당 날짜 총수입 | Long           | -        | X            | 0                     |
| expense  | 해당 날짜 총지출 | Long           | -        | X            | 16500                 |
| pigLevel | 돼지 레벨        | Integer        | -        | X            | 1                     |
| message  | 응답 메시지      | String         | -        | X            | "일별 통계 조회 성공" |

**Request Example**

**Response Example**

```jsx
{
  "success": true,
  "data": [
    {
      "date": "2026-04-09",
      "income": 0,
      "expense": 1025000,
      "pigLevel": 1
    },
    {
      "date": "2026-04-07",
      "income": 0,
      "expense": 16500,
      "pigLevel": 7
    }
  ],
  "message": "일별 통계 조회 성공"
}
```

### Status

| **status** | **response content**  |
| ---------- | --------------------- |
| 200        | 일별 통계 조회 성공   |
| 400        | 올바르지 않은 월 형식 |
| 401        | 인증되지 않은 사용자  |
| 500        | 서버 오류             |

9. 월간 결산 조회
   **엔드포인트**

GET http://localhost:8080/api/records/closing?month=2026-04

**Headers**

userId: 1

**Path Parameter**

## **화면 사용 위치**

| **화면 요소**     | **사용하는 응답 필드** |
| ----------------- | ---------------------- |
| 월평균 돼지 상태  | avgPigState            |
| 예산 대비 사용률  | avgExpenseRatio        |
| 총수입            | monthIncome            |
| 총지출            | monthExpense           |
| 순수익            | netIncome              |
| 현재 집 정보      | currentHouseLevel      |
| 예상 집 정보      | nextHouseLevel         |
| 카테고리별 지출   | categoryExpenses       |
| 우측 상단 월 선택 | month Query Parameter  |

### Request Body

| key   | 설명      | value 타입 | 옵션  | Nullable | 예시      |
| ----- | --------- | ---------- | ----- | -------- | --------- |
| month | 조회할 월 | String     | Query | X        | “2026-04” |

### Response

| **key**           | **설명**             | **value 타입** | **옵션** | **Nullable** | **예시**                   |
| ----------------- | -------------------- | -------------- | -------- | ------------ | -------------------------- |
| success           | 성공 여부            | Boolean        | -        | X            | true                       |
| data              | 응답 데이터          | Object         | -        | X            | {}                         |
| month             | 조회 월              | String         | -        | X            | "2026-04"                  |
| monthIncome       | 월 총수입            | Long           | -        | X            | 3000000                    |
| monthExpense      | 월 총지출            | Long           | -        | X            | 850000                     |
| netIncome         | 순수익               | Long           | -        | X            | 2150000                    |
| monthlyBudget     | 월 지출 목표 예산    | Long           | -        | X            | 1200000                    |
| avgExpenseRatio   | 예산 대비 지출률     | Number         | -        | X            | 71                         |
| avgPigLevel       | 월평균 돼지 상태     | Integer        | -        | X            | 1                          |
| currentHouseLevel | 현재 집 레벨         | Integer        | -        | X            | 3                          |
| nextHouseLevel    | 다음 집 레벨         | Integer        | -        | X            | 4                          |
| categoryExpenses  | 카테고리별 지출 목록 | Array          | -        | X            | []                         |
| message           | 응답 메시지          | String         | -        | X            | "월간 결산 통계 조회 성공" |

## **categoryExpenses**

| **key**  | **설명**                | **value 타입** | **옵션** | **Nullable** | **예시** |
| -------- | ----------------------- | -------------- | -------- | ------------ | -------- |
| category | 카테고리명              | String         | -        | X            | "식비"   |
| amount   | 해당 카테고리 지출 총액 | Long           | -        | X            | 300000   |
| ratio    | 총지출 대비 비율        | Number         | -        | X            | 35       |

**Request Example**

**Response Example**

```jsx
{
  "success": true,
  "data": {
    "month": "2026-04",
    "monthIncome": 3000000,
    "monthExpense": 850000,
    "netIncome": 2150000,
    "monthlyBudget": 1200000,
    "avgExpenseRatio": 71,
    "avgPigLevel": 7,
    "currentHouseLevel": 3,
    "nextHouseLevel": 4,
    "categoryExpenses": [
      {
        "category": {
				  "id": 6,
				  "name": "식비"
				},
        "amount": 300000,
        "ratio": 35,
      },
      {
        "category": {
				  "id": 6,
				  "name": "교통비"
				}
        "amount": 120000,
        "ratio": 14,
      }
    ]
  },
  "message": "월간 결산 통계 조회 성공"
}
```

### Status

| **status** | **response content**     |
| ---------- | ------------------------ |
| 200        | 월간 결산 통계 조회 성공 |
| 400        | 올바르지 않은 월 형식    |
| 401        | 인증되지 않은 사용자     |
| 500        | 서버 오류                |

10. 로그인 하기
    **엔드포인트**

POST http://localhost:8080/api/auth/login

### Request

| key          | 설명            | value 타입 | 옵션 | Nullable | 예시       |
| ------------ | --------------- | ---------- | ---- | -------- | ---------- |
| userId       | 로그인 아이디   | String     |      | X        | “userid01” |
| userPassword | 로그인 비밀번호 | String     |      | X        | “userpw01” |

**Query parameter**

### Response

| key     | 설명             | value 타입 | 옵션 | Nullable | 예시          |
| ------- | ---------------- | ---------- | ---- | -------- | ------------- |
| success | 로그인 성공 여부 | boolean    |      | X        | true          |
| message | 응답 메시지      | string     |      | X        | “로그인 성공” |
| data    | 로그인 데이터    | object     |      | O        |               |

**Example**

**Request Body**

```json
{
  "userId": "String",
  "userPassword": "String"
}
```

**Response Body**

로그인 성공(200 OK)

```json
{
  "success": true,
  "message": "로그인 성공",
  "data": {
    "id": 1,
    "name": "홍길동",
    "age": 23,
    "monthlyIncome": 2000000,
    "targetExpenseRatio": 80,
    "currentPigLevel": 3,
    "currentHouseLevel": 5,
    "currentCharacterLevel": 3
  }
}
```

로그인 실패(401 Unauthorized)

```json
{
  "success": false,
  "message": "아이디 또는 비밀번호가 일치하지 않습니다.",
  "data": null
}
```

### Status

| status | response content |
| ------ | ---------------- |
| 200    | 로그인 성공      |
| 401    | 로그인 실패      |

11. 회원가입 하기
    **엔드포인트**

POST http://localhost:8080/api/auth/signup

### Request

| key          | 설명            | value 타입 | 옵션 | Nullable | 예시       |
| ------------ | --------------- | ---------- | ---- | -------- | ---------- |
| userId       | 사용할 아이디   | string     |      | X        | “userId01” |
| userPassword | 사용할 비밀번호 | string     |      | X        | “userPw01” |
| name         | 사용할 닉네임   | string     |      | X        | “홍길동”   |

**Query parameter**

### Response

| key     | 설명               | value 타입 | 옵션 | Nullable | 예시                         |
| ------- | ------------------ | ---------- | ---- | -------- | ---------------------------- |
| success | 회원가입 성공 여부 | boolean    |      | X        | true                         |
| message | 응답 메시지        | string     |      | X        | “회원가입이 완료되었습니다.: |
| data    | 응답 데이터        | object     |      | X        |                              |
|         |                    |            |      |          |                              |

**Example**

**Request Body**

```jsx
{
	"userId": "userId01",
	"userPassword": "userPw01",
	"name": "홍길동"
}
```

**Response Body**

회원가입 성공(201 Created)

```json
{
  "success": true,
  "message": "회원가입이 완료되었습니다.",
  "data": {
    "id": 4
  }
}
```

회원가입 실패(409 Conflict)

```json
{
  "success": false,
  "message": "이미 사용 중인 아이디입니다.",
  "data": null
}
```

### Status

| status | response content |
| ------ | ---------------- |
| 201    | 회원가입 성공    |
| 409    | 회원가입 실패    |

12. 농장 목록 조회
    **엔드포인트**

GET [http://localhost:8080/api/](http://localhost:8080/api/rooms/1)farms

**Headers**

userId: 1

**Query Parameter**

### Request Body

### Response

| key         | 설명                 | value 타입 | 옵션          | Nullable | 예시         |
| ----------- | -------------------- | ---------- | ------------- | -------- | ------------ |
| success     | 성공 여부            | Boolean    | -             | X        | true         |
| farmId      | 농장ID               | Long       | -             | X        | {}           |
| farmName    | 농장 이름            | String     | -             | X        | "기본 농장”  |
| memberCount | 멤버수               | Integer    | derived field | X        | 5            |
| joined      | 현재 사용자 가입여부 | Boolean    | -             | X        | true         |
| createdAt   | 농장 생성 일시       | String     | -             | X        | "2026-05-26” |

**Request Example**

**Response Example**

```jsx
{
  "success": true,
  "data": [
    {
      "farmId": 1,
      "farmName": "기본 농장",
      "memberCount": 5,
      "joined": true,
      "createdAt": "2026-05-26T09:00:00"
    },
    {
      "farmId": 2,
      "farmName": "절약 농장",
      "memberCount": 2,
      "joined": false,
      "createdAt": "2026-05-26T10:00:00"
    }
  ]
}
```

### Status

| status | response content |
| ------ | ---------------- |
| 200    | 조회 성공        |
| 401    | 인증 토큰 없음   |
| 500    | 서버 내부 오류   |

13. 농장 생성
    **엔드포인트**

POST [http://localhost:8080/api/](http://localhost:8080/api/records)farms

**Headers**

| **Header**  | **값**           | **필수** |
| ----------- | ---------------- | -------- |
| contentType | application/json | O        |
| userId      |                  | O        |

**Path Parameter**

### Request

| key  | 설명      | value 타입 | 옵션 | Nullable | 예시        |
| ---- | --------- | ---------- | ---- | -------- | ----------- |
| name | 농장 이름 | String     | 필수 | X        | "절약 농장" |

**Query parameter**

### Response

| key            | 설명                  | value 타입 | 옵션 | Nullable | 예시                  |
| -------------- | --------------------- | ---------- | ---- | -------- | --------------------- |
| success        | 성공 여부             | Boolean    | -    | X        | true                  |
| data.farmId    | 생성된 농장 ID        | Long       | -    | X        | 12                    |
| data.farmName  | 생성된 농장 이름      | String     | -    | X        | "절약 농장"           |
| data.joined    | 생성자 자동 가입 여부 | Boolean    | -    | X        | true                  |
| data.createdAt | 생성일시              | String     | -    | X        | "2026-05-26T13:20:00" |

**Example**

**Request Example**

```jsx
{
  "name": "절약 농장"
}
```

**Response Example**

```jsx
{
  "success": true,
  "data": {
    "farmId": 12,
    "farmName": "절약 농장",
    "joined": true,
    "createdAt": "2026-05-26T13:20:00"
  }
}
```

### Status

| status | response content              |
| ------ | ----------------------------- |
| 201    | 생성 성공                     |
| 400    | 농장 이름 누락 또는 형식 오류 |
| 401    | 인증 토큰 없음                |
| 409    | 중복된 농장 이름              |
| 500    | 서버 내부 오류                |

14. 농장 상세 조회
    **엔드포인트**

GET [http://localhost:8080/api/](http://localhost:8080/api/records)farms/1

**Headers**

userId: 1

**Path Parameter**

| **파라미터명** | 설명    | 타입 | 필수 | 예시 |
| -------------- | ------- | ---- | ---- | ---- |
| farmId         | 농장 ID | Long | O    | 1    |

### Request

| key | 설명 | value 타입 | 옵션 | Nullable | 예시 |
| --- | ---- | ---------- | ---- | -------- | ---- |
|     |      |            |      |          |      |
|     |      |            |      |          |      |

**Query parameter**

### Response

| key                         | 설명                  | value 타입 | 옵션          | Nullable | 예시        |
| --------------------------- | --------------------- | ---------- | ------------- | -------- | ----------- |
| success                     | 성공 여부             | Boolean    | -             | X        | true        |
| farmId                      | 농장 ID               | Long       | -             | X        | 1           |
| farmName                    | 농장 이름             | String     | -             | X        | "기본 농장" |
| memberCount                 | 농장 멤버 수          | Integer    | derived field | X        | 3           |
| joined                      | 현재 사용자 가입 여부 | Boolean    | -             | X        | true        |
| members[].userId            | 사용자 PK             | Long       | -             | X        | 7           |
| members[].name              | 사용자 이름           | String     | -             | X        | "유현"      |
| members[].currentPigLevel   | 현재 돼지 레벨        | Integer    | -             | X        | 5           |
| members[].currentHouseLevel | 현재 집 레벨          | Integer    | -             | X        | 3           |

**Example**

**Request Example**

**Response Example**

```jsx
{
  "success": true,
  "data": {
    "farmId": 1,
    "farmName": "기본 농장",
    "memberCount": 3,
    "joined": true,
    "members": [
      {
        "userId": 7,
        "name": "유현",
        "currentPigLevel": 5,
        "currentHouseLevel": 3,
        "joinedAt": "2026-05-26T09:10:00"
      },
      {
        "userId": 8,
        "name": "아영",
        "currentPigLevel": 4,
        "currentHouseLevel": 2,
        "joinedAt": "2026-05-26T09:30:00"
      }
    ]
  }
}
```

### Status

| status | response content   |
| ------ | ------------------ |
| 200    | 조회 성공          |
| 401    | 인증 토큰 없음     |
| 403    | 농장 멤버가 아님   |
| 404    | 존재하지 않는 농장 |
| 500    | 서버 내부 오류     |

15. 농장 가입
    **엔드포인트**

POST [http://localhost:8080/api/](http://localhost:8080/api/records)farms/12

**Headers**

userId: 1

**Path Parameter**

| **파라미터명** | 설명           | 타입 | 필수 | 예시 |
| -------------- | -------------- | ---- | ---- | ---- |
| farmId         | 가입할 농장 ID | Long | O    | 12   |

### Request

**Query parameter**

### Response

| key           | 설명             | value 타입 | 옵션 | Nullable | 예시                  |
| ------------- | ---------------- | ---------- | ---- | -------- | --------------------- |
| success       | 성공 여부        | Boolean    | -    | X        | true                  |
| data.farmId   | 가입된 농장 ID   | Long       | -    | X        | 12                    |
| data.userId   | 가입한 사용자 ID | Long       | -    | X        | 7                     |
| data.joinedAt | 가입 일시        | String     | -    | X        | "2026-05-26T13:25:00" |

**Example**

**Request Example**

```jsx
{
  POST / api / farm / 12;
}
```

**Response Example**

```jsx
{
  "success": true,
  "data": {
    "farmId": 12,
    "userId": 7,
    "joinedAt": "2026-05-26T13:25:00"
  }
}
```

### Status

| status | response content   |
| ------ | ------------------ |
| 201    | 가입 성공          |
| 401    | 인증 토큰 없음     |
| 404    | 존재하지 않는 농장 |
| 409    | 이미 가입한 농장   |
| 500    | 서버 내부 오류     |

16. 농장 탈퇴
    **엔드포인트**

DELETE http://localhost:8080/api/farms/12

**Headers**

userId: 1

**Path Parameter**

| **파라미터명** | 설명           | 타입 | 필수 | 예시 |
| -------------- | -------------- | ---- | ---- | ---- |
| farmId         | 탈퇴할 농장 ID | Long | O    | 12   |

### Request

| key | 설명 | value 타입 | 옵션 | Nullable | 예시 |
| --- | ---- | ---------- | ---- | -------- | ---- |
|     |      |            |      |          |      |
|     |      |            |      |          |      |

처리 규칙

- farmMembers에서 현재 사용자 row 삭제
- 탈퇴 후 해당 농장의 남은 멤버 수를 다시 확인합니다.
- 남은 멤버 수가 0이면 농장 row도 자동 삭제합니다.

### Response

| key         | 설명             | value 타입 | 옵션 | Nullable | 예시 |
| ----------- | ---------------- | ---------- | ---- | -------- | ---- |
| success     | 성공 여부        | Boolean    | -    | X        | true |
| data.farmId | 탈퇴한 농장 ID   | Long       | -    | X        | 12   |
| data.userId | 탈퇴한 사용자 ID | Long       | -    | X        | 7    |

**Example**

**Request Example**

```jsx

```

**Response Example**

```jsx
{
  "success": true,
  "data": {
    "farmId": 12,
    "userId": 7
  }
}
```

### Status

| status | response content                       |
| ------ | -------------------------------------- |
| 200    | 탈퇴 성공                              |
| 401    | 인증 토큰 없음                         |
| 404    | 존재하지 않는 농장 또는 가입 정보 없음 |
| 500    | 서버 내부 오류                         |
