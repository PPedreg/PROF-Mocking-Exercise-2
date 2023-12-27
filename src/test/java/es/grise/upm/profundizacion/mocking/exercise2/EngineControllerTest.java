package es.grise.upm.profundizacion.mocking.exercise2;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class EngineControllerTest {

	private EngineController engine;
	private Gearbox gearbox;
	private Logger logger;
	private Speedometer speedometer;
	private Time time;
	
	private static final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	private static final SimpleDateFormat simpleDataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@BeforeEach
	public void setUp() {
		gearbox = mock(Gearbox.class);
		logger = mock(Logger.class);
		speedometer = mock(Speedometer.class);
		time = mock(Time.class);
		engine = new EngineController(logger, speedometer, gearbox, time);
	}
	
	@Test
	public void recordGearLogFormatTest() {
		when(time.getCurrentTime()).thenReturn(timestamp);
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        doNothing().when(logger).log(captor.capture());
        GearValues gearValue = GearValues.FIRST;
        engine.recordGear(gearValue);
        String expected = simpleDataFormat.format(timestamp) + " Gear changed to " + gearValue;
        assertEquals(expected, captor.getValue());
	}
	
	@Test
	public void getInstantaneousSpeedTest() {
        when(speedometer.getSpeed()).thenReturn(50.0, 60.0, 70.0);
        double expected = 60.0;
        assertEquals(expected, engine.getInstantaneousSpeed());
        verify(speedometer, times(3)).getSpeed();
	}
	
	@Test
	public void getInstantaneousSpeedNumberOfExecutionsTest() {
		when(speedometer.getSpeed()).thenReturn(50.0, 60.0, 70.0);
		when(time.getCurrentTime()).thenReturn(timestamp);
		engine.adjustGear();
		verify(speedometer, times(3)).getSpeed();
	}
	
	@Test
	public void recordGearTest() {
		when(speedometer.getSpeed()).thenReturn(0.0, 0.0, 0.0);
		when(time.getCurrentTime()).thenReturn(timestamp);
        engine.adjustGear();
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(logger).log(captor.capture());
        GearValues gearValue = GearValues.FIRST;
        String expected = simpleDataFormat.format(timestamp) + " Gear changed to " + gearValue;
        assertEquals(expected, captor.getValue());
	}
	
	@Test
	public void setGearTest() {
		when(time.getCurrentTime()).thenReturn(timestamp);
        GearValues gearValues = GearValues.FIRST;
        engine.setGear(gearValues);
        when(speedometer.getSpeed()).thenReturn(50.0, 60.0, 70.0);
        engine.adjustGear();
        verify(gearbox).setGear(GearValues.STOP);
	}
	
	
	
	
	
	
}
