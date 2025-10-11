- Explain how dependency injection works internally in Spring.
  - IOC container == Application context (Where all created objects are stored)
  - @Component, @Service, ... these types of classes will be managed by IOC container
  - Bean life cycle
    - Bean Definition Phase (scan @component, etc.. and create BeanDefinition for each)
    - Instantiation
    - Dependency Injection
    - Initialization Callbacks (bean-specific init logic, like @PostConstruct)

- How do you handle huge traffic in Spring Boot APIs? (connection pool, caching, async calls)
  - Connection Pooling: Reuses a limited number of pre-created DB connections to avoid creating a new one for every request.
  - Caching: Stores frequently accessed data in memory to reduce repeated DB or network calls.
  - Async Calls: Executes long-running tasks in separate threads so main requests don’t block. (web flux)
  - Thread Pool Tuning: Adjusts the number of threads handling requests to optimize CPU and memory usage.
  - HTTP Connection Optimization: Reuses and compresses HTTP connections to reduce latency and bandwidth.

- How does Spring Boot autoconfiguration pick the right DataSource bean?
  - DataSource = ex:HikariDataSource, a class which manages db-config and connection pool

- Explain Spring Boot starter parent and dependency management.
  - You don’t need to specify versions for Spring Boot dependencies (like spring-boot-starter-web or spring-boot-starter-data-jpa) because the parent already provides them.

- How do you debug lazy initialization exceptions in JPA/Hibernate?
  - stack trace
  - spring.jpa.show-sql=true

- Explain how @Transactional isolation levels work in banking systems. (RU, RC, RR, S)
  - READ_UNCOMMITTED : Can read uncommitted changes from other transactions (dirty reads)
  - READ_COMMITTED : Can only read committed data
  - REPEATABLE_READ : Same read twice in one transaction returns same data
  - SERIALIZABLE : Transactions execute as if sequentially

- How to implement role-based access control (RBAC) in Spring Security?
  -  authorize.requestMatchers(HttpMethod.GET, "/api/v1/offers/rule","/health", "/actuator/health/**" ...

- How to detect and fix N+1 query problem in Spring Data JPA?
  - show-sql: true
  - Use JOIN FETCH or EntityGraph in all “list with children” repo methods.

- How do you configure connection pool (HikariCP) for high-throughput services?
  - maximum-pool-size : (core count × 2) + some buffer
  - idle-timeout: 60000
