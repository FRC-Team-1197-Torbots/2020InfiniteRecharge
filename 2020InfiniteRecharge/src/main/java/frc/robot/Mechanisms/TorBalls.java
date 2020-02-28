package frc.robot.Mechanisms;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

public class TorBalls {
    private Flywheel flywheel;
    private Joystick player2;
    private VictorSPX intakeMotor;
    private Solenoid intakePiston;
    private Intake intake;
    private VictorSPX hopperMainMotor;
    private VictorSPX hopperShooterMotor;
    public TorBalls(Joystick player2, VictorSPX hopperMainMotor, VictorSPX hopperShooterMotor,
        VictorSPX intakeMotor, Solenoid intakePiston) {
        this.player2 = player2;
        this.hopperMainMotor = hopperMainMotor;
        this.hopperShooterMotor = hopperShooterMotor;
        this.intakeMotor = intakeMotor;
        this.intakePiston = intakePiston;
        flywheel = new Flywheel(player2);
    }

    public void init() {
        flywheel.resetEncoder();
    }
    public void run(boolean flywheelRun, boolean hopperRun) {
        flywheel.run(flywheelRun);
        if(hopperRun ) {
            if(player2.getRawButton(6)) {
                hopperShooterMotor.set(ControlMode.PercentOutput, 0.95);
            } else {
                hopperMainMotor.set(ControlMode.PercentOutput, 0.0);
            }
            if((Math.abs(player2.getRawAxis(3)) > 0.3) && flywheel.isFastEnough()) {//right trigger go
                intake.runState(3);
                hopperMainMotor.set(ControlMode.PercentOutput, 0.4);
            } else {
                if(Math.abs(player2.getRawAxis(2)) > 0.3) {
                    intake.runState(2);
                } else {
                    intake.runState(1);
                }
                hopperShooterMotor.set(ControlMode.PercentOutput, 0.0);
            }
        } else {
            hopperMainMotor.set(ControlMode.PercentOutput, 0.0);
            hopperShooterMotor.set(ControlMode.PercentOutput, 0.0);
        }
    }
}
