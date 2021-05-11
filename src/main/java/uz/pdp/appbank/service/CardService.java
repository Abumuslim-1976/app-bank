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
        Optional<User> optionalUser = userRepository.findById(cardDto.getUserId());
        if (!optionalUser.isPresent())
            return new ApiResponse("Mijoz topilmadi", false);

        LocalDate localDate = LocalDate.now().plusYears(5);

        Card card = new Card();
        card.setSpecialCode(cardDto.getSpecialCode());
        card.setSpecialNumber(cardDto.getSpecialNumber());
        card.setBalance(cardDto.getBalance());
        card.setLocalDate(localDate);                                   // kartani amal qilish muddati
        card.setPlasticType(PlasticType.UZCARD);
        card.setBankName(BankName.NBU_BANK);
        card.setUser(optionalUser.get());
        cardRepository.save(card);
        return new ApiResponse("Karta saqlandi", true);
    }


    //    ----------- kartani muddatini uzaytirish -----------
    public ApiResponse dateProlong(UUID id) {
        Optional<Card> optionalCard = cardRepository.findById(id);
        if (!optionalCard.isPresent())
            return new ApiResponse("Karta topilmadi", false);

        Card card = optionalCard.get();
        LocalDate localDateCard = card.getLocalDate().plusYears(5);
        card.setLocalDate(localDateCard);
        cardRepository.save(card);
        return new ApiResponse("Kartani muddati uzaytirildi", true);
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
