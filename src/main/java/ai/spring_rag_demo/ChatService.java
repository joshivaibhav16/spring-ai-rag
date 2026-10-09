package ai.spring_rag_demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    public ChatService(VectorStore vectorStore, ChatClient.Builder chatClientBuilder) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder.build();
    }

    public String prompt(String query) {
       List<Document> documents = vectorStore.similaritySearch(SearchRequest.builder().query(query).build());
       String context = documents.stream().map(Document::getFormattedContent).collect(Collectors.joining("\n"));
       String prompt = """
               Context %s
               Question %s
               """.formatted(context, query);
       return chatClient.prompt(prompt).call().content();
    }
}
