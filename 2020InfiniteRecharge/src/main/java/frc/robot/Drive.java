package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

public class Drive {
    private Solenoid shifter;
    private Joystick player1;

    private CANSparkMax leftMaster;
    private CANSparkMax leftSlave1;
    private CANSparkMax rightMaster;
    private CANSparkMax rightSlave1;

    private double throttleAxis;
    private double arcadeSteerAxis;
    private double leftMotorSpeed;
    private double rightMotorSpeed;
    
    public Drive() {
        shifter = new Solenoid(0);
        player1 = new Joystick(0);
        leftMaster = new CANSparkMax(3,MotorType.kBrushless);
        leftSlave1 = new CANSparkMax(4,MotorType.kBrushless);
        rightMaster = new CANSparkMax(5,MotorType.kBrushless);
        rightSlave1 = new CANSparkMax(6,MotorType.kBrushless);
        leftSlave1.follow(leftMaster);
        rightSlave1.follow(rightMaster);
    }

    public void DriveController(){
        //--  Drive Code  --//
        if(player1.getRawButton(5)) {
            shifter.set(true);
        } else {
            shifter.set(false);
        }
  
        throttleAxis = player1.getRawAxis(1);
        arcadeSteerAxis = -player1.getRawAxis(0);
  
        throttleAxis = Math.pow(throttleAxis, 3);
        arcadeSteerAxis = Math.pow(arcadeSteerAxis, 5);
        if (throttleAxis > 0.0D) {
            if (arcadeSteerAxis > 0.0D) {
                leftMotorSpeed = throttleAxis - arcadeSteerAxis;
                rightMotorSpeed = Math.max(throttleAxis, arcadeSteerAxis);
            } else {
                leftMotorSpeed = Math.max(throttleAxis, -arcadeSteerAxis);
                rightMotorSpeed = throttleAxis + arcadeSteerAxis;
            }
        } else {
            if (arcadeSteerAxis > 0.0D) {
                leftMotorSpeed = -Math.max(-throttleAxis, arcadeSteerAxis);
                rightMotorSpeed = throttleAxis + arcadeSteerAxis;
            } else {
                leftMotorSpeed = throttleAxis - arcadeSteerAxis;
                rightMotorSpeed = -Math.max(-throttleAxis, -arcadeSteerAxis);
            }
        }
        leftMaster.set(leftMotorSpeed);
        rightMaster.set(-rightMotorSpeed);
    }
}
