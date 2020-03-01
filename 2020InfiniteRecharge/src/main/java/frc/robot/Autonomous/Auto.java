package frc.robot.Autonomous;

import frc.robot.Mechanisms.*;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Drive.*;

public class Auto {
    private double autoToRun = 0;
    private TorBalls torBalls;
    private Joystick player1;

    private linearTrajectory linearRun;
    private pivotTrajectory pivotRun;
    
    public static enum testAuto {   
        IDLE, LinearRun, PivotRun;
        private testAuto() {}
    }
    
    private testAuto testAutoStateMachine = testAuto.IDLE;

    public Auto(TorBalls torBalls, TorDrive torDrive, Joystick player1) {
        this.torBalls = torBalls;
        this.player1 = player1;

        linearRun = new linearTrajectory(torDrive, 1.0, 100.0);//want to tune it with infinite time
        pivotRun = new pivotTrajectory(torDrive, 90.0, 100.0);
    }

    public void testRun() {
        torBalls.autoRun(0);
        if(testAutoStateMachine == testAuto.IDLE) {
            if(player1.getRawButton(5)) {
                linearRun.init();
                testAutoStateMachine = testAuto.LinearRun;
            } else if(player1.getRawButton(6)) {
                pivotRun.init();
                testAutoStateMachine = testAuto.PivotRun;
            }
        }
        switch(testAutoStateMachine) {
            case IDLE:
                break;
            case LinearRun:
                linearRun.run();
                if(linearRun.isDone()) {
                    testAutoStateMachine = testAuto.IDLE;
                }
                break;
            case PivotRun:
                pivotRun.run();
                if(pivotRun.isDone()) {
                    testAutoStateMachine = testAuto.IDLE;
                }
                break;
        }
    }

    public void setNumber(double x) {
        autoToRun = x;
    }

    public void run() {
        if(autoToRun == 1) {
            
        }
    }
}
