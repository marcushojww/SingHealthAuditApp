package com.c2g4.SingHealthWebApp.Chat;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepo extends CrudRepository<ChatModel, Integer> {
    @Query("SELECT * FROM Chat WHERE chat_id = :chat_id LIMIT 1")
    ChatModel findByChatId(@Param("chat_id") int chat_id);

    @Query("SELECT * FROM Chat WHERE tenant_id = :tenant_id")
    List<ChatModel> findChatsByTenantId(@Param("tenant_id") int tenant_id);

    @Query("SELECT * FROM Chat WHERE auditor_id = :auditor_id")
    List<ChatModel> findChatsByTAuditorId(@Param("auditor_id") int auditor_id);

    @Query("SELECT * FROM Chat WHERE auditor_id = :auditor_id AND tenant_id =:tenant_id LIMIT 1")
    ChatModel findChatByUsers(@Param("auditor_id") int auditor_id, @Param("tenant_id") int tenant_id);

    @Modifying
    @Query("UPDATE Chat c SET c.messages = :messages WHERE c.chat_id = :chat_id")
    void updateMessagesByChatId(@Param("chat_id") int acc_id, @Param("messages") String messages);
}
