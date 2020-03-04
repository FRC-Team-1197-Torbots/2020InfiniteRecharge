package frc.robot.Autonomous;
import frc.robot.Mechanisms.*;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Drive.*;

public class Auto3 {
    public static enum autoRun {
        INIT, Linear1, limeLightLineUp,
        Shoot, Linear2, Pivot1, Linear3, Linear4,
        Pivot2, Linear5, Linear6, Pivot3, Linear7,
        limeLightLineUp2, Shoot2, Done;
        private autoRun() {}
    }
    
    private TorBalls torBalls;
    private linearTrajectory linear1;
    private linearTrajectory linear2;
    private linearTrajectory linear3;
    private linearTrajectory linear4;
    private linearTrajectory linear5;
    private linearTrajectory linear6;
    private linearTrajectory linear7;
    private pivotTrajectory pivot1;
    private pivotTrajectory pivot2;
    private pivotTrajectory pivot3;
    
    private limelightLineUp limeLight1;
    private double currentTime;
    private double startTime;

    private autoRun autoState = autoRun.INIT;

    public Auto3(TorBalls torBalls, TorDrive torDrive) {
        this.torBalls = torBalls;
        linear1 = new linearTrajectory(torDrive, -4.5, 3.0);
        linear2 = new linearTrajectory(torDrive, 1.5, 3.0);
        pivot1 = new pivotTrajectory(torDrive, -170, 3.0);
        linear3 = new linearTrajectory(torDrive, 2.0, 3.0);
        linear4 = new linearTrajectory(torDrive, -2.0, 3.0);
        pivot2 = new pivotTrajectory(torDrive, 30, 3.0);
        linear5 = new linearTrajectory(torDrive, 2.5, 3.0);
        linear6 = new linearTrajectory(torDrive, -2.5, 3.0);
        pivot3 = new pivotTrajectory(torDrive, 110, 3.0);
        linear7 = new linearTrajectory(torDrive, -1.5, 3.0);
        limeLight1 = new limelightLineUp(torDrive, 0.25, 1.5);
    }

    public void run() {
        currentTime = Timer.getFPGATimestamp();
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
                if(currentTime > startTime + 2.0) {
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
                    linear3.init();
                    autoState = autoRun.Linear3;
                }
                break;
            case Linear3:
                torBalls.autoRun(3);
                linear3.run();
                if(linear3.isDone()) {
                    linear4.init();
                    autoState = autoRun.Linear4;
                }
                break;
            case Linear4:
                torBalls.autoRun(0);
                linear4.run();
                if(linear4.isDone()) {
                    pivot2.init();
                    autoState = autoRun.Pivot2;
                }
                break;
            case Pivot2:
                torBalls.autoRun(3);
                pivot2.run();
                if(pivot2.isDone()) {
                    linear5.init();
                    autoState = autoRun.Linear5;
                }
                break;
            case Linear5:
                torBalls.autoRun(3);
                linear5.run();
                if(linear5.isDone()) {
                    linear6.init();
                    autoState = autoRun.Linear6;
                }
                break;
            case Linear6:
                torBalls.autoRun(0);
                linear6.run();
                if(linear6.isDone()) {
                    pivot3.init();
                    autoState = autoRun.Pivot3;
                }
                break;
            case Pivot3:
                torBalls.autoRun(0);
                pivot3.run();
                if(pivot3.isDone()) {
                    linear7.init();
                    autoState = autoRun.Linear7;
                }
                break;
            case Linear7:
                torBalls.autoRun(1);
                linear7.run();
                if(linear7.isDone()) {
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
                if(currentTime - startTime > 2.0) {
                    autoState = autoRun.Done;
                }
                break;
            case Done:
                torBalls.autoRun(0);
                break;
        }
    }
}
