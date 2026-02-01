package in.bm.GatewayService.CONTROLLER;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class GatewayFallbackController {

    @GetMapping("/default")
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Map<String, Object> fallback() {
        return Map.of(
                "error", "SERVICE_UNAVAILABLE"
                , "message", "Service temporarily unavailable"
                , "timestamp", Instant.now().toString()
        );
    }


}
