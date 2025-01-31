package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous (name="Auton For Left Field Ascent", group = "Robot")

public class AutonAscentLeft extends AutonLibrary {

    @Override
    public void autonCommands() {
        strafeLeft(13);
        moveForward(15);
        rotateRight(2);
        armUp(5);
        moveForward(10);



    }
}

