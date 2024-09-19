package com.tejko.yamb.api.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.sun.management.OperatingSystemMXBean;
import com.tejko.yamb.exceptions.GlobalExceptionHandler;
import com.tejko.yamb.util.ResponseTimeAspect;

@Controller
public class HomeController {

    private final Environment environment;
    private final JdbcTemplate jdbcTemplate;
    private final MongoTemplate mongoTemplate;
    private final RestTemplate restTemplate;
    private final ResponseTimeAspect responseTimeAspect;
    private final GlobalExceptionHandler globalExceptionHandler;
    
    @Value("${RECAPTCHA_SECRET_KEY}")
    private String recaptchaSecretKey;

    @Value("${recaptcha.api.url}")
    private String recaptchaApiUrl;
    
    @Value("${app.version}")
    private String appVersion;

    private LongAdder requestsProcessed = new LongAdder();

    @Autowired
    public HomeController(Environment environment, JdbcTemplate jdbcTemplate, MongoTemplate mongoTemplate, RestTemplate restTemplate, ResponseTimeAspect responseTimeAspect, GlobalExceptionHandler globalExceptionHandler) {
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

        rootResource.add(linkTo(methodOn(AuthController.class).registerGuest(null)).withRel("guest"));
        rootResource.add(linkTo(methodOn(AuthController.class).register(null)).withRel("register"));
        rootResource.add(linkTo(methodOn(AuthController.class).getToken(null)).withRel("token"));
        rootResource.add(linkTo(methodOn(GameController.class).getOrCreate()).withRel("get-or-create-game"));
        rootResource.add(linkTo(methodOn(GameController.class).getAll(null)).withRel("games"));
        rootResource.add(linkTo(methodOn(PlayerController.class).getAll(null)).withRel("players"));
        rootResource.add(linkTo(methodOn(ScoreController.class).getAll(null)).withRel("scores"));

        return ResponseEntity.ok(rootResource);
    }

    @GetMapping("/api/version")
    public ResponseEntity<Map<String, String>> getVersionInfo() {
        Map<String, String> versionInfo = new HashMap<>();
        versionInfo.put("version", appVersion);
        return ResponseEntity.ok(versionInfo);
    }

    @GetMapping("/api/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "healthy");
        response.put("uptime", ManagementFactory.getRuntimeMXBean().getUptime() + "ms");
        response.put("version", appVersion);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/status")
    public ResponseEntity<Map<String, String>> statusCheck() {
        Map<String, String> status = new HashMap<>();
        status.put("postgres", checkPostgreSQLConnection() ? "connected" : "disconnected");
        status.put("mongo", checkMongoDBConnection() ? "connected" : "disconnected");
        status.put("recaptchaAPI", checkRecaptchaAPI() ? "reachable" : "unreachable");
        status.put("version", appVersion);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/api/system-info")
    public ResponseEntity<Map<String, String>> getSystemInfo() {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        Map<String, String> systemInfo = new HashMap<>();
        systemInfo.put("memoryUsage", osBean.getFreePhysicalMemorySize() + " bytes free");
        systemInfo.put("cpuUsage", osBean.getSystemCpuLoad() * 100 + "%");
        systemInfo.put("diskSpace", osBean.getFreeSwapSpaceSize() + " bytes free");
        return ResponseEntity.ok(systemInfo);
    }

    @GetMapping("/api/metrics")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        requestsProcessed.increment();

        long totalRequests = requestsProcessed.sum();
        long errorCount = globalExceptionHandler.getErrorCount();
        double averageResponseTime = responseTimeAspect.getAverageResponseTime();
        double errorRate = (totalRequests == 0) ? 0 : ((double) errorCount / totalRequests) * 100;

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("uptime", ManagementFactory.getRuntimeMXBean().getUptime() + "ms");
        metrics.put("requestsProcessed", totalRequests);
        metrics.put("averageResponseTime", averageResponseTime + "ms");
        metrics.put("errorRate", errorRate + "%");

        return ResponseEntity.ok(metrics);
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