package frc.robot.Autonomous;
import frc.robot.Mechanisms.*;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Drive.*;

public class Auto3 {
    public static enum autoRun {
        INIT, Linear1, PivotA, limeLightLineUp,
        Shoot, Linear2,
        Pivot1, Linear3, Linear4,
        // Pivot2, Linear5, Linear6, 
        Pivot3,
        limeLightLineUp2, Shoot2, Done;
        private autoRun() {}
    }
    
    private TorBalls torBalls;
    private TorDrive torDrive;
    private linearTrajectory linear1;
    private linearTrajectory linear2;
    // private linearTrajectory linear3;
    private linearTrajectory linear4;
    // private linearTrajectory linear5;
    // private linearTrajectory linear6;
    // private linearTrajectory linear7;
    private pivotTrajectory pivot1;
    private pivotTrajectory pivotA;
    // private pivotTrajectory pivot2;
    private pivotTrajectory pivot3;
    
    private limelightLineUp2 limeLight1;
    private double currentTime;
    private double startTime;

    private double lastPosition;
    private double currentPosition;

    private autoRun autoState = autoRun.INIT;

    public Auto3(TorBalls torBalls, TorDrive torDrive) {
        this.torBalls = torBalls;
        this.torDrive = torDrive;
        linear1 = new linearTrajectory(torDrive, -5.25, 3.0);
        linear2 = new linearTrajectory(torDrive, 1.5, 1.5);
        pivotA = new pivotTrajectory(torDrive, 15, 0.75);
        pivot1 = new pivotTrajectory(torDrive, 176, 3.0);
        // linear3 = new linearTrajectory(torDrive, 2.25, 2.0);
        linear4 = new linearTrajectory(torDrive, -2.0, 1.5);
        // pivot2 = new pivotTrajectory(torDrive, 10, 3.0);
        // linear5 = new linearTrajectory(torDrive, 2.5, 3.0);
        // linear6 = new linearTrajectory(torDrive, -2.5, 3.0);
        pivot3 = new pivotTrajectory(torDrive, -168, 1.5);
        // linear7 = new linearTrajectory(torDrive, -1.5, 0.0);
        limeLight1 = new limelightLineUp2(torDrive, 0.15, 1.5);
    }

    public void run() {
        currentTime = Timer.getFPGATimestamp();
        currentPosition = torDrive.getPosition();
        switch(autoState) {
            case INIT:
                linear1.init();
                torBalls.autoRun(0);
                currentTime = startTime;
                autoState = autoRun.Linear1;
                break;
            case Linear1:
                linear1.run();
                torBalls.autoRun(0);
                if(linear1.isDone()) {
                    pivotA.init();
                    autoState = autoRun.PivotA;
                }
                break;
            case PivotA:
                pivotA.run();
                torBalls.autoRun(1);
                if(pivotA.isDone()) {
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
                torBalls.autoRun(5);
                if(currentTime > startTime + 1.75) {
                    linear2.init();
                    autoState = autoRun.Linear2;
                }
                break;
            case Linear2:
                torBalls.autoRun(0);
                linear2.run();
                if(linear2.isDone()) {
                    pivot1.init();
                    autoState = autoRun.Pivot1;
                }
                break;
            case Pivot1:
                torBalls.autoRun(3);
                pivot1.run();
                if(pivot1.isDone()) {
                    lastPosition = currentPosition;
                    autoState = autoRun.Linear3;
                }
                break;
            case Linear3:
                torBalls.autoRun(3);
                torDrive.setMotorSpeeds(0.13, 0.13);
                if(currentPosition > lastPosition + 3.6) {
                    torDrive.setMotorSpeeds(0.0, 0.0);
                    linear4.init();
                    autoState = autoRun.Linear4;
                }
                break;
            case Linear4:
                torBalls.autoRun(0);
                linear4.run();
                if(linear4.isDone()) {
                    pivot3.init();
                    autoState = autoRun.Pivot3;
                }
                break;
            case Pivot3:
                torBalls.autoRun(1);
                pivot3.run();
                if(pivot3.isDone()) {
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
                if(currentTime - startTime > 3.0) {
                    autoState = autoRun.Done;
                }
                break;
            case Done:
                torBalls.autoRun(0);
                break;
        }
    }
}
