package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
@Autonomous (name="Main Left side auton", group = "Robot")
public class AutonLowBucketLeft extends AutonLibrary {
    @Override
    public void autonCommands() {
        wristHalf(1);
        //moveForward(1);
        movement(10);
        gyrorotate(135);
        score();
        moveForward(20);
        movement(-10);
        wristHalf(1);
        slideZero(2);
        armDown(2);
        gyrorotate( 45);
    }
}