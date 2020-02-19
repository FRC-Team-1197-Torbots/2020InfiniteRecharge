package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

public class Schwingster {
    private Joystick player2;
    private VictorSPX climbTalon1;
    private VictorSPX climbTalon2;
    private Solenoid adjustingPiston;
    private double joystickValue;
    public Schwingster(Joystick player2, VictorSPX climbTalon1, VictorSPX climbTalon2, Solenoid adjustingPiston) {
        this.player2 = player2;
        this.climbTalon1 = climbTalon1;
        this.climbTalon2 = climbTalon2;
        this.climbTalon2.follow(this.climbTalon1);
        this.adjustingPiston = adjustingPiston;
    }

    public void run() {
        if(player2.getRawButton(2)) {
            adjustingPiston.set(true);
        } else {
            adjustingPiston.set(false);
        }
        joystickValue = player2.getRawAxis(1);
        if(Math.abs(joystickValue) < 0.15) {
            joystickValue = 0;
        }

        climbTalon1.set(ControlMode.PercentOutput, joystickValue);
    }
}
