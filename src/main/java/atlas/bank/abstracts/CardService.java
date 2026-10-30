package atlas.bank.abstracts;

import atlas.bank.DTOs.CardCreateDTO;
import atlas.bank.DTOs.CardResponseDTO;

public interface CardService {

    CardResponseDTO createCard(CardCreateDTO dto);
}