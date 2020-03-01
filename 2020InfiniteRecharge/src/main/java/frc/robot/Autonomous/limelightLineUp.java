package frc.robot.Autonomous;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Drive.*;
import frc.robot.PID_Tools.*;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class limelightLineUp {
	private TorDrive drive;
	private double speed;
	private boolean isFinished = false;
	private double lasttime;
	private double currentTime;
	private final double kF = 0.005;
    
    //PID For rotation
	private final double rkP = 0.0;//.035
	private final double rkD = 0.0;//0.00087
	private final double rkI = 0.0;//0.0087
	
	//tolerances
	private final double angleTolerance = 2.75 * (Math.PI / 180.0);//radians
	private final double omegaTolerance = 1.5 * (Math.PI / 180.0);//radians per second
	
	private double omegaP;//turning proportional
	private double omegaD;//turning derivative
	private double omegaI;
	private final double lor = -1;
	
	private double angleError;
	
	private double currentVelocity;
	
	private double timeOutTime;
	
    private TorDerivative derivative;
    
    
    // for the limelight
    private NetworkTable table;
    private NetworkTableEntry tx;
	
	public static enum run {
		IDLE, GO;
		private run() {}
	}
	
	public run runIt = run.IDLE;
	
	public limelightLineUp(TorDrive drive, double timeOutTime) {
		this.drive = drive;
		this.timeOutTime = timeOutTime;
        derivative = new TorDerivative(kF);
         
        table = NetworkTableInstance.getDefault().getTable("limelight");//bottom
        tx = table.getEntry("tx");
	}

	public boolean isDone() {
		return isFinished;
	}
	
	public void init() {
		isFinished = false;
		runIt = run.GO;
        angleError = tx.getDouble(0.0) * Math.PI / 180.0;
        
		//we need to make sure control system is efficient so the angle error ranges from -pi to pi
		if(angleError > Math.PI) {
			angleError -= (2 * Math.PI);
		} else {
			if(angleError < -Math.PI) {
				angleError += (2 * Math.PI);
			}
		}
		derivative.resetValue(angleError);
		lasttime = Timer.getFPGATimestamp();
	}
	
	public void run() {
		currentTime = Timer.getFPGATimestamp();
		switch(runIt) {
		case IDLE:
			break;
		case GO:
            angleError = tx.getDouble(0.0) * Math.PI / 180.0;
            
			//we need to make sure control system is efficient so the angle error ranges from -pi to pi
			if(angleError > Math.PI) {
				angleError -= (2 * Math.PI);
			} else {
				if(angleError < -Math.PI) {
					angleError += (2 * Math.PI);
				}
			}
			
			omegaI += angleError;
			omegaP = angleError * rkP;
			if(Math.abs(angleError) < angleTolerance) {
				omegaI = 0;
			}
			if(omegaI > (0.7 / (rkI * kF))) {
				omegaI = (0.7 / (rkI * kF));
			}
			if(omegaI < -(0.7 / (rkI * kF))) {
				omegaI = -(0.7 / (rkI * kF));
			}
			if(omegaP > 0.7) {
				omegaP = 0.7;
			}
			if(omegaD < -0.7) {
				omegaD = -0.7;
			}
			currentVelocity = derivative.estimate(angleError);//radians per second
			omegaD = (currentVelocity * rkD);
			
			speed = omegaP + omegaD + (omegaI * rkI * kF);
			speed *= lor;
			
			drive.setMotorSpeeds(speed, -speed);
				
			if(((Math.abs(angleError) <= angleTolerance)
					&& (Math.abs(currentVelocity) < omegaTolerance))
					
					|| 
					(currentTime - lasttime > timeOutTime)) {//timeout
				drive.setMotorSpeeds(0, 0);
				isFinished = true;
				runIt = run.IDLE;
			}
			break;
		}
	}
}