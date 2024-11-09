# 우테코 프리코스 4주차 편의점 미션

### 코드리뷰 피드백
- List 자료구조에 불변성을 보장하기 위해 List.of()나 List.copyOf()를 사용한다.
- 스택 메모리 제한 문제가 있기 때문에 재귀함수로 기능을 구현하기보다는 반복문을 사용하자.
- View 에 도메인 로직을 넣지 말자.(출력할 내용 포함)

### 공통 피드백 중 해당하는 부분
- 기능을 구현하면서 지속적으로 readme 업데이트할 것
- 상수 > 클래스 변수 > 인스턴스 변수 > 생성자 > 메소드 구현 순서 지키기
- TDD 사용해서 구현해볼 것
- 함수의 길이를 10라인으로 제한한다.(공백 포함)
- 예외 처리를 상세히 한다.
- 연관성있는 상수는 enum 으로 묶어준다
- 객체의 필드를 줄이기 위해 노력한다.
- 객체에서 getter 를 사용하기보다는 스스로 처리할 수 있도록 구조를 변경해야 한다.
- 테스트에서 반환값은 같고 파라미터 값만 변경되는 경우 @ParameterizedTest 를 사용하자
- 단위 테스트하기 어려운 코드는 클래스 외부로 분리하자.
- private 메소드를 테스트하고 싶은 경우에는 클래스 분리를 고려할 필요가 있다.

### 사용해 볼 것
- 중복 검사 시 stream() 사용
- parsing 과 validation 이 함께 있다면 parsing 쪽에 두자
- 정적 팩토리 메소드, 파라미터가 많은 경우에는 빌더 메소드

### 설계 원칙
- 도메인: 생성과 검증
- 서비스: 그 외 비지니스 로직
- controller: 요청과 응답
- view: 입출력
- util: 프로젝트 전체에서 사용되는 메소드나 변수

### 사용해야하는 API
- camp.nextstep.edu.missionutils에서 제공하는 DateTimes 및 Console API를 사용하여 구현해야 한다.
    - 현재 날짜와 시간을 가져오려면 camp.nextstep.edu.missionutils.DateTimes의 now()를 활용한다.
    - 사용자가 입력하는 값은 camp.nextstep.edu.missionutils.Console의 readLine()을 활용한다.

### 편의점 미션 구현할 기능 정리
- 입력
    - [x] `src/main/resources/products.md`과 `src/main/resources/promotions.md` 파일을 파일 입출력을 통하여 상품 목록과 행사 목록을 불러온다
      - [x] 파일 입출력
      - [x] 파일 입출력 예외처리
      - [x] 상품 목록과 행사 목록으로 parsing 하여 repository 에 저장
    - [ ] 구매할 상품과 수량을 입력받는다
      - [ ] 입력
    > ex) [콜라-10],[사이다-3]
    - [ ] 프로모션 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부 입력(Y or N)
    > ex) Y
    - [ ] 프로모션 재고가 부족하여 일부 상품에 프로모션 혜택 없는 경우, 해당 수량에 대해 정가로 결제할지(Y) 제외할지(N) 입력
    > ex) N
    - [ ] 멤버십 할인 적용 여부 입력(Y or N)
    > ex) Y
    - [ ] 추가 구매 여부 입력
    > ex) N
- 재고관리
    - [ ] 재고 수량을 토대로 결제 가능 여부 확인
    - [ ] 상품 구매 시 구매 수량만큼 해당 상품의 재고에서 차감(메소드 원자성 고려해야)
- 프로모션 할인
    - [ ] 오늘 날짜가 프로모션 기간 내에 포함된 경우에만 할인 적용
    - [ ] 프로모션이 겹칠 경우 할인율 높은 순으로 우선 적용
- 프로모션 재고
    - [ ] 프로모션 재고를 우선적으로 차감 
        - [ ] 프로모션 재고가 부족할 시 일반 재고 사용
    - [ ] 프로모션 해당 수량보다 적게 가져온 경우 필요한 수량을 추가로 가져오면 혜택을 받을 수 있음을 안내
    - [ ] 프로모션 재고가 부족 시 프로모션 혜택 불가
        - [ ] 이 경우 일부 수량에 대해 정가로 결제하게 됨을 안내
- 멤버십 할인
    - [ ] 프로모션 적용 후 남은 금액에 대해서 할인 적용
    - [ ] 회원은 프로모션 미적용 금액의 30% 할인
    - [ ] 멤버십 할인 최대 한도는 8,000원
- 영수증 출력
    - [ ] 구매 상품 내역: 구매한 상품명, 수량, 가격
    - [ ] 증정 상품 내역: 프로모션에 따라 무료로 증정된 상품 목록
    - [ ] 금액 정보
        - [ ] 총 구매액: 구매한 상품의 총 수량과 총 금액. 상품별 가격과 수량을 곱하여 계산
        - [ ] 행사할인: 프로모션에 의한 할인 금액. 총 구매액에서 차감
        - [ ] 멤버십할인: 멤버십에 의한 할인 금액. 총 구매액에서 차감
        - [ ] 내실돈: 최종 결제 금액
