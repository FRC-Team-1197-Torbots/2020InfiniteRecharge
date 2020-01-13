package frc.robot;
import edu.wpi.first.wpilibj.TimedRobot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj.Joystick;


public class Robot extends TimedRobot {
  private final double kP = 0.03;
  private final TalonSRX turretMotor;
  private final VictorSPX hoodMotor;
  
  public Robot() {
    turretMotor = new TalonSRX(7);
    hoodMotor = new VictorSPX(8);
    //ta = table.getEntry("ta");
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
    
    

  }

  @Override
  public void testPeriodic() {
  }
}