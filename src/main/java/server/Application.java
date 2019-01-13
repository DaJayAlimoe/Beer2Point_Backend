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
import server.domain.datatypes.TokenGenerator;
import server.domain.dtos.BookingCreateDTO;
import server.domain.entities.*;
import server.domain.repositories.*;
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

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private QrtokenRepository qrtokenRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Override
    public void run(String... args) {

        // edit here to fill Data in h2 at the start
        Item bier = new Item("Bier","Ein cooles Blondes Pils","Bier_pic");
        Item cola = new Item("Cola","Coca Cola","Cola_pic");
        Item fanta = new Item("Fanta","Fanta","Fanta_pic");
        Item wasser = new Item("Wasser","Wasser","Wasser_pic");
        Item sprite = new Item("Sprite","Sprite","Sprite_pic");
        Item breezel = new Item("Breezel","Eine leckere st.p Paulianische Brääzzel","Breezel_pic");
        itemRepository.save(bier);
        itemRepository.save(cola);
        itemRepository.save(fanta);
        itemRepository.save(wasser);
        itemRepository.save(sprite);
        itemRepository.save(breezel);

        // create Token and Employees
        for(int i = 0; i < 6 ; i++)
        {
            Qrtoken token = qrtokenRepository.save(new Qrtoken(TokenGenerator.generateToken("empl")));
            employeeRepository.save(new Employee("Employee1",token));
        }

        // create Token and Seats
        for(int i = 0; i < 21 ; i++)
        {
            Qrtoken token = qrtokenRepository.save(new Qrtoken(TokenGenerator.generateToken("seat")));
            seatRepository.save(new Seat(i, token));
        }

    }
}