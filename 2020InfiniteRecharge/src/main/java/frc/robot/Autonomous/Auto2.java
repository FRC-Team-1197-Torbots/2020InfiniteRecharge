package frc.robot.Autonomous;
import frc.robot.Mechanisms.*;

import java.lang.Thread.State;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Drive.*;

public class Auto2 {
    public static enum autoRun {
        INIT, Linear1, Pivot1, limeLightLineUp,
        Shoot, Pivot2, Linear2, Pivot3, Linear3, limeLightLineUp2, Shoot2, DONE;
        private autoRun() {}
    }
    
    private TorBalls torBalls;
    private TorDrive torDrive;
    private linearTrajectory linear1;
    private pivotTrajectory pivot1;
    private linearTrajectory linear3;
    private pivotTrajectory pivot2;
    private pivotTrajectory pivot3;
    private limelightLineUp limeLight1;
    private double currentTime;
    private double startTime;

    private double startPosition;

    private double startAngle;

    private autoRun autoState = autoRun.INIT;

    public Auto2(TorBalls torBalls, TorDrive torDrive) {
        this.torBalls = torBalls;
        this.torDrive = torDrive;
        linear1 = new linearTrajectory(torDrive, -6.5, 1.5);
        pivot1 = new pivotTrajectory(torDrive, -21, 0.75);
        linear3 = new linearTrajectory(torDrive, 6, 1.25);
        // pivot2 = new pivotTrajectory(torDrive, -159, 3.0);
        pivot3 = new pivotTrajectory(torDrive, 162, 1.5);
        limeLight1 = new limelightLineUp(torDrive, 0.1, 1.75);
    }

    public void run() {
        currentTime = Timer.getFPGATimestamp();
        switch(autoState) {
            case INIT:
                startAngle = torDrive.getHeading();
                linear1.init();
                torBalls.autoRun(0);
                autoState = autoRun.Linear1;
                break;
            case Linear1:
                linear1.run();
                torBalls.autoRun(0);
                if(linear1.isDone()) {
                    pivot1.init();
                    autoState = autoRun.Pivot1;
                }
                break;
            case Pivot1:
                pivot1.run();
                torBalls.autoRun(1);
                if(pivot1.isDone()) {
                    limeLight1.init();
                    autoState = autoRun.limeLightLineUp;
                }
                break;
            case limeLightLineUp:
                limeLight1.run();
                torBalls.autoRun(1);
                if(limeLight1.isDone()) {
                    startTime = currentTime;
                    autoState = autoRun.Shoot;
                }
                break;
            case Shoot:
                torBalls.autoRun(2);
                if(currentTime > startTime + 1.65) {
                    pivot2 = new pivotTrajectory(torDrive, 
                    ((-Math.PI - (torDrive.getHeading() - startAngle)) * (180 / Math.PI)), 2.0);
                    pivot2.init();
                    autoState = autoRun.Pivot2;
                }
                break;
            case Pivot2:
                torBalls.autoRun(3);
                pivot2.run();
                if(pivot2.isDone()) {
                    startPosition = torDrive.getPosition();
                    startTime = currentTime;
                    autoState = autoRun.Linear2;
                }
                break;
            case Linear2:
                if(torDrive.getPosition() - startPosition > 8.0) {
                    torBalls.autoRun(0);
                    torDrive.setMotorSpeeds(0.1, 0.1);
                } else {
                    torBalls.autoRun(3);
                    torDrive.setMotorSpeeds(0.25, 0.25);
                }
                if((torDrive.getPosition() > startPosition + 8.45)
                || (startTime - currentTime > 7.0)) {
                    torDrive.setMotorSpeeds(0.0, 0.0);
                    torBalls.autoRun(0);
                    Timer.delay(0.5);
                    pivot3.init();
                    autoState = autoRun.Pivot3;
                }
                break;
            case Pivot3:
                torBalls.autoRun(0);
                pivot3.run();
                if(pivot3.isDone()) {
                    linear3.init();
                    autoState = autoRun.Linear3;
                }
                break;
            case Linear3:
                linear3.run();
                torBalls.autoRun(0);
                if(linear3.isDone()) {
                    limeLight1.init();
                    autoState = autoRun.limeLightLineUp2;
                }
                break;
            case limeLightLineUp2:
                torBalls.autoRun(1);
                limeLight1.run();
                if(limeLight1.isDone()) {
                    startTime = currentTime;
                    autoState = autoRun.Shoot2;
                }
                break;
            case Shoot2:
                torBalls.autoRun(2);
                if(currentTime - startTime > 5.0) {
                    autoState = autoRun.DONE;
                }
                break;
            case DONE:
                torBalls.autoRun(0);
        }
    }
}
