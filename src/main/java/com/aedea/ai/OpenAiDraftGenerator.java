package com.aedea.ai;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseOutputText;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class OpenAiDraftGenerator implements AiDraftGenerator {
    private static final String DEFAULT_MODEL = "gpt-5.4";

    private final OpenAIClient client;

    public OpenAiDraftGenerator() {
        this.client = OpenAIOkHttpClient.fromEnv();
    }

    @Override
    public String generateDraft(String prompt) {
        ResponseCreateParams params = ResponseCreateParams.builder()
            .model(DEFAULT_MODEL)
            .input(prompt)
            .build();
        Response response = client.responses().create(params);
        return response.output().stream()
            .flatMap(item -> item.message().stream())
            .flatMap(message -> message.content().stream())
            .flatMap(content -> content.outputText().stream())
            .map(ResponseOutputText::text)
            .collect(Collectors.joining());
    }
}
