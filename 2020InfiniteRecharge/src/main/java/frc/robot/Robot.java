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
  private final NetworkTable table;// for limelight
  private final NetworkTableEntry tx;
  private final NetworkTableEntry ty;
  //private final NetworkTableEntry ta;
  private double x;
  private double y;
  //private double a;
  private double h1;
  private double h2;
  private double a1;
  private double a2;
  private double d;

  public Robot() {
    turretMotor = new TalonSRX(7);
    hoodMotor = new VictorSPX(8);
    table = NetworkTableInstance.getDefault().getTable("limelight-ball");
    tx = table.getEntry("tx");
    ty = table.getEntry("ty");
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
    x = tx.getDouble(0.0);
    SmartDashboard.putNumber("X:",x);
    turretMotor.set(ControlMode.PercentOutput, kP * x);
    y = ty.getDouble(0.0);
    SmartDashboard.putNumber("Y:",y);
    a1 = 15;
    a2 = y;
    h1 = 41;
    h2= 94.5;
    d = (h2-h1) / Math.tan(Math.toRadians(a1+a2));
    SmartDashboard.putNumber("D", d);
    hoodMotor.set(ControlMode.PercentOutput, kP * d);



  }

  @Override
  public void testPeriodic() {
  }
}