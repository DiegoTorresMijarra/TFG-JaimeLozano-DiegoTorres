package es.jaimelozanodiegotorres.backapp.rest.status.controller;

import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.products.controller.ProductController;
import es.jaimelozanodiegotorres.backapp.rest.products.filters.ProductFiltersDto;
import es.jaimelozanodiegotorres.backapp.rest.products.models.Product;
import es.jaimelozanodiegotorres.backapp.rest.products.services.ProductServicePgSqlImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("app-status")
@Slf4j
public class StatusController {

    private final ProductController productController;

    public StatusController(ProductController productController) {
        this.productController = productController;
    }

    @GetMapping("/badge-status")
    public ResponseEntity<Map<String, Object>> getBadgeStatus() {
        log.info("Obteniendo Estado de la app");

        Map<String, Object> response = new HashMap<>();
        response.put("schemaVersion", 1);
        response.put("label", "Status");

        try {
            ResponseEntity<PageResponse<Product>> externalResponse = productController.pageAll(new ProductFiltersDto());

            if (externalResponse.getStatusCode() == HttpStatus.OK) {
                response.put("message", "success");
                response.put("color", "brightgreen");
            } else {
                response.put("message", "waiting");
                response.put("color", "orange");
            }
        } catch (Exception e) {
            if (e instanceof HttpStatusCodeException httpEx) {
                if (httpEx.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                    response.put("message", "paused");
                    response.put("color", "yellow");
                } else {
                    response.put("message", "unknown");
                    response.put("color", "lightgrey");
                }
            } else {
                response.put("message", "down");
                response.put("color", "lightgrey");
            }
        }

        return ResponseEntity.ok(response);
    }
}