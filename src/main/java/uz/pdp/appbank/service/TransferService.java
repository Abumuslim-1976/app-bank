package uz.pdp.appbank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.appbank.entity.Bankomat;
import uz.pdp.appbank.entity.Card;
import uz.pdp.appbank.entity.Income;
import uz.pdp.appbank.entity.Outcome;
import uz.pdp.appbank.payload.ApiResponse;
import uz.pdp.appbank.payload.CardLoginDto;
import uz.pdp.appbank.payload.TransferDto;
import uz.pdp.appbank.repository.BankomatRepository;
import uz.pdp.appbank.repository.CardRepository;
import uz.pdp.appbank.repository.IncomeRepository;
import uz.pdp.appbank.repository.OutcomeRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransferService {

    @Autowired
    CardRepository cardRepository;
    @Autowired
    BankomatRepository bankomatRepository;
    @Autowired
    IncomeRepository incomeRepository;
    @Autowired
    OutcomeRepository outcomeRepository;


    // ----------- kartani parolini tekshirib unga token qaytaramiz -----------
//    public ApiResponse card(UUID id, CardLoginDto cardLoginDto) {
//        Optional<Card> cardOptional = cardRepository.findById(id);
//        if (!cardOptional.isPresent())
//            return new ApiResponse("Karta topilmadi", false);
//
//        Card card = cardOptional.get();
//        if (card.getSpecialCode().equals(cardLoginDto.getPassword()) && card.isActive()) {
//
//        }
//
//    }


    //    Karta 2 martadan ortiq noto`g`ri parol kiritilsa block holatiga o`tkaziladi (x => sanoq uchun)
    int x = 0;


    // ---------- client bankomatdan naqt pul chiqarishi ------------
    @Transactional
    public ApiResponse transferMoney(TransferDto transferDto) {

        //        ---------- card ni ID orqali topib oldik ------------
        Optional<Card> cardOptional = cardRepository.findById(transferDto.getCardId());
        if (!cardOptional.isPresent())
            return new ApiResponse("Karta topilmadi", false);


        //        ---------- bankomatni ni ID orqali topib oldik -----------
        Optional<Bankomat> bankomatOptional = bankomatRepository.findById(transferDto.getBankomatId());
        if (!bankomatOptional.isPresent())
            return new ApiResponse("Bankomat topilmadi", false);

        Card card = cardOptional.get();
        Bankomat bankomat = bankomatOptional.get();


        //        ------------- bankomatda kartani turi userni kartasini turi bilan to`g`ri kelishi tekshirilyapti ----------
        if (!card.getPlasticType().name().equals(bankomat.getPlasticType().name())) {
            return new ApiResponse("Plastik karta turi mos emas", false);
        }


        //        ----------- kartani muddati tekshirilyapti -----------
        LocalDate localDateNow = LocalDate.now();
        LocalDate localDateCard = card.getLocalDate();
        if (localDateNow.getYear() == localDateCard.getYear()) {
            if (localDateCard.getDayOfYear() < localDateNow.getDayOfYear()) {
                return new ApiResponse("Plastik karta muddati tugagan", false);
            }
        } else if (localDateNow.getYear() > localDateCard.getYear()) {
            return new ApiResponse("Plastik karta muddati allaqachon tugagan", false);
        }


        //        ---------- kartadan pul yechib olishdan oldin parol tekshiriladi -----------
        //        ------------- card dan pul yechilishi va bankomatga pul tushishi ------------
        if (card.getSpecialCode().equals(transferDto.getSpecialCode())) {
            if (card.isActive()) {
                // kartani holati yangilanadi (blockdan ochildi)
                x = 0;

                //client kiritgan pul miqdori tekshiriladi
                if (transferDto.getAmount() % 10_000 != 0)
                    return new ApiResponse("Bankomatdan 10 ming so`mlik kupyuradan kichik kupyura chiqarilmaydi", false);

                // client kiritgan pul miqdori
                double amount = transferDto.getAmount();
                double commission = amount * 0.01;
                double amountWithCommission = amount + commission;


                if (card.getBalance() < amountWithCommission) {
                    return new ApiResponse("Karta ichida yetarli mablag` mavjud emas", false);
                }

                if (bankomat.getMaxMoney() <= amount) {
                    return new ApiResponse("Bankomatdan katta summa yechib olish mumkin emas", false);
                }

                // clientdan pul yechib olinayapti
                double balance = card.getBalance();
                double cardOutBalance = balance - amountWithCommission;
                card.setBalance(cardOutBalance);
                cardRepository.save(card);


                // bankomatga pul tushishi
                double plasticMoney = bankomat.getPlasticMoney();
                double allPlasticMoney = plasticMoney + amountWithCommission;
                bankomat.setPlasticMoney(allPlasticMoney);


                // bankomatni ichidan clientga pul chiqarib berish
                double readyMoney = bankomat.getReadyMoney();
                double minusReadyMoney = readyMoney - amount;
                bankomat.setReadyMoney(minusReadyMoney);

                double a = Math.floor(amount / 100_000);            // 100 minglik nechtaligi
                double a1 = amount - a * 100_000;

                double b = Math.floor(a1 / 50_000);                 // 50 minglik nechtaligi
                double b1 = a1 - b * 50_000;

                double c = Math.floor(b1 / 10_000);                 // 10 minglik nechtaligi

                double oneHundredThousand = bankomat.getOneHundredThousandCount() - a;
                double fiftyThousand = bankomat.getFiftyThousandCount() - b;
                double tenThousand = bankomat.getTenThousandCount() - c;

                bankomat.setOneHundredThousandCount(oneHundredThousand);
                bankomat.setFiftyThousandCount(fiftyThousand);
                bankomat.setTenThousandCount(tenThousand);

                bankomatRepository.save(bankomat);

                //Kirim-chiqimlarni saqlab boramiz
                Income income = new Income();
                income.setAmount(amount);
                income.setFromBankomat(bankomatOptional.get());
                income.setOneHundredThousand(a);
                income.setFiftyThousand(b);
                income.setTenThousand(c);
                income.setToUser(card.getUser());
                incomeRepository.save(income);

                Outcome outcome = new Outcome();
                outcome.setAmount(amount);
                outcome.setCommission(commission);
                outcome.setToBankomat(bankomatOptional.get());
                outcome.setFromCard(cardOptional.get());
                outcomeRepository.save(outcome);

                return new ApiResponse("Pul muvoffaqiyatli o`tkazildi", true);
            }
        } else {
            x++;
            if (x >= 3) {
                card.setActive(false);
                return new ApiResponse("Karta blok holatiga tushib qoldi", false);
            }
        }
        return new ApiResponse("Karta kodi xato", false);
    }


}



