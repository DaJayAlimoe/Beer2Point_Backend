package server.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor//(access = AccessLevel.PRIVATE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonRootName(value = "item")
public class Item{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ApiModelProperty(required = true)
    @Setter(AccessLevel.NONE)
    private String name;

    @ApiModelProperty(required = true)
    @Setter(AccessLevel.NONE)
    private String description;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