- 출력
    - [x] 환영 인사 출력
    > 안녕하세요. W편의점입니다.
    - [x] 보유 상품 안내 출력
    > 현재 보유하고 있는 상품입니다.
    - [x] 상품명, 가격, 프로모션, 이름, 재고 안내 출력(재고 0일 시 `재고 없음` 출력)
    ```
    - 콜라 1,000원 10개 탄산2+1
    - 콜라 1,000원 10개
    - 사이다 1,000원 8개 탄산2+1
    - 사이다 1,000원 7개
    - 오렌지주스 1,800원 9개 MD추천상품
    - 오렌지주스 1,800원 재고 없음
    - 탄산수 1,200원 5개 탄산2+1
    - 탄산수 1,200원 재고 없음
    - 물 500원 10개
    - 비타민워터 1,500원 6개
    - 감자칩 1,500원 5개 반짝할인
    - 감자칩 1,500원 5개
    - 초코바 1,200원 5개 MD추천상품
    - 초코바 1,200원 5개
    - 에너지바 2,000원 5개
    - 정식도시락 6,400원 8개
    - 컵라면 1,700원 1개 MD추천상품
    - 컵라면 1,700원 10개
    ```
    - [x] 상품명과 수량 입력 안내 출력
    > 구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
    - [ ] 프로모션 수량만큼 가져오지 않았을 경우, 혜택에 대한 안내 메시지 출력
    > 현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
    - [ ] 프로모션 재고가 부족하여 일부 수량 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지(Y) 제외할지(N) 대한 안내 메시지를 출력
    > 현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
    - [ ] 멤버십 할인 적용 여부 입력 안내 문구 출력
    > 멤버십 할인을 받으시겠습니까? (Y/N)
    - [ ] 구매 상품 내역, 증정 상품 내역, 금액 정보 출력
    ```
  ===========W 편의점=============
    상품명		수량	금액
    콜라		3 	3,000
    에너지바 		5 	10,000
    ===========증	정=============
    콜라		1
    ==============================
    총구매액		8	13,000
    행사할인			-1,000
    멤버십할인			-3,000
    내실돈			 9,000
  ```
  - [ ] 추가 구매 여부 확인 안내 문구 출력
  > 감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)

### 예외 정리
- 관리자가 잘못된 형식의 상품 파일을 등록 시 `[ERROR] 상품 목록 파일의 형식이 잘못되었습니다.`로 시작하는 에러 메세지 출력
  - [x] 이미 같은 이름의 상품이 존재합니다.
  - [x] 이름은 한글 형식으로 적어주세요.
  - [x] 1,000,000,000원 미만의 가격을 적어주세요.
  - [x] 가격은 100원 단위로 적어주세요.
  - [x] 1,000개 미만으로 등록해주세요.
  - [x] 해당하는 프로모션이 없습니다.
  - [x] name,price,quantity,promotion 형식으로 된 파일을 등록해주세요. (예: 탄산수,1200,5,탄산2+1)
- 관리자가 잘못된 형식의 프로모션 파일을 등록 시 `[ERROR] 프로모션 목록 파일의 형식이 잘못되었습니다.`로 시작하는 에러 메세지 출력
  - [x] 이미 같은 이름의 행사가 존재합니다.
  - [x] name,buy,get,start_date,end_date 형식으로 된 파일을 등록해주세요. (예: 탄산2+1,2,1,2024-01-01,2024-12-31)
  - [x] '{상품명}2+1', '{상품명}1+1', 'MD추천상품', '반짝할인'과 같은 행사명을 등록해주세요.
  - [x] buy와 get을 올바르게 등록해주세요.
- 사용자가 잘못된 값 입력 시 `[ERROR] 올바르지 않은 입력값입니다.`로 시작하는 에러 메세지 출력
  - [x] 예시와 같이 입력해주세요. (예: [사이다-2],[감자칩-1])
  - [x] Y 또는 N을 입력해주세요.
  - [x] 등록되지 않은 상품명입니다.
  - [x] 최대 입력치는 100 입니다.
  - [x] 재고보다 많은 숫자를 입력했습니다.

### 실행 결과 예시
```
안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 10개 탄산2+1
- 콜라 1,000원 10개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 5개
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[콜라-3],[에너지바-5]

멤버십 할인을 받으시겠습니까? (Y/N)
Y

===========W 편의점=============
상품명		수량	금액
콜라		3 	3,000
에너지바 		5 	10,000
===========증	정=============
콜라		1
==============================
총구매액		8	13,000
행사할인			-1,000
멤버십할인			-3,000
내실돈			 9,000

감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
Y

안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 7개 탄산2+1
- 콜라 1,000원 10개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 재고 없음
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[콜라-10]

현재 콜라 4개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
Y

멤버십 할인을 받으시겠습니까? (Y/N)
N

===========W 편의점=============
상품명		수량	금액
콜라		10 	10,000
===========증	정=============
콜라		2
==============================
총구매액		10	10,000
행사할인			-2,000
멤버십할인			-0
내실돈			 8,000

감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
Y

안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 재고 없음 탄산2+1
- 콜라 1,000원 7개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 재고 없음
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[오렌지주스-1]

현재 오렌지주스은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
Y

멤버십 할인을 받으시겠습니까? (Y/N)
Y

===========W 편의점=============
상품명		수량	금액
오렌지주스		2 	3,600
===========증	정=============
오렌지주스		1
==============================
총구매액		2	3,600
행사할인			-1,800
멤버십할인			-0
내실돈			 1,800

감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
N
```
