package com.currency.exchanger.controller;

import com.currency.exchanger.models.Pair;
import com.currency.exchanger.service.PairService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/pairs")
public class PairController {
    private final PairService pairService;

    public PairController(PairService pairService) {
        this.pairService = pairService;
    }

    @GetMapping
    public List<Pair> getAllPairs() {
        return pairService.getAllPairs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pair> getPairById(@PathVariable int id) {
        return pairService.getPairById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Pair createPair(@RequestBody Pair pair) {
        return pairService.savePair(pair);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePair(@PathVariable int id) {
        pairService.deletePair(id);
        return ResponseEntity.noContent().build();
    }
}

