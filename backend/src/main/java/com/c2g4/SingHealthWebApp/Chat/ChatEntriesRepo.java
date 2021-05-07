package com.c2g4.SingHealthWebApp.Chat;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ChatEntriesRepo extends CrudRepository<ChatEntriesModel, Integer> {
    @Query("SELECT * FROM ChatEntries WHERE chat_entry_id = :chat_entry_id LIMIT 1")
    ChatEntriesModel findByChatEntryId(@Param("chat_entry_id") int chat_entry_id);
}
