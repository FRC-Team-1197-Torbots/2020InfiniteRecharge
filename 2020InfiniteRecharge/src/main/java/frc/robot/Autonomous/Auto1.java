package frc.robot.Autonomous;
import frc.robot.Mechanisms.*;
import frc.robot.Drive.*;

public class Auto1 {
    public static enum autoRun {
        INIT, Linear1, Pivot1, limeLightLineUp,
        Shoot;
        private autoRun() {}
    }
    
    private TorBalls torBalls;
    private linearTrajectory linear1;
    private pivotTrajectory pivot1;
    private limelightLineUp limeLight1;

    public Auto1(TorBalls torBalls, TorDrive torDrive) {
        this.torBalls = torBalls;
        linear1 = new linearTrajectory(torDrive, -2.0, 2.0);
    }
}
