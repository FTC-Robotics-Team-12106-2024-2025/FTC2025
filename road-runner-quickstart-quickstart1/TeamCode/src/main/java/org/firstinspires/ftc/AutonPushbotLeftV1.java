package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous (name="Auton For Left Field Push Bot", group = "Robot")

public class AutonPushbotLeftV1 extends AutonLibrary {

    @Override
    public void autonCommands() {
        strafeLeft(20);
        moveForward(80);
        for (int i = 0; i < 2; i++) {
            strafeLeft(7);
            moveBackward(80);
            moveForward(80);
        }
        strafeRight(40);
    }
}
