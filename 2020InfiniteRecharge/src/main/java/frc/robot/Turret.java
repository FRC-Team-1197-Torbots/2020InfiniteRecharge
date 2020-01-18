package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

public class Turret {
    private final double dt = 0.005;
    private final TalonSRX turretMotor;
    private final VictorSPX hoodMotor;

    private final NetworkTable table;// for limelight
    private final NetworkTableEntry tx;
    private final NetworkTableEntry ty;
    //private final NetworkTableEntry ta;
    private double x;
    private double y;

    //PID for the horizontal spinner of the turret
    private final double hozkP = 0.03;
    private final double hozkI = 0;
    private final double hozkD = 0;
    private TorDerivative horizontalDerivative;
    private TorDerivative hoodDerivative;
    private double horizontalVelocity;
    private double horizontalIntegral = 0;
    //PID for the hood angle adjustment
    private final double hoodkP = 0;
    private final double hoodkI = 0;
    private final double hoodkD = 0;
    private final double hoodGearRatio = 500;//from the encoder to the movement fo the hood
    private double currentHoodAngle;
    private double currentHoodError;
    private double hoodVelocity;
    private double hoodIntegral = 0; 


    private final double height1 = 31;
    private final double height2 = 94.5;
    private final double angle1 = 15;
    private double angle2;
    private double distance;
    private double intermediateCalculation;
    private double hoodAngleToSet;

    private double horizontalSpeedToSet;
    private double hoodSpeedToSet;

    public Turret(TalonSRX turretMotor, VictorSPX hoodMotor) {
        table = NetworkTableInstance.getDefault().getTable("limelight-ball");
        tx = table.getEntry("tx");
        ty = table.getEntry("ty");
        this.turretMotor = turretMotor;
        this.hoodMotor = hoodMotor;

        hoodMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        hoodMotor.setSelectedSensorPosition(0, 0, 0);

        horizontalDerivative = new TorDerivative(dt);
        horizontalDerivative.resetValue(0);
    }

    public void run(boolean run) {//gets angle of the hood
        x = tx.getDouble(0.0);
        SmartDashboard.putNumber("X:",x);
        horizontalSpeedToSet = hozPID(x);
        if(run) {
            turretMotor.set(ControlMode.PercentOutput, horizontalSpeedToSet);
        }


        y = ty.getDouble(0.0);
        SmartDashboard.putNumber("Y:",y);
        angle2 = y;
        distance = (height2-height1) * (1 / Math.tan(Math.toRadians(angle1+angle2)));
        SmartDashboard.putNumber("D", distance);
        intermediateCalculation = ((98.25 - height1) / distance);
        hoodAngleToSet = Math.atan(intermediateCalculation + (15/distance));//hood angle is radians
        hoodAngleToSet *= 180 / (Math.PI);//hood angle is now degrees

        SmartDashboard.putNumber("Intermediate Calculation" , intermediateCalculation);
        SmartDashboard.putNumber("Hood Angle" , hoodAngleToSet);   
        
        
    }

    public double hoodPID(double hoodAngleToSet) {
        currentHoodAngle = 40 - 
            (360 * (hoodMotor.getSelectedSensorPosition(0) / (4096 * hoodGearRatio)));
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

    public double hozPID(double currentAngle) {
        horizontalVelocity = horizontalDerivative.estimate(currentAngle);
        horizontalIntegral += currentAngle * dt;
        if(currentAngle < 0.25) {
            horizontalIntegral = 0;
        }
        if(horizontalIntegral * hozkI > 0.5) {
            horizontalIntegral = 0.5 / hozkI;
        } else if(horizontalIntegral * hozkI < -0.5) {
            horizontalIntegral = -0.5 / hozkI;
        }
        return ((currentAngle * hozkP) + 
            (horizontalVelocity * hozkD) + 
            (horizontalIntegral * hozkI));
    }
}
