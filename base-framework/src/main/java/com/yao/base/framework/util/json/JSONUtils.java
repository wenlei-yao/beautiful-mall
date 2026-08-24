package com.yao.base.framework.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class JSONUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    static {
        // 1. 反序列化时忽略未知属性（避免多余字段导致解析报错）
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 2. 序列化时遇到空 Bean 不报错
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 3. 不将日期时间序列化为时间戳
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // 4. 统一时区与默认 Date 格式
        OBJECT_MAPPER.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT));

        // 5. 注册 Java 8 时间模块 (LocalDate / LocalDateTime)
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);

        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

        OBJECT_MAPPER.registerModule(javaTimeModule);
    }

    private JSONUtils() {
        // 工具类私有构造器
    }

    /**
     * 获取底层全局唯一的 ObjectMapper 单例
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    // =========================== 序列化 (Object -> String / Bytes) ===========================

    /**
     * 将对象转为 JSON 字符串
     */
    public static String toJSONString(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON serialization error", e);
        }
    }

    /**
     * 格式化输出 JSON 字符串（美化排版）
     */
    public static String toPrettyJSONString(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON serialization error", e);
        }
    }

    /**
     * 将对象转为 byte 数组
     */
    public static byte[] toBytes(Object object) {
        if (object == null) {
            return new byte[0];
        }
        try {
            return OBJECT_MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON serialization error", e);
        }
    }

    // =========================== 反序列化 (String / Bytes -> Object) ===========================

    /**
     * 反序列化为普通 Class 对象
     */
    public static <T> T parseObject(String jsonString, Class<T> clazz) {
        if (jsonString == null || jsonString.trim().isEmpty() || clazz == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonString, clazz);
        } catch (IOException e) {
            throw new RuntimeException("JSON deserialization error", e);
        }
    }

    /**
     * 反序列化支持泛型（如 List<DTO>、Map<String, DTO> 等）
     */
    public static <T> T parseObject(String jsonString, TypeReference<T> typeReference) {
        if (jsonString == null || jsonString.trim().isEmpty() || typeReference == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonString, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("JSON deserialization error", e);
        }
    }

    /**
     * 反序列化为 List
     */
    public static <T> List<T> parseList(String jsonString, Class<T> elementClass) {
        if (jsonString == null || jsonString.trim().isEmpty() || elementClass == null) {
            return Collections.emptyList();
        }
        try {
            JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, elementClass);
            return OBJECT_MAPPER.readValue(jsonString, javaType);
        } catch (IOException e) {
            throw new RuntimeException("JSON parse list error", e);
        }
    }

    /**
     * 反序列化为 Map
     */
    public static <K, V> Map<K, V> parseMap(String jsonString, Class<K> keyClass, Class<V> valueClass) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return Collections.emptyMap();
        }
        try {
            JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(Map.class, keyClass, valueClass);
            return OBJECT_MAPPER.readValue(jsonString, javaType);
        } catch (IOException e) {
            throw new RuntimeException("JSON parse map error", e);
        }
    }

    // =========================== 内存对象转换 (Object <-> Object) ===========================

    /**
     * 内存对象直接转换 (基于 TypeReference 泛型)
     */
    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
        if (fromValue == null || toValueTypeRef == null) {
            return null;
        }
        return OBJECT_MAPPER.convertValue(fromValue, toValueTypeRef);
    }

    /**
     * 内存对象直接转换 (普通 Class)
     */
    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        if (fromValue == null || toValueType == null) {
            return null;
        }
        return OBJECT_MAPPER.convertValue(fromValue, toValueType);
    }

    /**
     * 内存 List 快速深拷贝转换
     */
    public static <T> List<T> convertList(Object fromList, Class<T> elementClass) {
        if (fromList == null || elementClass == null) {
            return Collections.emptyList();
        }
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, elementClass);
        return OBJECT_MAPPER.convertValue(fromList, javaType);
    }
}