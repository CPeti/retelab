package hu.bme.mit.train.controller;

import hu.bme.mit.train.interfaces.TrainController;

public class TrainControllerImpl implements TrainController {

	private int step = 0;
	private int referenceSpeed = 0;
	private int speedLimit = 0;

	public void TrainControllerImpl() {
		FollowThread followThread = new FollowThread();
		followThread.start();
	}

	class FollowThread extends Thread {
		public void run() {
			while(true) {
				followSpeed();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}


	@Override
	public void followSpeed() {
		System.out.println("Follow speed");
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
