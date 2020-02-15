package frc.robot;

// import com.ctre.phoenix.motorcontrol.ControlMode;
// import com.ctre.phoenix.motorcontrol.FeedbackDevice;
// import com.ctre.phoenix.motorcontrol.IMotorController;
// import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
// import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
// import com.revrobotics.EncoderType;
// import com.revrobotics.SparkMax;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Flywheel {
    private final double targetHighSpeed = 5000;// rpm
    private final double targetLowSpeed = 3000;//rpm
    private final double highSpeedConstant = 0.8;
    private final double lowSpeedConstant = 0.4;
    private final double adjustingConstant = 1.0;
    private final double kP = 0.0;//.0005
    private final double kI = 0.0;//.00075
    private final double kD = 0;
    private double currentError = 0;

    private TorDerivative pidDerivative;
    private double pidDerivativeResult;

    private double pidIntegral = 0;

    private double targetSpeed;
    private double currentSpeed;
    private double speedToSetMotor;
    private TorDerivative findCurrentSpeed;
    private double currentPosition;
    private final double gearRatio = 1;// ratio from encoder to flywheel
    private CANSparkMax flywheelMotor1;
    private CANSparkMax flywheelMotor2;
    private CANEncoder flywheelEncoder;
    private Joystick player2;

    // time stuff to make sure only goes in correct intervals
    private long currentTime;
    private long startTime = (long) (Timer.getFPGATimestamp() * 1000);
    private double timeInterval = 0.005;
    private double dt = timeInterval;
    private long lastCountedTime;
    private boolean starting = true;

    public Flywheel(CANSparkMax flywheelMotor1, CANSparkMax flywheelMotor2, Joystick player2) {
        this.flywheelMotor1 = flywheelMotor1;
        this.flywheelMotor2 = flywheelMotor2;
        this.flywheelMotor2.follow(this.flywheelMotor1);
        this.flywheelMotor1.setInverted(false);
        this.flywheelMotor2.setInverted(true);
        this.flywheelEncoder = this.flywheelMotor1.getEncoder();

        this.player2 = player2;
        findCurrentSpeed = new TorDerivative(dt);
        findCurrentSpeed.resetValue(0);
        pidDerivative = new TorDerivative(dt);
        pidDerivative.resetValue(0);
        
    }

    public void resetEncoder() {
        flywheelEncoder.getPosition();
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
                currentPosition = (adjustingConstant * flywheelEncoder.getPosition()) / (gearRatio);
                currentSpeed = findCurrentSpeed.estimate(currentPosition) * 60;//rpm
                speedToSetMotor = pidRun(currentSpeed, targetSpeed) + highSpeedConstant;
            } else {
                targetSpeed = targetLowSpeed;
                currentPosition = (adjustingConstant * flywheelEncoder.getPosition()) / (gearRatio);
                currentSpeed = findCurrentSpeed.estimate(currentPosition) * 60;//rpm
                speedToSetMotor = pidRun(currentSpeed, targetSpeed) + lowSpeedConstant;
                speedToSetMotor = lowSpeedConstant;
            }
            if(run) {
                flywheelMotor1.set(speedToSetMotor);
                flywheelMotor2.set(speedToSetMotor);
            
            }
            SmartDashboard.putNumber("current Speed", currentSpeed);
        }
    }

    public double pidRun(double currentSpeed, double targetSpeed) {
        
        currentError = targetSpeed - currentSpeed;
        
        SmartDashboard.putNumber("currentError:", currentError);
        pidDerivativeResult = pidDerivative.estimate(currentError);
        pidIntegral += currentError;

        if(currentError < 20) {
            pidIntegral = 0;
        }

        if(pidIntegral * kI > 0.5) {
            pidIntegral = 0.5 / kI;
        } else if(pidIntegral * kI < -0.5) {
            pidIntegral = -0.5 / kI;
        }

        return ((currentError * kP) +
            (pidIntegral * kI) +
            (pidDerivativeResult * kD));
    }
}