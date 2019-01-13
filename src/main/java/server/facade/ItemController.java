package server.facade;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import server.domain.dtos.ItemDTO;
import server.domain.entities.Item;
import server.domain.entities.Qrtoken;
import server.domain.repositories.ItemRepository;
import server.exceptions.TokenAuthenticationException;
import server.service.LogicalService;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/v1/Item")
@Api(value = "/v1/Item", tags = "Items")
public class ItemController {

    private final ItemRepository itemRepository;

    private LogicalService logicalService;

    @Autowired
    public ItemController(ItemRepository itemRepository, LogicalService logicalService) {
        this.itemRepository = itemRepository;
        this.logicalService = logicalService;
    }

    @ApiOperation(value = "Get all Items", response = Item.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Items"),
    })
    @GetMapping(produces={"application/json"} )
    public ItemDTO getItems(@RequestHeader(value = "token") String token) throws TokenAuthenticationException {
        logicalService.isValidToken(token);
        List<Item> itemList = itemRepository.findAll();
        return new ItemDTO(itemList);
    }
}
