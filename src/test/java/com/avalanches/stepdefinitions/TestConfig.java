package com.avalanches.stepdefinitions;

import com.avalanches.frameworksanddrivers.databases.interfaces.BancoDeDadosContextoInterface;
import io.lettuce.core.api.sync.RedisCommands;
import okhttp3.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class TestConfig {

    @Bean
    public OkHttpClient mockHttpClient() {
        OkHttpClient mockHttpClient = mock(OkHttpClient.class);
        Call mockCall = mock(Call.class);
        Response mockResponse = new Response.Builder()
                .request(new Request.Builder().url("https://teste:5002/").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(ResponseBody.create("{}", MediaType.get("application/json; charset=utf-8")))
                .build();

        try {
            when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);
            when(mockCall.execute()).thenReturn(mockResponse);
        } catch (IOException e) {
            throw new RuntimeException("Error mocking HttpClient", e);
        }

        return mockHttpClient;
    }


    @Bean
    public BancoDeDadosContextoInterface mockBancoDeDadosContexto() {
        BancoDeDadosContextoInterface mockContext = mock(BancoDeDadosContextoInterface.class);
        RedisCommands<String, String> mockRedisCommands = mock(RedisCommands.class);
        when(mockContext.getRedisCommands()).thenReturn(mockRedisCommands);
        return mockContext;
    }
}
