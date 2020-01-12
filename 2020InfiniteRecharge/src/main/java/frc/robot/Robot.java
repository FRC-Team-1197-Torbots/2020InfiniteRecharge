package frc.robot;
import edu.wpi.first.wpilibj.TimedRobot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj.Joystick;

public class Robot extends TimedRobot {
  private final double kP = 0.03;
  private TalonSRX turretMotor;
  private NetworkTable table;//for limelight
  private NetworkTableEntry tx;
  // private NetworkTableEntry ta;
  private double x;

  public Robot() {
    turretMotor = new TalonSRX(7);
    table = NetworkTableInstance.getDefault().getTable("limelight-ball");
    tx = table.getEntry("tx");
    // ta = table.getEntry("ta");
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
    x = tx.getDouble(0.0);
    SmartDashboard.putNumber("X:",x);
    turretMotor.set(ControlMode.PercentOutput, kP * x);
  }

  @Override
  public void testPeriodic() {
  }
}
