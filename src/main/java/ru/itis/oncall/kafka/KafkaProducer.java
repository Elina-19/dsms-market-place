package ru.itis.oncall.kafka;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import ru.itis.oncall.kafka.dto.ItemToCartDTO;
import ru.itis.oncall.kafka.dto.ItemVisitDTO;
import ru.itis.oncall.kafka.dto.SupportServiceRequestDTO;

import javax.enterprise.context.ApplicationScoped;

@Slf4j
@ApplicationScoped
public class KafkaProducer {

    @Channel("item-visit")
    Emitter<ItemVisitDTO> itemsVisitEmitter;

    @Channel("support-service-request")
    Emitter<SupportServiceRequestDTO> supportServiceRequestsEmitter;

    @Channel("item-to-cart")
    Emitter<ItemToCartDTO> itemToCartEmitter;

    public void sendToItemsVisit(ItemVisitDTO dto) {
        log.info("Send message to item-visit");
        itemsVisitEmitter.send(dto);
    }

    public void sendToSupportServiceRequests(SupportServiceRequestDTO dto) {
        log.info("Send message to support-service-request");
        supportServiceRequestsEmitter.send(dto);
    }

    public void sendToItemToCart(ItemToCartDTO dto) {
        log.info("Send message to item-to-cart");
        itemToCartEmitter.send(dto);
    }
}
