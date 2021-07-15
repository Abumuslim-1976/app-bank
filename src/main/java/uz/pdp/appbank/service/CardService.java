package uz.pdp.appbank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.appbank.entity.*;
import uz.pdp.appbank.entity.enums.BankName;
import uz.pdp.appbank.entity.enums.PlasticType;
import uz.pdp.appbank.payload.ApiResponse;
import uz.pdp.appbank.payload.CardDto;
import uz.pdp.appbank.repository.BankomatRepository;
import uz.pdp.appbank.repository.CardRepository;
import uz.pdp.appbank.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class CardService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    BankomatRepository bankomatRepository;


    //    ---------- userga karta biriktirish -----------
    public ApiResponse createCardToUser(CardDto cardDto) {
        LocalDate localDate = LocalDate.now().plusYears(5);

        Card card = new Card();
        card.setSpecialCode(cardDto.getSpecialCode());
        card.setSpecialNumber(cardDto.getSpecialNumber());
        card.setBalance(cardDto.getBalance());
        card.setLocalDate(localDate);                                   // kartani amal qilish muddati
        card.setPlasticType(PlasticType.UZCARD);
        card.setBankName(BankName.NBU_BANK);
        cardRepository.save(card);
        return new ApiResponse("Karta saqlandi", true);
    }



    //    -----------kartani blockdan chiqarish -------------
    public ApiResponse unBlock(UUID id) {
        Optional<Card> optionalCard = cardRepository.findById(id);
        if (!optionalCard.isPresent())
            return new ApiResponse("Karta topilmadi", false);

        Card card = optionalCard.get();
        card.setActive(true);
        cardRepository.save(card);
        return new ApiResponse("Karta aktiv holatga keltirildi", true);
    }

}
