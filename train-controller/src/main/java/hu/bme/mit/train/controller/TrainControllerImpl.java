package hu.bme.mit.train.controller;

import hu.bme.mit.train.interfaces.TrainController;
import java.util.TimerTask;
import java.util.Timer; 

public class TrainControllerImpl implements TrainController {

	private int step = 0;
	private int referenceSpeed = 0;
	private int speedLimit = 0;

	TimerTask task = new TimerTask() {
		public void run(){
			try{
				followSpeed();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}; 

	public TrainControllerImpl(){
		Timer timer = new Timer("Timer");
		long delay=1000L;
		timer.schedule(task, delay);
	}


	@Override
	public void followSpeed() {
		if (referenceSpeed < 0) {
			referenceSpeed = 0;
		} else {
		    if(referenceSpeed+step > 0) {
                referenceSpeed += step;
            } else {
		        referenceSpeed = 0;
            }
		}

		enforceSpeedLimit();
	}

	@Override
	public int getReferenceSpeed() {
		return referenceSpeed;
	}

	@Override
	public void setSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
		enforceSpeedLimit();
		
	}

	private void enforceSpeedLimit() {
		if (referenceSpeed > speedLimit) {
			referenceSpeed = speedLimit;
		}
	}

	@Override
	public void setJoystickPosition(int joystickPosition) {
		this.step = joystickPosition;		
	}
	
	@Override
	public void setReferenceSpeed(int referenceSpeed) {
		if (referenceSpeed < 0) {
			this.referenceSpeed = 0;
		} else  if (referenceSpeed > speedLimit) {
		    this.referenceSpeed = speedLimit;
		} else {
		    this.referenceSpeed = referenceSpeed;
		}
	}

}
