package frc.robot;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Joystick;

import com.revrobotics.ColorSensorV3;

public class Robot extends TimedRobot {
  private Joystick player1;
  private double throttleAxis;
  private double arcadeSteerAxis;
  private double leftMotorSpeed;
  private double rightMotorSpeed;
  private TalonSRX leftMaster;
  private TalonSRX leftSlave1;
  private TalonSRX leftSlave2;
  private TalonSRX rightMaster;
  private TalonSRX rightSlave1;
  private TalonSRX rightSlave2;
  
  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

  public Robot() {
    player1 = new Joystick(0);
    leftMaster = new TalonSRX(1);
    leftSlave1 = new TalonSRX(2);
    leftSlave2 = new TalonSRX(3);
    rightMaster = new TalonSRX(4);
    rightSlave1 = new TalonSRX(5);
    rightSlave2 = new TalonSRX(6);

    leftSlave1.follow(leftMaster);
    leftSlave2.follow(leftMaster);
    leftSlave1.setInverted(true);
    leftSlave2.setInverted(true);

    rightSlave1.follow(rightMaster);
    rightSlave2.follow(rightMaster);
    rightSlave1.setInverted(true);
    rightSlave2.setInverted(true);
  }
  @Override
  public void robotInit() {

  }
  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {

  }

  @Override
  public void autonomousPeriodic() {

  }

  @Override
  public void teleopPeriodic() {
    throttleAxis = player1.getRawAxis(1);
    arcadeSteerAxis = player1.getRawAxis(0);

    throttleAxis = Math.pow(throttleAxis, 3);
    arcadeSteerAxis = Math.pow(arcadeSteerAxis, 3);
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
    leftMaster.set(ControlMode.PercentOutput, leftMotorSpeed);
    rightMaster.set(ControlMode.PercentOutput, rightMotorSpeed);

    //Color Sensor Code
    
    Color detectedColor = m_colorSensor.getColor();

    //double IR = m_colorSensor.getIR();
    
    // Cyan Green Red Yellow
    //R 0.132 0.173 0.500 0.315
    //G 0.426 0.563 0.352 0.554
    //B 0.441 0.263 0.147 0.132
    
    String ColorDetected = "None";
    // double Rmax = 0.0;
    // double Gmax = 0.0;
    // double Bmax = 0.0;
    // if(Rmax < detectedColor.red) {
    //   Rmax = detectedColor.red;
    // }
    // if(Gmax < detectedColor.green) {
    //   Gmax = detectedColor.green;
    // }
    // if(Bmax < detectedColor.blue) {
    //   Bmax = detectedColor.blue;
    // }

    SmartDashboard.putNumber("Red", detectedColor.red);
    //SmartDashboard.putNumber("Red Max", Rmax);
    SmartDashboard.putNumber("Green", detectedColor.green);
    //SmartDashboard.putNumber("Green Max", Gmax);
    SmartDashboard.putNumber("Blue", detectedColor.blue);
    //SmartDashboard.putNumber("Blue Max", Bmax);
    
    if(detectedColor.red > detectedColor.blue && detectedColor.red > detectedColor.green && detectedColor.red > 0.4){
      ColorDetected = "Red";
    } else if(Math.abs(detectedColor.red-detectedColor.green) < 0.25) {
      ColorDetected = "Yellow";
    } else if(Math.abs(detectedColor.green-detectedColor.blue) < 0.25) {
      ColorDetected = "Blue";
    } else if(detectedColor.green > detectedColor.blue && detectedColor.green > detectedColor.red) {
      ColorDetected = "Green"; 
    } else {
      ColorDetected = "Invalid";
    }
    int proximity = m_colorSensor.getProximity();
    if(proximity<50) {
      ColorDetected = "Out Of Range";
    }
    SmartDashboard.putString("Color Detected", ColorDetected);
    
    //SmartDashboard.putNumber("IR", IR);
    
    //SmartDashboard.putNumber("Proximity", proximity);
  }

  @Override
  public void testPeriodic() {
  }
}
