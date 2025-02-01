package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous (name="Auton For Right Field Push Bot", group = "Robot")

public class AutonPushBotRightV1 extends AutonLibrary {
    @Override
    public void autonCommands() {
        wristHalf(1);
        strafeRight(12);
        moveForward(15);
        for (int i = 0; i < 2; i++ ) {
            strafeRight(7);
            moveBackward(15);
            if (i < 1)
                moveForward(15);
        }

    }
}
