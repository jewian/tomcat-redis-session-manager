package com.radiadesign.catalina.session;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import com.esotericsoftware.shaded.org.objenesis.strategy.SerializingInstantiatorStrategy;
import com.esotericsoftware.shaded.org.objenesis.strategy.StdInstantiatorStrategy;
import com.radiadesign.catalina.session.kryo.LocaleSerializer;


public class KryoSerializer implements Serializer {
    private ClassLoader classLoader;

    @Override
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public byte[] serializeFrom(final HttpSession session) throws IOException {
        final Kryo kryo = getKryo();
        final RedisSession redisSession = (RedisSession) session;        
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        final Output output = new Output(byteArrayOutputStream);
        //kryo.writeObject(output, redisSession.getCreationTime());
        kryo.writeObject(output, redisSession);
        output.close();

        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public HttpSession deserializeInto(final byte[] data, final HttpSession session) throws IOException, ClassNotFoundException {
        final Kryo kryo = getKryo();
        final RedisSession redisSession = (RedisSession) session;        
        final Input input = new Input(new ByteArrayInputStream(data));
        //redisSession.setCreationTime(kryo.readObject(input, long.class));
        final RedisSession storedSession = kryo.readObject(input, RedisSession.class);
        storedSession.setManager(redisSession.getManager());
        return storedSession;
    }
    
    private Kryo getKryo() {
        Kryo kryo = new Kryo();
        kryo.setClassLoader(classLoader);
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        kryo.register(RedisSession.class, 32);
        kryo.register(Locale.class, new LocaleSerializer(), 33);
        return kryo;
    }
}
