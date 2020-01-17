package frc.robot;

//First Wpilib 
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
//Color Sensor
import edu.wpi.first.wpilibj.I2C;
//CTRE
import com.ctre.phoenix.motorcontrol.can.VictorSPX; 
//Rev Robotics
import com.revrobotics.ColorSensorV3;

public class Robot extends TimedRobot {
  private Joystick player1;
  
  private VictorSPX wheeltalon;

  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

  private ColorWheel colorwheel;
  private Drive drivecontrol;

  public Robot() {
    drivecontrol = new Drive();
    wheeltalon = new VictorSPX(0);
    colorwheel = new ColorWheel(wheeltalon, m_colorSensor, player1);
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
    //1 = blue, 2 = green, 3 = red, 4 = yellow
    colorwheel.setColor(3);
    super.teleopInit();
  }

  @Override
  public void teleopPeriodic() {
    String gameData;
    gameData = DriverStation.getInstance().getGameSpecificMessage();
    if(gameData.length() > 0)
    {
      switch (gameData.charAt(0))
      {
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
    drivecontrol.DriveController();
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