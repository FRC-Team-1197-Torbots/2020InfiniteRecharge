package frc.robot.Mechanisms;

import edu.wpi.first.wpilibj.util.Color;
import com.revrobotics.ColorSensorV3;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ColorWheel {
    private int TimesSpun = 0;
    private int countedChanges = 0;
    private long currentTime = (long) (1000 * Timer.getFPGATimestamp());
    private long lastTimeButton1Pressed = currentTime;
    private long lastTimeButton2Pressed = currentTime;
    private long startTimeGoDown = currentTime;

    private int MatchColorNumber;
    private String MatchColor;
    private String lastColor;
    private VictorSPX wheeltalon;
    private Solenoid colorWheelPiston;
    private ColorSensorV3 m_colorSensor;
    private Joystick player2;
    private int DetectedColorInt;

    //Enum for State

    public static enum mainStateMachine {   
        IDLE, RunRotations, STARTforcolor, GO1LEFTforcolor, GO1RIGHTforcolor, GO2RIGHTforcolor, GoDown;
        private mainStateMachine() {}
    }

    mainStateMachine StateMachE = mainStateMachine.IDLE;

    private Color ColorInput;
    private int ProximityInput;
    private String ColorDetected;

    public ColorWheel(VictorSPX wheeltalon, ColorSensorV3 m_colorSensor, Joystick player2, Solenoid colorWheelPiston) {
        this.player2 = player2;
        this.wheeltalon = wheeltalon;
        this.m_colorSensor = m_colorSensor;
        this.colorWheelPiston = colorWheelPiston;
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
        currentTime = (long) (1000 * Timer.getFPGATimestamp());
        ColorInput = m_colorSensor.getColor();
        ProximityInput = m_colorSensor.getProximity();
        // SmartDashboard.putNumber("proimity input", ProximityInput);
        ColorDetected = "None";
        getCurrentColor();

        //Button Detection and State Machine
        //A = 1, B = 2, X = 3, Y = 4

        if(player2.getRawButton(1)) {
            if(currentTime - lastTimeButton1Pressed > 250) {
                lastTimeButton1Pressed = currentTime;
                if(StateMachE == mainStateMachine.IDLE) {
                    StateMachE = mainStateMachine.STARTforcolor;
                } else {
                    StateMachE = mainStateMachine.IDLE;
                }
            }
        } else if(player2.getRawButton(2)) {
            if(currentTime - lastTimeButton2Pressed > 250) {
                lastTimeButton2Pressed = currentTime;
                if(StateMachE == mainStateMachine.IDLE) {
                    StateMachE = mainStateMachine.RunRotations;
                } else {
                    StateMachE = mainStateMachine.IDLE;
                }
            }
        }
        runStateMachine();
        controlPneumatic();
        
        //Display Data on Smart Dashboard
        // SmartDashboard.putNumber("Red", ColorInput.red);
        // SmartDashboard.putNumber("Green", ColorInput.green);
        // SmartDashboard.putNumber("Blue", ColorInput.blue);
        // SmartDashboard.putString("Color Detected", ColorDetected);
        // SmartDashboard.putNumber("Times Supn:", TimesSpun);
        // SmartDashboard.putString("current state:", StateMachE.toString());
    }

    public void getCurrentColor() {
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
        } else {
            ColorDetected = "Green";
            DetectedColorInt = 3; 
        }
    }

    public void controlPneumatic() {
        if(StateMachE != mainStateMachine.IDLE) {
            colorWheelPiston.set(true);
        } else {
            colorWheelPiston.set(false);
        }
    }

    public void runStateMachine() {
        switch(StateMachE) {
            case IDLE:
                lastColor = ColorDetected;
                countedChanges = 0;
                wheeltalon.set(ControlMode.PercentOutput, 0.0);
                break;
            case STARTforcolor:
                lastColor = ColorDetected;
                if(ColorDetected.equals(MatchColor)) {
                    startTimeGoDown = currentTime;
                    StateMachE = mainStateMachine.GoDown;
                } else if(DetectedColorInt == MatchColorNumber-1 || (MatchColorNumber-1==0 && DetectedColorInt == 4)) {
                    StateMachE = mainStateMachine.GO1LEFTforcolor;
                } else if(DetectedColorInt == MatchColorNumber+1 || (MatchColorNumber+1==4 && DetectedColorInt == 1)) {
                    StateMachE = mainStateMachine.GO1RIGHTforcolor;
                // } else if(DetectedColorInt == MatchColorNumber+2 || (MatchColorNumber+2==4 && DetectedColorInt == 1) || (MatchColorNumber+2==5 && DetectedColorInt == 2)) {
                } else {
                    StateMachE = mainStateMachine.GO2RIGHTforcolor;
                }
                wheeltalon.set(ControlMode.PercentOutput, 0.0);
                break;
            case GO1LEFTforcolor:
                lastColor = ColorDetected;
                if(ColorDetected.equals(MatchColor)) {
                    startTimeGoDown = currentTime;
                    StateMachE = mainStateMachine.GoDown;
                } else if(DetectedColorInt == MatchColorNumber-1 || (MatchColorNumber-1==0 && DetectedColorInt == 4)) {
                    StateMachE = mainStateMachine.GO1LEFTforcolor;
                } else if(DetectedColorInt == MatchColorNumber+1 || (MatchColorNumber+1==4 && DetectedColorInt == 1)) {
                    StateMachE = mainStateMachine.GO1RIGHTforcolor;
                // } else if(DetectedColorInt == MatchColorNumber+2 || (MatchColorNumber+2==4 && DetectedColorInt == 1) || (MatchColorNumber+2==5 && DetectedColorInt == 2)) {
                } else {
                    StateMachE = mainStateMachine.GO2RIGHTforcolor;
                }
                wheeltalon.set(ControlMode.PercentOutput, 0.6);
                break;
            case GO1RIGHTforcolor:
                lastColor = ColorDetected;
                if(ColorDetected.equals(MatchColor)) {
                    startTimeGoDown = currentTime;
                    StateMachE = mainStateMachine.GoDown;
                } else if(DetectedColorInt == MatchColorNumber-1 || (MatchColorNumber-1==0 && DetectedColorInt == 4)) {
                    StateMachE = mainStateMachine.GO1LEFTforcolor;
                } else if(DetectedColorInt == MatchColorNumber+1 || (MatchColorNumber+1==4 && DetectedColorInt == 1)) {
                    StateMachE = mainStateMachine.GO1RIGHTforcolor;
                // } else if(DetectedColorInt == MatchColorNumber+2 || (MatchColorNumber+2==4 && DetectedColorInt == 1) || (MatchColorNumber+2==5 && DetectedColorInt == 2)) {
                } else {
                    StateMachE = mainStateMachine.GO2RIGHTforcolor;
                }
                wheeltalon.set(ControlMode.PercentOutput, -0.6);
                break;
            case GO2RIGHTforcolor:
                lastColor = ColorDetected;
                if(ColorDetected.equals(MatchColor)) {
                    startTimeGoDown = currentTime;
                    StateMachE = mainStateMachine.GoDown;
                } else if(DetectedColorInt == MatchColorNumber-1 || (MatchColorNumber-1==0 && DetectedColorInt == 4)) {
                    StateMachE = mainStateMachine.GO1LEFTforcolor;
                } else if(DetectedColorInt == MatchColorNumber+1 || (MatchColorNumber+1==4 && DetectedColorInt == 1)) {
                    StateMachE = mainStateMachine.GO1RIGHTforcolor;
                // } else if(DetectedColorInt == MatchColorNumber+2 || (MatchColorNumber+2==4 && DetectedColorInt == 1) || (MatchColorNumber+2==5 && DetectedColorInt == 2)) {
                } else {
                    StateMachE = mainStateMachine.GO2RIGHTforcolor;
                }
                wheeltalon.set(ControlMode.PercentOutput, -0.6);
                break;  
            case GoDown:
                lastColor = ColorDetected;
                if(currentTime > startTimeGoDown + 1500) {
                    StateMachE = mainStateMachine.IDLE;
                }
                wheeltalon.set(ControlMode.PercentOutput, 0.0);
                break;
            case RunRotations:
                if(!ColorDetected.equals(lastColor)) {
                    countedChanges++;
                    lastColor = ColorDetected;
                }
                if(countedChanges > 30) {
                    StateMachE = mainStateMachine.IDLE;
                }
                wheeltalon.set(ControlMode.PercentOutput, 1);
                break;
        } 
    }
}
