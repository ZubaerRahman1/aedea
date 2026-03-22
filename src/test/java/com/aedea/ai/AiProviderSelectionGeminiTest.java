package com.aedea.ai;

import com.aedea.AedeaApplication;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AedeaApplication.class, properties = "app.ai.provider=gemini")
class AiProviderSelectionGeminiTest {
    @Autowired
    private ApplicationContext context;

    @Test
    void loadsGeminiGeneratorWhenProviderIsGemini() {
        Map<String, AiDraftGenerator> beans = context.getBeansOfType(AiDraftGenerator.class);
        assertThat(beans).hasSize(1);
        assertThat(beans.values().iterator().next()).isInstanceOf(GeminiDraftGenerator.class);
    }
}
