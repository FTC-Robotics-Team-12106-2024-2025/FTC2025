package org.firstinspires.ftc.teamcode;

import android.media.MediaPlayer;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


@TeleOp

public class FieldCentric extends LinearOpMode {
     Gamepad driveGamepad = new Gamepad();
     Gamepad manipulatorGamepad = new Gamepad();

     // Variables

     public int armPose = 0;

     public int slidePose = 0;
     public double extenderPose = 0;
     public int extenderTwo = 0;
     public int clampPose = 0;


     int colorFilter = 0; // 0 = filter for red, 1 = for blue, 2 = for yellow
    @Override

    //Defines the motor
    public void runOpMode() {
        // Drive Motors
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft"); //Port 0
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight"); //Port 1
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");


        Servo arm = hardwareMap.get(Servo.class,"arm");
        DcMotor leftSlide = hardwareMap.get(DcMotor.class, "leftSlide");
        CRServo intakeOne = hardwareMap.get(CRServo.class,"intakeOne");
        CRServo intakeTwo = hardwareMap.get(CRServo.class,"intakeTwo");
        Servo wrist = hardwareMap.get(Servo.class,"wrist");

        ColorSensor color = hardwareMap.get(ColorSensor.class, "color");
        DistanceSensor colorDistance = hardwareMap.get(DistanceSensor.class, "color");
        ElapsedTime timer = new ElapsedTime();
        boolean sortActive = false;

        // Setting Positions
        leftSlide.setTargetPosition(0);
        leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftSlide.setPower(0.65);



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

        int[][] thresholds = {{1,1,1},{1,1,1},{1,1,1}};// ind 0 = red, ind 1 = blue, ind 2 = green; List of thresholds per sample; red, blue, yellow

        HashMap<Integer, String> colorData = new HashMap<Integer, String>();
        colorData.put(0, "Filtering for RED samples");
        colorData.put(1, "Filtering for BLUE samples");
        colorData.put(2, "Filtering for YELLOW samples");

        HashMap<Integer, int[]> sampleHave = new HashMap<>();// colors when sample is held
        sampleHave.put(0, new int[] {211,41,37});
        sampleHave.put(1, new int[] {55,89,223});
        sampleHave.put(2, new int[] {241,178,14});

        HashMap<Integer, int[]> sampleLook = new HashMap<>(); // colors when looking for sample
        sampleLook.put(0, new int[] {233,141,138});
        sampleLook.put(1, new int[] {155,172,239});
        sampleLook.put(2, new int[] {246, 217, 135});

        waitForStart();

        if (isStopRequested()) {
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
//          boolean honk = driveGamepad.left_stick_button;
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
            arm.setPosition(armPose);


            telemetry.addData("Arm Pose: ", armPose);



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



            if (manipulatorGamepad.left_trigger >= .9) {
                intakeOne.setPower(-0.75);intakeTwo.setPower(-0.75);

            }
           if (manipulatorGamepad.right_trigger >= .9) {
               intakeOne.setPower(0.75); intakeTwo.setPower(0.75);
           }
           //Emergency Stop
           if (manipulatorGamepad.options) {
               intakeOne.setPower(0); intakeTwo.setPower(0);
           }
           if (manipulatorGamepad.circle){ // for async
               sortActive = true;
           }
           if (sortActive){
               manipulatorGamepad.setLedColor(sampleLook.get(colorFilter)[0],sampleLook.get(colorFilter)[1],sampleLook.get(colorFilter)[2],1000);
               intakeOne.setPower(-0.75); intakeOne.setPower(-0.75); // powers might be wrong
               int red = thresholds[colorFilter][0]; int blue = thresholds[colorFilter][1]; int green = thresholds[colorFilter][2];
               if (colorDistance.getDistance(DistanceUnit.CM) < 2.0){
                   intakeOne.setPower(0); intakeTwo.setPower(0);           // tolerance of 10 (can be changed)
                   if (Math.abs(color.red() - red) < 10 && Math.abs(color.blue() - blue) < 10 && Math.abs(color.green() - green) < 10){
                       driveGamepad.rumble(50); manipulatorGamepad.rumble(50);
                       manipulatorGamepad.setLedColor(sampleHave.get(colorFilter)[0],sampleHave.get(colorFilter)[1],sampleHave.get(colorFilter)[2], 1000);
                       sortActive = false;
                   }
                   else {
                       timer.reset();// might reset too much
                       intakeOne.setPower(0.75);intakeTwo.setPower(0.75);
                       if (timer.milliseconds() >= 500){ // might not resolve if it checks it immediately (checks right after timer is on and never flips direction)
                           intakeOne.setPower(-0.75);intakeTwo.setPower(-0.75);
                       }
                   }
               }
           }
           //ColorSort

           if (manipulatorGamepad.share) {
               colorFilter++;
               if (colorFilter > 2) {
                   colorFilter = 0;
               }
               telemetry.clearAll();// Updates which type of color sort it is; other info will be printed due to it being a while loop
               telemetry.addData(colorData.get(colorFilter), colorFilter);
               telemetry.update();
           }

            telemetry.addData("Red",color.red());
            telemetry.addData("Blue",color.blue());
            telemetry.addData("Green",color.green());
            telemetry.update();
        }
    }
}



