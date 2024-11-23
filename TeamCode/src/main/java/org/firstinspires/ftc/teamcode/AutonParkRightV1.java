package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous (name="Auton For Right Field", group = "Robot")

public class AutonParkRightV1 extends AutonLibrary {


    @Override
    public void autonCommands() {
        strafeRight(15);
        killSwitch(10);

    }
}
