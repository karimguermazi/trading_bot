package com.currency.exchanger.service;

import com.currency.exchanger.models.Pair;
import com.currency.exchanger.repository.PairRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PairService {
    private final PairRepository pairRepository;

    public PairService(PairRepository pairRepository) {
        this.pairRepository = pairRepository;
    }

    public List<Pair> getAllPairs() {
        return pairRepository.findAll();
    }

    public Optional<Pair> getPairById(int id) {
        return pairRepository.findById(id);
    }

    public Pair savePair(Pair pair) {
        return pairRepository.save(pair);
    }

    public void deletePair(int id) {
        pairRepository.deleteById(id);
    }
}

