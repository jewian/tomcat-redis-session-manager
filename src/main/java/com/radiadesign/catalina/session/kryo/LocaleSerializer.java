package com.radiadesign.catalina.session.kryo;

import java.util.Locale;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class LocaleSerializer extends Serializer<Locale> {
    public void write(Kryo kryo, Output output, Locale object) {
        output.writeAscii(object.getLanguage());
        output.writeAscii(object.getCountry());
        output.writeAscii(object.getVariant());
    }

    public Locale read(Kryo kryo, Input input, Class<Locale> type) {
        return new Locale(input.readString(), input.readString(), input.readString());
    }
}
