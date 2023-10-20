package com.htec.microservices.elastic.query.service.api;

import com.htec.microservices.elastic.query.service.business.ElasticQueryService;
import com.htec.microservices.elastic.query.service.model.ElasticQueryServiceRequestModel;
import com.htec.microservices.elastic.query.service.model.ElasticQueryServiceResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/documents")
@Slf4j
public class ElasticDocumentController {

    private final ElasticQueryService elasticQueryService;

    ElasticDocumentController(ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }

    @GetMapping
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments() {
        List<ElasticQueryServiceResponseModel> response = elasticQueryService.getAllDocuments();
        log.info("Elasticsearch returned {} of documents", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<Object> getAllDocuments(@PathVariable String id) {
        ElasticQueryServiceResponseModel responseModel = elasticQueryService.getDocumentById(id);
        log.info("Elasticsearch returned document with id {}", id);
        return ResponseEntity.ok(responseModel);
    }

    @PostMapping("/text")
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>>
                    getAllDocumentsByText(@RequestBody ElasticQueryServiceRequestModel requestModel) {
        List<ElasticQueryServiceResponseModel> response = elasticQueryService.getDocumentByText(requestModel.getText());
        log.info("Elasticsearch returned {} of documents", response.size());
        return ResponseEntity.ok(response);
    }
}
