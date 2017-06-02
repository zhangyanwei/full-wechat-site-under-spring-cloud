package com.askdog.model.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.nio.charset.Charset;

@Lazy
@Component
public class SerializableValueRedisTemplate<V extends Serializable> extends AbstractRedisTemplate<Object, V> {

    @Autowired
    public SerializableValueRedisTemplate(RedisConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    void initDefaultKeySerializers() {
        RedisSerializer<Object> jdkSerializer = new JdkSerializationRedisSerializer();
        RedisSerializer customStringRedisSerializer = new CustomStringRedisSerializer();
        setKeySerializer(customStringRedisSerializer);
        setHashKeySerializer(customStringRedisSerializer);
        setValueSerializer(jdkSerializer);
        setHashValueSerializer(jdkSerializer);
    }

    public class CustomStringRedisSerializer implements RedisSerializer<Object> {

        private final Charset charset;

        public CustomStringRedisSerializer() {
            this(Charset.forName("UTF8"));
        }

        public CustomStringRedisSerializer(Charset charset) {
            Assert.notNull(charset);
            this.charset = charset;
        }

        @Override
        public Object deserialize(byte[] bytes) throws SerializationException {
            return (bytes == null ? null : new String(bytes, charset));
        }

        @Override
        public byte[] serialize(Object object) {
            String string = String.valueOf(object);
            return (string == null ? null : string.getBytes(charset));
        }
    }
}
