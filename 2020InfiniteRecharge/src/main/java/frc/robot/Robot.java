package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.I2C;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Drive.*;
import frc.robot.Mechanisms.*;

public class Robot extends TimedRobot {
  
  // private final VictorSPX turretMotor;
  // private final TalonSRX hoodMotor;
  // private final CANSparkMax flywheelMotor1;
  // private final CANSparkMax flywheelMotor2;
  private final Compressor compressor;
  // private Flywheel flywheel;
  // private Turret turret;
  private VictorSPX climbTalon1;
  private VictorSPX climbTalon2;
  private VictorSPX colorwheelTalon;
  private Solenoid adjustingPiston;
  private Solenoid colorwheelPiston;
  private Schwingster climber;
  
  private VictorSPX wheeltalon;
  //private Drive drivecontrol;
  private ColorWheel colorwheel;
  
  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

  private DriveHardware hardware;
	private TorDrive drive;
	private Joystick player1;

  public boolean test;

  public Robot() {
    // turretMotor = new VictorSPX(10);
    // hoodMotor = new TalonSRX(8);
    // flywheelMotor1 = new CANSparkMax(7, MotorType.kBrushless);
    // flywheelMotor2 = new CANSparkMax(9, MotorType.kBrushless);
    compressor = new Compressor();
    adjustingPiston = new Solenoid(4);
    colorwheelPiston = new Solenoid(3);

    climbTalon1 = new VictorSPX(1);
    climbTalon2 = new VictorSPX(2);
    colorwheelTalon = new VictorSPX(3);
  
    
    hardware = new DriveHardware();																																																																																																					
		player1 = new Joystick(0);
		drive = new TorDrive(hardware, player1);

    test = false;
    wheeltalon = new VictorSPX(0);
    
    // turret = new Turret(turretMotor, hoodMotor);
    // flywheel = new Flywheel(flywheelMotor1, flywheelMotor2, player1);
    climber = new Schwingster(player1, climbTalon1, climbTalon2, adjustingPiston);
    colorwheel = new ColorWheel(wheeltalon, m_colorSensor, player1, colorwheelPiston);
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
    colorwheel.setColor(3);
    super.teleopInit();
  }

  @Override
  public void autonomousPeriodic() { 
    drive.Run(test, true);
  }

  @Override
  public void teleopPeriodic() {
    String gameData;
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
    } else {
      //Code for no data received yet
    }
    colorwheel.Main();
    drive.Run(test, true);

    boolean xButton = player1.getRawButton(3);
    boolean yButton = player1.getRawButton(4);
    
    // turret.run(false, xButton, yButton, false);
    // flywheel.run(true);
    climber.run();
    // colorwheel.run(true);
    compressor.start();
  }

  @Override
  public void testPeriodic() { 
    drive.Run(test, true);
  }

  //Fetch Buttons
  public boolean getButtonA(){ return player1.getRawButton(1); }
	public boolean getButtonB(){ return player1.getRawButton(2); }
	public boolean getButtonX(){ return player1.getRawButton(3); }
	public boolean getButtonY(){ return player1.getRawButton(4); }
}