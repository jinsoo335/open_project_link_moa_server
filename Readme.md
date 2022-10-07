### 동작 순서
- `Request` -> Route -> Controller -> Service/Provider -> DAO -> DB
- DB -> DAO -> Service/Provider -> Controller -> Route -> `Response`


### BaseException
`BaseException`을 통해 `Service`나 `Provider`에서 `Controller`에 Exception을 던진다. 마찬가지로 Status 값은 `BaseResponseStatus` 의 `enum`을 통해 관리한다.


### nohup
무중단 서비스를 위해 nohup을 사용한다. 자세한 내용은 환경 구축 실습 영상을 참고하자.


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



### Annotation
- Annotation은 클래스와 메소드에 추가하여 다양한 기능을 부여하는 역할
- 특별한 의미를 부여하거나 기능을 부여하는 등 다양한 역할을 수행할 수 있음

- @SpringBootApplication: SpringBoot의 시작점을 알림, 본 프로젝트에는 `DemoApplication.java` 에서 사용된다.
- @Component: 개발자가 생성한 Class를 Spring의 Bean으로 등록할 때 사용하는 Annotation
- @Controller: 해당 Class가 Controller의 역할을 한다고 명시하기 위해 사용하는 Annotation
- @RequestHeader: Request의 header값을 가져올 수 있게 해주는 Annotation
- @RequestMapping: @RequestMapping(value=”“)와 같은 형태로 작성, 요청 들어온 URI의 요청과 Annotation value 값이 일치하면 해당 클래스나 메소드가 실행된다.
- @RequestParam: URL에 전달되는 파라미터를 메소드의 인자와 매칭시켜, 파라미터를 받아서 처리할 수 있는 Annotation으로 아래와 같이 사용
- @RequestBody: Body에 전달되는 데이터를 메소드의 인자와 매칭시켜, 데이터를 받아서 처리할 수 있는 Annotation
- @ResponseBody: 메소드에서 리턴되는 값이 View 로 출력되지 않고 HTTP Response Body에 직접 쓰여지게 됨. return 시에 json, xml과 같은 데이터를 return
- @Autowired: Spring Framework에서 Bean 객체를 주입받기 위한 방법
- @GetMapping: RequestMapping(Method=RequestMethod.GET)과 똑같은 역할
- @PostMapping: RequestMapping(Method=RequestMethod.POST)과 똑같은 역할

- @Setter: Class 모든 필드의 Setter method를 생성
- @Getter: Class 모든 필드의 Getter method를 생성
- @AllArgsConstructor: Class 모든 필드 값을 파라미터로 받는 생성자를 추가