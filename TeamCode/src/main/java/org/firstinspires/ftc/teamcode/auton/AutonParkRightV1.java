package org.firstinspires.ftc.teamcode.auton;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.auton.AutonLibrary;

@Autonomous (name="Auton For Right Field", group = "Robot")

public class AutonParkRightV1 extends LinearOpMode {
    DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft"); //Port 0
    DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight"); //Port 1
    DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");
    DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");


    DcMotor arm = hardwareMap.get(DcMotor.class,"armOne");
    Servo clawRotate = hardwareMap.get(Servo.class,"clawRotate");
    Servo clawClamp = hardwareMap.get(Servo.class,"clawClamp");
    DcMotor leftSlide = hardwareMap.get(DcMotor.class, "leftSlide");
    public void runOpMode() {

    }
}
