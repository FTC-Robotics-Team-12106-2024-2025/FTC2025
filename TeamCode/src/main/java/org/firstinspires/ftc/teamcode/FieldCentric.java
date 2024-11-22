package org.firstinspires.ftc.teamcode;

import android.media.MediaPlayer;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp

public class FieldCentric extends LinearOpMode {
     Gamepad driveGamepad = new Gamepad();
     Gamepad manipulatorGamepad = new Gamepad();

     // Variables
     public double wrist = 4;
     public int armPose = 0;
     public int slidePose = 0;
     public double extenderPose = 0;
     public int extenderTwo = 0;
    @Override
    
    //Defines the motor
    public void runOpMode() {
        // Drive Motors
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft"); //Port 0
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight"); //Port 1
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");


        DcMotor arm = hardwareMap.get(DcMotor.class,"armOne");
        Servo clawRotate = hardwareMap.get(Servo.class,"clawRotate");
        Servo clawClamp = hardwareMap.get(Servo.class,"clawClamp");
        DcMotor leftSlide = hardwareMap.get(DcMotor.class, "leftSlide");
        Servo Extendo = hardwareMap.get(Servo.class,"Extendo");
        //DcMotor Extendo = hardwareMap.get(DcMotor.class,"Extendo")

        // Setting Positions
        leftSlide.setTargetPosition(0);
        leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftSlide.setPower(0.65);

        arm.setTargetPosition(0);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(0.65);

        /*
        Extendo.setTargetPosition(0);
        Extendo.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Extendo.setPower(0.65);
         */

        // Gyroscope
        BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);

        MediaPlayer mediaPlayer;
        mediaPlayer = MediaPlayer.create(hardwareMap.appContext, R.raw.horn);

        waitForStart();

        if (isStopRequested()) {
            mediaPlayer.release();
            return;
        }
        
        while (opModeIsActive()) {
            // Getting inputs
            driveGamepad.copy(gamepad1);
            manipulatorGamepad.copy(gamepad2);

            //
            // Drive
            //
            double wheelCPR = 423.2116; //Counts per revolution
            double linearCPR = 72.1;
            float y = -driveGamepad.left_stick_y;
            float x = driveGamepad.left_stick_x;
         
            if (driveGamepad.dpad_left) {
                 x = -0.25f;
            }
            if (driveGamepad.dpad_right) {
                 x = 0.25f;
            }
            if (driveGamepad.dpad_up) {
                y = 0.25f;
            }
            if (driveGamepad.dpad_down) {
                 y = -0.25f;
            }

            double currentHeading = -imu.getAngularOrientation().firstAngle;
//            boolean honk = driveGamepad.left_stick_button;
            double rotateRight = driveGamepad.right_trigger;
            double rotateLeft = driveGamepad.left_trigger;
            double rx = rotateRight-rotateLeft;
            double xRot = x * Math.cos(currentHeading) - y * Math.sin(currentHeading);
            double yRot = y * Math.cos(currentHeading) + x * Math.sin(currentHeading);

            // Motor powers
            double fl = (yRot+xRot+rx);
            double fr = (xRot-yRot+rx);
            double bl = (yRot-xRot+rx);
            double br = (-yRot-xRot+rx);

            //stops it from going greater than 1/-1
            double maxNumber = Math.max(Math.abs(xRot)+Math.abs(yRot)+Math.abs(rx),1);

            //powers the motor for wheels
            frontLeft.setPower(fl / maxNumber * .85);
            frontRight.setPower(fr / maxNumber * .85);
            backLeft.setPower(bl / maxNumber * .85);
            backRight.setPower(br / maxNumber * .85);

            //
            // Arm Angle
            //
            armPose += (-manipulatorGamepad.right_stick_y * 10);

            if (armPose < 0) {
                armPose = 0;
            }
            if (armPose > 1833) {
                armPose = 1833;
            }
            arm.setTargetPosition(armPose);


            telemetry.addData("Arm Pose: ", armPose);

            //
            //Extender
            //
            extenderPose += (manipulatorGamepad.left_stick_x);

            //extenderTwo += (manipulatorGamepad.left_stick_x);
            if (manipulatorGamepad.dpad_up) {
               extenderTwo--;
               extenderPose -= 0.01F;
            }
            if (manipulatorGamepad.dpad_down) {
                extenderTwo++;
                extenderPose += 0.01F;
            }
            telemetry.addData("Extender Pose for Servo: ", extenderPose);
            telemetry.addData("Extender Pose for Motor", extenderTwo);
            if (extenderPose < 0) {
                extenderPose = 0;
            }
            Extendo.setPosition(extenderPose);
            //Extendo.setPosition(extenderTwo);

            //
            // Linear Slide
            //
            slidePose += (-manipulatorGamepad.left_stick_y * 10);


            // Limits
            if (slidePose < 0) {
                slidePose = 0;
            }
            if (slidePose > 6510) {
                slidePose = 6510;
            }
            leftSlide.setTargetPosition(slidePose);

            //
            // Claw
            //
            //For up-movement of linear slide


            if (manipulatorGamepad.left_bumper) {
                clawClamp.setPosition(1);
            }
            if (manipulatorGamepad.right_bumper) {
                clawClamp.setPosition(0);
            }
            telemetry.addData("Claw Position", clawClamp.getPosition());


            if (driveGamepad.guide) {
                if (!mediaPlayer.isPlaying())
                    mediaPlayer.start();
                else {
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                }
            }
            if (manipulatorGamepad.left_trigger >= .9) {
                wrist -= .05;
            }
           if (manipulatorGamepad.right_trigger >= .9) {
               wrist += .05;
           }
           if (wrist >= 1) {
               wrist = 1;
           }
           if (wrist <= 0) {
               wrist = 0;
           }
           if (manipulatorGamepad.options) {
               wrist = .4;
           }
           clawRotate.setPosition(wrist);

           telemetry.update();
        }
    }
}



