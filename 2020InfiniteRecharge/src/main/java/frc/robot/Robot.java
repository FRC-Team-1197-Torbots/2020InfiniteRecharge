package frc.robot;
import edu.wpi.first.wpilibj.TimedRobot;

// import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
// import com.revrobotics.CANSparkMax;
// import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Joystick;


public class Robot extends TimedRobot {
  private final TalonSRX turretMotor;
  private final VictorSPX hoodMotor;

  private final TalonSRX flywheelMotor1;
  private final TalonSRX flywheelMotor2;

  private Flywheel flywheel;
  private Turret turret;

  private Joystick player2;
  
  public Robot() {
    player2 = new Joystick(1);
    turretMotor = new TalonSRX(7);
    hoodMotor = new VictorSPX(8);
    flywheelMotor1 = new TalonSRX(5);
    flywheelMotor2 = new TalonSRX(6);

    turret = new Turret(turretMotor, hoodMotor);
    // flywheel = new Flywheel(flywheelMotor1, flywheelMotor2, player2);
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
    flywheel.run(false);
    turret.run(true);
  }

  @Override
  public void testPeriodic() {
  }
}