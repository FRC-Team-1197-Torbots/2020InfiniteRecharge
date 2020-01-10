package frc.robot;

import edu.wpi.first.wpilibj.util.Color;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.ColorSensorV3;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ColorWheel {
    private String MatchColor;
    private VictorSPX wheeltalon;
    private ColorSensorV3 m_colorSensor;
    private Joystick player1;
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
    }

    public void Main(){
        Color ColorInput = m_colorSensor.getColor();
        int ProximityInput = m_colorSensor.getProximity();
        String ColorDetected = "None";
    
        if(ColorInput.red > ColorInput.blue && ColorInput.red > ColorInput.green && ColorInput.red > 0.4){
            ColorDetected = "Red";
        } else if(Math.abs(ColorInput.red-ColorInput.green) < 0.25) {
            ColorDetected = "Yellow";
        } else if(Math.abs(ColorInput.green-ColorInput.blue) < 0.25) {
            ColorDetected = "Blue";
        } else if(ColorInput.green > ColorInput.blue && ColorInput.green > ColorInput.red) {
            ColorDetected = "Green"; 
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
   
        double output = 0.2;
        //A = 1, B = 2, X = 3, Y = 4
        if(!ColorDetected.equals(MatchColor) && player1.getRawButton(1)) {
            wheeltalon.set(ControlMode.PercentOutput, -output);
        } else {
            wheeltalon.set(ControlMode.PercentOutput, 0);
        }
    }
}
