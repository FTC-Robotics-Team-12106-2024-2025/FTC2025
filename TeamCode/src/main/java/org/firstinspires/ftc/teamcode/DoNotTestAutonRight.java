package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous (name="DO NOT USE IN COMP", group = "Robot")

public class DoNotTestAutonRight extends NewAutonLib {

    @Override
    public void autonCommands() {
        strafeRight(2, 0, 0, 5);
    }
}
