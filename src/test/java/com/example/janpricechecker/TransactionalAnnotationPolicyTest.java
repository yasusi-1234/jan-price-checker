package com.example.janpricechecker;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class TransactionalAnnotationPolicyTest {

    @Test
    void jakartaTransactionalMustNotBeUsed() throws IOException {
        List<Path> javaFiles;
        try (Stream<Path> stream = Files.walk(Path.of("src"))) {
            javaFiles = stream
                    .filter(path -> path.toString().endsWith(".java"))
                    .toList();
        }

        List<Path> forbiddenUsage = javaFiles.stream()
                .filter(this::containsJakartaTransactional)
                .toList();

        assertThat(forbiddenUsage)
                .withFailMessage("Use org.springframework.transaction.annotation.Transactional instead of jakarta.transaction.Transactional: %s", forbiddenUsage)
                .isEmpty();
    }

    private boolean containsJakartaTransactional(Path file) {
        try {
            String content = Files.readString(file);
            return content.contains("jakarta.transaction.Transactional")
                    || content.contains("@jakarta.transaction.Transactional");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read " + file, e);
        }
    }
}
