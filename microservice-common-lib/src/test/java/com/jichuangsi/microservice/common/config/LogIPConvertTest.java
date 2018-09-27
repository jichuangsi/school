package com.jichuangsi.microservice.common.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.powermock.modules.junit4.PowerMockRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import java.net.SocketException;
import java.net.UnknownHostException;
import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {LogIPConvert.class}) // 2
public class LogIPConvertTest {

    @Before
    public void before() {
        PowerMockito.mockStatic(LogIPConvert.class); // 3
    }

    @Test
    public void test2() throws SocketException, UnknownHostException {
        ILoggingEvent event=PowerMockito.mock(ILoggingEvent.class);
        LogIPConvert logIPConvert=new LogIPConvert();
        assertNotNull(logIPConvert.convert(event));
    }
}
