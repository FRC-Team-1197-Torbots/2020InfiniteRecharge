package frc.robot.Mechanisms;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

public class Schwingster {
    private Joystick player2;
    private CANSparkMax climbTalon1;
    private Solenoid adjustingPiston;
    private double joystickValue;
    public Schwingster(Joystick player2, CANSparkMax climbTalon1, Solenoid adjustingPiston) {
        this.player2 = player2;
        this.climbTalon1 = climbTalon1;
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

        climbTalon1.set(joystickValue);
    }
}