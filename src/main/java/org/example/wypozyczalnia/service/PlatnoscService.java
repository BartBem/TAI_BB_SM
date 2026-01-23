package org.example.wypozyczalnia.service;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.example.wypozyczalnia.entity.Wypozyczenie;
import org.example.wypozyczalnia.repository.WypozyczenieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PlatnoscService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    private final WypozyczenieRepository wypozyczenieRepository;

    @Autowired
    public PlatnoscService(WypozyczenieRepository wypozyczenieRepository) {
        this.wypozyczenieRepository = wypozyczenieRepository;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    public String utworzLinkPlatnosci(Long wypozyczenieId) throws Exception {
        Wypozyczenie wypozyczenie = wypozyczenieRepository.findById(wypozyczenieId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono wypożyczenia"));

        if (!"OCZEKUJE_OPLACENIA".equals(wypozyczenie.getStatus())) {
            throw new RuntimeException(
                    "To wypożyczenie nie wymaga opłacenia (status: " + wypozyczenie.getStatus() + ")");
        }

        // Kwota w groszach (np. 15.50 PLN -> 1550)
        long kwotaGrosze = wypozyczenie.getFilm().getCenaWypozyczenia().multiply(BigDecimal.valueOf(100)).longValue();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/api/platnosci/sukces?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:8080/api/platnosci/anulowano")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("pln")
                                .setUnitAmount(kwotaGrosze)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Wypożyczenie filmu: " + wypozyczenie.getFilm().getTytul())
                                        .build())
                                .build())
                        .build())
                .putMetadata("wypozyczenie_id", wypozyczenie.getWypozyczenieId().toString())
                .build();

        Session session = Session.create(params);
        return session.getUrl();
    }

    // Metoda wywoływana po powrocie użytkownika ze Stripe (sukces)
    public void potwierdzPlatnosc(String sessionId) throws Exception {
        Session session = Session.retrieve(sessionId);
        String wypozyczenieIdStr = session.getMetadata().get("wypozyczenie_id");

        if (wypozyczenieIdStr != null) {
            Long wypozyczenieId = Long.parseLong(wypozyczenieIdStr);
            Wypozyczenie wypozyczenie = wypozyczenieRepository.findById(wypozyczenieId)
                    .orElseThrow(() -> new RuntimeException("Błąd danych płatności"));

            wypozyczenie.setStatus("OPLACONE");
            wypozyczenieRepository.save(wypozyczenie);
        }
    }
}
