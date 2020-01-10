package frc.robot;

//First Wpilib 
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
//Color Sensor
import edu.wpi.first.wpilibj.I2C;
//CTRE
import com.ctre.phoenix.motorcontrol.can.VictorSPX; 
  //import com.ctre.phoenix.motorcontrol.can.TalonSRX;
//Rev Robotics
import com.revrobotics.ColorSensorV3;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

public class Robot extends TimedRobot {
  private Solenoid shifter;
  private Joystick player1;
  
  //Drive Talons
  // private CANSparkMax leftMaster;
  // private CANSparkMax leftSlave1;
  // private CANSparkMax rightMaster;
  // private CANSparkMax rightSlave1;
  //Color Wheel Talon
  private VictorSPX wheeltalon;

  private double throttleAxis;
  private double arcadeSteerAxis;
  private double leftMotorSpeed;
  private double rightMotorSpeed;

  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

  private ColorWheel colorwheel;

  public Robot() {
    shifter = new Solenoid(0);
    player1 = new Joystick(0);
    // leftMaster = new CANSparkMax(3,MotorType.kBrushless);
    // leftSlave1 = new CANSparkMax(4,MotorType.kBrushless);
    // rightMaster = new CANSparkMax(5,MotorType.kBrushless);
    // rightSlave1 = new CANSparkMax(6,MotorType.kBrushless);
    wheeltalon = new VictorSPX(0);
    
    //--  Color Sensor Code  --//
    colorwheel = new ColorWheel(wheeltalon, m_colorSensor, player1);
    // leftSlave1.follow(leftMaster);
    // rightSlave1.follow(rightMaster);
  }
  
  @Override
  public void robotInit() { }
  
  @Override
  public void robotPeriodic() { }

  @Override
  public void autonomousInit() { }

  @Override
  public void autonomousPeriodic() { }

  @Override
  public void teleopInit() {
    //1 is blue
    //2 is green
    //3 is red
    //4 is yellow
    colorwheel.setColor(3);

    super.teleopInit();
  }

  @Override
  public void teleopPeriodic() {
    
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
    // leftMaster.set(leftMotorSpeed);
    // rightMaster.set(-rightMotorSpeed);

    colorwheel.Main();
  }

  @Override
  public void testPeriodic() { }

  //Fetch Buttons
  public boolean getButtonA(){
		return player1.getRawButton(1);
	}

	public boolean getButtonB(){
		return player1.getRawButton(2);
	}

	public boolean getButtonX(){
		return player1.getRawButton(3);
	}

	public boolean getButtonY(){
		return player1.getRawButton(4);
  }
}