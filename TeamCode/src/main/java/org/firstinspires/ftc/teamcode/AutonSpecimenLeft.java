package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous (name="Auton For Clip Specimen Left", group = "Robot")

public class AutonSpecimenLeft extends AutonLibrary {

    @Override
    public void autonCommands() {
        wristHalf(1);
        moveForward(1);
        armDown(10);







    }
}