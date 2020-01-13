package frc.robot;

import edu.wpi.first.wpilibj.util.Color;
import com.revrobotics.ColorSensorV3;

//import java.lang.Thread.State;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ColorWheel {
    private int MatchColorNumber;
    private String MatchColor;
    private VictorSPX wheeltalon;
    private ColorSensorV3 m_colorSensor;
    private Joystick player1;
    private int DetectedColorInt;

    //Enum for State
    StateMachine StateMachE = StateMachine.ONCOLOUR;

    public ColorWheel(VictorSPX wheeltalon, ColorSensorV3 m_colorSensor, Joystick player1) {
        this.player1 = player1;
        this.wheeltalon = wheeltalon;
        this.m_colorSensor = m_colorSensor;
    }

    public void setColor(int Color) {
        if(Color == 1) {
            MatchColor = "Blue";
        } else if(Color == 2) {
            MatchColor = "Green";
        } else if(Color == 3) {
            MatchColor = "Red";
        } else {
            MatchColor = "Yellow";
        }
        MatchColorNumber = Color;
    }

    public void Main(){
        Color ColorInput = m_colorSensor.getColor();
        int ProximityInput = m_colorSensor.getProximity();
        String ColorDetected = "None";
    
        if(ColorInput.red > ColorInput.blue && ColorInput.red > ColorInput.green && ColorInput.red > 0.4){
            ColorDetected = "Red";
            DetectedColorInt = 2;
        } else if(Math.abs(ColorInput.red-ColorInput.green) < 0.25) {
            ColorDetected = "Yellow";
            DetectedColorInt = 1;
        } else if(Math.abs(ColorInput.green-ColorInput.blue) < 0.25) {
            ColorDetected = "Blue";
            DetectedColorInt = 4;
        } else if(ColorInput.green > ColorInput.blue && ColorInput.green > ColorInput.red) {
            ColorDetected = "Green";
            DetectedColorInt = 3; 
        } else {
            ColorDetected = "Invalid";
        }
        if(ProximityInput<50) {
            ColorDetected = "Out Of Range";
        }

        SmartDashboard.putNumber("Red", ColorInput.red);
        SmartDashboard.putNumber("Green", ColorInput.green);
        SmartDashboard.putNumber("Blue", ColorInput.blue);
        SmartDashboard.putString("Color Detected", ColorDetected);
        //SmartDashboard.putNumber("Proximity", proximity);
   
        //A = 1, B = 2, X = 3, Y = 4
        if(player1.getRawButton(1)) {
            System.out.print("l");
            StateMachE = StateMachine.GO1LEFT;
            if(ColorDetected.equals(MatchColor)) {
                StateMachE = StateMachine.ONCOLOUR;
            } else if(DetectedColorInt == MatchColorNumber-1 || (MatchColorNumber-1==0 && DetectedColorInt == 4)) {
                StateMachE = StateMachine.GO1LEFT;
            } else if(DetectedColorInt == MatchColorNumber+1 || (MatchColorNumber+1==4 && DetectedColorInt == 1)) {
                StateMachE = StateMachine.GO1RIGHT;
            } else if(DetectedColorInt == MatchColorNumber+2 || (MatchColorNumber+2==4 && DetectedColorInt == 1) || (MatchColorNumber+2==5 && DetectedColorInt == 2)) {
                StateMachE = StateMachine.GO2RIGHT;
            }
        }
        StateMachine();
    }

    public static enum StateMachine {
        ONCOLOUR, GO1LEFT, GO1RIGHT, GO2RIGHT;
        private StateMachine() {}
    }

    public void StateMachine() {
        switch(StateMachE) {
            case ONCOLOUR:
                wheeltalon.set(ControlMode.PercentOutput, 0);
                break;
            case GO1LEFT:
                wheeltalon.set(ControlMode.PercentOutput, 0.25);
                break;
            case GO1RIGHT:
                wheeltalon.set(ControlMode.PercentOutput, -0.25);
                break;
            case GO2RIGHT:
                wheeltalon.set(ControlMode.PercentOutput, -0.5);
                break;
        } 
    }
}
