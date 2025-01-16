package com.aadi.ReviewMs.review.messaging;

import com.aadi.ReviewMs.review.Review;
import com.aadi.ReviewMs.review.dto.ReviewMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReviewMessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public ReviewMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public void sendMessage (Review review) {
        ReviewMessage reviewMessage = new ReviewMessage();
        reviewMessage.setId(review.getId());
        reviewMessage.setDescription(review.getDescription());
        reviewMessage.setTitle(review.getTitle());
        reviewMessage.setCompanyId(review.getCompanyId());
        reviewMessage.setRating(review.getRating());
        rabbitTemplate.convertAndSend("companyRatingQueue",reviewMessage);

    }
}
