package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

public class Flywheel {
    private final double targetHighSpeed = 5000;//rpm
    private final double targetLowSpeed = 3000;
    private final double kP = 0.01;
    private final double kI = 0;
    private final double kD = 0;
    private double currentError = 0;

    private double targetSpeed;
    private double currentSpeed;
    private double speedToSetMotor;
    private TorDerivative findCurrentSpeed;
    private double currentPosition;
    private final double gearRatio = 1;//ratio from encoder to flywheel
    private TalonSRX flywheelMotor1;
    private TalonSRX flywheelMotor2;
    private Joystick player2;




    //time stuff to make sure only goes in correct intervals
    private long currentTime;
    private long startTime = (long) (Timer.getFPGATimestamp() * 1000);
    private double timeInterval = 0.005;
    private double dt = timeInterval;
    private long lastCountedTime;
    private boolean starting = true;

    public Flywheel(TalonSRX flywheelMotor1, TalonSRX flywheelMotor2, Joystick player2) {
        this.flywheelMotor1 = flywheelMotor1;
        this.flywheelMotor2 = flywheelMotor2;
        this.flywheelMotor2.follow(this.flywheelMotor1);
        this.player2 = player2;
        flywheelMotor1.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
        findCurrentSpeed = new TorDerivative(dt);
        findCurrentSpeed.resetValue(0);
    }

    public void run(boolean run) {
        currentTime = (long) (Timer.getFPGATimestamp() * 1000);
        if (((currentTime - startTime) - ((currentTime - startTime) % (dt * 1000))) > // has current time minus start time to see the relative time the trajectory has been going
            ((lastCountedTime - startTime) - ((lastCountedTime - startTime) % (dt * 1000))) // subtracts that mod dt times 1000 so that it is floored to
            // the nearest multiple of dt times 1000 then checks if that is greater than the last one to see if it is time to move on to the next tick
            || starting) {

            starting = false;
            lastCountedTime = currentTime;

            if(player2.getRawButton(1)) {
                targetSpeed = targetHighSpeed;
            } else {
                targetSpeed = targetLowSpeed;
            }
            currentPosition = (flywheelMotor1.getSelectedSensorPosition(0) / (gearRatio * 4096));
            currentSpeed = findCurrentSpeed.estimate(currentPosition);
            speedToSetMotor = pidRun(currentSpeed, targetSpeed);
            flywheelMotor1.set(ControlMode.PercentOutput, speedToSetMotor);
        }
    }

    public double pidRun(double currentSpeed, double targetSpeed) {
        currentError = targetSpeed - currentSpeed;
        
    }
}
