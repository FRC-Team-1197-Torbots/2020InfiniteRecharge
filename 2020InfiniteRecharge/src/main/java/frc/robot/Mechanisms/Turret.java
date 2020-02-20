package frc.robot.Mechanisms;

import frc.robot.PID_Tools.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Turret {
    private final double dt = 0.005;
    private final VictorSPX turretMotor;
    private final TalonSRX hoodMotor;

    private final NetworkTable table;// for limelight
    private final NetworkTableEntry tx;
    private final NetworkTableEntry ty;
    //private final NetworkTableEntry ta;
    private double x;
    private double y;

    //--  PID for the horizontal spinner of the turret  --//
    private TorDerivative horizontalDerivative;
    private final double horizkP = 0.03;
    private final double horizkI = 0;
    private final double horizkD = 0;
        //Gear Ratio
        //Angle
        //Error
    private double horizontalVelocity;
    private double horizontalIntegral = 0;
    
    //--  PID for the hood angle adjustment  --//
    private TorDerivative hoodDerivative;
    private final double hoodkP = 0.02;
    private final double hoodkI = 0;
    private final double hoodkD = 0;
    private final double hoodGearRatio = 400;//from the encoder to the movement fo the hood
    private double currentHoodAngle;
    private double currentHoodError;
    private double hoodVelocity;
    private double hoodIntegral = 0; 

    private final double height1 = 15;
    private final double height2 = 94.5;
    private final double angle1 = 15;
    private double angle2;
    private double distance;
    private double intermediateCalculation;
    private double hoodAngleToSet;

    private double horizontalSpeedToSet;
    private double hoodSpeedToSet;

    private double DebugAngle;

    public Turret(VictorSPX turretMotor, TalonSRX hoodMotor) {
        table = NetworkTableInstance.getDefault().getTable("limelight-turret");
        tx = table.getEntry("tx");
        ty = table.getEntry("ty");
        this.turretMotor = turretMotor;
        this.hoodMotor = hoodMotor;

        hoodMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        hoodMotor.setSelectedSensorPosition(0, 0, 0);
        horizontalDerivative = new TorDerivative(dt);
        horizontalDerivative.resetValue(0);
        hoodDerivative = new TorDerivative(dt);
        hoodDerivative.resetValue(0);

        DebugAngle = 40.0;
    }

    public void run(boolean run, boolean xButton, boolean yButton, boolean DEBUG) {//gets angle of the hood

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!DEBUG!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//
        if(xButton) {
            DebugAngle += 1.0 / 40.0;
        }

        if(yButton) {
            DebugAngle -= 1.0 / 40.0;
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!DEBUG!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!//

        x = tx.getDouble(0.0);
        y = ty.getDouble(0.0);

        SmartDashboard.putNumber("X:",x);
        horizontalSpeedToSet = horizPID(x);

        SmartDashboard.putNumber("Y:",y);        
        distance = (height2-height1) * (1 / Math.tan(Math.toRadians(angle1+angle2)));
        SmartDashboard.putNumber("D", distance);
        intermediateCalculation = ((98.25 - height1) / distance);
        hoodAngleToSet = Math.atan(intermediateCalculation + (15/distance));//hood angle is radians
        hoodAngleToSet *= 180 / (Math.PI);//hood angle is now degrees
        if(DEBUG) {
            hoodAngleToSet = DebugAngle;
        }
        hoodSpeedToSet = hoodPID(hoodAngleToSet);
        
        if(run) {
            turretMotor.set(ControlMode.PercentOutput, horizontalSpeedToSet);
            hoodMotor.set(ControlMode.PercentOutput, hoodSpeedToSet);
        }
        SmartDashboard.putNumber("angle2", angle2);
        SmartDashboard.putNumber("Intermediate Calculation" , intermediateCalculation);
        SmartDashboard.putNumber("Hood Angle" , hoodAngleToSet);           
        SmartDashboard.putNumber("horizSpeedtoSet" , horizontalSpeedToSet);
    }

    public double hoodPID(double hoodAngleToSet) {
        currentHoodAngle = 40 - 
            (360 * 20 * (hoodMotor.getSelectedSensorPosition(0) / (4096 * hoodGearRatio)));
        currentHoodError = hoodAngleToSet - currentHoodAngle;
        //everything goes off of current hood error
        hoodVelocity = hoodDerivative.estimate(hoodAngleToSet);
        hoodIntegral += currentHoodError * dt;
        if(currentHoodError < 0.25) {
            hoodIntegral = 0;
        }
        if(hoodIntegral * hoodkI > 0.5) {
            hoodIntegral = 0.5 / hoodkI;
        } else if(hoodIntegral * hoodkI < -0.5) {
            hoodIntegral = -0.5 / hoodkI;
        }
        return ((currentHoodError * hoodkP) + 
            (hoodVelocity * hoodkD) + 
            (hoodIntegral * hoodkI));
            
    }

    public double horizPID(double currentAngle) {
        horizontalVelocity = horizontalDerivative.estimate(currentAngle);
        horizontalIntegral += currentAngle * dt;
        if(currentAngle < 0.25) {
            horizontalIntegral = 0;
        }
        if(horizontalIntegral * horizkI > 0.5) {
            horizontalIntegral = 0.5 / horizkI;
        } else if(horizontalIntegral * horizkI < -0.5) {
            horizontalIntegral = -0.5 / horizkI;
        }
        return ((currentAngle * horizkP) + 
            (horizontalVelocity * horizkD) + 
            (horizontalIntegral * horizkI));            
    }
}