/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.filippetti.ks.api.bpm.configuration.ChatConfiguration;
import it.filippetti.ks.api.bpm.model.Chat;
import it.filippetti.ks.api.bpm.model.ChatMessage;
import it.filippetti.ks.api.bpm.model.Instance;
import it.filippetti.ks.api.bpm.repository.ChatMessageRepository;
import it.filippetti.ks.api.bpm.repository.ChatRepository;
import java.util.UUID;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 *
 * @author marco.mazzocchetti
 */
@Service
public class ChatService implements MessageListener, MessageConverter {
    
    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Autowired
    private ChatConfiguration chatConfiguration;
    
    @Autowired
    @Qualifier("internalObjectMapper")
    private ObjectMapper objectMapper;
    
    @Autowired
    private IdentityService identityService;
    
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatService() {
    }
    
    /**
     * 
     * @param senderInstance
     * @param senderUserId
     * @param text 
     */
    public void publishMessage(Instance senderInstance, String senderUserId, String text) {

        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException();
        }
        
        if (senderInstance.getChatMembership().isPresent()) {
            chatConfiguration.jmsTemplate().convertAndSend(
                new MessagePayload(
                    UUID.randomUUID().toString(),
                    senderInstance.getChatMembership().get().getChat().getId(),
                    senderInstance.getBusinessName(), 
                    senderUserId,
                    identityService
                        .getUser(
                            senderInstance.getTenant(), 
                            senderUserId)
                        .map(u -> u.getFullName())
                        .orElse(null),
                    text));
        }
    }
    
    /**
     * 
     * @param message 
     */
    @Override
    public void onMessage(Message message) {

        MessagePayload messagePayload;
        Chat chat;
        ChatMessage chatMessage;

        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException();
        }
        
        try {
            // get message payload
            messagePayload = fromMessage(message);
            
            // get chat
            chat = chatRepository.findById(messagePayload.getChatId()).orElse(null);
            if (chat == null && chatConfiguration.acceptUnknownChatMessages()) {
                chat = new Chat(messagePayload.getChatId());
            }
            if (chat != null && !chatMessageRepository.existsByUuid(messagePayload.getUuid())) {
                // create message
                chatMessage = chatMessageRepository.save(new ChatMessage(
                    messagePayload.getUuid(),
                    chat, 
                    messagePayload.getSenderInstanceName(), 
                    messagePayload.getSenderUserId(), 
                    messagePayload.getSenderDisplayName(),
                    messagePayload.getText()));
                // update chat
                chat.setLastSendTs(chatMessage.getSendTs());
                chatRepository.save(chat);
            }
        } catch (Throwable t) {
            // set rollback only
            transactionManager
                .getTransaction(new DefaultTransactionDefinition())
                .setRollbackOnly();
            // log
            if (t instanceof JMSException) {
                log.error(t.getMessage());
            } else {
                log.error(t.getMessage(), t);
            }
        }
    }
    

    @Override
    public Message toMessage(Object messagePayload, Session session)
        throws JMSException {

        try {
            return session.createTextMessage(objectMapper.writeValueAsString((MessagePayload) messagePayload));
        } catch (ClassCastException | JsonProcessingException e) {
            throw new MessageConversionException(e.getMessage());
        }
    }

    @Override
    public MessagePayload fromMessage(Message message)
        throws JMSException {

        try {
           return objectMapper.readValue(((TextMessage) message).getText(), MessagePayload.class);
        } catch (ClassCastException | JsonProcessingException e) {
            throw new MessageConversionException(e.getMessage());
        }
    }
    
    public static class MessagePayload {
        
        private String uuid;
        private String chatId;
        private String senderInstanceName;
        private String senderUserId;
        private String senderDisplayName;
        private String text;

        private MessagePayload() {
        }

        private MessagePayload(
            String uuid,                
            String chatId,     
            String senderInstanceName, 
            String senderUserId,
            String senderDisplayName,
            String text) {
            this.uuid = uuid;
            this.chatId = chatId;
            this.senderInstanceName = senderInstanceName;
            this.senderUserId = senderUserId;
            this.senderDisplayName = senderDisplayName;
            this.text = text;
        }

        public String getUuid() {
            return uuid;
        }

        public String getChatId() {
            return chatId;
        }

        public String getSenderInstanceName() {
            return senderInstanceName;
        }

        public String getSenderUserId() {
            return senderUserId;
        }

        public String getSenderDisplayName() {
            return senderDisplayName;
        }

        public String getText() {
            return text;
        }
    }
}
