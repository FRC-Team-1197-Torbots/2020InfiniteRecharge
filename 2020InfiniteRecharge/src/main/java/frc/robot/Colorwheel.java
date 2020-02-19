package frc.robot;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Joystick;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.ColorSensorV3;

public class Colorwheel {
    public String CurrentColor;
    private boolean hasBeenSpun = false;
    private int TimesSpun = 0;
    private int Sample = 0;

    private int MatchColorNumber;
    private String MatchColor;
    private VictorSPX wheeltalon;
    private ColorSensorV3 colorSensor;
    private Solenoid colorwheelPiston;
    private Joystick player2;
    private int DetectedColorInt;

    //Enum for State
    StateMachine StateMachE = StateMachine.ONCOLOUR;

    public Colorwheel(VictorSPX wheeltalon, ColorSensorV3 colorSensor, Joystick player2, Solenoid colorwheelPiston) {
        this.player2 = player2;
        this.wheeltalon = wheeltalon;
        this.colorSensor = colorSensor;
        this.colorwheelPiston = colorwheelPiston;
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
        if(Sample == 30) {
            Sample = 0;
        } else {
            Sample++;
        }
        
        Color ColorInput = colorSensor.getColor();
        int ProximityInput = colorSensor.getProximity();
        String ColorDetected = "None";
        
        //Color Detection Algorithm
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
        if(ProximityInput<15) {
            ColorDetected = "Out Of Range";
        }

        //Button Detection and State Machine
        //A = 1, B = 2, X = 3, Y = 4
    
        if(player2.getRawButton(7)) {
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

        if(player2.getRawButton(8) && hasBeenSpun == false) {
            CurrentColor = ColorDetected;
            hasBeenSpun = true;
            wheeltalon.set(ControlMode.PercentOutput, 0.5);
        } else if(TimesSpun == 4) {
            wheeltalon.set(ControlMode.PercentOutput, 0);
        } else if (hasBeenSpun == true) {
            if(Sample == 1 && ColorDetected.equals(CurrentColor)) {
                TimesSpun++;
            }
        } else {
            StateMachine();
        }
        
        //Display Data on Smart Dashboard
        SmartDashboard.putNumber("Red", ColorInput.red);
        SmartDashboard.putNumber("Green", ColorInput.green);
        SmartDashboard.putNumber("Blue", ColorInput.blue);
        SmartDashboard.putString("Color Detected", ColorDetected);
        SmartDashboard.putNumber("Sample", Sample);
        SmartDashboard.putNumber("Times Supn:", TimesSpun);
        SmartDashboard.putString("Color", MatchColor);
        SmartDashboard.putNumber("Distance", ProximityInput);
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



    public void Actuation(Solenoid colorwheelPiston, Joystick player2Joystick) {
        if(player2Joystick.getRawButton(5)) {
            colorwheelPiston.set(true);
        }
        else{
            colorwheelPiston.set(false);
        }
            
        }
    }
