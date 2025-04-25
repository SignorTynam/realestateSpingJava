package com.realestate.realestate.service;

import com.realestate.realestate.model.Message;
import com.realestate.realestate.model.User;
import com.realestate.realestate.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getMessagesByUser(User user) {
        return messageRepository.findByUser(user);
    }
}