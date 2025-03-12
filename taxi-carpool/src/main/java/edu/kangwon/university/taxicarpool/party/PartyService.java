package edu.kangwon.university.taxicarpool.party;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartyService {

    private final PartyRepository partyRepository;
    private final PartyMapper partyMapper;

    @Autowired
    PartyService(PartyRepository partyRepository,
        PartyMapper partyMapper) {
        this.partyRepository = partyRepository;
        this.partyMapper = partyMapper;
    }

    public Optional<PartyDTO> getParty(Long partyId) {
        Optional<PartyEntity> partyEntity = partyRepository.findById(partyId);
        return partyEntity.map(partyMapper::convertToDTO);
    }

    public List<PartyDTO> getPartyList() {
        List<PartyEntity> partyEntities = partyRepository.findAll();
        return partyMapper.convertToDTOList(partyEntities);
    }

//    public PartyDTO createParty() {
//
//    }
//
//    public PartyDTO updateParty() {
//
//    }
//
//    public PartyDTO deleteParty() {
//
//    }


}
