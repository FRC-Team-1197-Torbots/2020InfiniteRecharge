package frc.robot;
import edu.wpi.first.wpilibj.TimedRobot;

// import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
//import edu.wpi.first.wpilibj.Compressor;
// import com.revrobotics.CANSparkMax;
// import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Joystick;


public class Robot extends TimedRobot {
  private final TalonSRX turretMotor;
  private final TalonSRX hoodMotor;
  private final TalonSRX flywheelMotor1;
  private final TalonSRX flywheelMotor2;

  private Flywheel flywheel;
  private Turret turret;

  private Joystick player2;
  
  public Robot() {
    player2 = new Joystick(1);
    turretMotor = new TalonSRX(10);
    hoodMotor = new TalonSRX(8);
    flywheelMotor1 = new TalonSRX(7);
    flywheelMotor2 = new TalonSRX(9);

    turret = new Turret(turretMotor, hoodMotor);
    flywheel = new Flywheel(flywheelMotor1, flywheelMotor2, player2);
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
  public void teleopInit() {
    turret.resetEncoder();
    super.teleopInit();
  }
  @Override
  public void teleopPeriodic() {
    flywheel.run(true);
    turret.run(true);
  }

  @Override
  public void testPeriodic() {
  }
}