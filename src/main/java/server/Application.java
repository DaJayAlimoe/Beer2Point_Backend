package server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
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

    private final Log log = LogFactory.getLog(getClass());

    @Override
    public void run(String... args) {

        // edit here to fill Data in h2 at the start
        Item bier = new Item("Bier","Ein cooles Blondes Pils");
        Item cola = new Item("Cola","Coca Cola");
        Item fanta = new Item("Fanta","Fanta");
        Item wasser = new Item("Wasser","Wasser");
        Item sprite = new Item("Sprite","Sprite");
        Item breezel = new Item("Breezel","Eine leckere st.p Paulianische Brääzzel");
        itemRepository.save(bier);
        itemRepository.save(cola);
        itemRepository.save(fanta);
        itemRepository.save(wasser);
        itemRepository.save(sprite);
        itemRepository.save(breezel);

        // create Token and Employees
//        for(int i = 0; i < 6 ; i++)
//        {
            // TokenGenerator.generateToken("empl")
        Qrtoken token1 = qrtokenRepository.save(new Qrtoken("empl:7d3248f91c93cfa"));
        Qrtoken token2 = qrtokenRepository.save(new Qrtoken("empl:5b12d7270585b999"));
        Qrtoken token3 = qrtokenRepository.save(new Qrtoken("empl:33c842d5fc68b17e"));
        Qrtoken token4 = qrtokenRepository.save(new Qrtoken("empl:59d3baa7c0f0aa3"));
        Qrtoken token5 = qrtokenRepository.save(new Qrtoken("empl:2d6daf315cf8cc63"));
        Qrtoken token6 = qrtokenRepository.save(new Qrtoken("empl:4c2f6419544aaa86"));
        employeeRepository.save(new Employee("Employee1",token1));
        employeeRepository.save(new Employee("Employee2",token2));
        employeeRepository.save(new Employee("Employee3",token3));
        employeeRepository.save(new Employee("Employee4",token4));
        employeeRepository.save(new Employee("Employee5",token5));
        employeeRepository.save(new Employee("Employee6",token6));
//        }

        // create Token and Seats
        // TokenGenerator.generateToken("seat")
        Qrtoken stoken0 = qrtokenRepository.save(new Qrtoken("seat:4842f5964017ee57"));
        Qrtoken stoken1 = qrtokenRepository.save(new Qrtoken("seat:998e0d4351dcecc"));
        Qrtoken stoken2 = qrtokenRepository.save(new Qrtoken("seat:2b2fbcb116406b75"));
        Qrtoken stoken3 = qrtokenRepository.save(new Qrtoken("seat:4c2586da593c62ce"));
        Qrtoken stoken4 = qrtokenRepository.save(new Qrtoken("seat:539ac94fbfcf287e"));
        Qrtoken stoken5 = qrtokenRepository.save(new Qrtoken("seat:18c736f1ad13229b"));
        Qrtoken stoken6 = qrtokenRepository.save(new Qrtoken("seat:4b02a31d8d4ea072"));
        Qrtoken stoken7 = qrtokenRepository.save(new Qrtoken("seat:1aeea2c206f21a54"));
        Qrtoken stoken8 = qrtokenRepository.save(new Qrtoken("seat:2d4d9c2648ed7bef"));
        Qrtoken stoken9 = qrtokenRepository.save(new Qrtoken("seat:384256f39c4bf6f7"));
        Qrtoken stoken10 = qrtokenRepository.save(new Qrtoken("seat:6cf1730c7efee5ec"));
        Qrtoken stoken11 = qrtokenRepository.save(new Qrtoken("seat:a9279194e6c8476"));
        Qrtoken stoken12 = qrtokenRepository.save(new Qrtoken("seat:1d41ca17a3d784ba"));
        Qrtoken stoken13 = qrtokenRepository.save(new Qrtoken("seat:5a7d5a78529eaebd"));
        Qrtoken stoken14 = qrtokenRepository.save(new Qrtoken("seat:3c885a5b73ec80da"));

        Qrtoken stoken15 = qrtokenRepository.save(new Qrtoken("seat:88e274d02fb4dfa"));
        Qrtoken stoken16 = qrtokenRepository.save(new Qrtoken("seat:3313777464d7393a"));
        Qrtoken stoken17 = qrtokenRepository.save(new Qrtoken("seat:2172020438e8eaba"));
        Qrtoken stoken18 = qrtokenRepository.save(new Qrtoken("seat:73b7071d212319d4"));
        Qrtoken stoken19 = qrtokenRepository.save(new Qrtoken("seat:76b7ef2044e08f62"));
        seatRepository.save(new Seat(0, stoken0));
        seatRepository.save(new Seat(1, stoken1));
        seatRepository.save(new Seat(2, stoken2));
        seatRepository.save(new Seat(3, stoken3));
        seatRepository.save(new Seat(4, stoken4));
        seatRepository.save(new Seat(5, stoken5));
        seatRepository.save(new Seat(6, stoken6));
        seatRepository.save(new Seat(7, stoken7));
        seatRepository.save(new Seat(8, stoken8));
        seatRepository.save(new Seat(9, stoken9));
        seatRepository.save(new Seat(10, stoken10));
        seatRepository.save(new Seat(11, stoken11));
        seatRepository.save(new Seat(12, stoken12));
        seatRepository.save(new Seat(13, stoken13));
        seatRepository.save(new Seat(14, stoken14));
        seatRepository.save(new Seat(15, stoken15));
        seatRepository.save(new Seat(16, stoken16));
        seatRepository.save(new Seat(17, stoken17));
        seatRepository.save(new Seat(18, stoken18));
        seatRepository.save(new Seat(19, stoken19));

    }
}