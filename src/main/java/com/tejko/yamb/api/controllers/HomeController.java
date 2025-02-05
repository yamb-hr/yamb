package com.tejko.yamb.api.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.sun.management.OperatingSystemMXBean;
import com.tejko.yamb.api.GlobalExceptionHandler;
import com.tejko.yamb.util.ResponseTimeAspect;

@Controller
public class HomeController {

    private final Environment environment;
    private final JdbcTemplate jdbcTemplate;
    private final MongoTemplate mongoTemplate;
    private final RestTemplate restTemplate;
    private final ResponseTimeAspect responseTimeAspect;
    private final GlobalExceptionHandler globalExceptionHandler;
    
    @Value("${recaptcha.secret.key}")
    private String recaptchaSecretKey;

    @Value("${recaptcha.api.url}")
    private String recaptchaApiUrl;
    
    @Value("${app.version}")
    private String appVersion;

    @Autowired
    public HomeController(Environment environment, JdbcTemplate jdbcTemplate, 
                          MongoTemplate mongoTemplate, RestTemplate restTemplate, 
                          ResponseTimeAspect responseTimeAspect, GlobalExceptionHandler globalExceptionHandler) {
        this.environment = environment;
        this.jdbcTemplate = jdbcTemplate;
        this.mongoTemplate = mongoTemplate;
        this.restTemplate = restTemplate;
        this.responseTimeAspect = responseTimeAspect;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @RequestMapping(value = { "/", "/{x:[\\w\\-]+}", "/{x:^(?!api$).*$}/**/{y:[\\w\\-]+}" })
    public String forward() {
        return "/index.html";
    }

    @GetMapping("/api")
    public ResponseEntity<RepresentationModel<?>> getApiRoot() {
        RepresentationModel<?> rootResource = new RepresentationModel<>();

        rootResource.add(linkTo(methodOn(HomeController.class).getHealthCheck()).withRel("health"));
        rootResource.add(linkTo(methodOn(HomeController.class).getMetrics()).withRel("metrics"));
        rootResource.add(linkTo(methodOn(AuthController.class).registerGuest(null, null)).withRel("register-guest"));
        rootResource.add(linkTo(methodOn(AuthController.class).register(null)).withRel("register"));
        rootResource.add(linkTo(methodOn(AuthController.class).getToken(null, null)).withRel("token"));
        rootResource.add(linkTo(methodOn(GameController.class).getOrCreate(null)).withRel("get-or-create-game"));
        rootResource.add(linkTo(methodOn(GameController.class).getAll(Pageable.unpaged())).withRel("games"));
        rootResource.add(linkTo(methodOn(PlayerController.class).getAll(Pageable.unpaged())).withRel("players"));
        rootResource.add(linkTo(methodOn(ScoreController.class).getAll(Pageable.unpaged())).withRel("scores"));

        return ResponseEntity.ok(rootResource);
    }

    @GetMapping("/api/health")
    public ResponseEntity<Map<String, String>> getHealthCheck() {        
        
        Map<String, String> response = new HashMap<>();
        response.put("uptime", ManagementFactory.getRuntimeMXBean().getUptime() + "ms");
        response.put("version", appVersion);
        response.put("postgres", checkPostgreSQLConnection() ? "connected" : "disconnected");
        response.put("mongo", checkMongoDBConnection() ? "connected" : "disconnected");
        response.put("recaptchaAPI", checkRecaptchaAPI() ? "reachable" : "unreachable");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/metrics")
	@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Number>> getMetrics() {

        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        
        long totalRequests = responseTimeAspect.getRequestCount();
        long errorCount = globalExceptionHandler.getErrorCount();
        double averageResponseTime = responseTimeAspect.getAverageResponseTime();
        double errorRate = (totalRequests == 0) ? 0 : ((double) errorCount / totalRequests);

        Map<String, Number> response = new HashMap<>();
        response.put("memoryUsage", osBean.getFreePhysicalMemorySize());
        response.put("cpuUsage", osBean.getSystemCpuLoad());
        response.put("diskSpace", osBean.getFreeSwapSpaceSize());
        response.put("uptime", ManagementFactory.getRuntimeMXBean().getUptime());
        response.put("averageResponseTime", averageResponseTime);
        response.put("requestsProcessed", totalRequests);
        response.put("errorCount", errorCount);
        response.put("errorRate", errorRate);

        return ResponseEntity.ok(response);
    }


    private boolean checkPostgreSQLConnection() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            return connection.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean checkMongoDBConnection() {
        try {
            return mongoTemplate.getDb().getName() != null;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkRecaptchaAPI() {
        String apiUrl = environment.getProperty(recaptchaSecretKey, recaptchaApiUrl);
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }

}