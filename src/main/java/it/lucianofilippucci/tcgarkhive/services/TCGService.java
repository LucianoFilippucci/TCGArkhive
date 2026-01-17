package it.lucianofilippucci.tcgarkhive.services;

import it.lucianofilippucci.tcgarkhive.entity.TCGEntity;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.TCGAlreadyExistsException;
import it.lucianofilippucci.tcgarkhive.repository.TCGRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TCGService {

    private final TCGRepository tcgRepository;

    public TCGService(TCGRepository tcgRepository) {
        this.tcgRepository = tcgRepository;
    }

    @Transactional
    public void CreateTcg(String tcgName, String tcgCode) throws TCGAlreadyExistsException {
        Optional<TCGEntity> tmp = this.tcgRepository.findByCode(tcgCode);
        if(tmp.isPresent()) throw new TCGAlreadyExistsException(tcgName);

        TCGEntity newTCG = new TCGEntity();
        newTCG.setCode(tcgCode);
        newTCG.setName(tcgName);
        this.tcgRepository.save(newTCG);
    }


}
