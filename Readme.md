[![Java CI with Gradle](https://github.com/jinsoo335/open_project_link_moa_server/actions/workflows/gradle.yml/badge.svg)](https://github.com/jinsoo335/open_project_link_moa_server/actions/workflows/gradle.yml)


##본 소스코드는 링크 폴더 프로젝트에 대한 서버 부분의 코드입니다.

클라이언트 부분 깃 허브 주소는 다음과 같습니다. 
https://github.com/yujimin413/open_project_link_moa_client


### 링크폴더 프로젝트 소개
나중에 다시 보려고 북마크한 웹페이지 링크들이 쌓여 다시 찾기 어려웠던 경험이 있으신가요?
친구들에게 링크를 전송하기 위해 채팅방에 하나하나 복사 붙여넣기 하거나,
공유한 링크를 다시 찾기 위해 채팅방을 끝없이 스크롤한 적이 있으신가요?

링크폴더를 사용하면 방문한 웹페이지 링크를 쉽게 저장하고 편하게 공유할 수 있습니다.

링크폴더의 두 가지 핵심 기능 '저장'과 '공유'를 소개합니다.
 
[저장]
(1) 웹 브라우저의 공유 기능으로 링크 저장
웹 브라우저를 탐색하다가 저장하고 싶은 링크가 있을 때 아이폰의 기본 공유 기능으로 링크폴더에 접근합니다.
원하는 폴더를 선택하면 링크 생성 창으로 이동합니다. 링크 주소는 자동으로 입력 되어있고, 링크 별명을 직접 설정해 링크를 저장할 수 있습니다.
링크 저장 후에는 다시 웹 브라우저 화면으로 돌아가 하던 작업을 이어갈 수 있습니다.
(2) 앱에서 직접 저장
링크폴더 앱에서 폴더를 생성해 목적에 따라 링크 모음을 분류할 수 있습니다.
원하는 폴더를 선택해 링크 주소와 링크 별명을 입력하여 링크를 저장합니다.

[공유]
(1) 친구 기능 : 앱 내부에서 링크 폴더 및 개별 링크 공유
링크폴더 유저라면 링크를 공유하기 위해 써드파티 앱을 사용하지 않아도 됩니다.
링크폴더 내부에서 친구를 맺고, 폴더/링크공유 요청 및 수락 과정을 통해 링크를 공유할 수 있습니다.
(2) 사본 전달 방식 : 전달 받은 폴더/링크를 편하게 수정
링크 모음은 장기간 수정, 추가가 필요하다기 보다는 주로 단기적으로 사용하는 성격이 강합니다.
따라서 사본 전달 방식을 채택하여 공유한 링크폴더/링크를 동시에 점유하지 않기 때문에 수정/삭제가 자유롭습니다.




### 서버 부분 소스코드 사용법...
본 소스코드에는 링크 폴더 프로젝트를 위해 필요한 api들을 개발한 내용이 있습니다.
api에 대한 자세한 명세는 API_Sheet.pdf 파일에 대략적인 설명이 있고,
api 폴더 안의 pdf들에 자세한 설명들이 있습니다.


ec2서버 ubuntu 18.04LTS OS위에 nginx를 설치하여 테스트했습니다.
데이터 베이스는 mysql을 사용했습니다.
또한 도메인을 linkfolder.shop으로 지정하여 사용했습니다.


본 소스코드를 다운받아 직접 api의 호출을 하고 싶은 경우에는 깃 허브 상의 코드를 받은 뒤에 application.yml 파일을 추가해야 합니다.


application.yml 파일에 들어가야하는 내용은 다음과 같습니다.
1. server.port  (포트 번호를 변경할 경우 필요)
2. application.name   (application name)
3. datasource.platform  (어떤 database를 사용하는지)
4. datasource.url       (database의 주소)
5. datasource.username  (database의 사용자 이름)
6. datasource.password  (database의 사용자 비밀번호)
7. datasource.driver-class-name   (jdbctemplate을 사용하기 위한 driver 정보)
8. jwt.JWT_SECRET_KEY    (jwt에 적용할 secret key 정보)
9. aws.credentials.accessKey  (s3를 사용할 경우 필요)
10. aws.credentials.secretKey 
11. aws.region.static
12. aws.s3.bucket
13. aws.s3.dir





### 밑으로는 본 소스코드들에 대한 설명 및 코드를 작성하면서 조사했던 부분들입니다.
<hr>

### 동작 순서
- `Request` -> Controller -> Service/Provider -> DAO -> DB
- DB -> DAO -> Service/Provider -> Controller -> `Response`
- Controller에 라우터 기능 있음 -> `@Controller`사용: 웹 요청과 응답을 처리함
- Service GET을 제외한 나머지 RESTAPI 요청처리 -> `@Service` 사용: 내부에서 자바 로직을 처리함 
- Provider 는 GET 처리 ( Get이 빈도가 높음)  -> `@Service` 사용: 내부에서 자바 로직을 처리함
- DAO는 service와 Provider 둘다 호출함.  -> `@Repository` 사용: DB나 파일같은 외부 I/O 작업을 처리함


### 이미지 처리...
  `업로드`
  - Multipart로 이미지 객체를 전달 받는다.
  - 전달 받은 이미지에 대한 고유 아이디를 생성해서 이미지와 함께 S3에 보낸다.
  - 해당 고유 아이디를 사용자 table에 저장(mysql DB)
  
  `다운로드`
  - 사용자 번호를 받아 DB에서 url 정보를 가져온다.
  - url 정보를 바탕으로 s3에서 이미지를 가져온다.
  - 가져온 이미지를 Multipart로 전달한다.


### BaseException
`BaseException`을 통해 `Service`나 `Provider`에서 `Controller`에 Exception을 던진다. 마찬가지로 Status 값은 `BaseResponseStatus` 의 `enum`을 통해 관리한다.


### nohup
무중단 서비스를 위해 nohup을 사용한다.


### Spring Bean
- Spring IoC 컨테이너가 관리하는 자바 객체를 빈(Bean)이라 부름
- Spring에 의하여 생성되고 관리되는 자바 객체를 Bean이라 한다.
- 자바 어노테이션을 사용해 스프링 빈을 스프링 ioc 컨테이너에 등록할 수 있다.


### gradle 
- 빌드 도구... 프로젝트의 의존성 주입에 사용된다.
- implementation : 의존 라이브러리 수정시 본 모듈까지만 재빌드한다.
본 모듈을 의존하는 모듈은 해당 라이브러리의 api 를 사용할 수 없음
- annotationProcessor: annotation processor 명시
- compileOnly: compile 시에만 빌드하고 빌드 결과물에는 포함하지 않는다.
- runtimeOnly: runtime 시에만 필요한 라이브러리인 경우
- testImplementation: 테스트 코드를 수행할 때만 적용.


### Lombok
- Lombok은 코드를 크게 줄여주어 가독성을 크게 높힐 수 있는 라이브러리
- setter, getter, AllArgsConstructor 등이 포함


### JDBC
- Spring Boot 에서는 jdbc연동을 위한 starter를 제공
- 자바에서 DB 프로그래밍을 하기 위해 사용되는 API

- **Jdbctemplate**
  - SQL쿼리들을 실행함
  - 구문과 저장된 프로시저 호출을 수정함
  - ResultSet 인스턴스에 대한 반복을 실행함
  - 반환된 매개변수 값을 추출함
  - JDBC 예외를 잡아 더 일반적이고 유익하게 변환함
  - ※ DataSource는 항상 스프링 컨테이너에서 빈으로 구성해야 함.


### WebSecurityConfig
- Spring security에서는 기본적으로 csrf protection을 제공한다.
- CSRF protection은 spring security에서 default로 설정된다. 
- 즉, protection을 통해 GET요청을 제외한 상태를 변화시킬 수 있는 POST, PUT, DELETE 요청으로부터 보호한다.
- 때문에, Get을 제외한 다른 요청을 받기 위해서는 csrf().disable() 처리가 필요하다.
- rest api에서 client는 권한이 필요한 요청을 하기 위해서는 요청에 필요한 인증 정보를(OAuth2, jwt토큰 등)을 포함시켜야 한다. 
- 따라서 서버에 인증정보를 저장하지 않기 때문에 굳이 불필요한 csrf 코드들을 작성할 필요가 없다.
- -> 회원용 API의 개발 등에서는 jwt를 사용하는 것을 염두해 두자.


### Annotation
- Annotation은 클래스와 메소드에 추가하여 다양한 기능을 부여하는 역할
- 특별한 의미를 부여하거나 기능을 부여하는 등 다양한 역할을 수행할 수 있음

- `@SpringBootApplication`: SpringBoot의 시작점을 알림, 본 프로젝트에는 `DemoApplication.java` 에서 사용된다.
- `@Component`: 개발자가 생성한 Class를 Spring의 Bean으로 등록할 때 사용하는 Annotation
- `@Controller`: 해당 Class가 Controller의 역할을 한다고 명시하기 위해 사용하는 Annotation
- `@RequestHeader`: Request의 header값을 가져올 수 있게 해주는 Annotation
- `@RequestMapping`: @RequestMapping(value=”“)와 같은 형태로 작성, 요청 들어온 URI의 요청과 Annotation value 값이 일치하면 해당 클래스나 메소드가 실행된다.
  - ex) 만약 , @RequestMapping("/users") 라면, 들어온 url의 /~부분과 같으면 실행.
- `@RestController` : controller 역할 + ResponseBody(반환값을 JSON으로 반환한다.)
- `@RequestParam`: URL에 전달되는 파라미터를 메소드의 인자와 매칭시켜, 파라미터를 받아서 처리할 수 있는 Annotation으로 아래와 같이 사용
- `@RequestBody`: Body에 전달되는 데이터를 메소드의 인자와 매칭시켜, 데이터를 받아서 처리할 수 있는 Annotation
- `@ResponseBody`: 메소드에서 리턴되는 값이 View 로 출력되지 않고 HTTP Response Body에 직접 쓰여지게 됨. return 시에 json, xml과 같은 데이터를 return
- `@Autowired`: Spring Framework에서 Bean 객체를 주입받기 위한 방법
- `@GetMapping`: RequestMapping(Method=RequestMethod.GET)과 똑같은 역할
- `@PostMapping`: RequestMapping(Method=RequestMethod.POST)과 똑같은 역할

- `@Setter`: Class 모든 필드의 Setter method를 생성
- `@Getter`: Class 모든 필드의 Getter method를 생성
- `@AllArgsConstructor`: Class 모든 필드 값을 파라미터로 받는 생성자를 추가

### JWT
- jwt 최초 빌드시 idx, secret key, 만료시간, jwt 알고리즘, 생성시간이 들어감.
- 다시 서버에서 헤더에 담아 jwt 를 보내면, secret key를 통해서 이를 decode하여 idx를 뽑아낼 수 있다.

### 포스트맨 테스트 하기.. 
- (intelij에 있는) 터미널에서  ./gradlew clean build
- 성공 시 위위 방향 키 눌러서  

### yml과 시크릿 키 위치
- 시크릿 키 : config 아래
- yml : src/main/resources/application.yml

### terminal 나가는 것
- ctrl+c

### query string, path variable
- query string : 말 그대로 string을 주로 넣음
- path variable: 보통 정수값을 넣음.
