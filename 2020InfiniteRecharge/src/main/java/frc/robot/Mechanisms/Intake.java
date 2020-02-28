package frc.robot.Mechanisms;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Solenoid;

public class Intake {
    private VictorSPX intakeMotor;
    private Solenoid intakePiston;
    public Intake(VictorSPX intakeMotor, Solenoid intakePiston) {
        this.intakeMotor = intakeMotor;
        this.intakePiston = intakePiston;
    }

    public void runState(int state) {
        //0 is idle
        //1 is intaking
        //2 is intake motor on but retracted (for shooting)
        if(state == 1) {
            
        } else if(state == 2) {

        } else {

        }
    }
}
