package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.I2C;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Drive.*;
import frc.robot.Mechanisms.*;

public class Robot extends TimedRobot {
  
  private final CANSparkMax flywheelMotor1;
  private final CANSparkMax flywheelMotor2;
  private final Compressor compressor;
  private Flywheel flywheel;
   private TalonSRX climbTalon1;
  private VictorSPX colorwheelTalon;
  private Solenoid adjustingPiston;
  private Solenoid colorwheelPiston;
  private VictorSPX hopperMotor1;
  private VictorSPX hopperMotor2;
  private Schwingster climber;
  
  // private Drive drivecontrol;
  private ColorWheel colorwheel;
  
  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

  private DriveHardware hardware;
	private TorDrive drive;
  private Joystick player1;
  private Joystick player2;

  public boolean test;
  private String gameData;

  private final CANSparkMax rightMaster;
	private final CANSparkMax rightSlave1;
	private final CANSparkMax rightSlave2;
	private final CANSparkMax leftMaster;
	private final CANSparkMax leftSlave1;
  private final CANSparkMax leftSlave2;
  
  public Robot() {

    flywheelMotor1 = new CANSparkMax(7, MotorType.kBrushless);
    flywheelMotor2 = new CANSparkMax(8, MotorType.kBrushless);
    compressor = new Compressor();
    adjustingPiston = new Solenoid(4);
    colorwheelPiston = new Solenoid(3);

    hopperMotor1 = new VictorSPX(13);
    hopperMotor2 = new VictorSPX(12);
    climbTalon1 = new TalonSRX(10);
    colorwheelTalon = new VictorSPX(11);
      
    hardware = new DriveHardware();																																																																																																					
    player1 = new Joystick(0);
    player2 = new Joystick(1);
		drive = new TorDrive(hardware, player1);

    test = false;
    
    
    flywheel = new Flywheel(flywheelMotor1, flywheelMotor2, player1);
    climber = new Schwingster(player2, climbTalon1, adjustingPiston);
    colorwheel = new ColorWheel(colorwheelTalon, m_colorSensor, player2, colorwheelPiston);

    leftMaster = new CANSparkMax(4, MotorType.kBrushless);
		leftSlave1 = new CANSparkMax(5, MotorType.kBrushless);
		leftSlave2 = new CANSparkMax(6, MotorType.kBrushless);  
		rightMaster = new CANSparkMax(1, MotorType.kBrushless);
		rightSlave1 = new CANSparkMax(2, MotorType.kBrushless);
		rightSlave2 = new CANSparkMax(3, MotorType.kBrushless);
  }
  
  @Override
  public void robotInit() { 
    compressor.start();
    hardware.init();

  }
  
  @Override
  public void autonomousInit() { }

  @Override
  public void teleopInit() {
    //1 = blue, 2 = green, 3 = red, 4 = yellow
    colorWheelRun();
    super.teleopInit();
  }

  @Override
  public void autonomousPeriodic() { 
    drive.Run(test, true);
  }

  @Override




  public void teleopPeriodic() {
    flywheel.run(false);
    climber.run();
    hopperMotor1.set(ControlMode.PercentOutput, .5);
    // hopperMotor2.set(ControlMode.PercentOutput, 0.5);
    drive.Run(test, true);
    colorWheelRun();
    colorwheel.Main();
    compressor.start();
    // flywheelMotor1.set(.7);
    // flywheelMotor2.set(-.7);
  }
  @Override
  public void testPeriodic() { 
    drive.Run(test, true);
  }

  public void colorWheelRun() {
    gameData = DriverStation.getInstance().getGameSpecificMessage();
    if(gameData.length() > 0) {
      switch (gameData.charAt(0)) {
        case 'B' :
          colorwheel.setColor(1);
          break;
        case 'G' :
          colorwheel.setColor(2);
          break;
        case 'R' :
          colorwheel.setColor(3);
          break;
        case 'Y' :
          colorwheel.setColor(4);
          break;
        default :
          //This is corrupt data
          break;
      }
    }
  }

  //Fetch Buttons
  public boolean getButtonA(){ return player1.getRawButton(1); }
	public boolean getButtonB(){ return player1.getRawButton(2); }
	public boolean getButtonX(){ return player1.getRawButton(3); }
	public boolean getButtonY(){ return player1.getRawButton(4); }
}