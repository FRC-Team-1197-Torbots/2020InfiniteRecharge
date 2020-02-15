package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Turret {
    private final double dt = 0.005;
    private final TalonSRX turretMotor;
    private final TalonSRX hoodMotor;

    private final NetworkTable table;// for limelight
    private final NetworkTableEntry tx;
    private final NetworkTableEntry ty;
    //private final NetworkTableEntry ta;
    private double x;
    private double y;

    //--  PID for the horizontal spinner of the turret  --//
    private TorDerivative horizontalDerivative;
    private final double horizkP = 0.04;
    private final double horizkI = 0;
    private final double horizkD = 0.001;
        //Gear Ratio
        //Angle
        //Error
    private double horizontalVelocity;
    private double horizontalIntegral = 0;
    
    //--  PID for the hood angle adjustment  --//
    private TorDerivative hoodDerivative;
    private final double hoodkP = 0.015;
    private final double hoodkI = 0.001;
    private final double hoodkD = 0;
    private final double hoodGearRatio = 40;//from the encoder to the movement fo the hood
    private double currentHoodAngle;
    private double currentHoodError;
    private double hoodVelocity;
    private double hoodIntegral = 0; 

    private final double angle1 = 15;
    private final double startAngleOfHood = 35;
    private double angle2;
    private double hoodAngleToSet;

    private double horizontalSpeedToSet;
    private double hoodSpeedToSet;

    public Turret(TalonSRX turretMotor, TalonSRX hoodMotor) {
        table = NetworkTableInstance.getDefault().getTable("limelight-turret");
        tx = table.getEntry("tx");
        ty = table.getEntry("ty");
        this.turretMotor = turretMotor;
        this.hoodMotor = hoodMotor;
        this.hoodMotor.setInverted(true);

        hoodMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        hoodMotor.setSelectedSensorPosition(0, 0, 0);
        horizontalDerivative = new TorDerivative(dt);
        horizontalDerivative.resetValue(0);
        hoodDerivative = new TorDerivative(dt);
        hoodDerivative.resetValue(0);
    }

    public void resetEncoder() {
        hoodMotor.setSelectedSensorPosition(0, 0, 0);
    }

    public void run(boolean run) {//gets angle of the hood
        x = tx.getDouble(0.0);
        SmartDashboard.putNumber("X:",x);
        horizontalSpeedToSet = horizPID(x + 2);

        y = ty.getDouble(0.0);
        SmartDashboard.putNumber("Y:",y);
        angle2 = y + angle1;
        hoodAngleToSet = startAngleOfHood - angle2;
        SmartDashboard.putNumber("hood angle to set:", hoodAngleToSet);
        hoodSpeedToSet = hoodPID(hoodAngleToSet);
        
        if(run) {
            turretMotor.set(ControlMode.PercentOutput, horizontalSpeedToSet);
            hoodMotor.set(ControlMode.PercentOutput, hoodSpeedToSet);
        }           
    }

    public double hoodPID(double hoodAngleToSet) {
        currentHoodAngle = (360 * (-hoodMotor.getSelectedSensorPosition(0) / (4096 * hoodGearRatio)));
        currentHoodError = hoodAngleToSet - currentHoodAngle;
        SmartDashboard.putNumber("current hood error", currentHoodError);
        SmartDashboard.putNumber("current hood angle over zero", currentHoodAngle);
        //everything goes off of current hood error
        hoodVelocity = hoodDerivative.estimate(hoodAngleToSet);
        hoodIntegral += currentHoodError * dt;
        if(currentHoodError < 0.1) {
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