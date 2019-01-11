package server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import server.domain.entities.Booking;
import server.domain.entities.Employee;
import server.domain.repositories.EmployeeRepository;
import server.domain.repositories.BookingRepository;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@SpringBootApplication
@EnableSwagger2
public class Application implements HealthIndicator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public Health health() {
        return Health.up().build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("server"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "HCI-Service-Server REST API",
                "REST API for St.Pauli in cooperation with @ HAW-Hamburg",
                "API v1",
                "Terms of service",
                new Contact("Hendrik Scheve", "no-Url",
                        "hendrik.scheve@haw-hamburg.de"),
                "MIT License", "https://opensource.org/licenses/MIT", Collections.emptyList());
    }
}

// local profile for inMemory Database
@Component
@Profile("local")
class PopulateTestDataRunner implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public void run(String... args) {

//        employeeRepository.deleteAll();
//        orderRepository.deleteAll();
        // edit here to fill Data in h2 at the start
//        Employee employee = new Employee("MitarbeiterTest", "qr1234");
//        employeeRepository.save(employee);
//
//        // Order(Item itemName, int amount, String seat)
//        Booking order = new Booking(Item.BIER, 5, "A:5");
//        order.addEmployee(employee);
//        bookingRepository.save(order);

        // Set child reference(userProfile) in parent entity(user)
        //user.setUserProfile(userProfile);

        // Set parent reference(user) in child entity(userProfile)
        //userProfile.setUser(user);

        // Save Parent Reference (which will save the child as well)
        //userRepository.save(user);

    }
}