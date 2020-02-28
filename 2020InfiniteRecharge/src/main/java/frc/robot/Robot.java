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

import frc.robot.Drive.*;
import frc.robot.Mechanisms.*;

public class Robot extends TimedRobot {
  
  private TorBalls torBalls;
  private VictorSPX hopperMainMotor;
  private VictorSPX hopperShooterMotor;
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

  public boolean test;
  private String gameData;
  
  public Robot() {

    // flywheelMotor1 = new CANSparkMax(7, MotorType.kBrushless);
    // otherFlywheelMotor = new CANSparkMax(8, MotorType.kBrushless);
    compressor = new Compressor();
    adjustingPiston = new Solenoid(4);
    colorwheelPiston = new Solenoid(3);

    // hopperMotor1 = new VictorSPX(13);
    // hopperMotor2 = new VictorSPX(12);
    climbTalon1 = new TalonSRX(10);
    colorwheelTalon = new VictorSPX(11);
      
    hardware = new DriveHardware();																																																																																																					
    player1 = new Joystick(0);
    player2 = new Joystick(1);
		drive = new TorDrive(hardware, player1);

    test = false;
    
    hopperMainMotor = new VictorSPX(13);
    hopperShooterMotor = new VictorSPX(12);
    intakeMotor = new VictorSPX(1);
    intakePiston = new Solenoid(6);

    torBalls = new TorBalls(player2, hopperMainMotor, hopperShooterMotor, intakeMotor, intakePiston);
    climber = new Schwingster(player2, climbTalon1, adjustingPiston);
    colorwheel = new ColorWheel(colorwheelTalon, m_colorSensor, player2, colorwheelPiston);
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
    torBalls.init();
    super.teleopInit();
  }

  @Override
  public void autonomousPeriodic() { 
    drive.Run(test, true);
  }

  @Override
  public void teleopPeriodic() {
    climber.run();
    drive.Run(test, true);
    colorWheelRun();
    torBalls.run(true, true);
    colorwheel.Main();
    compressor.start();
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