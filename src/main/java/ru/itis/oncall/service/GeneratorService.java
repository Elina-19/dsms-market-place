package ru.itis.oncall.service;

import lombok.extern.slf4j.Slf4j;
import ru.itis.oncall.dto.SignUpDTO;
import ru.itis.oncall.kafka.KafkaProducer;
import ru.itis.oncall.kafka.dto.ItemToCartDTO;
import ru.itis.oncall.kafka.dto.ItemVisitDTO;
import ru.itis.oncall.kafka.dto.SupportServiceRequestDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@ApplicationScoped
public class GeneratorService {

    private final Map<String, String> categories = Map.of(
            "1", "Clothes",
            "2", "Shoes",
            "3", "Hats",
            "4", "Cosmetic",
            "5", "Toys",
            "6", "Electronics",
            "7", "Food",
            "8", "Accessories"
    );

    private final Map<String, String> communicationSources = Map.of(
            "1", "Chat",
            "2", "Email",
            "3", "Phone"
    );

    @Inject
    KafkaProducer kafkaProducer;

    @Inject
    UserService userService;

    public void generateUsers(Integer count) {
        log.info("Start generate [{}] users", count);
        var uuid = UUID.randomUUID().toString();
        var signUpDtos = IntStream.range(0, count)
                .boxed()
                .map(i -> generateSignUp(uuid, i))
                .collect(Collectors.toList());
        userService.save(signUpDtos);
        log.info("Finished generate [{}] users", count);
    }

    public void generateVisits(Integer count) {
        log.info("Start generate [{}] item visits", count);
        IntStream.range(0, count)
                .boxed()
                .map(i -> generateItemVisitDto())
                .forEach(kafkaProducer::sendToItemsVisit);
        log.info("Finished generate [{}] item visits", count);
    }

    public void generateSupportServiceRequests(Integer count) {
        log.info("Start generate [{}] support service requests", count);
        IntStream.range(0, count)
                .boxed()
                .map(i -> generateSupportServiceRequestDto())
                .forEach(kafkaProducer::sendToSupportServiceRequests);
        log.info("Finished generate [{}] support service requests", count);
    }

    public void generateItemToCart(Integer count) {
        log.info("Start generate [{}] item to cart", count);
        IntStream.range(0, count)
                .boxed()
                .map(i -> generateItemToCartDto())
                .forEach(kafkaProducer::sendToItemToCart);
        log.info("Finished generate [{}] item to cart", count);
    }

    private SignUpDTO generateSignUp(String uuid, Integer index) {
        return SignUpDTO.builder()
                .firstName("Elina")
                .lastName("Zagidullina")
                .email(uuid + index + "@gmail.com")
                .password("qwerty007")
                .build();
    }

    private ItemVisitDTO generateItemVisitDto() {
        var categoryId = String.valueOf((int) Math.round((Math.random() * 7)) + 1);
        var fromRecommendation = (int) Math.round(Math.random());
        return ItemVisitDTO.builder()
                .createDate(LocalDateTime.now())
                .userId(UUID.randomUUID().toString())
                .itemId(UUID.randomUUID().toString())
                .categoryId(categoryId)
                .categoryName(categories.get(categoryId))
                .fromRecommendation(fromRecommendation == 1 ? true : false)
                .build();
    }

    private SupportServiceRequestDTO generateSupportServiceRequestDto() {
        var communicationSourceId = String.valueOf((int) Math.round((Math.random() * 2)) + 1);
        return SupportServiceRequestDTO.builder()
                .createDate(LocalDateTime.now())
                .userId(UUID.randomUUID().toString())
                .communicationSourceId(communicationSourceId)
                .communicationSourceName(communicationSources.get(communicationSourceId))
                .build();
    }

    private ItemToCartDTO generateItemToCartDto() {
        var fromRecommendation = (int) Math.round(Math.random());
        var isAdded = (int) Math.round(Math.random());
        return ItemToCartDTO.builder()
                .createDate(LocalDateTime.now())
                .userId(UUID.randomUUID().toString())
                .itemId(UUID.randomUUID().toString())
                .fromRecommendation(fromRecommendation == 1 ? true : false)
                .isAdded(isAdded == 1 ? true : false)
                .build();
    }
}
