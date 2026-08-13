package com.yao.base.framework.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yao.base.framework.common.Result;
import com.yao.base.framework.expection.ClientException;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;

/**
 * 将远程服务返回的 Result 错误响应转换为 ClientException。
 */
public class ResultFeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;
    private final ErrorDecoder defaultErrorDecoder = new ErrorDecoder.Default();

    public ResultFeignErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.body() == null) {
            return defaultErrorDecoder.decode(methodKey, response);
        }

        byte[] responseBody;
        try {
            responseBody = readResponseBody(response);
        } catch (IOException e) {
            return defaultErrorDecoder.decode(methodKey, response);
        }

        try {
            Result<?> result = objectMapper.readValue(responseBody, Result.class);
            // 只要 code 不是 0（假设 0 为成功码），就认为是下游业务异常
            if (result != null && result.getCode() != 0) {
                String msg = (result.getMessage() != null && !result.getMessage().trim().isEmpty())
                        ? result.getMessage()
                        : "远程服务调用异常 (code=" + result.getCode() + ")";
                return new ClientException(result.getCode(), msg);
            }
        } catch (IOException ignored) {
            // 非统一 Result 响应交给 Feign 默认解码器处理。
        }

        Response rebuiltResponse = response.toBuilder()
                .body(responseBody)
                .build();
        return defaultErrorDecoder.decode(methodKey, rebuiltResponse);
    }

    private byte[] readResponseBody(Response response) throws IOException {
        return feign.Util.toByteArray(response.body().asInputStream());
    }
}
