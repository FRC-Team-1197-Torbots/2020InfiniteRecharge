package frc.robot.Autonomous;
import frc.robot.Mechanisms.*;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Drive.*;

public class Auto3 {
    public static enum autoRun {
        INIT, Linear1, limeLightLineUp,
        Shoot, DONE;
        private autoRun() {}
    }
    
    private TorBalls torBalls;
    private linearTrajectory linear1;
    private limelightLineUp limeLight1;
    private double currentTime;
    private double startTime;

    private autoRun autoState = autoRun.INIT;

    public Auto3(TorBalls torBalls, TorDrive torDrive) {
        this.torBalls = torBalls;
        linear1 = new linearTrajectory(torDrive, -5.5, 5.0);
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
                if(currentTime - startTime > 1.0) {
                    torBalls.autoRun(1);
                } else {
                    torBalls.autoRun(0);
                }
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
                    autoState = autoRun.DONE;
                }
                break;
            case DONE:
                torBalls.autoRun(0);
                break;
        }
    }
}
