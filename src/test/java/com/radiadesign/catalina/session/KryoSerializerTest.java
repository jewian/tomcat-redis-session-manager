package com.radiadesign.catalina.session;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Locale;

import org.apache.catalina.Context;
import org.apache.catalina.Manager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class KryoSerializerTest {

    @Test
    public void serializeDeserialize() throws IOException, ClassNotFoundException {
        Serializer serializer = new KryoSerializer();
        serializer.setClassLoader(this.getClass().getClassLoader());

        Manager manager = mock(Manager.class);
        when(manager.getContainer()).thenReturn(mock(Context.class));

        RedisSession originalSession = new RedisSession(manager);
        originalSession.setValid(true);
        originalSession.setCreationTime(1234L);
        originalSession.setAttribute("A", new Object());
        originalSession.setAttribute("LOCALE", Locale.CANADA);

        byte[] result = serializer.serializeFrom(originalSession);
        assertThat(result.length > 0, is(true));

        RedisSession newSession = new RedisSession(mock(Manager.class));
        RedisSession finalSession = (RedisSession) serializer.deserializeInto(result, newSession);
        assertThat(finalSession, notNullValue());
        assertThat(finalSession.getCreationTime(), is(1234L));
        assertThat(finalSession.getAttribute("A"), notNullValue());
        assertThat(finalSession.getAttribute("LOCALE"), notNullValue());
    }

}
