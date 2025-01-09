package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous (name="Auton For Left Field", group = "Robot")

public class AutonParkLeftV1 extends  AutonLibrary{

    @Override
    public void autonCommands() {
        strafeRight(200);
        killSwitch(10);

    }
}
