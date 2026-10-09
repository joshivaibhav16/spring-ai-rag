package ai.spring_rag_demo;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class EmbeddingService {

    private final VectorStore vectorStore;

    public EmbeddingService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void embeddFileData() throws IOException {
        String fileContent = Files.readString(Path.of(
                "src/main/resources/input.txt"
        ));
        TextSplitter textSplitter = TokenTextSplitter.builder().build();
        var documents = textSplitter.split(new Document(fileContent));
        vectorStore.add(documents);
    }
}
