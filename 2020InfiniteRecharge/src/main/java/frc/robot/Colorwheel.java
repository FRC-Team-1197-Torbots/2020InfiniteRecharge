package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Joystick;

public class Colorwheel {
    public Colorwheel(Solenoid solenoid, Joystick player2Joystick) {
        if(player2Joystick.getRawButton(4)) {
            solenoid.set(true);
        }
        else{
            solenoid.set(false);
        }
            
        }
    }
