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
import server.domain.repositories.ItemRepository;
import server.service.LogicalService;

import java.util.List;

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

    @ApiOperation(value = "Get all Items", response = ItemDTO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Items"),
    })
    @ResponseBody
    @GetMapping(produces={"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public ItemDTO getItems(@RequestParam(value = "token") String token) {
        List<Item> itemList = itemRepository.findAll();
        String returnToken = logicalService.isValidToken(token);
        return new ItemDTO(returnToken,itemList);
    }
}
