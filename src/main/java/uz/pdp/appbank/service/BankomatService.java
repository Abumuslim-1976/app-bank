package uz.pdp.appbank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uz.pdp.appbank.entity.Bankomat;
import uz.pdp.appbank.entity.enums.PlasticType;
import uz.pdp.appbank.payload.ApiResponse;
import uz.pdp.appbank.payload.BankomatDto;
import uz.pdp.appbank.repository.BankomatRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BankomatService {

    @Autowired
    BankomatRepository bankomatRepository;
    @Autowired
    JavaMailSender javaMailSender;

    double hundred = 100_000;              // 100 ming so`mlik kupyura
    double fifty = 50_000;                 // 50 ming so`mlik kupyura
    double ten = 10_000;                   // 10 ming so`mlik kupyura

    //    ------------- bankomat create qilish --------------
    public ApiResponse createBankomat(BankomatDto bankomatDto) {

        Bankomat bankomat = new Bankomat();
        bankomat.setPlasticType(PlasticType.UZCARD);
        bankomat.setMaxMoney(bankomatDto.getMaxMoney());
        bankomat.setAddress(bankomatDto.getAddress());
        bankomat.setOneHundredThousandCount(bankomatDto.getOneHundredThousandCount());
        bankomat.setFiftyThousandCount(bankomatDto.getFiftyThousandCount());
        bankomat.setTenThousandCount(bankomatDto.getTenThousandCount());

        Double sum = bankomatDto.getOneHundredThousandCount() * hundred +
                bankomatDto.getFiftyThousandCount() * fifty + bankomatDto.getTenThousandCount() * ten;

        bankomat.setReadyMoney(sum);
        bankomatRepository.save(bankomat);
        return new ApiResponse("Bankomat created", true);
    }


    //    ------------ bankomatni ichida pul kam qolganda mas`ul xodimning emailiga link jo`natish ------------
    public ApiResponse sendMessageToWorker() {
        List<Bankomat> bankomatList = bankomatRepository.findAll();
        for (Bankomat bankomat : bankomatList) {
            if (bankomat.getReadyMoney() < 12_000_000) {
                try {
                    SimpleMailMessage mailMessage = new SimpleMailMessage();
                    mailMessage.setFrom("noreply@gmail.com");
                    mailMessage.setTo("bankomat2344@gmail.com");
                    mailMessage.setSubject("Bankomatda pul kam qoldi !!!");
                    mailMessage.setText("Hurmatli bankomatga mas`ul shaxs , bankomatni pul bilan to`ldirib qo`ying");
                    javaMailSender.send(mailMessage);
                    return new ApiResponse("Bankka pul to`ldirib qo`yildi", true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return new ApiResponse("Bankomatni ichida pul miqdori yetarli", false);
        }
        return new ApiResponse("Xatolik !!!", false);
    }


    //    ---------- mas`ul xodim bankomatga pul to`ldirishi -----------
    @PreAuthorize(value = "hasRole('BANKOMAT_WORKER')")
    public ApiResponse fillMoneyToBankomat(UUID id, BankomatDto bankomatDto) {
        Optional<Bankomat> bankomatOptional = bankomatRepository.findById(id);
        if (!bankomatOptional.isPresent())
            return new ApiResponse("Bankomat not found", false);

        Bankomat bankomat = bankomatOptional.get();
        double a = bankomatDto.getOneHundredThousandCount();
        double b = bankomatDto.getFiftyThousandCount();
        double c = bankomatDto.getTenThousandCount();

        bankomat.setOneHundredThousandCount(bankomat.getOneHundredThousandCount() + a);
        bankomat.setFiftyThousandCount(bankomat.getFiftyThousandCount() + b);
        bankomat.setTenThousandCount(bankomat.getTenThousandCount() + c);

        Double sum = a * hundred + b * fifty + c * ten;

        bankomat.setReadyMoney(bankomat.getReadyMoney() + sum);
        bankomatRepository.save(bankomat);
        return new ApiResponse("Bankomat account replenished", true);
    }


}
