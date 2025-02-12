package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
@Autonomous (name="Main Left side auton", group = "Robot")
public class AutonLowBucketLeft extends AutonLibrary {
    @Override
    public void autonCommands() {
        wristHalf(1);
        //moveForward(1);
        strafeLeft(1);
        rotateLeft(2);
        score();






    }
}