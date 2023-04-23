package hu.bme.mit.train.sensor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import hu.bme.mit.train.interfaces.TrainSensor;
import hu.bme.mit.train.interfaces.TrainController;
import hu.bme.mit.train.interfaces.TrainUser;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TrainSensorTest {
    TrainController controller;
	TrainSensor sensor;
	TrainUser user;
	
	@Before
	public void before() {
		controller = mock(TrainController.class);
        user = mock(TrainUser.class);
        sensor = new TrainSensorImpl(controller, user);
	}
    @Test
    public void tachographTest() {
        String date = "2019.11.11. 11:11:11";
        int referenceSpeed = 10;
        int joystickPosition = 1;
        sensor.saveTachograph(date, referenceSpeed, joystickPosition);
        Assert.assertEquals(1, sensor.getTachograph().size());

        boolean entryIsPresent = sensor.getTachograph().containsRow(date);
        Assert.assertTrue(entryIsPresent);
        boolean entryIsCorrect = sensor.getTachograph().contains(date, referenceSpeed) && sensor.getTachograph().containsValue(joystickPosition);
        Assert.assertTrue(entryIsPresent);

    }

    @Test
    public void testOverrideSpeedLimit_withinValidRange() {
        int speedLimit = 100;
        sensor.overrideSpeedLimit(speedLimit);
        assertEquals(speedLimit, sensor.getSpeedLimit());
    }

    @Test
    public void testOverrideSpeedLimit_aboveValidRange() {


        int speedLimit = 600;
        int referenceSpeed = 50;
        when(controller.getReferenceSpeed()).thenReturn(referenceSpeed);
        sensor.overrideSpeedLimit(speedLimit);
        verify(user, times(1)).setAlarmState(true);
    }

    @Test
    public void testAnalyzeSpeed_limitBelowHalfOfReferenceSpeed() {
        int speedLimit = 20;
        int referenceSpeed = 50;
        when(controller.getReferenceSpeed()).thenReturn(referenceSpeed);
        sensor.overrideSpeedLimit(speedLimit);
        verify(user, times(1)).setAlarmState(true);
    }

    @Test
    public void testAnalyzeSpeed_limitAboveHalfOfReferenceSpeed() {
        int speedLimit = 80;
        int referenceSpeed = 50;
        when(controller.getReferenceSpeed()).thenReturn(referenceSpeed);
        sensor.overrideSpeedLimit(speedLimit);
        verify(user, times(0)).setAlarmState(true);
    }
}
