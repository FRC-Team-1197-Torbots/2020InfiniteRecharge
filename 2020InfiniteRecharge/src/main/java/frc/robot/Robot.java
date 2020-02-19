package frc.robot;



import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.TimedRobot;


public class Robot extends TimedRobot {
  private final VictorSPX turretMotor;
  private final TalonSRX hoodMotor;
  private final CANSparkMax flywheelMotor1;
  private final CANSparkMax flywheelMotor2;
  private final Compressor compressor;
  private Flywheel flywheel;
  private Turret turret;
  private Joystick player2; 
  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);
  private VictorSPX climbTalon1;
  private VictorSPX climbTalon2;
  private VictorSPX colorwheelTalon;
  private Solenoid adjustingPiston;
  private Solenoid colorwheelPiston;
  private Schwingster climber;
  private Colorwheel colorwheel;
  
  public Robot() {
    player2 = new Joystick(1);
    turretMotor = new VictorSPX(10);
    hoodMotor = new TalonSRX(8);
    flywheelMotor1 = new CANSparkMax(7, MotorType.kBrushless);
    flywheelMotor2 = new CANSparkMax(9, MotorType.kBrushless);
    compressor = new Compressor();
    adjustingPiston = new Solenoid(4);
    colorwheelPiston = new Solenoid(3);

    climbTalon1 = new VictorSPX(1);
    climbTalon2 = new VictorSPX(2);
    colorwheelTalon = new VictorSPX(3);
    turret = new Turret(turretMotor, hoodMotor);
    flywheel = new Flywheel(flywheelMotor1, flywheelMotor2, player2);
    climber = new Schwingster(player2, climbTalon1, climbTalon2, adjustingPiston);
    colorwheel = new Colorwheel(colorwheelTalon, colorSensor, player2, colorwheelPiston);
  }
  @Override
  public void robotInit() {
    compressor.start();
  }

  @Override
  public void robotPeriodic() {
    compressor.start();
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopPeriodic() {
    boolean xButton = player2.getRawButton(3);
    boolean yButton = player2.getRawButton(4);
    
    turret.run(false, xButton, yButton, false);
    flywheel.run(true);
    climber.run();
    // colorwheel.run(true);
    compressor.start();

    

    //turretMotor.set(ControlMode.PercentOutput, 0.5f);
    //wheel 1   | - = out + = in
    //flywheelMotor1.set(-0.8f);

    //wheel two | + = out - = in
    //flywheelMotor2.set(0.8f);
  }

  @Override
  public void testPeriodic() {
  }
}