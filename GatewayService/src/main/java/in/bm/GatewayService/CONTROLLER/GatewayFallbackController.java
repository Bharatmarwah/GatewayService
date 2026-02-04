package in.bm.GatewayService.CONTROLLER;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class GatewayFallbackController {

    @RequestMapping(
            value = "/default",
            method = {
                    RequestMethod.GET,
                    RequestMethod.POST,
                    RequestMethod.PUT,
                    RequestMethod.DELETE
            }
    )
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Map<String, Object> fallback() {
        return Map.of(
                "error", "SERVICE_UNAVAILABLE"
                , "message", "Service temporarily unavailable"
                , "timestamp", Instant.now().toString()
        );
    }


}
