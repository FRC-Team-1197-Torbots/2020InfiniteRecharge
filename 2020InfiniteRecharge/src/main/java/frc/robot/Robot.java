package frc.robot;
import edu.wpi.first.wpilibj.TimedRobot;

// import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
//import com.ctre.phoenix.motorcontrol.can.VictorSPX;
// import edu.wpi.first.wpilibj.Solenoid;
// import edu.wpi.first.wpilibj.Compressor;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Joystick;


public class Robot extends TimedRobot {
  private final TalonSRX turretMotor;
  private final TalonSRX hoodMotor;
  private final CANSparkMax flywheelMotor1;
  private final CANSparkMax flywheelMotor2;
  // private final Compressor compressor;
  // private final Solenoid solenoid;
  private Flywheel flywheel;
  private Turret turret;
  

  private Joystick player2;
  
  public Robot() {
    player2 = new Joystick(1);
    turretMotor = new TalonSRX(10);
    hoodMotor = new TalonSRX(8);
    flywheelMotor1 = new CANSparkMax(7, MotorType.kBrushless);
    flywheelMotor2 = new CANSparkMax(9, MotorType.kBrushless);
    // compressor = new Compressor();
    // solenoid = new Solenoid(0);

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

  @Override
  public void teleopPeriodic() {
    turret.run(true);
    flywheel.run(false);
  
    // compressor.start();
  }

  @Override
  public void testPeriodic() {
  }
}