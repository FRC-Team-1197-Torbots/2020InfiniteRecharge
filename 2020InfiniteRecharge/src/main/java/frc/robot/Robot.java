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
import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Drive.*;
import frc.robot.Mechanisms.*;
import frc.robot.Autonomous.*;

public class Robot extends TimedRobot {
  private Auto Auto;

  private TorBalls torBalls;
  private VictorSPX hopperMainMotor;
  private CANSparkMax hopperShooterMotor;
  private VictorSPX intakeMotor;
  private Solenoid intakePiston;
  private final Compressor compressor;
   private TalonSRX climbTalon1;
  private VictorSPX colorwheelTalon;
  private Solenoid adjustingPiston;
  private Solenoid colorwheelPiston;
  private Schwingster climber;
  
  // private Drive drivecontrol;
  private ColorWheel colorwheel;
  
  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

  private DriveHardware hardware;
	private TorDrive drive;
  private Joystick player1;
  private Joystick player2;
  private Joystick autoBox;

  public boolean test;
  private String gameData;

  private double autoNumber;
  
  public Robot() {
    compressor = new Compressor();
    adjustingPiston = new Solenoid(3);
    colorwheelPiston = new Solenoid(4);

    climbTalon1 = new TalonSRX(10);
    colorwheelTalon = new VictorSPX(13);
      
    hardware = new DriveHardware();																																																																																																					
    player1 = new Joystick(0);
    player2 = new Joystick(1);
    autoBox = new Joystick(2);
		drive = new TorDrive(hardware, player1);

    test = false;
    
    hopperMainMotor = new VictorSPX(12);
    hopperShooterMotor = new CANSparkMax(9, MotorType.kBrushless);
    intakeMotor = new VictorSPX(3);
    intakePiston = new Solenoid(2);

    torBalls = new TorBalls(player2, hopperMainMotor, hopperShooterMotor, intakeMotor, intakePiston);
    climber = new Schwingster(player2, climbTalon1, adjustingPiston);
    colorwheel = new ColorWheel(colorwheelTalon, m_colorSensor, player2, colorwheelPiston);

    Auto = new Auto(torBalls, drive, player1);
  }
  
  @Override
  public void robotInit() { 
    compressor.start();
    hardware.init();

  }
  
  @Override
  public void autonomousInit() {
    for(int i = 1; i<5; i++) {
      if(autoBox.getRawButton(i)) {
        autoNumber += Math.pow(2, i-1);
      }
    }
    Auto.setNumber(autoNumber);
  }

  @Override
  public void teleopInit() {
    //1 = blue, 2 = green, 3 = red, 4 = yellow
    colorWheelRun();
    torBalls.init();
    super.teleopInit();
  }

  @Override
  public void autonomousPeriodic() { 
    // drive.Run(test, true);
    // Auto.testRun();
    Auto.run();
  }

  @Override
  public void teleopPeriodic() {
    climber.run();
    // Auto.testRun();
    drive.Run(test, true);
    colorWheelRun();
    torBalls.run(true, true);
    colorwheel.Main();
    compressor.start();
    
    SmartDashboard.putNumber("current position:", drive.getPosition());
    SmartDashboard.putNumber("left encoder:", drive.getLeftEncoder());
    SmartDashboard.putNumber("right encoder:", drive.getRightEncoder());
    SmartDashboard.putNumber("current heading degrees:", (drive.getHeading() * 180 / Math.PI));
  }
  
  @Override
  public void testPeriodic() {
    Auto.testRun(); 
    // drive.Run(test, true);
  }

  public void colorWheelRun() {
    gameData = DriverStation.getInstance().getGameSpecificMessage();
    //we have to add 2 to the number since we have to spin to the opposite color
    //since the colorwheel is sense from the middle
    if(gameData.length() > 0) {
      switch (gameData.charAt(0)) {
        case 'B' :
          // colorwheel.setColor(1);
          colorwheel.setColor(3);
          break;
        case 'G' :
          // colorwheel.setColor(2);
          colorwheel.setColor(4);
          break;
        case 'R' :
          // colorwheel.setColor(3);
          colorwheel.setColor(1);
          break;
        case 'Y' :
          // colorwheel.setColor(4);
          colorwheel.setColor(2);
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