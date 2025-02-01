package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous (name="Auton For Right Field Push Bot", group = "Robot")

public class AutonPushBotRightV1 extends AutonLibrary {
    @Override
    public void autonCommands() {
        wristHalf(1);
        strafeRight(20);
        moveForward(30);
        for (int i = 0; i < 2; i++ ) {
            strafeRight(7);
            moveBackward(30);
            if (i < 1)
                moveForward(30);
        }

    }
}
